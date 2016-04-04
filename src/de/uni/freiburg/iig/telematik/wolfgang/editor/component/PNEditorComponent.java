package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import de.invation.code.toval.properties.PropertyException;

import de.invation.code.toval.validate.ExceptionDialog;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.event.PNEditorListener;
import de.uni.freiburg.iig.telematik.wolfgang.event.PNEditorListenerSupport;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphListener;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar.Mode;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.NodePalettePanel;
import de.uni.freiburg.iig.telematik.wolfgang.properties.check.AbstractPropertyCheckView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PropertiesView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.tree.PNTreeNode;
import java.io.IOException;

public abstract class PNEditorComponent extends JPanel implements TreeSelectionListener, PNGraphListener, ViewComponent {

	public static final boolean DEFAULT_ASK_FOR_LAYOUT = false;

	private static final long serialVersionUID = 1023415244830760771L;
	protected JPanel pnlStatus = null;
	protected NodePalettePanel palettePanel = null;
	protected PNGraphComponent graphComponent;
	protected AbstractToolBar toolbar = null;
	protected boolean modified = false;
	protected PNProperties properties = null;
	protected PropertiesView propertiesView = null;
	public AbstractGraphicalPN netContainer = null;
	protected AbstractWolfgang wolfgang = null;
	protected mxRubberband rubberband;
	protected mxUndoManager undoManager;

	private PNEditorListenerSupport editorListenerSupport = new PNEditorListenerSupport();
	
	

	protected mxIEventListener undoHandler = new mxIEventListener() {
                @Override
		public void invoke(Object source, mxEventObject evt) {
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};
	protected mxIEventListener changeTracker = new mxIEventListener() {
                @Override
		public void invoke(Object source, mxEventObject evt) {
			setModified(true);
		}
	};

	private AbstractPropertyCheckView propertyCheckView;

	// ------- Constructors
	// --------------------------------------------------------------------

	public PNEditorComponent() {
		super();
		initialize(null);
		setUpGUI();
	}

	public PNEditorComponent(AbstractGraphicalPN netContainer) {
		this(netContainer, DEFAULT_ASK_FOR_LAYOUT);
	}

	public PNEditorComponent(AbstractGraphicalPN netContainer, boolean askForLayout) {
		super();
		Validate.notNull(netContainer);
		initialize(netContainer);
		setUpGUI();
		try {
			propertiesView.setUpGUI();
		} catch (PropertyException | IOException e) {
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(getParent()), "Property Exception. ", e);
		}

		if (!graphComponent.getGraph().containedGraphics() && askForLayout) {
			String selectedLayout = showLayoutDialog();
			if (selectedLayout != null) {
				setLayout(selectedLayout);
			}
		}
	}

	public PNEditorComponent(AbstractGraphicalPN netContainer, LayoutOption layoutOption) {
		this(netContainer, false);
		if (layoutOption != null)
			setLayout(layoutOption.getLayoutCode());
	}

	public AbstractWolfgang getWolfgang() {
		return wolfgang;
	}

	public void setWolfgang(AbstractWolfgang wolfgang) {
		this.wolfgang = wolfgang;
	}

	private String showLayoutDialog() {
		String[] layouts = { "verticalHierarchical", "horizontalHierarchical", "organicLayout", "circleLayout" };
		String selectedLayout = (String) JOptionPane.showInputDialog(getGraphComponent(), "Selected Layout:", "Do you wish to layout your net?", JOptionPane.QUESTION_MESSAGE, null, layouts,
				layouts[0]);
		return selectedLayout;
	}

	private void setLayout(String selectedLayout) {
		mxIGraphLayout layout = createLayout(selectedLayout, true);
		mxGraph graph = graphComponent.getGraph();
		Object cell = graph.getSelectionCell();

		if (cell == null || graph.getModel().getChildCount(cell) == 0) {
			cell = graph.getDefaultParent();
		}

		graph.getModel().beginUpdate();
		try {
			layout.execute(cell);
		} finally {
			mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

			morph.addListener(mxEvent.DONE, new mxIEventListener() {

                                @Override
				public void invoke(Object sender, mxEventObject evt) {
					getGraph().getModel().endUpdate();
					// getGraph().updatePositionPropertiesFromCells();
				}

			});

			morph.startAnimation();
		}
	}

	private void initialize(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer) {
		if (netContainer == null) {
			this.netContainer = createNetContainer();
		} else {
			this.netContainer = netContainer;
		}

		// UIManager.put("Tree.rendererFillBackground", false);
		properties = createPNProperties();
		propertiesView = new PropertiesView(properties);
		propertiesView.addTreeSelectionListener(this);
//		addEditorListener(new PNEditorListener() {
//			
//			@Override
//			public void modificationStateChanged(boolean modified) {
//			}
//		});
		properties.addPNPropertiesListener(propertiesView);
		properties.setPropertiesView(propertiesView);
		
		propertyCheckView = createPropertyCheckView();
		
	}

	protected abstract AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> createNetContainer();

	protected abstract PNProperties createPNProperties();
	
