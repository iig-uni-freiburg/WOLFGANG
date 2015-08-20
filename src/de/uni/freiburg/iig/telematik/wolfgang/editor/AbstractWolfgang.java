package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphActions.DeleteAction;
import de.invation.code.toval.os.WindowsUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.ExitAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.LoadAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewCPNAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewPTAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAsAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SettingsAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.export.ExportPDFAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.help.AboutAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.help.SendExceptionsAsEmail;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.MoveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.NewNodeAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.PrintAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands.SelectAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangPropertyAdapter;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class AbstractWolfgang< P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object, X extends AbstractMarkingGraphState<M, S>, Y extends AbstractMarkingGraphRelation<M, X, S>, N extends AbstractPetriNet<P, T, F, M, S>, G extends AbstractPNGraphics<P, T, F, M, S>, NN extends AbstractGraphicalPN<P, T, F, M, S, N, G>> extends JFrame {

    private static final long serialVersionUID = -7994645960400940612L;
    private static final String titleFormat = "WOLFGANG | %s | %s";

    public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024, 768);
//	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(120, 768);
    private static final Dimension MINIMUM_SIZE_EDITOR_PANEL = new Dimension(904, 768);

    private File fileReference = null;

    protected PNEditorComponent editorComponent = null;
    private WGMenuBar menuBar = null;
    protected JPanel content = null;
    protected JPanel centerPanel = null;
    private JPanel rightPanel;

    @SuppressWarnings("rawtypes")
    private static Set<AbstractWolfgang> runningInstances = new HashSet<>();
    private JScrollPane editorScrollPane;
    private final int FIX_SIZE_RIGHT_PANEL = 200;
    private final int DIVIDER_LOCATION_RIGHT_PANEL = 400;
    private JScrollPane rightScrollPane;
    private JScrollPane toolbarScrollPane;

    private static final Map<String, String> ASSOCIATED_FILE_EXTENSIONS = new HashMap<>();
    static {
        ASSOCIATED_FILE_EXTENSIONS.put("iig.pnml.v1", ".pnml");
    }


    protected AbstractWolfgang() throws Exception {
        this(null, null);
    }

    protected AbstractWolfgang(NN net) throws Exception {
        this(net, null);
    }

    protected AbstractWolfgang(NN net, LayoutOption layoutOption) throws Exception {
        runningInstances.add(this);
        setNet(net, layoutOption);
    }

    protected AbstractWolfgang(NN net, boolean askForLayout) throws Exception {
        this();
        runningInstances.add(this);
        setNet(net, askForLayout);
    }

    protected abstract NN newNet();

    protected abstract PNEditorComponent newEditorComponent(NN net, LayoutOption layoutOption);

    protected abstract PNEditorComponent newEditorComponent(NN net, boolean askForLayout);

    protected abstract NetType getAcceptedNetType();

    public void setNet(NN net, LayoutOption layoutOption) {
        if (net == null) {
            net = newNet();
        }
        prepareNetInsertion(net);
        this.editorComponent = newEditorComponent(net, layoutOption);
        setName(net.getPetriNet().getName());
    }

    public void setNet(NN net, boolean askForLayout) {
        prepareNetInsertion(net);
        this.editorComponent = newEditorComponent(net, askForLayout);
        setName(net.getPetriNet().getName());
    }

    private void prepareNetInsertion(NN net) {
        Validate.notNull(net);
        NetType netType = net.getPetriNet().getNetType();
        if (!netType.equals(getAcceptedNetType())) {
            throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unsupported net type: " + netType);
        }

        if (editorComponent != null) {
            centerPanel.remove(editorComponent);
            centerPanel.remove(editorComponent.getPropertiesView());
        }
    }

    public PNEditorComponent getEditorComponent() {
        return editorComponent;
    }

    public File getFileReference() {
        return fileReference;
    }

    public void setFileReference(File fileReference) {
        this.fileReference = fileReference;
    }

    @Override
    public void dispose() {
        int saveNet = JOptionPane.showConfirmDialog(null, "Save edited net \"" + getEditorComponent().getNetContainer().getPetriNet().getName() + "\"?", "Save", JOptionPane.YES_NO_OPTION);
        if (saveNet == JOptionPane.YES_OPTION) {
            try {
                SaveAction save = new SaveAction(AbstractWolfgang.this);
                save.actionPerformed(null);
            } catch (PropertyException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error while saving edited net.", JOptionPane.ERROR_MESSAGE);
            }
        }
        super.dispose();
    }

    public void setUpGUI() throws PropertyException, IOException, Exception {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                try {
					new WolfgangKeyboardHandler(editorComponent.getGraphComponent());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (runningInstances.size() > 1) {
                    int closeAllInstances = JOptionPane.showConfirmDialog(AbstractWolfgang.this, "Close all " + runningInstances.size() + " Wolfgang instanes?", "Multiple running instances", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (closeAllInstances == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                    if (closeAllInstances == JOptionPane.YES_OPTION) {
                        for (AbstractWolfgang runningInstance : runningInstances) {
                            runningInstance.dispose();
                        }
                        runningInstances.clear();
                        System.exit(0);
                    } else if (closeAllInstances == JOptionPane.NO_OPTION) {
                        dispose();
                        runningInstances.remove(AbstractWolfgang.this);
                    }
                } else {
                    dispose();
                    runningInstances.remove(AbstractWolfgang.this);
                    System.exit(0);
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLookAndFeel();
        setPreferredSize(PREFERRED_SIZE_WORKBENCH);
        setResizable(true);
        setContentPane(getContent());
        setJMenuBar(getWGMenuBar());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // file extension check
        if (WindowsUtils.instance().isApplicable()) {
            String applicationPathURI = WindowsUtils.getExecutionPath().getAbsolutePath();
            String applicationPath = WindowsUtils.instance().toWindowsPath(applicationPathURI);
            for (Map.Entry<String, String> fileExtension : ASSOCIATED_FILE_EXTENSIONS.entrySet()) {
                boolean registerFileExtension = false;

                // check if file extension is already registered
                if (WindowsUtils.instance().isFileExtensionRegistered(fileExtension.getValue())) {
                    String execCmd = WindowsUtils.instance().getFileExtension(fileExtension.getValue());
                    if (execCmd == null || !execCmd.toLowerCase().contains(applicationPath.toLowerCase())) {
                        registerFileExtension = true;
                    }
                } else {
                    registerFileExtension = true;
                }

                // ask for file extension association
                if (registerFileExtension) {
                    // TODO create dialog
                    int reg = JOptionPane.showConfirmDialog(this, "Register Wolfgang as default editor of files\nwith the extension \"" + fileExtension.getValue() + "\"?", "File Type Association", JOptionPane.YES_NO_OPTION);
                    if (reg == JOptionPane.YES_OPTION) {
                        WindowsUtils.instance().registerFileExtension(fileExtension.getKey(), fileExtension.getValue(), applicationPathURI);
                    }
                }
            }
        }
    }

    @Override
    public String getTitle() {
        return String.format(titleFormat, getNetType(), getNetName());
    }

    private NetType getNetType() {
        return getEditorComponent().getNetContainer().getPetriNet().getNetType();
    }

    private String getNetName() {
        return getEditorComponent().getNetContainer().getPetriNet().getName();
    }

    /**
     * Changes Look and Feel if running on Linux *
     */
    private void setLookAndFeel() {
        if (System.getProperty("os.name").toLowerCase().contains("nux")) {
            try {
                setLocationByPlatform(true);
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            }
        } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            }
        }
    }

    private JComponent getContent() throws Exception {
        if (content == null) {
            content = new JPanel(new BorderLayout());
            content.add(getCenterComponent(), BorderLayout.CENTER);
            EditorProperties.getInstance().addListener(new WolfgangPropertyAdapter() {

                @Override
                public void iconSizeChanged(IconSize size) {
                    content.remove(toolbarScrollPane); 
                    editorComponent.loadEditorToolbar();
                    content.add(getToolbarPanel(), BorderLayout.NORTH);
                    pack();
                }

            });
            setEditorPanels();
            JComponent bottomComponent = getBottomComponent();
            if (bottomComponent != null) {
                content.add(bottomComponent, BorderLayout.PAGE_END);
            }
        }
        return content;
    }

    protected JComponent getCenterComponent() throws Exception {
        if (centerPanel == null) {
            centerPanel = new JPanel(new BorderLayout());
        }
        return centerPanel;
    }

    protected JComponent getRightComponent() throws Exception {
        if (rightPanel == null) {
            rightPanel = new JPanel(new BorderLayout());

        }
        return rightPanel;
    }

    protected JComponent getBottomComponent() throws Exception {
        return null;
    }

    protected void setEditorPanels() throws Exception {
        content.add(getToolbarPanel(), BorderLayout.NORTH);
        centerPanel.add(getEditorPanel(), BorderLayout.CENTER);
        getRightComponent(); //generate rightPanel
        rightPanel.add(editorComponent.getPropertiesView(), BorderLayout.NORTH);
        rightPanel.add(editorComponent.getPropertyCheckView(), BorderLayout.SOUTH);
        centerPanel.add(getRightPanel(),BorderLayout.EAST);

    }
    
        protected JComponent getToolbarPanel() {
        toolbarScrollPane = new JScrollPane(editorComponent.getEditorToolbar(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return toolbarScrollPane;
    }

    protected JComponent getEditorPanel() {
        editorScrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
        return editorScrollPane;
    }
    
        protected JComponent getRightPanel() {
        rightScrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension rightPanelDim = new Dimension(FIX_SIZE_RIGHT_PANEL, MINIMUM_SIZE_EDITOR_PANEL.height);
        rightScrollPane.setPreferredSize(rightPanelDim);
        return rightScrollPane;
    }

    public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
        if (menuBar == null) {
            menuBar = new WGMenuBar(this);
        }
        return menuBar;
    }

    protected class WolfgangKeyboardHandler {

        public WolfgangKeyboardHandler(PNGraphComponent pnGraphComponent) throws Exception {
            installKeyboardActions(pnGraphComponent);
        }

        protected void installKeyboardActions(PNGraphComponent pnGraphComponent) throws Exception {
            InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            SwingUtilities.replaceUIInputMap(pnGraphComponent,
                    JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);

            inputMap = getInputMap(JComponent.WHEN_FOCUSED);
            SwingUtilities.replaceUIInputMap(pnGraphComponent,
                    JComponent.WHEN_FOCUSED, inputMap);
            SwingUtilities.replaceUIActionMap(pnGraphComponent, createActionMap());
        }

        protected InputMap getInputMap(int condition) {
            InputMap map = getBasicInputMap(condition);
            if (condition == JComponent.WHEN_FOCUSED && map != null) {

                int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
                int commandAndShift = commandKey | InputEvent.SHIFT_DOWN_MASK;

                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, commandKey), "save");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, commandAndShift), "saveAs");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, commandKey), "newPT");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, commandAndShift), "newCP");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, commandKey), "open");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, commandKey), "quit");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, commandKey), "settings");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, commandAndShift), "about");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, commandAndShift), "email");        
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, commandKey), "undo");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, commandKey), "redo");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, commandAndShift), "selectVertices");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, commandAndShift), "selectEdges");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, commandKey), "selectPlaces");
                map.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, commandKey), "selectTransitions");
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

        private InputMap getBasicInputMap(int condition) {
            InputMap map = null;

            if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
                map = (InputMap) UIManager.get("ScrollPane.ancestorInputMap");
            } else if (condition == JComponent.WHEN_FOCUSED) {
                map = new InputMap();

                map.put(KeyStroke.getKeyStroke("F2"), "edit");
                map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
                map.put(KeyStroke.getKeyStroke("UP"), "selectParent");
                map.put(KeyStroke.getKeyStroke("DOWN"), "selectChild");
                map.put(KeyStroke.getKeyStroke("RIGHT"), "selectNext");
                map.put(KeyStroke.getKeyStroke("LEFT"), "selectPrevious");
//                map.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "enterGroup");
//                map.put(KeyStroke.getKeyStroke("PAGE_UP"), "exitGroup");
                map.put(KeyStroke.getKeyStroke("HOME"), "home");
                map.put(KeyStroke.getKeyStroke("ENTER"), "expand");
                map.put(KeyStroke.getKeyStroke("BACK_SPACE"), "collapse");
                map.put(KeyStroke.getKeyStroke("control A"), "selectAll");
                map.put(KeyStroke.getKeyStroke("control D"), "selectNone");
                map.put(KeyStroke.getKeyStroke("control X"), "cut");
                map.put(KeyStroke.getKeyStroke("CUT"), "cut");
                map.put(KeyStroke.getKeyStroke("control C"), "copy");
                map.put(KeyStroke.getKeyStroke("COPY"), "copy");
                map.put(KeyStroke.getKeyStroke("control V"), "paste");
                map.put(KeyStroke.getKeyStroke("PASTE"), "paste");
//                map.put(KeyStroke.getKeyStroke("control G"), "group");
//                map.put(KeyStroke.getKeyStroke("control U"), "ungroup");
                map.put(KeyStroke.getKeyStroke("control ADD"), "zoomIn");
                map.put(KeyStroke.getKeyStroke("control SUBTRACT"), "zoomOut");
            }

            return map;
        }

        protected ActionMap createActionMap() throws Exception {
            ActionMap map = createBasicActionMap();
            try {
                map.put("undo", new UndoAction(editorComponent));
                map.put("redo", new RedoAction(editorComponent));
                map.put("printNet", new PrintAction(editorComponent));
                map.put("newPT", new NewPTAction(AbstractWolfgang.this));
                map.put("newCP", new NewCPNAction(AbstractWolfgang.this));
                map.put("open", new LoadAction(AbstractWolfgang.this));
                map.put("save", new SaveAction(AbstractWolfgang.this));
                map.put("saveAs", new SaveAsAction(AbstractWolfgang.this));
                map.put("quit", new ExitAction(AbstractWolfgang.this));
                map.put("settings", new SettingsAction(AbstractWolfgang.this, AbstractWolfgang.this.getWGMenuBar()));
                map.put("about", new AboutAction(AbstractWolfgang.this));
                map.put("email", new SendExceptionsAsEmail(AbstractWolfgang.this));
                map.put("export", new ExportPDFAction(editorComponent));

                int offset = EditorProperties.getInstance().getDefaultPlaceSize() * 4;
                map.put("newNodeLeft", new NewNodeAction(editorComponent, -offset, 0));
                map.put("newNodeRight", new NewNodeAction(editorComponent, offset, 0));
                map.put("newNodeDown", new NewNodeAction(editorComponent, 0, offset));
                map.put("newNodeUp", new NewNodeAction(editorComponent, 0, -offset));

                map.put("moveLeft", new MoveAction(editorComponent, -1, 0));
                map.put("moveRight", new MoveAction(editorComponent, 1, 0));
                map.put("moveDown", new MoveAction(editorComponent, 0, 1));
                map.put("moveUp", new MoveAction(editorComponent, 0, -1));

                int movingGap = 5;
                map.put("bigMoveLeft", new MoveAction(editorComponent, -movingGap, 0));
                map.put("bigMoveRight", new MoveAction(editorComponent, movingGap, 0));
                map.put("bigMoveDown", new MoveAction(editorComponent, 0, movingGap));
                map.put("bigMoveUp", new MoveAction(editorComponent, 0, -movingGap));

                map.put("selectPlaces", new SelectAction(editorComponent, PNComponent.PLACE));
                map.put("selectTransitions", new SelectAction(editorComponent, PNComponent.TRANSITION));
                map.put("selectArcs", new SelectAction(editorComponent, PNComponent.ARC));

            } catch (PropertyException | IOException | ParameterException e) {
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

        private ActionMap createBasicActionMap() {
            ActionMap map = (ActionMap) UIManager.get("ScrollPane.actionMap");

            map.put("edit", mxGraphActions.getEditAction());
            map.put("delete", mxGraphActions.getDeleteAction());
            map.put("home", mxGraphActions.getHomeAction());
//            map.put("enterGroup", mxGraphActions.getEnterGroupAction());
//            map.put("exitGroup", mxGraphActions.getExitGroupAction());
            map.put("collapse", mxGraphActions.getCollapseAction());
            map.put("expand", mxGraphActions.getExpandAction());
            map.put("toBack", mxGraphActions.getToBackAction());
            map.put("toFront", mxGraphActions.getToFrontAction());
            map.put("selectNone", mxGraphActions.getSelectNoneAction());
            map.put("selectAll", mxGraphActions.getSelectAllAction());
            map.put("selectNext", mxGraphActions.getSelectNextAction());
            map.put("selectPrevious", mxGraphActions.getSelectPreviousAction());
            map.put("selectParent", mxGraphActions.getSelectParentAction());
            map.put("selectChild", mxGraphActions.getSelectChildAction());
            map.put("cut", TransferHandler.getCutAction());
            map.put("copy", TransferHandler.getCopyAction());
            map.put("paste", TransferHandler.getPasteAction());
//            map.put("group", mxGraphActions.getGroupAction());
//            map.put("ungroup", mxGraphActions.getUngroupAction());
            map.put("zoomIn", mxGraphActions.getZoomInAction());
            map.put("zoomOut", mxGraphActions.getZoomOutAction());

            return map;
        }
    }
}
