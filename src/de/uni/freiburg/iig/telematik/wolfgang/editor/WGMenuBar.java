package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.LoadAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewCPNAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewPTAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAsAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WGPropertySettingDialog;

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
			     int question = JOptionPane.showConfirmDialog(null, "Save the net?",
			    	  "Save", JOptionPane.YES_NO_OPTION);
			     if(question == JOptionPane.YES_OPTION)
			     {
						SaveAction save;
						try {
							save = new SaveAction(wolfgang);
							save.actionPerformed(null);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(wolfgang, ex.getMessage(), "Saving Error", JOptionPane.ERROR_MESSAGE);
						}
					
			
			     }
			     
			     wolfgang.dispose();
			}
		});
		fileMenu.add(exit);
		return fileMenu;
	}




	private JMenu getSettingsMenu() {
		JMenu settings = new JMenu("Settings");
		JMenuItem settingsItem = new JMenuItem("Edit Wolfgang properties...");
		settingsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					WGPropertySettingDialog.showDialog(SwingUtilities.getWindowAncestor(WGMenuBar.this));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(WGMenuBar.this), "Cannot open settings dialog.\nReason: " + e1.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		settings.add(settingsItem);
		return settings;
	}

}
