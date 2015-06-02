package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class PTNetPropertyCheckToolbar extends AbstractPropertyCheckToolbar {

	private static final long serialVersionUID = -685698026002872162L;
	private PTValidityCheckLabel ptValidityCheckLabel;
	private WFNetCheckLabel wfNetCheckLabel;

	public PTNetPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificCheckLabels(PNEditorComponent pnEditor) {
		ptValidityCheckLabel = new PTValidityCheckLabel(pnEditor, "Validity");
		add(ptValidityCheckLabel);	
		
		wfNetCheckLabel = new WFNetCheckLabel(pnEditor, "WF-Net");
		add(wfNetCheckLabel);	
	}
	
	

}
