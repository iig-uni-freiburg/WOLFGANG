package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.validity.CPNValidity;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CPNValidityCheckLabel extends AbstractValidityCheckLabel {

	private static final long serialVersionUID = -4780367090327356516L;

	public CPNValidityCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected void checkValidity(AbstractPetriNet net) throws PNValidationException {
		CPNValidity.checkValidity((AbstractCPN<?,?,?,?>) net);		
	}
}
