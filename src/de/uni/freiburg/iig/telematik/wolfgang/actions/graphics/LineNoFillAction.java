package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.LineStyle;

public class LineNoFillAction extends AbstractPNEditorGraphicsAction {
	public LineNoFillAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "NoFill", IconFactory.getIcon("no_fill"));
		java.awt.Image img = getIcon().getImage();
		int size = WolfgangProperties.getInstance().getIconSize().getSize() / 3;
		java.awt.Image newimg = img.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);

	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "none");
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(LineStyle.NOFILL);
	}

}
