package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class FontLineThroughStyleAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7450908146578160638L;
	protected boolean linethrough = false;

	public FontLineThroughStyleAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Linethrough", IconFactory.getIcon("linethrough"));

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (!linethrough) {
			getGraph().setCellStyles((String) MXConstants.FONT_DECORATION, "line-through");
			linethrough = true;
		} else {
			getGraph().setCellStyles((String) MXConstants.FONT_DECORATION, null);
			linethrough = false;
		}
	}
}