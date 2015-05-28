package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CheckValidityAction extends AbstractPropertyCheckAction {

	public CheckValidityAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("Valid-\nity");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<PNValidationException, String>() {
			@Override
			public PNValidationException doInBackground() {
//				setIconImage(getLoadingDots());
//				AbstractPetriNet net = getEditor().getNetContainer().getPetriNet().clone();
//				try {
//					net.checkValidity();
//				} catch (PNValidationException e) {
//					return e;
//				}
				return null;
			}

			@Override
			public void done() {
				try {
					if (get() == null)// hasWFNetStructure
						setFillColor(PropertyHolds);
					else {
						JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net is not valid", JOptionPane.ERROR_MESSAGE);
						setFillColor(PropertyDoesntHold);
					}
				} catch (InterruptedException e) {
					setFillColor(PropertyUnknownColor);
				} catch (ExecutionException e) {
					setFillColor(PropertyUnknownColor);
				}
			};
		};
		setWorker(worker);
	}

}
