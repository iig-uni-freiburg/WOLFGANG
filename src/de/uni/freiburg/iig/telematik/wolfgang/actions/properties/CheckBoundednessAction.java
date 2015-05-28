package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.CWNChecker.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
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

		SwingWorker worker = new SwingWorker<CWNProperties, String>() {
			@Override
			public CWNProperties doInBackground() {
				setIconImage(getLoadingDots());
				AbstractPetriNet net = getEditor().getNetContainer().getPetriNet().clone();
				CWNProperties result = new CWNProperties();
//				try {
//					PNPropertiesChecker.validateBoundedness(net);
//					result.isBounded = PropertyCheckingResult.fromBoundedness(net.getBoundedness());
//				} catch (PNValidationException e1) {
//					result.isBounded = PropertyCheckingResult.FALSE;
//					result.exception = new PNSoundnessException("Net is not bounded.");
//					return result;
//				}
				return result;

			}

			@Override
			public void done() {
				try {
					getEditor().getPropertyCheckView().updateBoundedness(get().isBounded, get().exception);
					switch(get().isBounded){
					case FALSE:
						setFillColor(PropertyDoesntHold);
						break;
					case TRUE:
						setFillColor(PropertyHolds);
						break;
					case UNKNOWN:
						setFillColor(PropertyUnknownColor);
						break;
					default:
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
