package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.WFNetChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class CheckWFNetStructureAction extends AbstractPropertyCheckAction {

	public CheckWFNetStructureAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor);
	}

	@Override
	protected void setInitialFill() {
		setPropertyString("WF-Str\nucture");
		setFillColor(PropertyUnknownColor);
	}

	@Override
	protected void createNewWorker() {

		SwingWorker worker = new SwingWorker<PNValidationException, String>() {
			@Override
			public PNValidationException doInBackground() {
				setIconImage(getLoadingDots());
				AbstractPTNet net = (AbstractPTNet) getEditor().getNetContainer().getPetriNet().clone();
				try {
					WFNetChecker.checkWFNetStructure(net);
				} catch (PNValidationException e) {
					return e;
				}
				return null;
			}

			@Override
			public void done() {
				try {
					if (get() == null)// hasWFNetStructure
						setFillColor(PropertyHolds);
					else {
						JOptionPane.showMessageDialog(editor.getGraphComponent(), get().getMessage(), "Net has no WF Net Structure", JOptionPane.ERROR_MESSAGE);
						setFillColor(PropertyDoesntHold);
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
