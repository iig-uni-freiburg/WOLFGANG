package de.uni.freiburg.iig.telematik.wolfgang.graph.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;

public abstract class MXConstants {

	// public static final String CONTAINER = "container";
	// public static final String PLACE = "place";
	// public static final String TRANSITION = "transition";
	// public static final String CONFIG_FILE = "PetriNet.cfg";
	// public static final String ROOT_COLOR = "#aaaaaa";
	// public static final String ROOT_FONT_COLOR = "#ffffff";
	// public static final String TERMINAL_COLOR = "#ffaaaa";
	// public static final String IMMEDIATE_COLOR = "#dddddd";
	// public static final String SIMULATION_START = "Start";
	// public static final String SIMULATION_END = "End";
	// public static final String SIMULATION_PLAN = "Plan";
	// public static final String SIMULATION_EXEC = "Execute";

	public static BufferedImage EMPTY_IMAGE;
	static {
		try {
			MXConstants.EMPTY_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		} catch (Exception e) {
			// Occurs when running on GAE, BufferedImage is a
			// blacklisted class
			MXConstants.EMPTY_IMAGE = null;
		}
	}

	public static final Color bluelow = new Color(107, 134, 167);
	public static final Color bluemid = new Color(214, 227, 242);
	public static final Color bluehigh = new Color(182, 202, 228);
	public static final Color blueBG = new Color(234, 243, 252);
	// public static final Color blueBG = new Color(255, 255, 255);
	public static final Color SHADOW_COLOR = Color.gray;
	public static final Color DEFAULT_VALID_COLOR = MXConstants.bluehigh;
	public static final Color DEFAULT_INVALID_COLOR = Color.RED;
	public static final Color RUBBERBAND_BORDERCOLOR = new Color(51, 153, 255);
	public static final Color RUBBERBAND_FILLCOLOR = new Color(51, 153, 255, 80);
	public static final Color HANDLE_BORDERCOLOR = Color.black;
	public static final Color HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color LABEL_HANDLE_FILLCOLOR = Color.orange;
	public static final Color LOCKED_HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color CONNECT_HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color EDGE_SELECTION_COLOR = MXConstants.bluelow;
	public static final Color VERTEX_SELECTION_COLOR = MXConstants.bluelow;

	public static int EDGE_HANDLE_SIZE = 10;

	public static final String PLACE_NAME_PREFIX = "p";
	public static final String TRANSITION_NAME_PREFIX = "t";

	/** FILL **/
	// mxConstants.STYLE_FILLCOLOR
	// mxConstants.STYLE_GRADIENTCOLOR
	// mxConstants.STYLE_IMAGE
	public static final String GRADIENT_ROTATION = "Gradient_Rotation";

	/** LINE **/
	// mxConstants.STYLE_STROKECOLOR
	// mxConstants.STYLE_STROKEWIDTH
	public static final String LINE_STYLE = "Line_Style";
	public static final String LINE_SHAPE = "lineShape";

	/** LABEL FILL **/
	// mxConstants.STYLE_LABEL_BORDERCOLOR
	public static final String LABEL_LINE_STYLE = "labelLineStyle";
	public static final String LABEL_LINE_WIDTH = "labelStrokeWidth";
	public static final String LABEL_LINE_SHAPE = "labelLineShape";

	/** LABEL LINE **/
	// mxConstants.STYLE_LABEL_BACKGROUNDCOLOR
	public static final String LABEL_GRADIENTCOLOR = "labelGradientColor";
	public static final String LABEL_GRADIENT_ROTATION = "labelGradientDirection";
	public static final String LABEL_IMAGE = "labelImage";

	/** LABEL FONT **/
	// mxConstants.STYLE_ALIGN
	// mxConstants.STYLE_FONTFAMILY
	// mxConstants.STYLE_FONTSIZE
	public static final String FONT_ROTATION_DEGREE = "180";
	public static final String FONT_DECORATION = "labelFontDecoration";
	public static final String FONT_STYLE = "labelFontStyle";
	public static final String FONT_WEIGHT = "labelFontWeight";

	public static String getNodeStyle(PNComponent type, NodeGraphics initialNodeGraphics, AnnotationGraphics annotationGraphics) throws PropertyException, IOException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();

		switch (type) {
		case PLACE:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
			break;
		case TRANSITION:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			break;
		}

		NodeGraphics nodeGraphics = (initialNodeGraphics != null) ? initialNodeGraphics : new NodeGraphics();

		Fill fill = (nodeGraphics.getFill() != null) ? nodeGraphics.getFill() : new Fill();
		fill = nodeGraphics.getFill();

