package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Box.Filler;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.graphic.dialog.StringDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.mode.EnterEditingAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.mode.EnterExecutionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.mode.ReloadExecutionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.mode.ToggleModeAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.nodes.NodeToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphListener;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.ExportToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.FontToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.NodeToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.ZoomToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;
import java.util.Set;

public abstract class AbstractToolBar extends JToolBar implements PNGraphListener {

    private static final Color DEFAULT_BG_COLOR = UIManager.getColor("Panel.background");

    private static final long serialVersionUID = -6491749112943066366L;

    private static final int DEFAULT_TB_HEIGHT = 50;

    protected static final String NO_SELECTION = "no selection...";

    protected static final String NO_SELECTION_TIME = "no time context...";

    private PNEditingModeListenerSupport editingModeListenerSupport = new PNEditingModeListenerSupport();

    public void addEditingModeListener(PNEditingModeListener listener) {
        editingModeListenerSupport.addEditorListener(listener);
    }

    public void removeEditingModeListener(PNEditingModeListener listener) {
        editingModeListenerSupport.removeEditorListener(listener);
    }

    // further variables
    protected PNEditorComponent pnEditor = null;
    private boolean ignoreZoomChange = false;
    private Mode mode = Mode.EDIT;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode {

        EDIT, PLAY
    }

	// Actions
    // private SaveAction saveAction = null;
    private UndoAction undoAction = null;
    private RedoAction redoAction = null;
    private EnterExecutionAction enterExecutionAction;
    private EnterEditingAction enterEditingAction;
    private PopUpToolBarAction fontAction;
    private PopUpToolBarAction graphicsAction;
    private PopUpToolBarAction zoomAction;
    private NodeToolBarAction nodeAction;
    private PopUpToolBarAction exportAction;
    private ToggleModeAction toggleModeAction;
    private ReloadExecutionAction reloadExecutionAction;
	// Buttons
    // private JButton saveButton;
    private JToggleButton tglUndo;
    private JToggleButton tglRedo;
    private JToggleButton tglFont = null;
    private JToggleButton tglGraphics = null;
    // private JButton enterExecutionButton;
    private JButton btnReloadExecution;
    // private JButton enterEditingButton;
    private JToggleButton tglZoom;
    private JToggleButton tglNode;
    private JToggleButton tglExport;
    private JButton btnToggleMode;

    // Sub-Toolbars
    private FontToolBar fontToolbar;
    private GraphicsToolBar graphicsToolbar;
    private ExportToolBar exportToolbar;
    private ZoomToolBar zoomToolbar;
    private NodeToolBar nodeToolbar;

	// Tooltips
    // private String executionButtonTooltip = "execution mode";
    // private String editingButtonTooltip = " editing mode";
    private String fontTooltip = "font";
    private String saveButtonTooltip = "save";
    private String tglExportTooltip = "export to pdf";
    private String undoTooltip = "undo";
    private String redoTooltip = "redo";

    public JTextField executionTraceTextField = null;

    private JLabel executionTraceLabel;

    private JToolBar propertyCheckToolbar;

	private JScrollPane scrollPane = new JScrollPane();

	private String executionTrace = "";

	private JButton btnFullTrace;;


    public JTextField getExecutionTrace() {
    	if(executionTraceTextField == null){
            executionTraceTextField = new JTextField();
            executionTraceTextField.setEditable(false);            
    	}
    	Dimension d = new Dimension(150, 20);
		executionTraceTextField.setMinimumSize(d);
		executionTraceTextField.setPreferredSize(d);
		executionTraceTextField.setMaximumSize(d);
        return executionTraceTextField;
    }

    public AbstractToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
        super(orientation);
        Validate.notNull(pnEditor);
//        setLayout(new SpringLayout);
        this.pnEditor = pnEditor;
        this.pnEditor.getGraphComponent().getGraph().addPNGraphListener(this);
        try {
            createToolbarActions(pnEditor);
            createAdditionalToolbarActions(pnEditor);


        } catch (ParameterException e) {
            throw new EditorToolbarException("Invalid Parameter.\nReason: " + e.getMessage());
        } catch (PropertyException e) {
            throw new EditorToolbarException("Invalid Property.\nReason: " + e.getMessage());
        } catch (IOException e) {
            throw new EditorToolbarException("Invalid File Path.\nReason: " + e.getMessage());
        }
        setFloatable(false);

