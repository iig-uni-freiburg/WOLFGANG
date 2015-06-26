package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Icon;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public abstract class AbstractPNEditorGraphicsAction extends AbstractPNEditorAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8723535025613390455L;
	private Color buttonFillColor;
	private int heightDenominator = 1;
	private int widthDenominator = 1;
	private Color buttonGradientColor;

	public Color getButtonFillColor() {
		return buttonFillColor;
	}

	public void setButtonFillColor(Color buttonFillColor) {
		this.buttonFillColor = buttonFillColor;
	}

	public AbstractPNEditorGraphicsAction(PNEditorComponent editor, String name, Icon icon) throws ParameterException {
		super(editor, name, icon);
	}

	public AbstractPNEditorGraphicsAction(PNEditorComponent editor, Color fillColor, int widthDenominator, int heightDenominator) throws ParameterException, PropertyException, IOException {
		super(editor);
		setButtonScale(widthDenominator, heightDenominator);
		setFillColor(fillColor);
	}

	public AbstractPNEditorGraphicsAction(PNEditorComponent editor) throws ParameterException {
		super(editor);
	}



	public void setFillColor(Color fillColor) throws PropertyException, IOException {
		setFillColor(fillColor, fillColor);
	}

	public void setFillColor(Color fillColor, Color gradientColor) throws PropertyException, IOException {
		gradientColor = (gradientColor == null) ? fillColor : gradientColor;
		setFillColor(fillColor, gradientColor, GradientRotation.VERTICAL);
	}

	public void setFillColor(Color fillColor, Color gradientColor, GradientRotation gradientRotation) throws PropertyException, IOException {
		gradientColor = (gradientColor == null) ? fillColor : gradientColor;
		Image image = Utils.createIconImage(fillColor, gradientColor, gradientRotation, EditorProperties.getInstance().getIconSize().getSize());
		setButtonFillColor(fillColor);
		setButtonGradientColor(gradientColor);
		setIconImage(image);
	}


	public Color getButtonGradientColor() {
		return buttonGradientColor;
	}

	public void setButtonGradientColor(Color buttonGradienColor) {
		this.buttonGradientColor = buttonGradienColor;
	}

	public void setLineColor(Color fillColor, Double defaultStrokewidth) throws PropertyException, IOException {
		setLineColor(fillColor, defaultStrokewidth, Line.Style.SOLID, false);
	}

	public void setLineColor(Color fillColor, Double defaultStrokewidth, Style defaultLinestyle, boolean isLineCurve) throws PropertyException, IOException {
		Image image = Utils.createLIconImage(fillColor, EditorProperties.getInstance().getIconSize().getSize(), defaultStrokewidth, defaultLinestyle, isLineCurve);
		setButtonFillColor(fillColor);
		setIconImage(image);
	}

	public void setNoFill() throws ParameterException, PropertyException, IOException {
		setIconImage(IconFactory.getIcon("no_fill").getImage().getScaledInstance(getIcon().getIconWidth(), getIcon().getIconWidth(), java.awt.Image.SCALE_SMOOTH));
	}

	protected Image scaleButtonDown(Image image, int widthDenominator, int heightDenominator) throws PropertyException, IOException {
		int size = EditorProperties.getInstance().getIconSize().getSize();
		return image.getScaledInstance(size / widthDenominator, size / heightDenominator, java.awt.Image.SCALE_SMOOTH);
	}
	
	protected void setButtonScale(int wd, int hd) {
		widthDenominator = wd;
		heightDenominator = hd;
	}


	public void setIconImage(Image image) throws PropertyException, IOException {
		Validate.notNull(image);
		getIcon().setImage(scaleButtonDown(image, widthDenominator, heightDenominator));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getGraph().isLabelSelected())
			performLabelAction();
		else {
			performNoLabelAction();
		}
		doMoreFancyStuff(e);
	}

	protected abstract void performLabelAction() throws PropertyException, IOException;

	protected abstract void performNoLabelAction() throws PropertyException, IOException;

	protected abstract void doMoreFancyStuff(ActionEvent e) throws Exception;

}
