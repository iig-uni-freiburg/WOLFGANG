package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckResult;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractWFCheckToolbar<P> extends AbstractPNPropertyCheckToolbar implements WFCheckLabelListener<P>{

	private static final long serialVersionUID = 6739944713478494578L;
	
	protected AbstractWFCheckLabel<P> structureCheckLabel;
	protected AbstractWFCheckLabel<P> soundnessCheckLabel;
	
	protected boolean checkStructure = true;
	protected boolean checkSoundness = true;

	public AbstractWFCheckToolbar(PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(pnEditor, orientation);
	}
	
	@Override
	protected void addNetSpecificCheckLabels(PNEditorComponent pnEditor) {
		structureCheckLabel = createStructureCheckLabel();
		add(structureCheckLabel);
		structureCheckLabel.setEnabled(false);
		structureCheckLabel.addWFCheckListener(this);

		soundnessCheckLabel = createSoundnessCheckLabel();
		add(soundnessCheckLabel);
		soundnessCheckLabel.setEnabled(false);
		soundnessCheckLabel.addWFCheckListener(this);
	}
	

	@Override
	protected void validityCheckFinished(Boolean result) {
		super.validityCheckFinished(result);
		structureCheckLabel.setEnabled(result != null ? result : false);
	}

	@Override
	protected void boundednessCheckFinished(BoundednessCheckResult result) {
		super.boundednessCheckFinished(result);
		Boundedness boundedness = result.getBoundedness();
		soundnessCheckLabel.setEnabled(!checkStructure && (boundedness != null ? result.getBoundedness() == Boundedness.BOUNDED : false));
	}

	@Override
	protected void validityCheckStopped(Boolean result) {
		super.validityCheckStopped(result);
		structureCheckLabel.setEnabled(result != null ? result : false);
	}

	@Override
	protected void boundednessCheckStopped(BoundednessCheckResult result) {
		super.boundednessCheckStopped(result);
		if(result == null){
			soundnessCheckLabel.setEnabled(false);
		} else {
			Boundedness boundedness = result.getBoundedness();
			soundnessCheckLabel.setEnabled(boundedness != null ? boundedness == Boundedness.BOUNDED : false);
		}
	}
	
	@Override
	public void wfCheckStopped(Object sender, P result) {
		if (sender == structureCheckLabel) {
			structureCheckStopped();
		} else if (sender == soundnessCheckLabel) {
			soundnessCheckStopped();
		}
	}
	
	protected void structureCheckStopped(){
		soundnessCheckLabel.setEnabled(false);
//		wfNetSoundnessCheckLabel.executorStopped();
	}
	
	protected void soundnessCheckStopped(){}
	
	@Override
	protected void reactOnNetChange() throws Exception {
		super.reactOnNetChange();
		structureCheckLabel.reset();
		structureCheckLabel.setEnabled(false);
		soundnessCheckLabel.reset();
		soundnessCheckLabel.setEnabled(false);
		checkStructure = true;
		checkSoundness = true;
	}

	protected abstract AbstractWFCheckLabel<P> createStructureCheckLabel();

	protected abstract AbstractWFCheckLabel<P> createSoundnessCheckLabel();
	
	
}
