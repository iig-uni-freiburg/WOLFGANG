package de.uni.freiburg.iig.telematik.wolfgang.actions.history;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JToggleButton;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class RedoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public RedoAction(PNEditorComponent pnEditor) throws PropertyException, IOException {
		super(pnEditor, "Redo", IconFactory.getIcon("redo"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			editor.getUndoManager().redo();
			editor.getGraphComponent().getGraph().updateViews();
			editor.requestFocus();
		}
	}

}
