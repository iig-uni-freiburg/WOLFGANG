package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JColorChooser;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class LineStrokeColorAction extends AbstractPNEditorGraphicsAction {
	private static final Color DEFAULT_LINE_COLOR = new Color(0, 0, 0);
	private Color lineColor;
	private Color newColor;

	public LineStrokeColorAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "StokeColor", IconFactory.getIcon("border_color"));
		setLineColor(DEFAULT_LINE_COLOR);
	}

	public void setLineColor(Color fillColor) throws PropertyException, IOException {
		Image image = Utils.createLineIconImage(fillColor, fillColor, GradientRotation.VERTICAL, WolfgangProperties.getInstance().getIconSize().getSize() / 3);
		this.lineColor = fillColor;
		setIconImage(image);
	}

	public void setIconImage(Image image) throws PropertyException, IOException {
		getIcon().setImage(image);

	}

	@Override
	protected void performLabelAction() {
		newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) 
			getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));

	}

	@Override
	protected void performNoLabelAction() {
		newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) 
				getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));

				
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		setLineColor(newColor);
	}
}
