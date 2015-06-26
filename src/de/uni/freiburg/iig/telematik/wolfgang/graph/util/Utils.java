package de.uni.freiburg.iig.telematik.wolfgang.graph.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.mxgraph.io.graphml.mxGraphMlUtils;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

public class Utils extends mxUtils {
	/**
	 * Returns the paint bounds for the given label.
	 * 
	 * @param centery
	 * @param centerx
	 * @param centery
	 * @param centerx
	 */
	public static mxRectangle getLabelPaintBounds(String label, Map<String, Object> style, boolean isHtml, mxPoint offset, double centerx, double centery, mxRectangle vertexBounds, double scale) {
		double wrapWidth = 0;

		if (isHtml && vertexBounds != null && mxUtils.getString(style, mxConstants.STYLE_WHITE_SPACE, "nowrap").equals("wrap")) {
			wrapWidth = vertexBounds.getWidth();
		}

		mxRectangle size = mxUtils.getLabelSize(label, style, isHtml, scale, wrapWidth);

		// Measures font with full scale and scales back
		size.setWidth(size.getWidth() / scale);
		size.setHeight(size.getHeight() / scale);

		double x = offset.getX();
		double y = offset.getY();
		double width = 0;
		double height = 0;

		if (vertexBounds != null) {
			x += vertexBounds.getCenterX();
			y += vertexBounds.getCenterY();

			if (mxUtils.getString(style, mxConstants.STYLE_SHAPE, "").equals(mxConstants.SHAPE_SWIMLANE)) {
				// Limits the label to the swimlane title
				boolean horizontal = mxUtils.isTrue(style, mxConstants.STYLE_HORIZONTAL, true);
				double start = mxUtils.getDouble(style, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_STARTSIZE) * scale;

				if (horizontal) {
					width += vertexBounds.getWidth();
					height += start;
				} else {
					width += start;
					height += vertexBounds.getHeight();
				}
			} else {
				width += vertexBounds.getWidth();
				height += vertexBounds.getHeight();
			}
		}

		return Utils.getScaledLabelBounds(x, y, size, width, height, style, scale);
	}