        tglExport = (JToggleButton) add(exportAction, true);
        exportAction.setButton(tglExport);

        addSeparator();

        btnToggleMode = add(toggleModeAction);
        btnToggleMode.setBorderPainted(false);
        btnToggleMode.setIconTextGap(0);
        btnToggleMode.setText("EDIT");

        add(new Filler(new Dimension(0, 0), new Dimension(20, 0), new Dimension(30, 0)));
        btnReloadExecution = add(reloadExecutionAction);
        setButtonSettings(btnReloadExecution);

        executionTraceLabel = new JLabel("    Execution Trace: ");

        setExecutionButtonsVisible(false);

        tglUndo = (JToggleButton) add(undoAction, true);
        tglRedo = (JToggleButton) add(redoAction, true);

        tglNode = (JToggleButton) add(nodeAction, true);

        nodeAction.setButton(tglNode);

        tglFont = (JToggleButton) add(fontAction, true);
        fontAction.setButton(tglFont);
        fontAction.setEnabled(false);

        tglGraphics = (JToggleButton) add(graphicsAction, true);
        graphicsAction.setButton(tglGraphics);
        graphicsAction.setEnabled(false);



        add(executionTraceLabel);	
		scrollPane.setViewportView(getExecutionTrace());
		add(scrollPane);
		
		add(getButtonFullExecution());
		
		

        tglZoom = (JToggleButton) add(zoomAction, true);
        tglZoomSettings();
        zoomAction.setButton(tglZoom);

		// if
        // (!SwatComponents.getInstance().getTimeContexts(this.pnEditor.getNetContainer().getPetriNet().getName()).isEmpty())
        // {
        // addSeparator();
        // add(getComboTimeContextModel());
        // addSeparator();
        // }
        addNetSpecificToolbarButtons();

        try {
            add(getPropertyCheckToolbar());
        } catch (ParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PropertyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        doLayout();

        tglExport.setToolTipText(tglExportTooltip);
		// enterExecutionButton.setToolTipText(executionButtonTooltip);
        // enterEditingButton.setToolTipText(editingButtonTooltip);

        tglUndo.setToolTipText(undoTooltip);
        tglRedo.setToolTipText(redoTooltip);
        tglFont.setToolTipText(fontTooltip);

        // setting background colour for toolbar and buttons
//		setBackground(DEFAULT_BG_COLOR);
        tglExport.setBackground(DEFAULT_BG_COLOR);
        tglUndo.setBackground(DEFAULT_BG_COLOR);
        tglRedo.setBackground(DEFAULT_BG_COLOR);
        tglFont.setBackground(DEFAULT_BG_COLOR);
        tglGraphics.setBackground(DEFAULT_BG_COLOR);
        btnReloadExecution.setBackground(DEFAULT_BG_COLOR);
        tglZoom.setBackground(DEFAULT_BG_COLOR);
        tglNode.setBackground(DEFAULT_BG_COLOR);
        tglExport.setBackground(DEFAULT_BG_COLOR);
        btnToggleMode.setBackground(DEFAULT_BG_COLOR);

    }

    protected abstract JToolBar createPropertyCheckToolbar() throws ParameterException, PropertyException, IOException;

    protected abstract void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException;

    protected abstract void addNetSpecificToolbarButtons();

