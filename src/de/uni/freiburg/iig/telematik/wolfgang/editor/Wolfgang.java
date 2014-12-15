package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.mg.MGSequenceGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;

public class Wolfgang extends JFrame {

	private static final long serialVersionUID = 6384805641357595809L;
	
	public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024,768);
	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(200, 768);
	private static final Dimension MINIMUM_SIZE_EDITOR_PANEL = new Dimension(824, 768);
	
	private File fileReference = null;
	
	protected PNEditorComponent editorComponent = null;
	private WGMenuBar menuBar = null;
	protected JPanel content = null;
	protected JSplitPane centerPanel = null;
	
	public Wolfgang() throws Exception{
		this(new GraphicalPTNet());
	}
	
	public Wolfgang(AbstractGraphicalPN net) throws Exception{
		this(net, null);
	}
	
	public Wolfgang(AbstractGraphicalPN net, LayoutOption layoutOption) throws Exception{
		setNet(net, layoutOption);
	}
	
	public Wolfgang(AbstractGraphicalPN net, boolean askForLayout) throws Exception{
		setNet(net, askForLayout);
	}
	
	public void setNet(AbstractGraphicalPN net, LayoutOption layoutOption){
		prepareNetInsertion(net);
		if(net.getPetriNet().getNetType().equals(NetType.PTNet)){
			this.editorComponent = new PTNetEditorComponent((GraphicalPTNet) net, layoutOption);
		} else {
			this.editorComponent = new CPNEditorComponent((GraphicalCPN) net, layoutOption);
		}
	}
	
	public void setNet(AbstractGraphicalPN net, boolean askForLayout){
		prepareNetInsertion(net);
		if(net.getPetriNet().getNetType().equals(NetType.PTNet)){
			this.editorComponent = new PTNetEditorComponent((GraphicalPTNet) net, askForLayout);
		} else {
			this.editorComponent = new CPNEditorComponent((GraphicalCPN) net, askForLayout);
		}
	}
	
	private void prepareNetInsertion(AbstractGraphicalPN net){
		Validate.notNull(net);
		NetType netType = net.getPetriNet().getNetType();
		if(!netType.equals(NetType.PTNet) && !netType.equals(NetType.CPN))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unsupported net type: " + net.getPetriNet().getNetType());
		
		if(editorComponent != null){
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
	
	public void setUpGUI() throws PropertyException, IOException, Exception{
		setLookAndFeel();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
		setResizable(true);
		setContentPane(getContent());
		setJMenuBar(getWGMenuBar());
		pack();
		setVisible(true);
	}
	
	@Override
	public String getTitle(){
		return "WOLFGANG";
	}
	
	/** Changes Look and Feel if running on Linux **/
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
				setLocationByPlatform(true);
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {}
		} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {}
		}
	}
	
	private JComponent getContent() throws Exception{
		if(content == null){
			content = new JPanel(new BorderLayout());
			content.add(getCenterComponent(), BorderLayout.CENTER);
			setEditorPanels();
			JComponent bottomComponent = getBottomComponent();
			if(bottomComponent != null)
				content.add(bottomComponent, BorderLayout.PAGE_END);
		}
		return content;
	}
	
	protected JComponent getCenterComponent() throws Exception {
		if(centerPanel == null){
			centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			centerPanel.setDividerLocation(0.8);
		}
		return centerPanel;
	}
	
	protected JComponent getBottomComponent() throws Exception {
		return null;
	}

	protected void setEditorPanels() throws Exception {
		content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
		centerPanel.add(getEditorPanel());
		centerPanel.add(editorComponent.getPropertiesView());
	}
	
	protected JComponent getEditorPanel(){
		JScrollPane scrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
		return scrollPane;
	}
	
	public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
		if(menuBar == null){
			menuBar = new WGMenuBar(this);
		}
		return menuBar;
	}

	
	public static void main(String[] args) throws Exception {
		Wolfgang wolfgang = new Wolfgang(new GraphicalPTNet());
		wolfgang.setUpGUI();
	}

}
