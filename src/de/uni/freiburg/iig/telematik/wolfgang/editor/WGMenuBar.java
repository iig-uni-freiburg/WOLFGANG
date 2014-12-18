package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.LoadAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewCPNAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewPTAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAsAction;

public class WGMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4524611329436093661L;
	
	private Wolfgang wolfgang = null;
	
	public WGMenuBar(Wolfgang wolfgang) throws PropertyException, IOException {
		super();
		this.wolfgang = wolfgang;
		add(getFileMenu());
//		add(getSettingsMenu());
	}

	private JMenu getFileMenu() throws PropertyException, IOException {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newSubMenu = new JMenu("New");
	
		JMenuItem createPT = new JMenuItem("PT-Net");
		createPT.addActionListener(new NewPTAction(wolfgang));
		newSubMenu.add(createPT);
		
		
		JMenuItem createCPN = new JMenuItem("CP-Net");
		createCPN.addActionListener(new NewCPNAction(wolfgang));
		newSubMenu.add(createCPN);
		fileMenu.add(newSubMenu);
		
		
		JMenuItem load = new JMenuItem("Open .pnml in new Window");
		load.addActionListener(new LoadAction(wolfgang));
		fileMenu.add(load);
		JMenuItem save = new JMenuItem("Save");
		
		save.addActionListener(new SaveAction(wolfgang));
		fileMenu.add(save);
//		if(wolfgang.getFileReference() == null)
//			save.setEnabled(false);
//		else
//			save.setEnabled(true);
		
		JMenuItem saveAS = new JMenuItem("Save as...");
		//save.setRolloverEnabled(true);
		saveAS.addActionListener(new SaveAsAction(wolfgang));
		fileMenu.add(saveAS);
		
	
		

		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wolfgang.dispose();
			}
		});
		fileMenu.add(exit);
		return fileMenu;
	}




	private JMenu getSettingsMenu() {
		JMenu settings = new JMenu("Settings");
		//TODO:
		return settings;
	}

}
