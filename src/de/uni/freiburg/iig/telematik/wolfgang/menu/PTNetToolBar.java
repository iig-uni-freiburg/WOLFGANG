package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JButton;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckSoundnessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckValidityAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckBoundednessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckWFNetStructureAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;

public class PTNetToolBar extends AbstractToolBar {

	private CheckBoundednessAction checkBoundednessAction;
	private JButton boundednessButton;
	
	private CheckValidityAction checkValidityAction;
	private JButton validityButton;
	private CheckWFNetStructureAction checkWFNetAction;
	private JButton wfButton;
	private CheckSoundnessAction checkSoundnessAction;
	private JButton soudnessButton;


	public PTNetToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}


	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) {
		try {
			checkValidityAction= new CheckValidityAction(pnEditor);
			checkSoundnessAction= new CheckSoundnessAction(pnEditor);
			checkBoundednessAction= new CheckBoundednessAction(pnEditor);
			checkWFNetAction= new CheckWFNetStructureAction(pnEditor);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	protected void addNetSpecificToolbarButtons() {
		
		validityButton = add(checkValidityAction);	
		validityButton.setBorderPainted(false);
		
		soudnessButton = add(checkSoundnessAction);	
		soudnessButton.setBorderPainted(false);
		
		boundednessButton = add(checkBoundednessAction);	
		boundednessButton.setBorderPainted(false);
		
		wfButton = add(checkWFNetAction);	
		wfButton.setBorderPainted(false);

	}



	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}


}
