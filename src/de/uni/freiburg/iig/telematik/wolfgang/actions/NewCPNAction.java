package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.Wolfgang;

public class NewCPNAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 8830243374604859523L;

	public NewCPNAction(Wolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "New CPN");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		wolfgang = new Wolfgang(new GraphicalCPN());
		wolfgang.setUpGUI();
	}

}
