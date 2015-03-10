package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.FillStyle;

public class FillGradientColorAction extends AbstractPNEditorGraphicsAction {

	public FillGradientColorAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "GradientColor", IconFactory.getIcon("fill"));
		setButtonScale(3, 3);
		setFillColor(WolfgangProperties.getInstance().getDefaultPlaceColor(), WolfgangProperties.getInstance().getDefaultGradientColor());
	}

	@Override
	protected void performLabelAction() throws PropertyException, IOException {
		NodeGraphics nodeGraphics = null;
		switch (getGraphSelectionCell().getType()) {
		case PLACE:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(getGraphSelectionCell().getId());
			break;
		case TRANSITION:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(getGraphSelectionCell().getId());
			break;
		case ARC:
			break;
		}
		Fill fill = null;
		if (nodeGraphics != null)
			fill = nodeGraphics.getFill();
		if (fill != null) {

			String colorString = fill.getColor();
			if (colorString.equals("transparent")) {
				getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(getButtonFillColor()));
			}

			String gradientColorString = fill.getGradientColor();
			if (gradientColorString == null) {
				gradientColorString = Utils.hexString(WolfgangProperties.getInstance().getDefaultGradientColor());

				getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, gradientColorString);

			}
			GradientRotation rotation = fill.getGradientRotation();
			if (rotation == null) {
				rotation = GradientRotation.VERTICAL;
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, rotation.toString());
			}

		}
	}

	@Override
	protected void performNoLabelAction() throws PropertyException, IOException {
		NodeGraphics nodeGraphics = null;
		switch (getGraphSelectionCell().getType()) {
		case PLACE:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(getGraphSelectionCell().getId());
			break;
		case TRANSITION:
			nodeGraphics = getEditor().getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(getGraphSelectionCell().getId());
			break;
		case ARC:
			break;
		}
		Fill fill = null;
		if (nodeGraphics != null)
			fill = nodeGraphics.getFill();
		if (fill != null) {

			String colorString = fill.getColor();
			if (colorString.equals("transparent")) {
				getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(getButtonFillColor()));
			}

			String gradientColorString = fill.getGradientColor();
			if (gradientColorString == null) {
				gradientColorString = Utils.hexString(WolfgangProperties.getInstance().getDefaultGradientColor());
				getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, gradientColorString);
			}
			GradientRotation rotation = fill.getGradientRotation();
			if (rotation == null) {
				rotation = GradientRotation.VERTICAL;
				getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, rotation.toString());
			}

		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.GRADIENT);
	}
}
