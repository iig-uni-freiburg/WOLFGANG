package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;

public class NewPTAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	@SuppressWarnings("rawtypes")
	public NewPTAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "New PT");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		wolfgang = new WolfgangPT();
		wolfgang.setUpGUI();
	}
}
