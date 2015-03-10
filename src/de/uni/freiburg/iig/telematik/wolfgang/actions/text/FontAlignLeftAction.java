package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class FontAlignLeftAction extends AbstractPNEditorAction {

	public FontAlignLeftAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Left", IconFactory.getIcon("left"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getGraph() != null && !getGraph().isSelectionEmpty())
			getGraph().setCellStyles(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);
	}
}