		String fillColorPN = (fill.getColor() != null) ? fill.getColor() : WolfgangProperties.getInstance().getDefaultNodeColor();
		String fillColorMX = ((fillColorPN).equals("transparent")) ? "none" : fillColorPN;
		style.put(mxConstants.STYLE_FILLCOLOR, fillColorMX);

		GradientRotation gradientRotation = (fill.getGradientRotation() != null) ? fill.getGradientRotation() : WolfgangProperties.getInstance().getDefaultGradientDirection();
		if (gradientRotation != null)
			style.put(MXConstants.GRADIENT_ROTATION, gradientRotation);

		String gradientColorPN = (fill.getGradientColor() != null) ? fill.getGradientColor() : WolfgangProperties.getInstance().getDefaultGradientColor();
		if (gradientColorPN != null) {
			String gradientColorMX = ((gradientColorPN).equals("transparent")) ? "none" : gradientColorPN;
			style.put(mxConstants.STYLE_GRADIENTCOLOR, gradientColorMX);
		}

		URI image = fill.getImage();
		if (image != null) {
			String path = decodePath(image);

			try {
				path = URLDecoder.decode(path, "utf-8");
			} catch (UnsupportedEncodingException e) {
				JOptionPane.showMessageDialog(null, "utf-8 encryption is not supported", "unsupported Encoding Exception", JOptionPane.ERROR);
			}

			path = new File(path).getPath();

			if (image != null)
				style.put(mxConstants.STYLE_IMAGE, "file:" + path);
		}
		nodeGraphics.setFill(new Fill(fillColorPN, gradientColorPN, gradientRotation, image));

		Line line = (nodeGraphics.getLine() != null) ? nodeGraphics.getLine() : new Line();

		String lineColorPN = (line.getColor() != null) ? line.getColor() : WolfgangProperties.getInstance().getDefaultLineColor();
		String lineColorMX = ((lineColorPN).equals("transparent")) ? "none" : lineColorPN;
		style.put(mxConstants.STYLE_STROKECOLOR, lineColorMX);

		Style lineStyle = (line.getStyle() != null) ? line.getStyle() : Line.Style.SOLID;
		style.put(MXConstants.LINE_STYLE, lineStyle);

		Shape lineShape = (line.getShape() != null) ? line.getShape() : Shape.LINE;
		switch (lineShape) {
		case CURVE:
			style.put(mxConstants.STYLE_ROUNDED, "true");
			style.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
			break;
		case LINE:
			style.put(mxConstants.STYLE_ROUNDED, "false");
			style.put(mxConstants.STYLE_EDGE, "direct");
			break;

		}

