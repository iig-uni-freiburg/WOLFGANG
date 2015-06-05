package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure.ThreadedWFNetStructureChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure.WFNetStructureCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.WFCheckLabelListener;

public class WFNetStructureCheckLabel extends AbstractWFCheckLabel<WFNetProperties> {

	private static final long serialVersionUID = -8561240983245503666L;

	public WFNetStructureCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?,?,?,?,?,?,WFNetProperties,?> createNewExecutor() {
		return new ThreadedWFNetStructureChecker(new WFNetStructureCheckingCallableGenerator((AbstractPTNet<?,?,?,?>) editorComponent.getNetContainer().getPetriNet().clone()));
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
		}
		for(WFCheckLabelListener<WFNetProperties> listener: wfCheckListeners)
			listener.wfCheckException(WFNetStructureCheckLabel.this, exception);
	}
	
	
}
