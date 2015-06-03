package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CPNPropertyCheckToolbar extends AbstractPropertyCheckToolbar {

	private static final long serialVersionUID = -5578367037954223835L;
	private CPNValidityCheckLabel cpnValidityCheckLabel;

	public CPNPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificCheckLabels(PNEditorComponent pnEditor) {
		cpnValidityCheckLabel = new CPNValidityCheckLabel(pnEditor, "Validity");
		add(cpnValidityCheckLabel);		
	}

	@Override
	public void labelCalculationFinished(Object sender, Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void labelCalculationException(Object sender, Exception exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void labelCalculationStopped(Object sender, Object result) {
		// TODO Auto-generated method stub
		
	}
	
	

}