    private void createToolbarActions(final PNEditorComponent pnEditor) throws PropertyException, IOException {
        exportToolbar = new ExportToolBar(pnEditor, JToolBar.HORIZONTAL);
        exportAction = new PopUpToolBarAction(pnEditor, "Export", IconFactory.getIcon("export"), exportToolbar);

        toggleModeAction = new ToggleModeAction(pnEditor);
        enterExecutionAction = new EnterExecutionAction(pnEditor);
        reloadExecutionAction = new ReloadExecutionAction(pnEditor);
        enterEditingAction = new EnterEditingAction(pnEditor);

        undoAction = new UndoAction(pnEditor);
        redoAction = new RedoAction(pnEditor);

        nodeToolbar = new NodeToolBar(pnEditor, JToolBar.HORIZONTAL);
        nodeAction = new NodeToolBarAction(pnEditor, "Node", nodeToolbar);

        fontToolbar = new FontToolBar(pnEditor, JToolBar.HORIZONTAL);
        fontAction = new PopUpToolBarAction(pnEditor, "Font", IconFactory.getIcon("text"), fontToolbar);

        graphicsToolbar = new GraphicsToolBar(pnEditor, JToolBar.HORIZONTAL);
        graphicsAction = new PopUpToolBarAction(pnEditor, "Graphics", IconFactory.getIcon("bg_color"), graphicsToolbar);

        zoomToolbar = new ZoomToolBar(pnEditor, JToolBar.HORIZONTAL);
        zoomAction = new PopUpToolBarAction(pnEditor, "Zoom", IconFactory.getIcon("zoom_in"), zoomToolbar);
    }

    protected JToolBar getPropertyCheckToolbar() throws ParameterException, PropertyException, IOException {
        if (propertyCheckToolbar == null) {
            propertyCheckToolbar = createPropertyCheckToolbar();
        }
        return propertyCheckToolbar;
    }
    
	private JButton getButtonFullExecution(){
		if(btnFullTrace == null){
			btnFullTrace = new JButton("Show Full Trace");
			btnFullTrace.setEnabled(executionTrace != null);
			btnFullTrace.setBorderPainted(true);
			btnFullTrace.setRolloverEnabled(true);
			btnFullTrace.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(executionTrace == null)
						return;
					try {
						StringDialog.showDialog(SwingUtilities.getWindowAncestor(AbstractToolBar.this), "Execution Trace", executionTrace);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return btnFullTrace;
	}

    private void tglZoomSettings() {
        final mxGraphView view = pnEditor.getGraphComponent().getGraph().getView();
        double scale = view.getScale();
        int scaleInt = (int) (scale * 100);
        tglZoom.setVerticalAlignment(SwingConstants.CENTER);
        tglZoom.setText(scaleInt + "%  ");
        tglZoom.setIconTextGap(-5);

        // Sets the zoom in the zoom combo the current value
        mxIEventListener scaleTracker = new mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt) {
                ignoreZoomChange = true;

                try {
                } finally {
                    ignoreZoomChange = false;
                }
                if (!ignoreZoomChange) {
                    double scale = view.getScale();
                    int scaleInt = (int) (scale * 100);
                    tglZoom.setText(scaleInt + "%");
                    tglZoom.setIconTextGap(-5);
                }
            }
        };

		// Installs the scale tracker to update the value in the combo box
        // if the zoom is changed from outside the combo box
        view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
        view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);

