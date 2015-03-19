package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PropertyCheckView;

public class CheckCWNSoundnessAction extends AbstractPropertyCheckAction {

	protected mxIEventListener changeTracker = new mxIEventListener() {
		public void invoke(Object source, mxEventObject evt) {

		}
	};
	
	public CheckCWNSoundnessAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
		getGraph().getModel().addListener(mxEvent.CHANGE, changeTracker);

	}

	@Override
	protected void setInitialFill() {
		setPropertyString("sound\nCWN");
		setFillColor(PropertyUnknownColor);
		
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<CWNProperties, String>() {
			@Override
			public CWNProperties doInBackground() {
				setIconImage(getLoadingDots());
				AbstractCPN net = (AbstractCPN) getEditor().getNetContainer().getPetriNet().clone();
			
				return CWNChecker.checkCWNSoundness(net, true, null);

			}

			@Override
			public void done() {
					try {
						getEditor().getPropertyCheckView().updateCWNProperties(get());
						if(get().exception != null){
							setFillColor(PropertyDoesntHold);
						}
						else {
							setFillColor(PropertyHolds);
						}
						switch(get().hasCWNStructure){
						case FALSE:
							setFillColor(PropertyDoesntHold);
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyDoesntHold);
							break;
						case TRUE:
							setFillColor(PropertyHolds);
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckBoundednessAction().setFillColor(PropertyHolds);
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyHolds);
							break;
						case UNKNOWN:
							setFillColor(PropertyUnknownColor);
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyUnknownColor);
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
