package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;

public class NewCPNAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 8830243374604859523L;

	@SuppressWarnings("rawtypes")
	public NewCPNAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "New CPN");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		wolfgang = new WolfgangCPN();
		wolfgang.setUpGUI();
	}

}
