package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
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
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphActions.DeleteAction;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.export.ExportPDFAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.MoveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.NewNodeAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.PrintAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.SelectAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
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
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PropertiesView.PropertiesField;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.tree.PNTreeNode;

public abstract class PNEditorComponent extends JPanel implements TreeSelectionListener, PNGraphListener, ViewComponent {

	public static final boolean DEFAULT_ASK_FOR_LAYOUT = false;

	private static final long serialVersionUID = 1023415244830760771L;
	protected JPanel statusPanel = null;
	protected NodePalettePanel palettePanel = null;
	protected PNGraphComponent graphComponent;
	protected AbstractToolBar toolbar = null;
	protected boolean modified = false;
	protected PNProperties properties = null;
	protected PropertiesView propertiesView = null;
	public AbstractGraphicalPN netContainer = null;
	protected AbstractWolfgang wolfgang = null;
	protected mxRubberband rubberband;
	protected mxKeyboardHandler keyboardHandler;
	protected mxUndoManager undoManager;

	private PNEditorListenerSupport editorListenerSupport = new PNEditorListenerSupport();
	
	

	protected mxIEventListener undoHandler = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};
	protected mxIEventListener changeTracker = new mxIEventListener() {
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
		} catch (Exception e) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Property Exception.\nReason: " + e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
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
//				// TODO Auto-generated method stub
//				
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
		// add(getStatusPanel(), BorderLayout.SOUTH);

		rubberband = new mxRubberband(graphComponent);
		keyboardHandler = new KeyboardHandler(graphComponent);

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
			public void mouseDragged(MouseEvent e) {
			}

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

	public JComponent getMainComponent() {
		return this;
	}

	// ------- Functionality
	// --------------------------------------------------------------------

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

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

	protected class KeyboardHandler extends mxKeyboardHandler {

		public KeyboardHandler(mxGraphComponent graphComponent) {
			super(graphComponent);
		}

		@Override
		protected InputMap getInputMap(int condition) {
			InputMap map = super.getInputMap(condition);
			if (condition == JComponent.WHEN_FOCUSED && map != null) {

				int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
				int commandAndShift = commandKey | InputEvent.SHIFT_DOWN_MASK;

				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, commandKey), "save");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, commandAndShift), "saveAs");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, commandKey), "new");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, commandKey), "open");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, commandKey), "undo");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, commandKey), "redo");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, commandAndShift), "selectVertices");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, commandAndShift), "selectEdges");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, commandKey), "selectPlaces");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, commandKey), "selectTransitions");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, commandKey), "selectPlaces");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, commandKey), "selectArcs");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, commandKey), "selectArcs");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, commandKey), "selectAll");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, commandKey), "cut");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, commandKey), "copy");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, commandKey), "paste");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, commandAndShift), "printNet");
				map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, commandKey), "export");

				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, commandKey), "newNodeLeft");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, commandKey), "newNodeRight");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, commandKey), "newNodeDown");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, commandKey), "newNodeUp");

				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");

				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), "bigMoveLeft");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), "bigMoveRight");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), "bigMoveDown");
				map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), "bigMoveUp");

			}
			return map;
		}

		@Override
		protected ActionMap createActionMap() {
			ActionMap map = super.createActionMap();
			try {
				map.put("undo", new UndoAction(PNEditorComponent.this));
				map.put("redo", new RedoAction(PNEditorComponent.this));
				map.put("printNet", new PrintAction(PNEditorComponent.this));

				map.put("export", new ExportPDFAction(PNEditorComponent.this));

				int offset = EditorProperties.getInstance().getDefaultPlaceSize() * 4;
				map.put("newNodeLeft", new NewNodeAction(PNEditorComponent.this, -offset, 0));
				map.put("newNodeRight", new NewNodeAction(PNEditorComponent.this, offset, 0));
				map.put("newNodeDown", new NewNodeAction(PNEditorComponent.this, 0, offset));
				map.put("newNodeUp", new NewNodeAction(PNEditorComponent.this, 0, -offset));

				map.put("moveLeft", new MoveAction(PNEditorComponent.this, -1, 0));
				map.put("moveRight", new MoveAction(PNEditorComponent.this, 1, 0));
				map.put("moveDown", new MoveAction(PNEditorComponent.this, 0, 1));
				map.put("moveUp", new MoveAction(PNEditorComponent.this, 0, -1));

				int movingGap = 5;
				map.put("bigMoveLeft", new MoveAction(PNEditorComponent.this, -movingGap, 0));
				map.put("bigMoveRight", new MoveAction(PNEditorComponent.this, movingGap, 0));
				map.put("bigMoveDown", new MoveAction(PNEditorComponent.this, 0, movingGap));
				map.put("bigMoveUp", new MoveAction(PNEditorComponent.this, 0, -movingGap));

				map.put("selectPlaces", new SelectAction(PNEditorComponent.this, PNComponent.PLACE));
				map.put("selectTransitions", new SelectAction(PNEditorComponent.this, PNComponent.TRANSITION));
				map.put("selectArcs", new SelectAction(PNEditorComponent.this, PNComponent.ARC));

			} catch (Exception e) {
				// Cannot happen, since this is not null
				e.printStackTrace();
			}

			map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
			map.put("selectEdges", mxGraphActions.getSelectEdgesAction());
			map.put("selectAll", mxGraphActions.getSelectAllAction());
			map.put("selectAllEdges", mxGraphActions.getSelectEdgesAction());

			map.put(("cut"), TransferHandler.getCutAction());
			map.put(("copy"), TransferHandler.getCopyAction());
			map.put(("paste"), TransferHandler.getPasteAction());
			map.put("delete", new DeleteAction("delete"));
			return map;
		}
	}

	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical")) {
				layout = new mxHierarchicalLayout(graph);
			} else if (ident.equals("horizontalHierarchical")) {
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			} else if (ident.equals("verticalTree")) {
				layout = new mxCompactTreeLayout(graph, false);
			} else if (ident.equals("horizontalTree")) {
				layout = new mxCompactTreeLayout(graph, true);
			} else if (ident.equals("parallelEdges")) {
				layout = new mxParallelEdgeLayout(graph);
			} else if (ident.equals("placeEdgeLabels")) {
				layout = new mxEdgeLabelLayout(graph);
			} else if (ident.equals("organicLayout")) {
				layout = new mxOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition")) {
				layout = new mxPartitionLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalPartition")) {
				layout = new mxPartitionLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("verticalStack")) {
				layout = new mxStackLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalStack")) {
				layout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (ident.equals("circleLayout")) {
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object treeSelection = e.getPath().getLastPathComponent();
		if (treeSelection instanceof PNTreeNode) {
			String treeSelectionName = ((PNTreeNode) treeSelection).toString();
			ArrayList<PNGraphCell> selectedCells = new ArrayList<PNGraphCell>();

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