	/**
	 * Returns the bounds for a label for the given location and size, taking into account the alignment and spacing in the specified style, as well as the width and height of the rectangle that contains the label. (For edge labels this width and height is 0.) The scale is used to scale the given size and the spacings in the specified style.
	 */
	public static mxRectangle getScaledLabelBounds(double x, double y, mxRectangle size, double outerWidth, double outerHeight, Map<String, Object> style, double scale) {
		double inset = mxConstants.LABEL_INSET * scale;

		// Scales the size of the label
		// FIXME: Correct rounded font size and not-rounded scale
		double width = size.getWidth() * scale + 2 * inset;
		double height = size.getHeight() * scale + 2 * inset;

		// // Gets the global spacing and orientation
		// boolean horizontal = isTrue(style, mxConstants.STYLE_HORIZONTAL,
		// true);
		// int spacing = (int) (getInt(style, mxConstants.STYLE_SPACING) *
		// scale);

		// Gets the alignment settings
		Object align = getString(style, mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		Object valign = getString(style, mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);

		// // Gets the vertical spacing
		// int top = (int) (getInt(style, mxConstants.STYLE_SPACING_TOP) *
		// scale);
		// int bottom = (int) (getInt(style, mxConstants.STYLE_SPACING_BOTTOM) *
		// scale);
		//
		// // Gets the horizontal spacing
		// int left = (int) (getInt(style, mxConstants.STYLE_SPACING_LEFT) *
		// scale);
		// int right = (int) (getInt(style, mxConstants.STYLE_SPACING_RIGHT) *
		// scale);

		// // Applies the orientation to the spacings and dimension
		// if (!horizontal)
		// {
		// int tmp = top;
		// top = right;
		// right = bottom;
		// bottom = left;
		// left = tmp;
		//
		// double tmp2 = width;
		// width = height;
		// height = tmp2;
		// }

		String degree = (String) style.get(MXConstants.FONT_ROTATION_DEGREE);
		if (degree != null) {
			if (degree.equals("90") || degree.equals("270")) {
				double tmp2 = width;
				width = height;
				height = tmp2;

				// if (degree.equals("180")) {
				//
				// } else if (degree.equals("360")) {
				//
				// }
			}
		}
		// // Computes the position of the label for the horizontal alignment
		// if ((horizontal && align.equals(mxConstants.ALIGN_CENTER))
		// || (!horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
		// {
		// x += (outerWidth - width) / 2;
		// }
		// else if ((horizontal && align.equals(mxConstants.ALIGN_RIGHT))
		// || (!horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
		// {
		// x += outerWidth - width - spacing - right;
		// }
		// else
		// {
		// x += spacing + left;
		// }

		// // Computes the position of the label for the vertical alignment
		// if ((!horizontal && align.equals(mxConstants.ALIGN_CENTER))
		// || (horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
		// {
		// y += (outerHeight - height) / 2;
		// }
		// else if ((!horizontal && align.equals(mxConstants.ALIGN_LEFT))
		// || (horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
		// {
		// y += outerHeight - height - spacing - bottom;
		// }
		// else
		// {
		// y += spacing + top;
		// }
		// int spacing = (int) (getInt(style, MXConstants.LABEL_POSITION_X) *
		// scale);
		// labelPositionX = 20;
		// labelPositionY = 50;

		return new mxRectangle(x, y, width, height);
	}

	// /**
	// * Returns the bounds for a label for the given location and size, taking
	// * into account the alignment and spacing in the specified style, as well
	// as
	// * the width and height of the rectangle that contains the label. (For
	// edge
	// * labels this width and height is 0.) The scale is used to scale the
	// given
	// * size and the spacings in the specified style.
	// */
	// public static mxRectangle getScaledLabelBounds(double x, double y,
	// mxRectangle size, double outerWidth, double outerHeight,
	// Map<String, Object> style, double scale)
	// {
	// double inset = mxConstants.LABEL_INSET * scale;
	//
	// // Scales the size of the label
	// // FIXME: Correct rounded font size and not-rounded scale
	// double width = size.getWidth() * scale + 2 * inset;
	// double height = size.getHeight() * scale + 2 * inset;
	//
	// // Gets the global spacing and orientation
	// boolean horizontal = isTrue(style, mxConstants.STYLE_HORIZONTAL, true);
	// int spacing = (int) (getInt(style, mxConstants.STYLE_SPACING) * scale);
	//
	// // Gets the alignment settings
	// Object align = getString(style, mxConstants.STYLE_ALIGN,
	// mxConstants.ALIGN_CENTER);
	// Object valign = getString(style, mxConstants.STYLE_VERTICAL_ALIGN,
	// mxConstants.ALIGN_MIDDLE);
	//
	// // Gets the vertical spacing
	// int top = (int) (getInt(style, mxConstants.STYLE_SPACING_TOP) * scale);
	// int bottom = (int) (getInt(style, mxConstants.STYLE_SPACING_BOTTOM) *
	// scale);
	//
	// // Gets the horizontal spacing
	// int left = (int) (getInt(style, mxConstants.STYLE_SPACING_LEFT) * scale);
	// int right = (int) (getInt(style, mxConstants.STYLE_SPACING_RIGHT) *
	// scale);
	//
	// // Applies the orientation to the spacings and dimension
	// if (!horizontal)
	// {
	// int tmp = top;
	// top = right;
	// right = bottom;
	// bottom = left;
	// left = tmp;
	//
	// double tmp2 = width;
	// width = height;
	// height = tmp2;
	// }
	//
	// // Computes the position of the label for the horizontal alignment
	// if ((horizontal && align.equals(mxConstants.ALIGN_CENTER))
	// || (!horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
	// {
	// x += (outerWidth - width) / 2 + left - right;
	// }
	// else if ((horizontal && align.equals(mxConstants.ALIGN_RIGHT))
	// || (!horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
	// {
	// x += outerWidth - width - spacing - right;
	// }
	// else
	// {
	// x += spacing + left;
	// }
	//
	// // Computes the position of the label for the vertical alignment
	// if ((!horizontal && align.equals(mxConstants.ALIGN_CENTER))
	// || (horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
	// {
	// y += (outerHeight - height) / 2 + top - bottom;
	// }
	// else if ((!horizontal && align.equals(mxConstants.ALIGN_LEFT))
	// || (horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
	// {
	// y += outerHeight - height - spacing - bottom;
	// }
	// else
	// {
	// y += spacing + top;
	// }
	//
	// return new mxRectangle(x, y, width, height);
	// }

	public static Object createFillPaint(mxRectangle bounds, Map<String, Object> style) {
		Color fillColor = mxUtils.getColor(style, mxConstants.STYLE_FILLCOLOR);
		Paint fillPaint = null;

		if (fillColor != null) {
			Color gradientColor = mxUtils.getColor(style, mxConstants.STYLE_GRADIENTCOLOR);
			String gradientRotationString = mxUtils.getString(style, MXConstants.GRADIENT_ROTATION);
			if (gradientColor != null && gradientRotationString != null) {
				GradientRotation gradientRotation = GradientRotation.getGradientRotation(gradientRotationString);

				float x1 = (float) bounds.getX();
				float y1 = (float) bounds.getY();
				float x2 = (float) bounds.getX();
				float y2 = (float) bounds.getY();

				switch (gradientRotation) {
				case DIAGONAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case HORIZONTAL:
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case VERTICAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					break;
				default:
					break;

				}

				fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, gradientColor, true);
			}
		}

		return fillPaint;
	}

