package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CheckSoundnessAction extends AbstractPropertyCheckAction {

	public CheckSoundnessAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("Sound-\nness");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<PNException, String>() {
			@Override
			public PNException doInBackground() {
				setIconImage(getLoadingDots());
				AbstractPetriNet net = getEditor().getNetContainer().getPetriNet().clone();
				try {
					net.checkSoundness();
				} catch (PNException e) {
					return e;
				}
				return null;
			}

			@Override
			public void done() {

				try {
					if (get() == null)// hasWFNetStructure
						setFillColor(PropertyHolds);
					else {
						if (get() instanceof PNValidationException)
							JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net is not valid", JOptionPane.ERROR_MESSAGE);
						if (get() instanceof PNSoundnessException)
							JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net is not sound", JOptionPane.ERROR_MESSAGE);
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
