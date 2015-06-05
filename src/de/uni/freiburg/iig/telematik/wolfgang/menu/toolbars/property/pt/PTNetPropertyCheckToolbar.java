package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.pt;

import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractValidityCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.AbstractWFCheckToolbar;

public class PTNetPropertyCheckToolbar extends AbstractWFCheckToolbar<WFNetProperties> {

	private static final long serialVersionUID = -685698026002872162L;
	
	public PTNetPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(pnEditor, orientation);
	}
	
	@Override
	public void wfCheckFinished(Object sender, WFNetProperties result) {
		if (sender instanceof WFNetStructureCheckLabel) {
			soundnessCheckLabel.setEnabled(markingGraph != null && result.hasWFNetStructure == PropertyCheckingResult.TRUE);
		} else if (sender instanceof WFNetSoundnessCheckLabel) {
			
		}
	}

	@Override
	public void wfCheckException(Object sender, Exception exception) {
		WFNetProperties properties = null;
		if(sender == structureCheckLabel){
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "WFNet Structure Check Exception", exception, true);
			properties = ((WFNetException) exception).getProperties();
			if (properties == null)
				return;
			this.checkStructure = !(properties.hasWFNetStructure == PropertyCheckingResult.TRUE);
		} else if(sender == soundnessCheckLabel){
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "WFNet Soundness Check Exception", exception, true);
			properties = ((WFNetException) exception).getProperties();
			if (properties == null)
				return;
			this.checkStructure = !(properties.hasWFNetStructure == PropertyCheckingResult.TRUE);
			this.checkSoundness = !(properties.isSoundWFNet == PropertyCheckingResult.TRUE);
			this.markingGraph = properties.markingGraph;	
		}
	}

	@Override
	protected AbstractValidityCheckLabel createValidityCheckLabel() {
		PTValidityCheckLabel ptValidityCheckLabel = new PTValidityCheckLabel(pnEditor, "Validity");
		ptValidityCheckLabel.setEnabled(true);
		return ptValidityCheckLabel;
	}


	@Override
	protected AbstractWFCheckLabel<WFNetProperties> createStructureCheckLabel() {
		return new WFNetStructureCheckLabel(pnEditor, "WF-Net\nStructure");
	}


	@Override
	protected AbstractWFCheckLabel<WFNetProperties> createSoundnessCheckLabel() {
		return new WFNetSoundnessCheckLabel(pnEditor, "WF-Net\nSoundness");
	}

}
