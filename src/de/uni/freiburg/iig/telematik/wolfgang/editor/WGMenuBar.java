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
		setUpGui();
	}

	private void setUpGui() throws PropertyException, IOException {
		add(getFileMenu());
		add(getSettingsMenu());
        add(getHelpEntry());
		
	}

	private JMenu getFileMenu() throws PropertyException, IOException {
        
		JMenu mnuFile = new JMenu("File");
		
		JMenuItem mniNewSubMenu = new JMenu("New");
	
		JMenuItem mniCreatePT = new JMenuItem("PT-Net");
		mniCreatePT.addActionListener(new NewPTAction(wolfgang));
		mniCreatePT.setAccelerator(KeyStroke.getKeyStroke('N', commandKey));
		mniNewSubMenu.add(mniCreatePT);
		
		
		JMenuItem mniCreateCPN = new JMenuItem("CP-Net");
		mniCreateCPN.addActionListener(new NewCPNAction(wolfgang));
		mniCreateCPN.setAccelerator(KeyStroke.getKeyStroke('N', commandAndShift));
		mniNewSubMenu.add(mniCreateCPN);
		mnuFile.add(mniNewSubMenu);
		
		
		JMenuItem mniLoad = new JMenuItem("Open .pnml in new Window");
		mniLoad.setAccelerator(KeyStroke.getKeyStroke('O', commandKey));
		mniLoad.addActionListener(new LoadAction(wolfgang));
		mnuFile.add(mniLoad);
		
		JMenuItem mniSave = new JMenuItem("save");
		mniSave.setAccelerator(KeyStroke.getKeyStroke('S', commandKey));
		mniSave.addActionListener(new SaveAction(wolfgang));
		mnuFile.add(mniSave);
//		if(wolfgang.getFileReference() == null)
//			save.setEnabled(false);
//		else
//			save.setEnabled(true);
		
		JMenuItem mniSaveAS = new JMenuItem("Save as...");
		//save.setRolloverEnabled(true);
		mniSaveAS.addActionListener(new SaveAsAction(wolfgang));
		mniSaveAS.setAccelerator(KeyStroke.getKeyStroke('S', commandAndShift));
		mnuFile.add(mniSaveAS);
		
		JMenuItem mniQuit = new JMenuItem("Quit");
		mniQuit.addActionListener(new ExitAction(wolfgang));
		mniQuit.setAccelerator(KeyStroke.getKeyStroke('Q', commandKey));
		mnuFile.add(mniQuit);
		return mnuFile;
	}




	private JMenu getSettingsMenu() throws PropertyException, IOException {
		JMenu mnuSettings = new JMenu("Settings");
		JMenuItem mniSettings = new JMenuItem("Edit Wolfgang properties...");
		mniSettings.setAccelerator(KeyStroke.getKeyStroke('M', commandKey));
		mniSettings.addActionListener(new SettingsAction(wolfgang, WGMenuBar.this));
		mnuSettings.add(mniSettings);
		return mnuSettings;
	}
        
        private JMenu getHelpEntry() {
		JMenu mnuHelp = new JMenu("Help");
            try {
            	JMenuItem mniAbout = new JMenuItem("About");
            	mniAbout.addActionListener(new AboutAction(wolfgang));
            	mniAbout.setAccelerator(KeyStroke.getKeyStroke('A', commandAndShift));
         
            	JMenuItem mniError = new JMenuItem("Error");
            	mniError.addActionListener(new SendExceptionsAsEmail(wolfgang));
            	mniError.setAccelerator(KeyStroke.getKeyStroke('M', commandAndShift));
            	
            	mnuHelp.add(mniAbout);
            	mnuHelp.add(mniError);

            } catch (PropertyException ex) {
                Logger.getLogger(WGMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WGMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
		return mnuHelp;
	}

}
