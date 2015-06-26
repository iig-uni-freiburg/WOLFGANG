package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

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
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangPropertyAdapter;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

public abstract class AbstractWolfgang< P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>,
										M extends AbstractMarking<S>,
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>, 
										Y extends AbstractMarkingGraphRelation<M,X,S>,
										N extends AbstractPetriNet<P,T,F,M,S>, 
										G extends AbstractPNGraphics<P,T,F,M,S>,
										NN extends AbstractGraphicalPN<P,T,F,M,S,N,G>> extends JFrame {

	private static final long serialVersionUID = -7994645960400940612L;
	private static final String titleFormat = "WOLFGANG | %s | %s";
	
	public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024, 768);
//	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(120, 768);
	private static final Dimension MINIMUM_SIZE_EDITOR_PANEL = new Dimension(904, 768);

	private File fileReference = null;

	protected PNEditorComponent editorComponent = null;
	private WGMenuBar menuBar = null;
	protected JPanel content = null;
	protected JSplitPane centerPanel = null;
	private JSplitPane rightPanel;

	
	@SuppressWarnings("rawtypes")
	private static Set<AbstractWolfgang> runningInstances = new HashSet<AbstractWolfgang>();

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
		if(net == null)
			net = newNet();
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
		if (!netType.equals(getAcceptedNetType()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unsupported net type: " + netType);

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
	public void dispose(){
		int saveNet = JOptionPane.showConfirmDialog(null, "Save edited net \"" + getEditorComponent().getNetContainer().getPetriNet().getName()+"\"?", "Save", JOptionPane.YES_NO_OPTION);
		if (saveNet == JOptionPane.YES_OPTION) {
			try {
				SaveAction save = new SaveAction(AbstractWolfgang.this);
				save.actionPerformed(null);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error while saving edited net.", JOptionPane.ERROR_MESSAGE);
			}
		}
		super.dispose();
	}

	public void setUpGUI() throws PropertyException, IOException, Exception {
		addWindowListener(new WindowAdapter() {
			
			
			
			public void windowClosing(WindowEvent e) {
				if(runningInstances.size() > 1){
					int closeAllInstances = JOptionPane.showConfirmDialog(AbstractWolfgang.this, "Close all "+runningInstances.size()+" Wolfgang instanes?", "Multiple running instances", JOptionPane.YES_NO_CANCEL_OPTION);
					if(closeAllInstances == JOptionPane.CANCEL_OPTION){
						return;
					}
					if(closeAllInstances == JOptionPane.YES_OPTION){
						for(@SuppressWarnings("rawtypes") AbstractWolfgang runningInstance: runningInstances){
							runningInstance.dispose();
						}
						runningInstances.clear();
						System.exit(0);
					} else if(closeAllInstances == JOptionPane.NO_OPTION){
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
	}

	@Override
	public String getTitle() {
		return String.format(titleFormat, getNetType(), getNetName());
	}
	
	private NetType getNetType(){
		return getEditorComponent().getNetContainer().getPetriNet().getNetType();
	}

	private String getNetName() {
		return getEditorComponent().getNetContainer().getPetriNet().getName();
	}

	/** Changes Look and Feel if running on Linux **/
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
				setLocationByPlatform(true);
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {
			}
		} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
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
					content.remove(editorComponent.getEditorToolbar());
					editorComponent.loadEditorToolbar();
					content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
					pack();
				}

			});
			setEditorPanels();
			JComponent bottomComponent = getBottomComponent();
			if (bottomComponent != null)
				content.add(bottomComponent, BorderLayout.PAGE_END);
		}
		return content;
	}

	protected JComponent getCenterComponent() throws Exception {
		if (centerPanel == null) {
			centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			centerPanel.setDividerLocation(835);
		}
		return centerPanel;
	}
	
	protected JComponent getRightComponent() throws Exception {
		if (rightPanel == null) {
			rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
			rightPanel.setDividerLocation(450);
		}
		return rightPanel;
	}

	protected JComponent getBottomComponent() throws Exception {
		return null;
	}

	protected void setEditorPanels() throws Exception {
		content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
		centerPanel.add(getEditorPanel());
		centerPanel.add(getRightComponent());
		rightPanel.add(editorComponent.getPropertiesView());
		rightPanel.add(editorComponent.getPropertyCheckView());
	}

	protected JComponent getEditorPanel() {
		JScrollPane scrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
		return scrollPane;
	}

	public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
		if (menuBar == null) {
			menuBar = new WGMenuBar(this);
		}
		return menuBar;
	}

}
