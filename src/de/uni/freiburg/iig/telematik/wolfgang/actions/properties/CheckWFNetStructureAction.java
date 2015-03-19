package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.WFNetChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.WFNetProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CheckWFNetStructureAction extends AbstractPropertyCheckAction {

	public CheckWFNetStructureAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("WF-Str\nucture");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<WFNetProperties, String>() {
			@Override
			public WFNetProperties doInBackground() {
				setIconImage(getLoadingDots());
				AbstractPTNet net = (AbstractPTNet) getEditor().getNetContainer().getPetriNet().clone();
				return WFNetChecker.checkWFNetStructure(net);
			}

			@Override
			public void done() {
				try {
					getEditor().getPropertyCheckView().updateWFNetStructuredness(get().hasWFNetStructure,get().validInOutPlaces, get().strongConnectedness, get().exception);
					switch(get().hasWFNetStructure){
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
