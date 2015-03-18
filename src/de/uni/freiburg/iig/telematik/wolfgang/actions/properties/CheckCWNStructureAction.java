package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;

public class CheckCWNStructureAction extends AbstractPropertyCheckAction {

	public CheckCWNStructureAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("struct.\nCWN");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<PNValidationException, String>() {
			@Override
			public PNValidationException doInBackground() {
				setIconImage(getLoadingDots());
				AbstractCPN net = (AbstractCPN) getEditor().getNetContainer().getPetriNet().clone();
				try {
					CWNChecker.checkCWNStructure(net);
				} catch (PNValidationException e) {
					return e;
				}
				return null;
			}

			@Override
			public void done() {
				try {
					if (get() == null) {
						setFillColor(PropertyHolds);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(PropertyUnknownColor);
					} else {
						JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net has no WF Net Structure", JOptionPane.ERROR_MESSAGE);
						setFillColor(PropertyDoesntHold);
						((CPNToolBar) getEditor().getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(PropertyDoesntHold);
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
