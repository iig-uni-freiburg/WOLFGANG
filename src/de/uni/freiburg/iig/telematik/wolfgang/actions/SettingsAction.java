package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WGMenuBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WGPropertySettingDialog;

public class SettingsAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349752001L;
	
	protected WGMenuBar menubar;

	@SuppressWarnings("rawtypes")
	public SettingsAction(AbstractWolfgang wolfgang, WGMenuBar menu) throws PropertyException, IOException {
		super(wolfgang, "Exit");
		menubar = menu;
	}
	
	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		try {
			WGPropertySettingDialog.showDialog(SwingUtilities.getWindowAncestor(menubar));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(menubar), "Cannot open settings dialog.\nReason: " + e1.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
		}
}
