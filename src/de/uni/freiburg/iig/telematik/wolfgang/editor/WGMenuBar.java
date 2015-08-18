package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.ExitAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.LoadAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewCPNAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.NewPTAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SaveAsAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.SettingsAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.help.AboutAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.help.SendExceptionsAsEmail;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WGMenuBar extends JMenuBar {

	private static final long serialVersionUID = -4524611329436093661L;
	
	int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    int commandAndShift = commandKey | InputEvent.SHIFT_DOWN_MASK;
	
	private AbstractWolfgang wolfgang = null;
	
	public WGMenuBar(AbstractWolfgang wolfgang) throws PropertyException, IOException {
		super();
		this.wolfgang = wolfgang;
		add(getFileMenu());
		add(getSettingsMenu());
        add(getHelpEntry());
	}

	private JMenu getFileMenu() throws PropertyException, IOException {
        
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newSubMenu = new JMenu("New");
	
		JMenuItem createPT = new JMenuItem("PT-Net");
		createPT.addActionListener(new NewPTAction(wolfgang));
		createPT.setAccelerator(KeyStroke.getKeyStroke('N', commandKey));
		newSubMenu.add(createPT);
		
		
		JMenuItem createCPN = new JMenuItem("CP-Net");
		createCPN.addActionListener(new NewCPNAction(wolfgang));
		createCPN.setAccelerator(KeyStroke.getKeyStroke('N', commandAndShift));
		newSubMenu.add(createCPN);
		fileMenu.add(newSubMenu);
		
		
		JMenuItem load = new JMenuItem("Open .pnml in new Window");
		load.setAccelerator(KeyStroke.getKeyStroke('O', commandKey));
		load.addActionListener(new LoadAction(wolfgang));
		fileMenu.add(load);
		
		JMenuItem save = new JMenuItem("save");
		save.setAccelerator(KeyStroke.getKeyStroke('S', commandKey));
		save.addActionListener(new SaveAction(wolfgang));
		fileMenu.add(save);
//		if(wolfgang.getFileReference() == null)
//			save.setEnabled(false);
//		else
//			save.setEnabled(true);
		
		JMenuItem saveAS = new JMenuItem("Save as...");
		//save.setRolloverEnabled(true);
		saveAS.addActionListener(new SaveAsAction(wolfgang));
		saveAS.setAccelerator(KeyStroke.getKeyStroke('S', commandAndShift));
		fileMenu.add(saveAS);
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ExitAction(wolfgang));
		quit.setAccelerator(KeyStroke.getKeyStroke('Q', commandKey));
		fileMenu.add(quit);
		return fileMenu;
	}




	private JMenu getSettingsMenu() throws PropertyException, IOException {
		JMenu settings = new JMenu("Settings");
		JMenuItem settingsItem = new JMenuItem("Edit Wolfgang properties...");
		settingsItem.setAccelerator(KeyStroke.getKeyStroke('M', commandKey));
		settingsItem.addActionListener(new SettingsAction(wolfgang, WGMenuBar.this));
		settings.add(settingsItem);
		return settings;
	}
        
        private JMenu getHelpEntry() {
		JMenu helpEntry = new JMenu("Help");
            try {
            	JMenuItem about = new JMenuItem("About");
            	about.addActionListener(new AboutAction(wolfgang));
            	about.setAccelerator(KeyStroke.getKeyStroke('A', commandAndShift));
         
            	JMenuItem error = new JMenuItem("Error");
            	error.addActionListener(new SendExceptionsAsEmail(wolfgang));
            	error.setAccelerator(KeyStroke.getKeyStroke('M', commandAndShift));
            	
            	helpEntry.add(about);
            	helpEntry.add(error);

            } catch (PropertyException ex) {
                Logger.getLogger(WGMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WGMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
		return helpEntry;
	}

}
