package de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class CopyAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = -7309816433378748227L;

	private Action actTransfer = null;

	public CopyAction(PNEditorComponent editor, Action actTransfer) throws PropertyException, IOException {
		super(editor, "Copy", IconFactory.getIcon("copy"));
		this.actTransfer = actTransfer;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		actTransfer.actionPerformed(new ActionEvent(this.getEditor().getGraphComponent(), e.getID(), e.getActionCommand()));
	}

}
