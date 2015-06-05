package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.cpn;

import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness.CWNSoundnessCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness.ThreadedCWNSoundnessChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.WFCheckLabelListener;

public class CWNSoundnessCheckLabel extends AbstractWFCheckLabel<CWNProperties> {

	private static final long serialVersionUID = -8561240983245503666L;
	
	private boolean checkCWNStructure = true;
	private AbstractMarkingGraph markingGraph = null;

	public CWNSoundnessCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}
	
	public boolean isCheckCWNStructure() {
		return checkCWNStructure;
	}

	public void setCheckCWNStructure(boolean checkCWNStructure) {
		this.checkCWNStructure = checkCWNStructure;
	}

	public AbstractMarkingGraph getMarkingGraph() {
		return markingGraph;
	}

	public void setMarkingGraph(AbstractMarkingGraph markingGraph) {
		this.markingGraph = markingGraph;
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?,?,?,?,?,?,CWNProperties,?> createNewExecutor() {
		CWNSoundnessCheckingCallableGenerator generator = new CWNSoundnessCheckingCallableGenerator((AbstractCPN<?,?,?,?>) editorComponent.getNetContainer().getPetriNet().clone());
		generator.setCheckCWNStructure(isCheckCWNStructure());
		if(getMarkingGraph() != null)
			generator.setMarkingGraph(getMarkingGraph());
		return new ThreadedCWNSoundnessChecker(generator);
	}

	@Override
	protected void setPropertyHolds(CWNProperties calculationResult) {
		this.propertyHolds = calculationResult.hasCWNStructure == PropertyCheckingResult.TRUE;
		editorComponent.getPropertyCheckView().updateFieldContent(calculationResult, null);
	}

	@Override
	public void executorException(Exception exception) {
		super.executorException(exception);
		if(exception instanceof CWNException){
			if(((CWNException) exception).getProperties() != null){
				editorComponent.getPropertyCheckView().updateFieldContent(((CWNException) exception).getProperties(), exception);
			}
		} else {
			editorComponent.getPropertyCheckView().resetFieldContent();
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(editorComponent), "CWN Soundness Check Exception", exception, true);
		}
		for(WFCheckLabelListener<CWNProperties> listener: wfCheckListeners)
			listener.wfCheckException(CWNSoundnessCheckLabel.this, exception);
	}
	
}
