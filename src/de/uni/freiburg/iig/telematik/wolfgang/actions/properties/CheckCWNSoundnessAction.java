package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;

public class CheckCWNSoundnessAction extends AbstractPropertyCheckAction {

	public CheckCWNSoundnessAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("sound\nCWN");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<PNSoundnessException, String>() {
			@Override
			public PNSoundnessException doInBackground() {
				setIconImage(getLoadingDots());
				AbstractCPN net = (AbstractCPN) getEditor().getNetContainer().getPetriNet().clone();
				try {
					CWNChecker.checkCWNSoundness(net, true, null);
				} catch (PNSoundnessException e) {
					return e;
				}
				return null;
			}

			@Override
			public void done() {
				try {
					if (get() == null) {// hasWFNetStructure
						setFillColor(PropertyHolds);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyHolds);
					} else {
						JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net is not CWF-Net sound", JOptionPane.ERROR_MESSAGE);
						setFillColor(PropertyDoesntHold);

						if (get().getMessage().endsWith("does not consume control flow token") || get().getMessage().endsWith("does not produce control flow token"))
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyDoesntHold);
						else
							((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNStructureAction().setFillColor(PropertyUnknownColor);
					}

				} catch (InterruptedException e) {
					setFillColor(PropertyUnknownColor);
				} catch (ExecutionException e) {
					setFillColor(PropertyUnknownColor);
				}
			};
		};
		setWorker(worker);
	}

}
