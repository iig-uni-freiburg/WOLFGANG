package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;

public class WGMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4524611329436093661L;
	
	private Wolfgang wolfgang = null;
	
	public WGMenuBar(Wolfgang wolfgang) throws PropertyException, IOException {
		super();
		this.wolfgang = wolfgang;
		add(getFileMenu());
		add(getSettingsMenu());
	}

	private JMenu getFileMenu() throws PropertyException, IOException {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem save = new JMenuItem("Save");
		//save.setRolloverEnabled(true);
		save.addActionListener(new SaveAction(wolfgang));
		fileMenu.add(save);
		
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
