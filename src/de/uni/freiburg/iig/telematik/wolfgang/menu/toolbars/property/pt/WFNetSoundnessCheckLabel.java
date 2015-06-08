package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.pt;

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
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.WFCheckLabelListener;

public class WFNetSoundnessCheckLabel extends AbstractWFCheckLabel<WFNetProperties> {

	private static final long serialVersionUID = -8561240983245503666L;
	
	private boolean checkWFNetStructure = true;
	private AbstractMarkingGraph markingGraph = null;

	public WFNetSoundnessCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}
	
	public boolean isCheckWFNetStructure() {
		return checkWFNetStructure;
	}

	public void setCheckWFNetStructure(boolean checkWFNetStructure) {
		this.checkWFNetStructure = checkWFNetStructure;
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
		generator.setCheckCWNStructure(isCheckWFNetStructure());
		if(getMarkingGraph() != null){
			generator.setMarkingGraph(getMarkingGraph());
			}
		return new ThreadedWFNetSoundnessChecker(generator);
	}

	@Override
	protected void setPropertyHolds(WFNetProperties calculationResult) {
		this.propertyHolds = calculationResult.isSoundWFNet == PropertyCheckingResult.TRUE;
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
		for(WFCheckLabelListener<WFNetProperties> listener: wfCheckListeners)
			listener.wfCheckException(WFNetSoundnessCheckLabel.this, exception);
	}
	
}
