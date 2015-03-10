package de.uni.freiburg.iig.telematik.wolfgang.menu.popup;

import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup.LayoutAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class EditorPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -2983257974918330746L;

	public EditorPopupMenu(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);

		JMenu submenu = (JMenu) add(new JMenu("Layout"));

		submenu.add(new LayoutAction(pnEditor, "verticalHierarchical", false));
		submenu.add(new LayoutAction(pnEditor, "horizontalHierarchical", false));

		submenu.addSeparator();

		submenu.add(new LayoutAction(pnEditor, "organicLayout", true));
		submenu.add(new LayoutAction(pnEditor, "circleLayout", true));

	}
}
