package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
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
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.FillStyle;

public class FillColorSelectionAction extends AbstractPNEditorGraphicsAction {

	private static final long serialVersionUID = -7823581979779506827L;

	public FillColorSelectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setFillColor(WolfgangProperties.getInstance().getDefaultPlaceColor(), WolfgangProperties.getInstance().getDefaultGradientColor(), GradientRotation.VERTICAL);
	}

	@Override
	protected void performLabelAction() throws PropertyException, IOException {

		switch (getEditor().getEditorToolbar().getGraphicsToolbar().getFillStyle()) {
		case SOLID:
			Color backgroundColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
			if (backgroundColor != null) {
				getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(backgroundColor));
			}
			break;
		case GRADIENT:
			Color gradientColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Gradient Color", null);
			if (gradientColor != null) {
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(gradientColor));
			}
			break;
		case NOFILL:
			Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
			if (newColor != null) {
				getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(newColor));
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(WolfgangProperties.getInstance().getDefaultGradientColor()));
				getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);
			}
			break;
		}
	}

	@Override
	protected void performNoLabelAction() throws PropertyException, IOException {
		switch (getEditor().getEditorToolbar().getGraphicsToolbar().getFillStyle()) {
		case SOLID:
			if (!getGraph().isSelectionEmpty()) {
				Color backgroundColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
				if (backgroundColor != null) {
					getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(backgroundColor));
				}
			}
			break;
		case GRADIENT:
			Color gradientColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Gradient Color", null);
			if (gradientColor != null) {
				getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(gradientColor));

			}

			break;
		case NOFILL:
			Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Background Color", null);
			if (newColor != null) {
				getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(newColor));
				getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, null);
				getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(WolfgangProperties.getInstance().getDefaultGradientColor()));
				getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);
			}

			break;

		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {}

}
