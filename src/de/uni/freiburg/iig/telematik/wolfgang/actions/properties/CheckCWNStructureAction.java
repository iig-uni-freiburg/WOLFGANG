package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.CWNChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;

public class CheckCWNStructureAction extends AbstractPropertyCheckAction {

	public CheckCWNStructureAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("struct.\nCWN");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<CWNProperties, String>() {
			@Override
			public CWNProperties doInBackground() {
				setIconImage(getLoadingDots());
				AbstractCPN net = (AbstractCPN) getEditor().getNetContainer().getPetriNet().clone();
			
				return CWNChecker.checkCWNStructure(net);
			}

			@Override
			public void done() {
				try {
					getEditor().getPropertyCheckView().updateCWNStructuredness(get().hasCWNStructure,get().validInOutPlaces, get().strongConnectedness, get().validInitialMarking, get().controlFlowDependency, get().exception);
					switch(get().hasCWNStructure){
					case FALSE:
						setFillColor(PropertyDoesntHold);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(PropertyDoesntHold);
						break;
					case TRUE:
						setFillColor(PropertyHolds);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(PropertyUnknownColor);
						break;
					case UNKNOWN:
						setFillColor(PropertyUnknownColor);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(PropertyUnknownColor);
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
