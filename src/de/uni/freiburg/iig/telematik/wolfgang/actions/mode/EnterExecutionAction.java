package de.uni.freiburg.iig.telematik.wolfgang.actions.mode;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class EnterExecutionAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	private Image play;

	private Image reset;


	
	public EnterExecutionAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Exeuctin", IconFactory.getIcon("play"));		
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().setExecutionMode();		
	}


}
