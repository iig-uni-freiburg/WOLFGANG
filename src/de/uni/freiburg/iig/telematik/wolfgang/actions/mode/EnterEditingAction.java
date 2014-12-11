package de.uni.freiburg.iig.telematik.wolfgang.actions.mode;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class EnterEditingAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	
	public EnterEditingAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Edit", IconFactory.getIcon("edit"));
	}
	
	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().setEditingMode();
		
	}


}
