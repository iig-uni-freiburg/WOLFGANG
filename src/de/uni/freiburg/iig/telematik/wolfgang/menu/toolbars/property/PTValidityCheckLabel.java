package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.validity.PTNetValidity;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class PTValidityCheckLabel extends AbstractValidityCheckLabel {

	public PTValidityCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected void checkValidity(AbstractPetriNet net) throws PNValidationException {
		PTNetValidity.checkValidity((AbstractPTNet<?, ?, ?, ?>) net);		
	}
}