	public static Image createIconImage(Color fillColor, Color gradientColor, GradientRotation gradientRotation, int size) {

		Image image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		mxRectangle bounds = new mxRectangle(0, 0, size, size);
		float x1 = (float) bounds.getX();
		float y1 = (float) bounds.getY();
		float x2 = (float) bounds.getX();
		float y2 = (float) bounds.getY();

		if (fillColor != null) {
			if (gradientColor != null && gradientRotation != null) {

				switch (gradientRotation) {
				case DIAGONAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case HORIZONTAL:
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case VERTICAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					break;
				default:
					break;

				}

			}
		}

		GradientPaint fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, gradientColor, false);
		g2.setPaint(fillPaint);

		g2.fillRect(0, 0, size - 1, size - 1);
		g2.setColor(new Color(0, 0, 0));
		// g2.setStroke(new BasicStroke(2));
		// g2.drawOval(0, size/5, size*2, size*3);
		g2.setStroke(new BasicStroke(1));
		g2.drawRect(0, 0, size - 1, size - 1);

		g2.dispose();
		return image;

	}

	public static Image createLIconImage(Color fillColor, int size, double width, Style defaultLinestyle, boolean curve) {

		Image image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		mxRectangle bounds = new mxRectangle(0, 0, size, size);
		float x1 = (float) bounds.getX();
		float y1 = (float) bounds.getY();
		float x2 = (float) bounds.getX();
		float y2 = (float) bounds.getY();

		// if (fillColor != null) {
		// if (gradientColor != null && gradientRotation != null) {
		//
		// switch (gradientRotation) {
		// case DIAGONAL:
		// y2 = (float) (bounds.getY() + bounds.getHeight());
		// x2 = (float) (bounds.getX() + bounds.getWidth());
		// break;
		// case HORIZONTAL:
		// x2 = (float) (bounds.getX() + bounds.getWidth());
		// break;
		// case VERTICAL:
		// y2 = (float) (bounds.getY() + bounds.getHeight());
		// break;
		// default:
		// break;
		//
		// }
		//
		// }
		// }

		GradientPaint fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, fillColor, false);
		g2.setPaint(fillPaint);

		g2.fillRect(0, 0, size - 1, size - 1);
		g2.setColor(new Color(0, 0, 0));
		// g2.setStroke(new BasicStroke(2));

		Stroke s = getStrokeWithStyleForToolbar(1.0, width, defaultLinestyle);
		g2.setStroke(s);

		if (curve)
			g2.drawOval(0, size / 5, size * 2, size * 3);
		else
			g2.drawLine(0, size, size, 0);
		g2.setStroke(new BasicStroke(1));
		g2.drawRect(0, 0, size - 1, size - 1);

		g2.dispose();
		return image;

	}

	public static Image createLineIconImage(Color fillColor, Color gradientColor, GradientRotation gradientRotation, int size) {
		int sizex = size * 3;
		int sizey = size * (3 / 2);
		Image image = new BufferedImage(sizex, sizey, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		mxRectangle bounds = new mxRectangle(0, 0, sizex, sizey);
		float x1 = (float) bounds.getX();
		float y1 = (float) bounds.getY();
		float x2 = (float) bounds.getX();
		float y2 = (float) bounds.getY();

		if (fillColor != null) {
			if (gradientColor != null && gradientRotation != null) {

				switch (gradientRotation) {
				case DIAGONAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case HORIZONTAL:
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case VERTICAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					break;
				default:
					break;

				}

			}
		}

		GradientPaint fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, gradientColor, false);
		g2.setPaint(fillPaint);

		g2.fillRect(0, 0, sizex - 1, sizey - 1);
		g2.setColor(new Color(0, 0, 0));

		g2.setStroke(new BasicStroke(1));
		g2.drawRect(0, 0, sizex - 1, sizey - 1);

		g2.dispose();
		return image;

	}

	public static Stroke createStroke(Map<String, Object> style, double scale) {
		String lineStyleString = mxUtils.getString(style, MXConstants.LINE_STYLE);
		double width = mxUtils.getFloat(style, mxConstants.STYLE_STROKEWIDTH, 1) * scale;
		return getStrokeForLineStyle(style, scale, lineStyleString, width);
	}

	public static Stroke createLabelStroke(Map<String, Object> style, double scale) {
		String lineStyleString = mxUtils.getString(style, MXConstants.LABEL_LINE_STYLE);
		double width = mxUtils.getFloat(style, MXConstants.LABEL_LINE_WIDTH, 1) * scale;
		return getStrokeForLineStyle(style, scale, lineStyleString, width);
	}

	/**
	 * @param style
	 * @param scale
	 * @param lineStyleString
	 * @param width
	 * @return
	 */
	protected static Stroke getStrokeForLineStyle(Map<String, Object> style, double scale, String lineStyleString, double width) {
		Style linestyle;
		if (lineStyleString != null)
			linestyle = Line.Style.getStyle(lineStyleString);
		else
			linestyle = Line.Style.SOLID;

		return getStrokeWithStyle(style, scale, width, linestyle);
	}

	private static Stroke getStrokeWithStyle(Map<String, Object> style, double scale, double width, Style linestyle) {
		float f;
		switch (linestyle) {
		case DASH:
			float[] dashPattern = mxUtils.getFloatArray(style, mxConstants.STYLE_DASH_PATTERN, mxConstants.DEFAULT_DASHED_PATTERN, " ");
			float[] scaledDashPattern = new float[dashPattern.length];
			f = (width > 0) ? (float) width : 1;
			for (int i = 0; i < dashPattern.length; i++) {
				scaledDashPattern[i] = (float) (dashPattern[i] * scale * f);
			}
			return new BasicStroke((float) width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, scaledDashPattern, 0.0f);
		case DOT:
			f = (width > 0) ? (float) width : 1;
			float[] dash = { 0.0f, f * 2 };
			return new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 10.0f);
		case SOLID:
			return new BasicStroke((float) width);
		}
		return null;
	}

	private static Stroke getStrokeWithStyleForToolbar(double scale, double width, Style linestyle) {
		float f;
		switch (linestyle) {
		case DASH:
			float[] dashPattern = new float[] { 3.0f, 3.0f };
			float[] scaledDashPattern = new float[dashPattern.length];
			f = (width > 0) ? (float) width : 1;
			for (int i = 0; i < dashPattern.length; i++) {
				scaledDashPattern[i] = (float) (dashPattern[i] * scale * f);
			}
			return new BasicStroke((float) width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, scaledDashPattern, 0.0f);
		case DOT:
			f = (width > 0) ? (float) width : 1;
			float[] dash = { 0.0f, f * 2 };
			return new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 10.0f);
		case SOLID:
			return new BasicStroke((float) width);
		}
		return null;
	}

	/**
 * 
 */
	public static java.awt.Font getFont(Map<String, Object> style, double scale) {
		String fontFamily = getString(style, mxConstants.STYLE_FONTFAMILY, mxConstants.DEFAULT_FONTFAMILY);
		int fontSize = getInt(style, mxConstants.STYLE_FONTSIZE, mxConstants.DEFAULT_FONTSIZE);
		String fontStyle = getString(style, MXConstants.FONT_STYLE, "normal");
		String fontWeight = getString(style, MXConstants.FONT_WEIGHT, "normal");
		int swingFontStyle = 0;
		if (fontStyle.equals("normal"))
			swingFontStyle = java.awt.Font.PLAIN;
		if (fontWeight.equals("normal"))
			swingFontStyle = java.awt.Font.PLAIN;
		if (fontStyle.equals("italic"))
			swingFontStyle += java.awt.Font.ITALIC;
		if (fontWeight.equals("bold"))
			swingFontStyle += java.awt.Font.BOLD;

		return new java.awt.Font(fontFamily, swingFontStyle, (int) (fontSize * scale));
	}

	public static AbstractObjectGraphics getPNGraphics(PNGraph graph, PNGraphCell cell) {
		switch (cell.getType()) {
		case PLACE:
			if (graph.isLabelSelected())
				return graph.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
		case TRANSITION:
			if (graph.isLabelSelected())
				return graph.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
		case ARC:
			if (graph.isLabelSelected())
				return graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());

		}
		return null;
	}

	public static void updateGraphics(PNGraph graph, PNGraphCell cell, String key, Object value, boolean isLabel) throws ParameterException {
		AbstractObjectGraphics graphics = getPNGraphics(graph, cell, isLabel);
		if (graphics instanceof NodeGraphics)
			updateNodeGraphics((NodeGraphics) graphics, key, value);
		if (graphics instanceof ArcGraphics)
			updateArcGraphics((ArcGraphics) graphics, key, value);
		if (graphics instanceof AnnotationGraphics)
			updateAnnotationGraphics((AnnotationGraphics) graphics, key, value);
	}

	private static AbstractObjectGraphics getPNGraphics(PNGraph graph, PNGraphCell cell, boolean isLabel) {
		switch (cell.getType()) {
		case PLACE:
			if (isLabel)
				return graph.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
		case TRANSITION:
			if (isLabel)
				return graph.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
		case ARC:
			if (isLabel)
				return graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
			else
				return graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
		}
		return null;
	}

	private static void updateAnnotationGraphics(AnnotationGraphics graphics, String key, Object value) throws ParameterException {
		if (key.equals(mxConstants.STYLE_NOLABEL)) {
			boolean isVisible = ((((String) value).equals("0")) ? true : false);
			graphics.setVisibility(isVisible);
		}
		// FILL
		if (key.equals(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)) {
			String color = (String) ((((String) value).equals("none")) ? "transparent" : value);
			graphics.getFill().setColor(color);
		}
		if (key.equals(MXConstants.LABEL_GRADIENTCOLOR)) {
			graphics.getFill().setGradientColor((String) value);
		}
		if (key.equals(MXConstants.LABEL_GRADIENT_ROTATION)) {
			GradientRotation gradientRotation = (value != null) ? GradientRotation.getGradientRotation((String) value) : null;
			graphics.getFill().setGradientRotation(gradientRotation);
		}
		// if (key.equals(MXConstants.LABEL_IMAGE)) {
		// graphics.getFill().setImage((URI) value);
		// }

		// LINE
		if (key.equals(MXConstants.LABEL_LINE_WIDTH)) {
			graphics.getLine().setWidth(new Double((String) value));
		}
		if (key.equals(mxConstants.STYLE_LABEL_BORDERCOLOR)) {
			String color = (String) ((((String) value).equals("none")) ? "transparent" : value);
			graphics.getLine().setColor(color);
		}
		if (key.equals(MXConstants.LABEL_LINE_STYLE)) {
			Style lineStlye = (value != null) ? Line.Style.getStyle((String) value) : null;
			graphics.getLine().setStyle(lineStlye);
		}
		if (key.equals(MXConstants.LABEL_LINE_SHAPE)) {
			if (value.equals("true"))
				graphics.getLine().setShape(Line.Shape.CURVE);
			if (value.equals("false"))
				graphics.getLine().setShape(Line.Shape.LINE);
		}

		// Font
		if (key.equals(mxConstants.STYLE_ALIGN)) {
			graphics.getFont().setAlign(Font.Align.getAlign((String) value));
		}
		if (key.equals(MXConstants.FONT_DECORATION)) {
			Decoration fontDecoration = (value != null) ? Font.Decoration.getDecoration((String) value) : null;
			graphics.getFont().setDecoration(fontDecoration);

		}
		if (key.equals(mxConstants.STYLE_FONTFAMILY)) {
			graphics.getFont().setFamily((String) value);
		}
		if (key.equals(MXConstants.FONT_ROTATION_DEGREE)) {
			graphics.getFont().setRotation(new Double((String) value));
		}
		if (key.equals(mxConstants.STYLE_FONTSIZE)) {
			graphics.getFont().setSize((String) value);
		}
		if (key.equals(MXConstants.FONT_STYLE)) {
			graphics.getFont().setStyle((String) value);
		}
		if (key.equals(MXConstants.FONT_WEIGHT)) {
			graphics.getFont().setWeight((String) value);
		}

	}

	private static void updateArcGraphics(ArcGraphics graphics, String key, Object value) throws ParameterException {
		// LINE
		if (key.equals(mxConstants.STYLE_STROKEWIDTH)) {
			graphics.getLine().setWidth(new Double((String) value));
		}
		if (key.equals(mxConstants.STYLE_STROKECOLOR)) {
			String color = (String) ((((String) value).equals("none")) ? "transparent" : value);
			graphics.getLine().setColor(color);
		}
		if (key.equals(MXConstants.LINE_STYLE)) {
			Style lineStlye = (value != null) ? Line.Style.getStyle((String) value) : null;
			graphics.getLine().setStyle(lineStlye);
		}
		if (key.equals(mxConstants.STYLE_ROUNDED)) {
			if (value.equals("true"))
				graphics.getLine().setShape(Line.Shape.CURVE);
			if (value.equals("false"))
				graphics.getLine().setShape(Line.Shape.LINE);
		}

	}

	private static void updateNodeGraphics(NodeGraphics graphics, String key, Object value) throws ParameterException {

		// FILL
		if (key.equals(mxConstants.STYLE_FILLCOLOR)) {
			String color = (String) ((((String) value).equals("none")) ? "transparent" : value);
			graphics.getFill().setColor(color);
		}
		if (key.equals(mxConstants.STYLE_GRADIENTCOLOR)) {
			graphics.getFill().setGradientColor((String) value);
		}
		if (key.equals(MXConstants.GRADIENT_ROTATION)) {
			GradientRotation gradientRotation = (value != null) ? GradientRotation.getGradientRotation((String) value) : null;
			graphics.getFill().setGradientRotation(gradientRotation);
		}
		if (key.equals(mxConstants.STYLE_IMAGE)) {
			File file = new File((String) value);
			URI newUri = file.toURI();
			graphics.getFill().setImage(newUri);
		}

		// LINE
		if (key.equals(mxConstants.STYLE_STROKEWIDTH)) {
			graphics.getLine().setWidth(new Double((String) value));
		}
		if (key.equals(mxConstants.STYLE_STROKECOLOR)) {
			String color = (String) ((((String) value).equals("none")) ? "transparent" : value);
			graphics.getLine().setColor(color);
		}
		if (key.equals(MXConstants.LINE_STYLE)) {
			Style lineStlye = (value != null) ? Line.Style.getStyle((String) value) : null;
			graphics.getLine().setStyle(lineStlye);
		}
		if (key.equals(mxConstants.STYLE_ROUNDED)) {
			if (value.equals("true"))
				graphics.getLine().setShape(Line.Shape.CURVE);
			if (value.equals("false"))
				graphics.getLine().setShape(Line.Shape.LINE);
		}
	}

	public static NodeGraphics createNodeGraphicsFromStyle(String style) {
		NodeGraphics graphics = new NodeGraphics();
		graphics.setFill(new Fill("#000000", null, null, null));
		graphics.setLine(new Line("#000000", Line.Shape.LINE, Line.Style.SOLID, 1.0));

		HashMap<String, Object> styleMap = mxGraphMlUtils.getStyleMap(style, "=");
		for (Entry<String, Object> s : styleMap.entrySet()) {
			String key = s.getKey();
			if (key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN)) {
			} else {
				updateNodeGraphics(graphics, s.getKey(), s.getValue());
			}
		}
		return graphics;
	}

	public static AnnotationGraphics createAnnotationGraphicsFromStyle(String style) {
		AnnotationGraphics graphics = new AnnotationGraphics();
		try {
			graphics.setFill(new Fill("#000000", null, null, null));
			graphics.setLine(new Line("#000000", Line.Shape.LINE, Line.Style.SOLID, 1.0));
			graphics.setFont(new Font(Font.Align.CENTER, null, EditorProperties.getInstance().getDefaultFontFamily().toString(), 0.0, EditorProperties.getInstance().getDefaultFontSize().toString(), "normal", "normal"));

			HashMap<String, Object> styleMap = mxGraphMlUtils.getStyleMap(style, "=");
			for (Entry<String, Object> s : styleMap.entrySet()) {
				String key = s.getKey();
				if (key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN))
					updateAnnotationGraphics(graphics, s.getKey(), s.getValue());
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
		return graphics;
	}

	public static ArcGraphics createArcGraphicsFromStyle(String style) {
		ArcGraphics graphics = new ArcGraphics();
		graphics.setLine(new Line("#000000", Line.Shape.LINE, Line.Style.SOLID, 1.0));
		HashMap<String, Object> styleMap = mxGraphMlUtils.getStyleMap(style, "=");
		for (Entry<String, Object> s : styleMap.entrySet()) {
			String key = s.getKey();
			if (key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN)) {
			} else {
				updateArcGraphics(graphics, s.getKey(), s.getValue());
			}
		}
		return graphics;
	}

	public static  Image createIconImage(Color fillColor, IconSize iconSize) {
		return createIconImage(fillColor, fillColor, GradientRotation.VERTICAL, iconSize.getSize()/3);
	}
	
}
