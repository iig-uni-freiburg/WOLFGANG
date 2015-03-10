package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CheckBoundednessAction extends AbstractPropertyCheckAction {

	public CheckBoundednessAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("Bound-\nedness");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<Boundedness, String>() {
			@Override
			public Boundedness doInBackground() throws PNValidationException {
				setIconImage(getLoadingDots());
				AbstractPetriNet net = getEditor().getNetContainer().getPetriNet().clone();
				PNPropertiesChecker.validateBoundedness(net);
				return net.getBoundedness();
			}

			@Override
			public void done() {
				try {
					switch (get()) {
					case BOUNDED:
						setFillColor(PropertyHolds);
						break;
					case UNBOUNDED:
						setFillColor(PropertyDoesntHold);
						break;
					case UNKNOWN:
						setFillColor(PropertyUnknownColor);
						break;
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
