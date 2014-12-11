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
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
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
	private JPanel editorComponentPanel = null;
	private JPanel editorPropertiesPanel = null;
	
	public Wolfgang() throws PropertyException, IOException{
		super();
		this.editorComponent = new PTNetEditorComponent();
		setLookAndFeel();
		setUpGUI();
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
			if(editorComponent != null){
				
			}
			content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
	
			JSplitPane centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			
			JScrollPane scrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
			centerPanel.add(scrollPane);
			
			centerPanel.add(editorComponent.getPropertiesView());
			centerPanel.setDividerLocation(0.8);
			
			content.add(centerPanel, BorderLayout.CENTER);
		}
		return content;
	}
	
	private setEditorPanels(){
		
	}
	
	public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
		if(menuBar == null){
			menuBar = new WGMenuBar(this);
		}
		return menuBar;
	}
	
	public static void main(String[] args) throws PropertyException, IOException {
		new Wolfgang();
	}

}
