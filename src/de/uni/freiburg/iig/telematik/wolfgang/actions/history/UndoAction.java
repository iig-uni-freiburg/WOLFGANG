package de.uni.freiburg.iig.telematik.wolfgang.actions.history;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JToggleButton;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class UndoAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public UndoAction(PNEditorComponent pnEditor) throws PropertyException, IOException {
		super(pnEditor, "Undo", IconFactory.getIcon("undo"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {

		if (editor != null) {
			editor.getUndoManager().undo();
			editor.requestFocus();
		}
	}
}
