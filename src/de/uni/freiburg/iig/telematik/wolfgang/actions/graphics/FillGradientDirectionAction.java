package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

@SuppressWarnings("serial")
public class FillGradientDirectionAction extends AbstractPNEditorGraphicsAction {
	private Image diagonal;
	private Image vertical;
	private Image horizontal;
	private Image gradientno;

	public FillGradientDirectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_horizontal", IconFactory.getIcon("gradient_horizontal"));
		horizontal = getIcon().getImage();
		vertical = getVerticalImage();
		diagonal = IconFactory.getIcon("gradient-diagonal").getImage();
		gradientno = IconFactory.getIcon("gradient_no").getImage();
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size / 2, size / 2, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
	}

	private Image getVerticalImage() throws PropertyException, IOException {
		int size = 0;
		IconSize iconSize = WolfgangProperties.getInstance().getIconSize();
		size = iconSize.getSize();

		Image image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color1 = new Color(255, 0, 0, 100);
		g2.setColor(color1);
		g2.fillOval(0, 0, 80, 80);
		g2.dispose();
		return image;
	}

	public void setImageIcon(Image image) {
		getIcon().setImage(image);
	}

	public void setGradientnoIconImage() {
		getIcon().setImage(gradientno);
	}

	public void setVerticalIconImage() {
		getIcon().setImage(vertical);
	}

	public void setHorizontalIconImage() {
		getIcon().setImage(horizontal);
	}

	public void setDiagonalIconImage() {
		getIcon().setImage(diagonal);
	}

	@Override
	protected void performLabelAction() {
		if (getIcon().getImage() == gradientno) {
			getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());
			getIcon().setImage(horizontal);
		} else if (getIcon().getImage() == horizontal) {
			getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());
			getIcon().setImage(vertical);
		} else if (getIcon().getImage() == vertical) {
			getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			getIcon().setImage(diagonal);
		} else if (getIcon().getImage() == diagonal) {
			getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
			getIcon().setImage(gradientno);
		}
	}

	@Override
	protected void performNoLabelAction() {
		if (getIcon().getImage() == gradientno) {
			getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.HORIZONTAL.toString());
			getIcon().setImage(horizontal);
		} else if (getIcon().getImage() == horizontal) {
			getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.VERTICAL.toString());
			getIcon().setImage(vertical);
		} else if (getIcon().getImage() == vertical) {
			getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());
			getIcon().setImage(diagonal);
		} else if (getIcon().getImage() == diagonal) {
			getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, null);
			getIcon().setImage(gradientno);
		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub

	}

}