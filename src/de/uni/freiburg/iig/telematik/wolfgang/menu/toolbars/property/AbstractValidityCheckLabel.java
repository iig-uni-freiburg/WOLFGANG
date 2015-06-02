package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractValidityCheckLabel extends PNPropertyCheckLabel<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2534069643060024803L;

	public AbstractValidityCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected void startExecutor() throws Exception {
		try {
			checkValidity(editorComponent.getNetContainer().getPetriNet());
			setPropertyHolds(true);
		} catch (PNValidationException e) {
			setPropertyHolds(false);
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editorComponent), "Violated validity constraint:\n" + e.getMessage(), "Validation Exception", JOptionPane.ERROR_MESSAGE);
		}
		setGraphicsFinished();
		
	}
	
	protected abstract void checkValidity(AbstractPetriNet net) throws PNValidationException;

	@Override
	protected void setGraphicsFinished() {
		super.setGraphicsFinished();
	}

	@Override
	protected void setPropertyHolds(Boolean result) {
		this.propertyHolds = result;
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?, ?, ?, ?, ?, ?, Boolean, ?> createNewExecutor() {
		return null;
	}

}
