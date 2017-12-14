package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.ExceptionDialog;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;

public abstract class AbstractNewNetAction<N extends AbstractGraphicalPN> extends AbstractWolfgangAction {

	private static final long serialVersionUID = 2651449758330094274L;

	N net = null;

	protected AbstractNewNetAction(AbstractWolfgang wolfgang) {
		super(wolfgang);
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		String netName = requestFileName("Please choose a name for the new net:", "New Petri-Net");
		if (netName != null) {
			try {
				setUpGui(netName);
			} catch (Exception ex) {
				ExceptionDialog.showException(SwingUtilities.getWindowAncestor(wolfgang), "Could not add net.", ex);
			}
		}
	}

	private void setUpGui(String netName) {
		net = createNewGraphicalPN();
		net.getPetriNet().setName(netName);
		
	}

	public N getNet() {
		return net;
	}

	protected abstract N createNewGraphicalPN();

	private String requestFileName(String message, String title) {
		return FileNameDialog.showDialog(wolfgang, message, title, false);
	}

}