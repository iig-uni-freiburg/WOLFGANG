package de.uni.freiburg.iig.telematik.wolfgang.actions.mode;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class ToggleModeAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	private Image edit;
	private Image play;

	public ToggleModeAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Mode", IconFactory.getIcon("switch_edit"));
		edit = getIcon().getImage();
		play = IconFactory.getIcon("switch_play").getImage();

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getIcon().getImage() == edit) {
			getEditor().getEditorToolbar().setExecutionMode();
			getIcon().setImage(play);
		} else if (getIcon().getImage() == play) {
			getEditor().getEditorToolbar().setEditingMode();
			getIcon().setImage(edit);
		}
	}

}