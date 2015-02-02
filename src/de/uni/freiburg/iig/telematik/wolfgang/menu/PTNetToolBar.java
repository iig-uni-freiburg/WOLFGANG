package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JButton;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckBoundednessAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;

public class PTNetToolBar extends AbstractToolBar {

	private CheckBoundednessAction checkBoundednessAction;
	private JButton boundednessButton;


	public PTNetToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}


	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) {
		try {
			checkBoundednessAction= new CheckBoundednessAction(pnEditor);
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
		
		boundednessButton = add(checkBoundednessAction);	
		boundednessButton.setBorderPainted(false);

	}



	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}


}
