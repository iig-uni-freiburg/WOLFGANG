package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
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
	
	private PNEditorComponent editorComponent = null;
	private WGMenuBar menuBar = null;
	private JPanel content = null;
	private JSplitPane centerPanel = null;
	
	public Wolfgang() throws PropertyException, IOException{
		this(new GraphicalPTNet());
	}
	
	public Wolfgang(AbstractGraphicalPN net) throws PropertyException, IOException{
		setLookAndFeel();
		setNet(net, null);
		setUpGUI();
	}
	
	public Wolfgang(AbstractGraphicalPN net, LayoutOption layoutOption) throws PropertyException, IOException{
		setLookAndFeel();
		setNet(net, layoutOption);
		setUpGUI();
	}
	
	public void setNet(AbstractGraphicalPN net, LayoutOption layoutOption){
		Validate.notNull(net);
		switch(net.getPetriNet().getNetType()){
		case CPN:
			if(editorComponent != null){
				centerPanel.remove(editorComponent);
				centerPanel.remove(editorComponent.getPropertiesView());
			}
			this.editorComponent = new CPNEditorComponent((GraphicalCPN) net, layoutOption);
			break;
		case PTNet:
			if(editorComponent != null){
				centerPanel.remove(editorComponent);
				centerPanel.remove(editorComponent.getPropertiesView());
			}
			this.editorComponent = new PTNetEditorComponent((GraphicalPTNet) net, layoutOption);
			break;
		default: throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unsupported net type: " + net.getPetriNet().getNetType());
		}
	}
	
	public PNEditorComponent getEditorComponent() {
		return editorComponent;
	}

	public File getFileReference() {
		return fileReference;
	}
	
	private void setUpGUI() throws PropertyException, IOException{
		setTitle("WOLFGANG");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
		setResizable(true);
		setContentPane(getContent());
		setJMenuBar(getWGMenuBar());
		pack();
		setVisible(true);
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
	
	private JComponent getContent(){
		if(content == null){
			content = new JPanel(new BorderLayout());
			centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			content.add(centerPanel, BorderLayout.CENTER);
			centerPanel.setDividerLocation(0.8);
			setEditorPanels();
		}
		return content;
	}
	
	private void setEditorPanels(){
		content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
		centerPanel.add(scrollPane);
		centerPanel.add(editorComponent.getPropertiesView());
	}
	
	public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
		if(menuBar == null){
			menuBar = new WGMenuBar(this);
		}
		return menuBar;
	}
	
	public static void main(String[] args) throws PropertyException, IOException {
		new Wolfgang(new GraphicalPTNet());
	}

}