        // Invokes once to sync with the actual zoom value
        scaleTracker.invoke(null, null);
    }

    private void setButtonSettings(final JButton btn) {
    	btn.setBorderPainted(false);
    	btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
            	btn.setBorderPainted(false);
                super.mouseReleased(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            	btn.setBorderPainted(true);
                super.mousePressed(e);
            }

        });
    }

    protected JComponent add(Action action, boolean asToggleButton) {
        if (!asToggleButton) {
            return super.add(action);
        }
        JToggleButton b = createToggleActionComponent(action);
        b.setAction(action);
        add(b);
        return b;
    }

    protected JToggleButton createToggleActionComponent(Action a) {
        JToggleButton b = new JToggleButton() {
            private static final long serialVersionUID = -3143341784881719155L;

            protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                return super.createActionPropertyChangeListener(a);
            }
        };
        if (a != null && (a.getValue(Action.SMALL_ICON) != null || a.getValue(Action.LARGE_ICON_KEY) != null)) {
            b.setHideActionText(true);
        }
        b.setHorizontalTextPosition(JButton.CENTER);
        b.setVerticalTextPosition(JButton.BOTTOM);
        b.setBorderPainted(false);
        return b;
    }

    public void setExecutionMode() {
        mode = Mode.PLAY;
        editingModeListenerSupport.notifyEditingModeChange(mode);
        pnEditor.getGraphComponent().getGraph().clearSelection();
        pnEditor.getGraphComponent().getGraph().setExecution(true);
        pnEditor.getGraphComponent().highlightEnabledTransitions();
        btnToggleMode.setText("PLAY");
        setExecutionButtonsVisible(true);
        setEditButtonsVisible(false);
        executionTrace = "";
        getExecutionTrace().setText(executionTrace);

    }

    private void setEditButtonsVisible(boolean b) {
        // enterEditingButton.setVisible(b);
        tglUndo.setVisible(b);
        tglRedo.setVisible(b);
        tglNode.setVisible(b);
        tglFont.setVisible(b);
        tglGraphics.setVisible(b);
        if (nodeAction.getDialog() != null && tglNode.isSelected()) {
            nodeAction.getDialog().setVisible(b);
        }
        if (fontAction.getDialog() != null && tglFont.isSelected()) {
            fontAction.getDialog().setVisible(b);
        }
        if (graphicsAction.getDialog() != null && tglGraphics.isSelected()) {
            graphicsAction.getDialog().setVisible(b);
        }
        setNetSpecificButtonsVisible(b);
    }

    protected abstract void setNetSpecificButtonsVisible(boolean b);

    private void setExecutionButtonsVisible(boolean b) {
        // enterExecutionButton.setVisible(b);
        btnReloadExecution.setVisible(b);
        setExecutionTraceVisible(b);
        getButtonFullExecution().setVisible(b);
    }

    private void setExecutionTraceVisible(boolean b) {
        executionTraceLabel.setVisible(b);
        getExecutionTrace().setVisible(b);
        scrollPane.setVisible(b);

    }

    public void setEditingMode() {
        mode = Mode.EDIT;
        editingModeListenerSupport.notifyEditingModeChange(mode);
        pnEditor.getGraphComponent().removeCellOverlays();
        pnEditor.getGraphComponent().getGraph().enterEditingMode();
        btnToggleMode.setText("EDIT");
        setEditButtonsVisible(true);
        setExecutionButtonsVisible(false);
    }

	// public JButton getExecutionButton() {
    // return enterExecutionButton;
    //
    // }
    public GraphicsToolBar getGraphicsToolbar() {
        // TODO Auto-generated method stub
        return graphicsToolbar;
    }

    public void addTransitionToTrace(PNGraphCell cell) {
        String lastFiredTransistion = pnEditor.getNetContainer().getPetriNet().getLastFiredTransition().getName();
        if (cell.getType().equals(PNComponent.TRANSITION) && cell.getId().equals(lastFiredTransistion)) {
            String label = pnEditor.getNetContainer().getPetriNet().getTransition(cell.getId()).getLabel();
            if (getExecutionTrace().getText().length() > 0) {
                
                executionTrace = getExecutionTrace().getText() + " -> " + label;
                getExecutionTrace().setText(executionTrace);
            } else {
            	executionTrace = label;
                getExecutionTrace().setText(executionTrace);
            }
        }
        
    }

    @Override
    public void placeAdded(AbstractPlace place) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transitionAdded(AbstractTransition transition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void relationAdded(AbstractFlowRelation relation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void placeRemoved(AbstractPlace place) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transitionRemoved(AbstractTransition transition) {
        // TODO Auto-generated method stub

    }

    @Override
    public void relationRemoved(AbstractFlowRelation relation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
        // TODO Auto-generated method stub

    }

    @Override
    public void placeCapacityChanged(String placeName, String color, int newCapacity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void constraintChanged(String flowRelation, Multiset constraint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentsSelected(Set<PNGraphCell> selectedComponents) {
        if (selectedComponents != null && selectedComponents.isEmpty()){
            fontAction.setEnabled(false);
            graphicsAction.setEnabled(false);
        }
        else{
            fontAction.setEnabled(true);
            graphicsAction.setEnabled(true);
        }
    }

}
