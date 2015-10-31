package de.uni.freiburg.iig.telematik.wolfgang.menu.popup;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup.TransitionSilentAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class TransitionPopupMenu extends JPopupMenu {
	private static final long serialVersionUID = -2983257974918330746L;
	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}
	
	public TransitionPopupMenu(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);
		setUpGui(pnEditor);		

	}

	private void setUpGui(PNEditorComponent pnEditor) {
		JMenu mnuSub = (JMenu) add(new JMenu("Transition"));
		mnuSub.add(new TransitionSilentAction(pnEditor, "silent", true));
		mnuSub.add(new TransitionSilentAction(pnEditor, "not silent", false));
		
	}

}
