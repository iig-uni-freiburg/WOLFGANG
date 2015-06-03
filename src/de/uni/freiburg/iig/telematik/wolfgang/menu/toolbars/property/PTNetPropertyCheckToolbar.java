package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class PTNetPropertyCheckToolbar extends AbstractPropertyCheckToolbar implements WFNetCheckLabelListener<WFNetProperties>{

	private static final long serialVersionUID = -685698026002872162L;
	private PTValidityCheckLabel ptValidityCheckLabel;
	private WFNetStructureCheckLabel wfNetCheckLabel;
	
	private boolean checkWFNetStructure = true;
	private AbstractMarkingGraph<?,?,?,?> markingGraph = null;

	public PTNetPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificCheckLabels(PNEditorComponent pnEditor) {
		ptValidityCheckLabel = new PTValidityCheckLabel(pnEditor, "Validity");
		add(ptValidityCheckLabel);	
		ptValidityCheckLabel.setEnabled(false);
		ptValidityCheckLabel.addListener(this);
		
		wfNetCheckLabel = new WFNetStructureCheckLabel(pnEditor, "WF-Net");
		add(wfNetCheckLabel);	
		wfNetCheckLabel.setEnabled(false);
		wfNetCheckLabel.addWFNetCheckListener(this);
	}


	@Override
	public void labelCalculationFinished(Object sender, Object result) {
		if(sender == ptValidityCheckLabel){
			wfNetCheckLabel.setEnabled(true);
		}
	}

	@Override
	public void labelCalculationStopped(Object sender, Object result) {
		if(sender == ptValidityCheckLabel){
			wfNetCheckLabel.setEnabled(false);
		}
	}
	
	@Override
	public void labelCalculationException(Object sender, Exception exception) {
		if(sender == ptValidityCheckLabel){
			wfNetCheckLabel.setEnabled(false);
		}
	}

	@Override
	public void wfNetCheckFinished(Object sender, WFNetProperties result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wfNetCheckException(Object sender, Exception exception) {
		if(!(exception instanceof WFNetException)){
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "WFNet Structure Check Exception", exception, true);
			return;
		}
		WFNetProperties properties = ((WFNetException) exception).getProperties();
		if(properties == null)
			return;
		this.checkWFNetStructure = !(properties.hasWFNetStructure == PropertyCheckingResult.TRUE);
		this.markingGraph = properties.markingGraph;
	}

	@Override
	public void wfNetCheckStopped(Object sender, WFNetProperties result) {
		// TODO Auto-generated method stub
		
	}
	
}
