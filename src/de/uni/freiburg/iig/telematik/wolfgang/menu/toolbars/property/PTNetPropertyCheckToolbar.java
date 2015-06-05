package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class PTNetPropertyCheckToolbar extends AbstractPropertyCheckToolbar implements WFNetCheckLabelListener<WFNetProperties> {

	private static final long serialVersionUID = -685698026002872162L;
	private PTValidityCheckLabel ptValidityCheckLabel;
	private WFNetStructureCheckLabel wfNetStructueCheckLabel;

	// @Override
	// public void setEnabled(boolean enabled) {
	// new JButton().setEnabled(b); super.setEnabled(enabled);
	// }

	private boolean checkWFNetStructure = true;
	private boolean checkWFNetSoundness = true;
	private AbstractMarkingGraph<?, ?, ?, ?> markingGraph = null;
	private WFNetSoundnessCheckLabel wfNetSoundnessCheckLabel;

	public PTNetPropertyCheckToolbar(PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificCheckLabels(PNEditorComponent pnEditor) {
		ptValidityCheckLabel = new PTValidityCheckLabel(pnEditor, "Validity");
		add(ptValidityCheckLabel);
		ptValidityCheckLabel.setEnabled(true);
		ptValidityCheckLabel.addListener(this);

		wfNetStructueCheckLabel = new WFNetStructureCheckLabel(pnEditor, "WF-Net\nStructure");
		add(wfNetStructueCheckLabel);
		wfNetStructueCheckLabel.setEnabled(false);
		wfNetStructueCheckLabel.addWFNetCheckListener(this);

		wfNetSoundnessCheckLabel = new WFNetSoundnessCheckLabel(pnEditor, "WF-Net\nnSoundness");
		add(wfNetSoundnessCheckLabel);
		wfNetSoundnessCheckLabel.setEnabled(false);
		wfNetSoundnessCheckLabel.addWFNetCheckListener(this);
	}

	@Override
	public void labelCalculationFinished(Object sender, Object result) {
		if (sender == ptValidityCheckLabel) {
			wfNetStructueCheckLabel.setEnabled(true);
		}
	}

	@Override
	public void labelCalculationStopped(Object sender, Object result) {
		System.out.println("stop");

		if (sender == ptValidityCheckLabel) {
			wfNetStructueCheckLabel.setEnabled(false);
		}
	}

	@Override
	public void labelCalculationException(Object sender, Exception exception) {
		System.out.println("lbl");

		if (sender == ptValidityCheckLabel) {
			wfNetStructueCheckLabel.setEnabled(false);
		}
	}

	@Override
	public void wfNetCheckFinished(Object sender, WFNetProperties result) {
		System.out.println("finish");
		if (sender instanceof WFNetStructureCheckLabel) {
			wfNetSoundnessCheckLabel.setEnabled(result.hasWFNetStructure == PropertyCheckingResult.TRUE);
		}
		if (sender instanceof WFNetSoundnessCheckLabel) {
		}
	}

	@Override
	public void wfNetCheckException(Object sender, Exception exception) {
		System.out.println("exception");
		if (!(exception instanceof WFNetException)) {
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "WFNet Structure Check Exception", exception, true);
			return;
		}
		WFNetProperties properties = ((WFNetException) exception).getProperties();
		if (properties == null)
			return;
		this.checkWFNetStructure = !(properties.hasWFNetStructure == PropertyCheckingResult.TRUE);
		this.checkWFNetSoundness = !(properties.isSoundWFNet == PropertyCheckingResult.TRUE);
		this.markingGraph = properties.markingGraph;
	}

	@Override
	public void wfNetCheckStopped(Object sender, WFNetProperties result) {
		System.out.println("stop");
		if (sender instanceof WFNetStructureCheckLabel) {
			wfNetSoundnessCheckLabel.setEnabled(false);
//			wfNetSoundnessCheckLabel.executorStopped();
		}
		if (sender instanceof WFNetSoundnessCheckLabel) {
		}
	}

}