	abstract protected AbstractPropertyCheckView createPropertyCheckView();
	
	public void addEditorListener(PNEditorListener listener) {
		editorListenerSupport.addEditorListener(listener);
	}
	
	public void removeEditorListener(PNEditorListener listener) {
		editorListenerSupport.removeEditorListener(listener);
	}

	// ------- Set Up GUI
	// -----------------------------------------------------------------------

	private void setUpGUI() {
		setLayout(new BorderLayout());
		loadEditorToolbar();
		add(getGraphComponent(), BorderLayout.CENTER);
		// add(getpnlStatus(), BorderLayout.SOUTH);

		rubberband = new mxRubberband(graphComponent);
	}

	protected abstract AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException;

	public AbstractToolBar getEditorToolbar() {
		return toolbar;
	}

	public PNGraphComponent getGraphComponent() {
		if (graphComponent == null) {
			graphComponent = createGraphComponent();
			graphComponent.setPopupMenu(getPopupMenu());
			graphComponent.setTransitionPopupMenu(getTransitionPopupMenu());
			graphComponent.getHorizontalScrollBar().setUnitIncrement(16);
			graphComponent.getVerticalScrollBar().setUnitIncrement(16);
			
			Map<String, Object> style = getGraph().getStylesheet().getDefaultEdgeStyle();
			style.put("strokeWidth", 2.0);
			style.put("strokeColor", mxUtils.hexString(MXConstants.bluelow));

			addGraphComponentListeners();
			setUpUndo();

		}
		return graphComponent;
	}

	protected abstract PNGraphComponent createGraphComponent();

	private void addGraphComponentListeners() {

		graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {
                        @Override
			public void mouseDragged(MouseEvent e) {
			}

                        @Override
			public void mouseMoved(MouseEvent e) {
				displayStatusMessage(e.getX() + ", " + e.getY());
			}
		});