		style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(line.getWidth()));

		nodeGraphics.setLine(new Line(lineColorPN, lineShape, lineStyle, line.getWidth()));

		getAnnotationGraphics(annotationGraphics, style);

		String convertedStyle = style.toString().replaceAll(", ", ";");
		String shortendStyle = convertedStyle.substring(1, convertedStyle.length() - 1);
		return shortendStyle;
	}

	public static String decodePath(URI uriFile) {
		return uriFile.getPath();
	}

	public static String getArcStyle(ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) throws IOException, PropertyException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();

		Line line = (arcGraphics.getLine() != null) ? arcGraphics.getLine() : new Line();

		String lineColorPN = (line.getColor() != null) ? line.getColor() : WolfgangProperties.getInstance().getDefaultLineColor();
		String lineColorMX = ((lineColorPN).equals("transparent")) ? "none" : lineColorPN;
		style.put(mxConstants.STYLE_STROKECOLOR, lineColorMX);

		Style lineStyle = (line.getStyle() != null) ? line.getStyle() : Line.Style.SOLID;
		style.put(MXConstants.LINE_STYLE, lineStyle);

		Shape lineShape = (line.getShape() != null) ? line.getShape() : Shape.LINE;
		switch (lineShape) {
		case CURVE:
			style.put(mxConstants.STYLE_ROUNDED, "true");
			style.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
			break;
		case LINE:
			style.put(mxConstants.STYLE_ROUNDED, "false");
			style.put(mxConstants.STYLE_EDGE, "direct");
			break;

		}

		style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(line.getWidth()));

		arcGraphics.setLine(new Line(lineColorPN, lineShape, lineStyle, line.getWidth()));

		getAnnotationGraphics(annotationGraphics, style);
		String convertedStyle = style.toString().replaceAll(", ", ";");
		String shortendStyle = convertedStyle.substring(1, convertedStyle.length() - 1);

		return shortendStyle;
	}

	protected static void getAnnotationGraphics(AnnotationGraphics initialAnnotationGraphics, Hashtable<String, Object> style) throws IOException, PropertyException {
		AnnotationGraphics annotationGraphics = (initialAnnotationGraphics != null) ? initialAnnotationGraphics : new AnnotationGraphics();

		if (annotationGraphics.isVisible()) {
			style.put(mxConstants.STYLE_NOLABEL, "0");
			annotationGraphics.setVisibility(true);
		} else {
			style.put(mxConstants.STYLE_NOLABEL, "1");
			annotationGraphics.setVisibility(false);
		}
		Fill fill = (annotationGraphics.getFill() != null) ? annotationGraphics.getFill() : new Fill();

		String fillColorPN = (fill.getColor() != null) ? fill.getColor() : WolfgangProperties.getInstance().getDefaultLabelBackgroundColor();
		String fillColorMX = ((fillColorPN).equals("transparent")) ? "none" : fillColorPN;
		style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, fillColorMX);
		GradientRotation gradientRotation = (fill.getGradientRotation() != null) ? fill.getGradientRotation() : WolfgangProperties.getInstance().getDefaultGradientDirection();
		if (gradientRotation != null)
			style.put(MXConstants.LABEL_GRADIENT_ROTATION, gradientRotation);

		String gradientColor = (fill.getGradientColor() != null) ? fill.getGradientColor() : WolfgangProperties.getInstance().getDefaultGradientColor();
		if (gradientColor != null) {
			gradientColor = ((gradientColor).equals("transparent")) ? "none" : gradientColor;
			style.put(MXConstants.LABEL_GRADIENTCOLOR, gradientColor);
		}

		URI image = fill.getImage();
		if (image != null)
			style.put(MXConstants.LABEL_IMAGE, image);

		annotationGraphics.setFill(new Fill(fillColorPN, gradientColor, gradientRotation, image));

		Font font = (annotationGraphics.getFont() != null) ? annotationGraphics.getFont() : new Font();

		Align fontAlign = (font.getAlign() != null) ? font.getAlign() : Align.CENTER;
		style.put(mxConstants.STYLE_ALIGN, fontAlign);

		String fontFamily = (font.getFamily() != null) ? font.getFamily() : WolfgangProperties.getInstance().getDefaultFontFamily();
		style.put(mxConstants.STYLE_FONTFAMILY, fontFamily);

		String fontSize = (String) ((font.getSize() != null) ? getSizeFromCSS(font.getSize()) : WolfgangProperties.getInstance().getDefaultFontSize().toString());
		style.put(mxConstants.STYLE_FONTSIZE, fontSize);

		String fontWeight = (font.getWeight() != null) ? font.getWeight() : "normal";
		style.put(MXConstants.FONT_WEIGHT, fontWeight);

		String fontStyle = (font.getStyle() != null) ? font.getStyle() : "normal";
		style.put(MXConstants.FONT_STYLE, fontStyle);

		Decoration fontDecoration = (font.getDecoration() != null) ? font.getDecoration() : null;
		if (fontDecoration != null)
			style.put(MXConstants.FONT_DECORATION, fontDecoration);

		style.put(MXConstants.FONT_ROTATION_DEGREE, font.getRotation());

		annotationGraphics.setFont(new Font(fontAlign, fontDecoration, fontFamily, font.getRotation(), fontSize, fontStyle, fontWeight));

		Line line = (annotationGraphics.getLine() != null) ? annotationGraphics.getLine() : new Line();

		String lineColorPN = (line.getColor() != null) ? line.getColor() : WolfgangProperties.getInstance().getDefaultLabelLineColor();
		String lineColorMX = ((lineColorPN).equals("transparent")) ? "none" : lineColorPN;
		style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, lineColorMX);

		Style lineStyle = (line.getStyle() != null) ? line.getStyle() : Line.Style.SOLID;
		style.put(MXConstants.LABEL_LINE_STYLE, lineStyle);

		Shape lineShape = (line.getShape() != null) ? line.getShape() : Shape.LINE;
		style.put(MXConstants.LABEL_LINE_SHAPE, lineShape);
		// Round not implemented for Labels, maybe implement when implementing
		// gradient functionality in label background

		style.put(MXConstants.LABEL_LINE_WIDTH, Double.toString(line.getWidth()));
		annotationGraphics.setLine(new Line(lineColorPN, lineShape, lineStyle, line.getWidth()));

	}

	private static String getSizeFromCSS(String size) throws PropertyException, IOException {
		if (size.equals("medium"))
			return WolfgangProperties.getInstance().getDefaultFontSize().toString();
		// if(size.equals("small"))
		// return mxConstants.DEFAULT_FONTSIZE;...
		// Other cases...

		return size;
	}

}
