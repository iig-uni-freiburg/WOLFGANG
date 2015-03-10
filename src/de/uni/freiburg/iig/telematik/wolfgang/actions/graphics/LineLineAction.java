package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.LineStyle;

public class LineLineAction extends AbstractPNEditorGraphicsAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 414413025130651923L;
	private Color fillColor;

	public LineLineAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "LineColor", IconFactory.getIcon("line"));
		setButtonScale(3,3);
		setIconImage(getIcon().getImage());
	}


	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(fillColor));
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(fillColor));
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "false");
		getGraph().setCellStyles(mxConstants.STYLE_EDGE, "direct");
		getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(LineStyle.NORMAL);
	}

}
