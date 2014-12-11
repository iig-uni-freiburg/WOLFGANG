package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.FillStyle;

public class FillColorSelectionAction extends AbstractPNEditorGraphicsAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7823581979779506827L;
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);

	public FillColorSelectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setFillColor(DEFAULT_FILL_COLOR, DEFAULT_GRADIENT_COLOR, GradientRotation.VERTICAL);

	}

	public void setFillColor(Color fillColor, Color gradientColor, GradientRotation gradientRotation) throws PropertyException, IOException {
		Image image = Utils.createIconImage(fillColor, gradientColor, gradientRotation, WolfgangProperties.getInstance().getIconSize().getSize());
			setIconImage(image);


	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void setNoFill() throws ParameterException, PropertyException, IOException {
		ImageIcon noFill;

			noFill = IconFactory.getIcon("no_fill");
			java.awt.Image img = noFill.getImage();
			int size = getIcon().getIconWidth();
			java.awt.Image newimg = img.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
			getIcon().setImage(newimg);


	}

	@Override
	protected void performLabelAction() {

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
				getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(FillGradientColorAction.DEFAULT_GRADIENT_COLOR));
				getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);
			}
			break;
		}
	}

	@Override
	protected void performNoLabelAction() {
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
				getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(FillGradientColorAction.DEFAULT_GRADIENT_COLOR));
				getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.SOLID);
			}

			break;

		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub

	}

}
