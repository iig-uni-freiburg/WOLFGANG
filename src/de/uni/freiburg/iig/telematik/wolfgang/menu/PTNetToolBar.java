package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.pt.PTNetPropertyCheckToolbar;

public class PTNetToolBar extends AbstractToolBar {

	private static final long serialVersionUID = -8456877591478451824L;

	public PTNetToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws PropertyException, IOException {}

	@Override
	protected void addNetSpecificToolbarButtons() {}

	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {}

	@Override
	protected JToolBar createPropertyCheckToolbar() throws ParameterException, PropertyException, IOException {
		return new PTNetPropertyCheckToolbar(pnEditor, JToolBar.HORIZONTAL);
	}

}
