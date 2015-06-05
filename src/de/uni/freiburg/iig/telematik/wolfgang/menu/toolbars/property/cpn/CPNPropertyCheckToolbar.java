package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.cpn;

import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractValidityCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckToolbar;

public class CPNPropertyCheckToolbar extends AbstractWFCheckToolbar<CWNProperties> {

	private static final long serialVersionUID = -5578367037954223835L;

	public CPNPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(pnEditor, orientation);
	}

	@Override
	public void wfCheckFinished(Object sender, CWNProperties result) {
		if (sender == structureCheckLabel) {
			soundnessCheckLabel.setEnabled(markingGraph != null && result.hasCWNStructure == PropertyCheckingResult.TRUE);
		} else if (sender instanceof CWNSoundnessCheckLabel) {
			
		}
	}

	@Override
	public void wfCheckException(Object sender, Exception exception) {
		CWNProperties properties = null;
		if(sender == structureCheckLabel){
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "CWN Structure Check Exception", exception, true);
			properties = ((CWNException) exception).getProperties();
			if (properties == null)
				return;
			this.checkStructure = !(properties.hasCWNStructure == PropertyCheckingResult.TRUE);
		} else if(sender == soundnessCheckLabel){
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "CWN Soundness Check Exception", exception, true);
			properties = ((CWNException) exception).getProperties();
			if (properties == null)
				return;
			this.checkStructure = !(properties.hasCWNStructure == PropertyCheckingResult.TRUE);
			this.checkSoundness = !(properties.isSoundCWN == PropertyCheckingResult.TRUE);
			this.markingGraph = properties.markingGraph;	
		}
	}

	@Override
	protected AbstractWFCheckLabel<CWNProperties> createStructureCheckLabel() {
		return new CWNStructureCheckLabel(pnEditor, "CWN\nStructure");
	}


	@Override
	protected AbstractWFCheckLabel<CWNProperties> createSoundnessCheckLabel() {
		return new CWNSoundnessCheckLabel(pnEditor, "CWN\nSoundness");
	}

	@Override
	protected AbstractValidityCheckLabel createValidityCheckLabel() {
		CPNValidityCheckLabel validityCheckLabel = new CPNValidityCheckLabel(pnEditor, "Validity");
		validityCheckLabel.setEnabled(true);
		return validityCheckLabel;
	}
	
}
