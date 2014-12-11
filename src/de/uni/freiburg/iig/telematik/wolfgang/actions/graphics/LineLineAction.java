package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.LineStyle;

public class LineLineAction extends AbstractPNEditorGraphicsAction{
	private static final Double DEFAULT_STROKEWIDTH = 1.0;
	private static final Style DEFAULT_LINESTYLE = Style.SOLID;
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	private Color fillColor;

	public LineLineAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "LineColor", IconFactory.getIcon("line"));
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size / 3, size / 3, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_STROKEWIDTH);
	}


	public void setFillColor(Color fillColor, Double defaultStrokewidth) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, WolfgangProperties.getInstance().getIconSize().getSize() / 3, defaultStrokewidth, Line.Style.SOLID, false);
		this.fillColor = fillColor;
		setIconImage(image);
	}

	public void setIconImage(Image image) throws PropertyException, IOException {
		getIcon().setImage(image);

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