		graphComponent.getGraph().addPNGraphListener(this);
	}

	private void setUpUndo() {

		undoManager = new mxUndoManager();

		// Do not change the scale and translation after files have been loaded
		getGraph().setResetViewOnRootChange(false);

		// Updates the modified flag if the graph model changes
		getGraph().getModel().addListener(mxEvent.CHANGE, changeTracker);
		

		// Adds the command history to the model and view
		getGraph().getModel().addListener(mxEvent.UNDO, undoHandler);
		getGraph().getView().addListener(mxEvent.UNDO, undoHandler);

	}

        @Override
	public JComponent getMainComponent() {
		return this;
	}

	// ------- Functionality
	// --------------------------------------------------------------------

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

        @Override
	public PropertiesView getPropertiesView() {
		return propertiesView;
	}
	
	public AbstractPropertyCheckView getPropertyCheckView() {
		return propertyCheckView;
	}
	

	protected PNProperties getPNProperties() {
		return properties;
	}
	
	

	private PNGraph getGraph() {
		return graphComponent.getGraph();
	}

	public void setPlaceLabel(String placeName, String placeLabel) throws ParameterException {
		Validate.notNull(placeName);
		Validate.notNull(placeLabel);
		Validate.notEmpty(placeName);
		Validate.notEmpty(placeLabel);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown place.");

		properties.setPlaceLabel(this, placeName, placeLabel);
	}

	public abstract EditorPopupMenu getPopupMenu();

	public abstract JPopupMenu getTransitionPopupMenu();

	public void setModified(boolean modified) {
		boolean oldValue = this.modified;
		this.modified = modified;
		firePropertyChange("modified", oldValue, modified);
		editorListenerSupport.notifyModificationStateChange(modified);
	}

	public boolean isModified() {
		return modified;
	}

	public mxUndoManager getUndoManager() {
		return undoManager;
	}

	public void displayStatusMessage(String msg) {
		// TODO: Do something
	}



	/**
	 * Creates a layout instance for the given identifier.
         * @param ident
         * @param animate
         * @return 
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			mxGraph graph = graphComponent.getGraph();

                        switch (ident) {
                                case "verticalHierarchical":
                                        layout = new mxHierarchicalLayout(graph);
                                        break;
                                case "horizontalHierarchical":
                                        layout = new mxHierarchicalLayout(graph, JLabel.WEST);
                                        break;
                                case "verticalTree":
                                        layout = new mxCompactTreeLayout(graph, false);
                                        break;
                                case "horizontalTree":
                                        layout = new mxCompactTreeLayout(graph, true);
                                        break;
                                case "parallelEdges":
                                        layout = new mxParallelEdgeLayout(graph);
                                        break;
                                case "placeEdgeLabels":
                                        layout = new mxEdgeLabelLayout(graph);
                                        break;
                                case "organicLayout":
                                        layout = new mxOrganicLayout(graph);
                                        break;
                                case "verticalPartition":
                                        layout = new mxPartitionLayout(graph, false) {
                                                /**
                                                 * Overrides the empty implementation to return the size of
                                                 * the graph control.
                                                 */
                                                @Override
                                                public mxRectangle getContainerSize() {
                                                        return graphComponent.getLayoutAreaSize();
                                                }
                                        };      break;
                                case "horizontalPartition":
                                        layout = new mxPartitionLayout(graph, true) {
                                                /**
                                                 * Overrides the empty implementation to return the size of
                                                 * the graph control.
                                                 */
                                                @Override
                                                public mxRectangle getContainerSize() {
                                                        return graphComponent.getLayoutAreaSize();
                                                }
                                        };      break;
                                case "verticalStack":
                                        layout = new mxStackLayout(graph, false) {
                                                /**
                                                 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
                                        @Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};      break;
                                case "horizontalStack":
                                        layout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
                                        @Override
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};      break;
                                case "circleLayout":
                                        layout = new mxCircleLayout(graph);
                                        break;
                        }
		}

		return layout;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object treeSelection = e.getPath().getLastPathComponent();
		if (treeSelection instanceof PNTreeNode) {
			String treeSelectionName = ((PNTreeNode) treeSelection).toString();
			ArrayList<PNGraphCell> selectedCells = new ArrayList<>();

			switch (((PNTreeNode) treeSelection).getFieldType()) {
			case ARC:
				getGraph().setSelectionCell(getGraph().getNodeCell(treeSelectionName));
				break;
			case ARCS:
				getGraph().selectCells(false, true);
				break;
			case LEAF:
				break;
			case PLACE:
				getGraph().setSelectionCell(getGraph().getNodeCell(treeSelectionName));
				break;
			case PLACES:
				for (AbstractPlace p : getNetContainer().getPetriNet().getPlaces()) {
					selectedCells.add(getGraph().getNodeCell(p.getName()));
				}
				getGraph().setSelectionCells(selectedCells.toArray());
				break;
			case ROOT:
				break;
			case TRANSITION:
				getGraph().setSelectionCell(getGraph().getNodeCell(treeSelectionName));
				break;
			case TRANSITIONS:
				for (AbstractTransition p : getNetContainer().getPetriNet().getTransitions()) {
					selectedCells.add(getGraph().getNodeCell(p.getName()));
				}
				getGraph().setSelectionCells(selectedCells.toArray());
				break;
			default:
				break;
			}
			getGraphComponent().requestFocus();
		}
	}

	@Override
	public void placeAdded(AbstractPlace place) {
		propertiesView.componentAdded(PNComponent.PLACE, place.getName());
		resetPropertyCheckView();
	}

	protected void resetPropertyCheckView(){
		getPropertyCheckView().resetFieldContent(); 
	}

	@Override
	public void transitionAdded(AbstractTransition transition) {
		propertiesView.componentAdded(PNComponent.TRANSITION, transition.getName());
		resetPropertyCheckView();
	}

	@Override
	public void relationAdded(AbstractFlowRelation relation) {
		propertiesView.componentAdded(PNComponent.ARC, relation.getName());
		resetPropertyCheckView();
	}

	@Override
	public void placeRemoved(AbstractPlace place) {
		propertiesView.componentRemoved(PNComponent.PLACE, place.getName());
		resetPropertyCheckView();
	}

	@Override
	public void transitionRemoved(AbstractTransition transition) {
		propertiesView.componentRemoved(PNComponent.TRANSITION, transition.getName());
		resetPropertyCheckView();
	}

	@Override
	public void relationRemoved(AbstractFlowRelation relation) {
		propertiesView.componentRemoved(PNComponent.ARC, relation.getName());
		resetPropertyCheckView();
	}
	

	@Override
	public void componentsSelected(Set<PNGraphCell> selectedComponents) {
		if (getEditorToolbar().getMode().equals(Mode.PLAY) && selectedComponents != null && !selectedComponents.isEmpty()) {
			getEditorToolbar().addTransitionToTrace(selectedComponents.iterator().next());
		} else {
			if (selectedComponents == null || selectedComponents.isEmpty() || selectedComponents.size() > 1) {
				propertiesView.deselect();
			} else {
				PNGraphCell selectedCell = selectedComponents.iterator().next();
				if(!getNetContainer().getPetriNet().getFlowRelations().isEmpty()) // Fixing wrong display of arc count >9 of (...) in PropertiesView
                                        propertiesView.selectNode(getNetContainer().getPetriNet().getFlowRelations().iterator().next().getName());
				
				propertiesView.selectNode(selectedCell.getId());
			}
		}
	}

	public enum LayoutOption {

		VERTICAL_HIERARCHICAL("verticalHierarchical"), HORIZONTAL_HIERARCHICAL("horizontalHierarchical"), ORGANIC("organicLayout"), CIRCLE("circleLayout");

		private String layoutCode = null;

		private LayoutOption(String layoutCode) {
			this.layoutCode = layoutCode;
		}

		public String getLayoutCode() {
			return layoutCode;
		}

	}

	public void loadEditorToolbar() {
		try {
			toolbar = createNetSpecificToolbar();
		} catch (EditorToolbarException e) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Cannot create Toolbar.\nReason: " + e.getMessage(), "Editor Toolbar Exception", JOptionPane.ERROR_MESSAGE);
		}
		propertiesView.setEditorToolbar(toolbar);
	}


}
