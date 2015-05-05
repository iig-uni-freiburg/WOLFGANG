package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JButton;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.BoundednessCheckLabel;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckBoundednessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckWFNetStructureAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;

public class PTNetToolBar extends AbstractToolBar {

	private CheckBoundednessAction checkBoundednessAction;

	public CheckBoundednessAction getCheckBoundednessAction() {
		return checkBoundednessAction;
	}

	public void setCheckBoundednessAction(CheckBoundednessAction checkBoundednessAction) {
		this.checkBoundednessAction = checkBoundednessAction;
	}

	private JButton boundednessButton;
	// Currently checkSoundness or checkValidity do nothing in SEPIA, therefore
	// it is commented out
	// private CheckValidityAction checkValidityAction;
	// private JButton validityButton;
	// private CheckSoundnessAction checkSoundnessAction;
	// private JButton soudnessButton;
	private CheckWFNetStructureAction checkWFNetAction;
	public CheckWFNetStructureAction getCheckWFNetAction() {
		return checkWFNetAction;
	}

	public void setCheckWFNetAction(CheckWFNetStructureAction checkWFNetAction) {
		this.checkWFNetAction = checkWFNetAction;
	}

	private JButton wfButton;

	public PTNetToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {

			// checkValidityAction= new CheckValidityAction(pnEditor);
			// checkSoundnessAction= new CheckSoundnessAction(pnEditor);
			checkBoundednessAction = new CheckBoundednessAction(pnEditor);
			checkWFNetAction = new CheckWFNetStructureAction(pnEditor);


	}

	@Override
	protected void addNetSpecificToolbarButtons() {

		// validityButton = add(checkValidityAction);
		// validityButton.setBorderPainted(false);
		//
		// soudnessButton = add(checkSoundnessAction);
		// soudnessButton.setBorderPainted(false);

		boundednessButton = add(checkBoundednessAction);
		boundednessButton.setBorderPainted(false);

		wfButton = add(checkWFNetAction);
		wfButton.setBorderPainted(false);
		
		add(new BoundednessCheckLabel(pnEditor));
	}

	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {
		// TODO Auto-generated method stub

	}


}
