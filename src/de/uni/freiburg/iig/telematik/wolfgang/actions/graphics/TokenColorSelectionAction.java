package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.FillStyle;

public class TokenColorSelectionAction extends AbstractPNEditorAction {
	public static Color DEFAULT_FILL_COLOR = new Color(255, 255, 255);
	public static Color DEFAULT_GRADIENT_COLOR = new Color(0, 0, 0);

	private Color tokenColor;
	private String tokenLabel;
	private JPanel parent;

	public TokenColorSelectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setTokenColor(DEFAULT_FILL_COLOR);

	}

	public TokenColorSelectionAction(PNEditorComponent editor, String tokenLabel) throws ParameterException, PropertyException, IOException {
		this(editor);
		this.tokenLabel = tokenLabel;
	}

	public void setTokenColor(Color fillColor) throws PropertyException, IOException {
		this.tokenColor = fillColor;
		Image image;
		image = Utils.createIconImage(fillColor, fillColor, GradientRotation.VERTICAL, WolfgangProperties.getInstance().getIconSize().getSize());
		setIconImage(image);
	}

	public Color getTokenColor() {
		return tokenColor;
	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);

	}

	public void setParent(JPanel parent) {
		this.parent = parent;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		FillStyle fillStlye = getEditor().getEditorToolbar().getGraphicsToolbar().getFillStyle();
		Color backgroundColor = JColorChooser.showDialog(parent, "Token Color", null);
		if (backgroundColor != null) {
			setTokenColor(backgroundColor);

			if (tokenLabel != null) {
				AbstractCPNGraphics cpnGraphics = (AbstractCPNGraphics) editor.getGraphComponent().getGraph().getNetContainer().getPetriNetGraphics();
				((mxGraphModel) getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, getTokenColor()));
			}
		}
	}

}
