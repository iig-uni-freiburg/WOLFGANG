package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;

public class ExitAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349752001L;

	@SuppressWarnings("rawtypes")
	public ExitAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "Exit");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		     wolfgang.dispose();
		}
}
