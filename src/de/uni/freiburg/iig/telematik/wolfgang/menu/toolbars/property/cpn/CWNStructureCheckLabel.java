package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.ThreadedCWNStructureChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.WFCheckLabelListener;

public class CWNStructureCheckLabel extends AbstractWFCheckLabel<CWNProperties> {

	private static final long serialVersionUID = -8561240983245503666L;
	
	public CWNStructureCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?,?,?,?,?,?,CWNProperties,?> createNewExecutor() {
		return new ThreadedCWNStructureChecker(new CWNStructureCheckingCallableGenerator((AbstractCPN<?,?,?,?>) editorComponent.getNetContainer().getPetriNet().clone()));
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
		}
		for(WFCheckLabelListener<CWNProperties> listener: wfCheckListeners)
			listener.wfCheckException(CWNStructureCheckLabel.this, exception);
	}
	
	
}
