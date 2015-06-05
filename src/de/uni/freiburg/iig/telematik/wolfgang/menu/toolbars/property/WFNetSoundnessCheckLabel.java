package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness.ThreadedWFNetSoundnessChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness.WFNetSoundnessCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class WFNetSoundnessCheckLabel extends PNPropertyCheckLabel<WFNetProperties> {

	private static final long serialVersionUID = -8561240983245503666L;
	
	private boolean checkCWNStructure = true;
	private AbstractMarkingGraph markingGraph = null;
	protected Set<WFNetCheckLabelListener<WFNetProperties>> wfNetCheckListeners = new HashSet<WFNetCheckLabelListener<WFNetProperties>>();


	public WFNetSoundnessCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}
	
	public boolean isCheckCWNStructure() {
		return checkCWNStructure;
	}

	public void setCheckCWNStructure(boolean checkCWNStructure) {
		this.checkCWNStructure = checkCWNStructure;
	}
	
	public void addWFNetCheckListener(WFNetCheckLabelListener<WFNetProperties> listener){
		wfNetCheckListeners.add(listener);
	}
	
	@Override
	public void executorFinished(WFNetProperties result) {
		super.executorFinished(result);
		for(WFNetCheckLabelListener listener: wfNetCheckListeners)
			listener.wfNetCheckFinished(WFNetSoundnessCheckLabel.this, result);
	}

	@Override
	public void executorStopped() {
		super.executorStopped();
		for(WFNetCheckLabelListener listener: wfNetCheckListeners)			
			listener.wfNetCheckStopped(WFNetSoundnessCheckLabel.this, null);
	}

	public AbstractMarkingGraph getMarkingGraph() {
		return markingGraph;
	}

	public void setMarkingGraph(AbstractMarkingGraph markingGraph) {
		this.markingGraph = markingGraph;
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?,?,?,?,?,?,WFNetProperties,?> createNewExecutor() {
		WFNetSoundnessCheckingCallableGenerator generator = new WFNetSoundnessCheckingCallableGenerator((AbstractPTNet<?,?,?,?>) editorComponent.getNetContainer().getPetriNet().clone());
		generator.setCheckCWNStructure(isCheckCWNStructure());
		if(getMarkingGraph() != null)
			generator.setMarkingGraph(getMarkingGraph());
		return new ThreadedWFNetSoundnessChecker(generator);
	}

	@Override
	protected void setPropertyHolds(WFNetProperties calculationResult) {
		this.propertyHolds = calculationResult.hasWFNetStructure == PropertyCheckingResult.TRUE;
		editorComponent.getPropertyCheckView().updateFieldContent(calculationResult, null);
	}

	@Override
	public void executorException(Exception exception) {
		super.executorException(exception);
		if(exception instanceof WFNetException){
			if(((WFNetException) exception).getProperties() != null){
				editorComponent.getPropertyCheckView().updateFieldContent(((WFNetException) exception).getProperties(), exception);
			}
		} else {
			editorComponent.getPropertyCheckView().resetFieldContent();
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(editorComponent), "WFNet Soundness Check Exception", exception, true);
		}
		for(WFNetCheckLabelListener listener: wfNetCheckListeners)
			listener.wfNetCheckException(WFNetSoundnessCheckLabel.this, exception);
	}
	
}
