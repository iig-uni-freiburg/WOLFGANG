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
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;

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
	
	public static final Shape DEFAULT_LINE_SHAPE = Shape.LINE;
	public static final Style DEFAULT_LINE_STYLE = Style.SOLID;
	public static final double DEFAULT_LINE_WIDTH = 1.0;
	public static final Align DEFAULT_LABEL_FONT_ALIGN = Align.CENTER;
	public static final String DEFAULT_LABEL_FONT_WEIGHT = "normal";
	public static final String DEFAULT_LABEL_FONT_STYLE = "normal";

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
	public static final String SILENT = "transitionSilent";
	
	public static String getDefaultNodeStyle(PNComponent type) throws PropertyException, IOException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		
		// Set node shape and fill color
		Color fillColorPNDefault = null;
		switch (type) {
		case PLACE:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
			style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
			fillColorPNDefault = WolfgangProperties.getInstance().getDefaultPlaceColor();
			break;
		case TRANSITION:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_RECTANGLE);
			fillColorPNDefault = WolfgangProperties.getInstance().getDefaultTransitionColor();
			break;
		}
		style.put(mxConstants.STYLE_FILLCOLOR, getMXColor(fillColorPNDefault));
		
		// Set fill gradient rotation
		GradientRotation gradientRotation = WolfgangProperties.getInstance().getDefaultGradientDirection();
		if (gradientRotation != null)
			style.put(MXConstants.GRADIENT_ROTATION, gradientRotation);

		// Set fill gradient color
		Color gradientColorPNDefault = WolfgangProperties.getInstance().getDefaultGradientColor();
		style.put(mxConstants.STYLE_GRADIENTCOLOR, getMXColor(gradientColorPNDefault));
		
		addDefaultLineStyle(style);
		
		addDefaultAnnotationStyle(style);

		return getShortenedStyle(style);
	}
	
	private static void addDefaultLineStyle(Hashtable<String, Object> style) throws PropertyException, IOException{
		// Set line color
		Color lineColorPNDefault = WolfgangProperties.getInstance().getDefaultLineColor();
		style.put(mxConstants.STYLE_STROKECOLOR, getMXColor(lineColorPNDefault));

		// Set line style
		style.put(MXConstants.LINE_STYLE, DEFAULT_LINE_STYLE);

		// Set line shape
		Shape lineShape = DEFAULT_LINE_SHAPE;
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

		// Set line width
		style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(DEFAULT_LINE_WIDTH));
	}
	
	private static String getShortenedStyle(Hashtable<String, Object> styleMap){
		String convertedStyle = styleMap.toString().replaceAll(", ", ";");
		return convertedStyle.substring(1, convertedStyle.length() - 1);
	}
	
	private static String getMXColor(Color color){
		if (color == null) {
			return "none";
		} else {
			return mxUtils.hexString(color);
		}
	}

	/**
	 * Defines the style of newly introduced (possibly copied!!!) PN nodes.<br>
	 * In case of copied nodes, the annotation- and node-graphics might be non-empty.<br>
	 * In case of new nodes without copy, all content must be obtained from WolfgangProperties.
	 * @param type
	 * @param nodeGraphics
	 * @param annotationGraphics
	 * @return
	 * @throws PropertyException
	 * @throws IOException
	 */
	public static String extractNodeStyleFromGraphics(PNComponent type, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) throws PropertyException, IOException {
		Validate.notNull(nodeGraphics);
		Validate.notNull(annotationGraphics);
		
		Hashtable<String, Object> style = new Hashtable<String, Object>();

		// Set node shape
		switch (type) {
		case PLACE:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
			style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
			break;
		case TRANSITION:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_RECTANGLE);

			break;
		}
		
		// Set fill color
		if(nodeGraphics.getFill().getColor().equals("transparent")){
			style.put(mxConstants.STYLE_FILLCOLOR, "none");
		} else {
			style.put(mxConstants.STYLE_FILLCOLOR, nodeGraphics.getFill().getColor());
		}

		// Set fill gradient rotation
		GradientRotation nodeGradientRotation = nodeGraphics.getFill().getGradientRotation();
		if(nodeGradientRotation != null){
		style.put(MXConstants.GRADIENT_ROTATION, nodeGradientRotation);
		}

		// Set fill gradient color
		String nodeGradientColor = nodeGraphics.getFill().getGradientColor();
		if(nodeGradientColor != null){
		if(nodeGradientColor.equals("transparent")){
			style.put(mxConstants.STYLE_GRADIENTCOLOR, "none");
		} else {
			style.put(mxConstants.STYLE_GRADIENTCOLOR, nodeGradientColor);
		}
		}

		// Set fill image
		URI nodeImage = nodeGraphics.getFill().getImage();
		if (nodeImage != null) {
			String path = decodePath(nodeImage);
			try {
				path = URLDecoder.decode(path, "utf-8");
			} catch (UnsupportedEncodingException e) {
				JOptionPane.showMessageDialog(null, "utf-8 encryption is not supported", "unsupported Encoding Exception", JOptionPane.ERROR);
			}
			path = new File(path).getPath();
			if (nodeImage != null)
				style.put(mxConstants.STYLE_IMAGE, "file:" + path);
		}
		
		
		// Set line color
		if (nodeGraphics.getLine().getColor().equals("transparent")) {
			style.put(mxConstants.STYLE_STROKECOLOR, "none");
		} else {
			style.put(mxConstants.STYLE_STROKECOLOR, nodeGraphics.getLine().getColor());
		}
		
		// Set line style
		style.put(MXConstants.LINE_STYLE, nodeGraphics.getLine().getStyle());

		// Set line shape
		Shape lineShape = nodeGraphics.getLine().getShape();
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

		// Set line width
		style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(nodeGraphics.getLine().getWidth()));

		extractAnnotationStyleFromGraphics(annotationGraphics, style);

		return getShortenedStyle(style);
	}

	public static String decodePath(URI uriFile) {
		return uriFile.getPath();
	}

	public static String getDefaultArcStyle() throws IOException, PropertyException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		addDefaultLineStyle(style);
		addDefaultAnnotationStyle(style);
		return getShortenedStyle(style);
	}
	
	public static String extractArcStyleFromGraphics(ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) throws IOException, PropertyException {
		Validate.notNull(annotationGraphics);
		
		Hashtable<String, Object> style = new Hashtable<String, Object>();

		String lineColor = arcGraphics.getLine().getColor();
		if(lineColor != null){
			if(lineColor.equals("transparent")){
				style.put(mxConstants.STYLE_STROKECOLOR, "none");
			} else {
				style.put(mxConstants.STYLE_STROKECOLOR, lineColor);
			}
		}
		
		Style lineStyle = annotationGraphics.getLine().getStyle();
		if(lineStyle != null)
			style.put(MXConstants.LINE_STYLE, lineStyle);
		
		Shape lineShape = annotationGraphics.getLine().getShape();
		if(lineShape != null){
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
		}

		Double lineWidth = annotationGraphics.getLine().getWidth();
		if(lineWidth != null)
			style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(lineWidth));

		extractAnnotationStyleFromGraphics(annotationGraphics, style);
		
		return getShortenedStyle(style);
	}
	
	private static void addDefaultAnnotationStyle(Hashtable<String, Object> style) throws IOException, PropertyException {
		// Set visibility
		style.put(mxConstants.STYLE_NOLABEL, "0");
		
		// Set label background color
		Color fillColorPNDefault = WolfgangProperties.getInstance().getDefaultLabelBackgroundColor();
		style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, getMXColor(fillColorPNDefault));
		
		// Set label gradient rotation
		GradientRotation gradientRotation = WolfgangProperties.getInstance().getDefaultGradientDirection();
		if (gradientRotation != null)
			style.put(MXConstants.LABEL_GRADIENT_ROTATION, gradientRotation);

		// Set label gradient color
		Color gradientColorPNDefault = WolfgangProperties.getInstance().getDefaultGradientColor();
		style.put(MXConstants.LABEL_GRADIENTCOLOR, getMXColor(gradientColorPNDefault));

		// Set label font align
		style.put(mxConstants.STYLE_ALIGN, DEFAULT_LABEL_FONT_ALIGN);

		// Set label font family
		style.put(mxConstants.STYLE_FONTFAMILY, WolfgangProperties.getInstance().getDefaultFontFamily());
		
		// Set label font size
		style.put(mxConstants.STYLE_FONTSIZE, WolfgangProperties.getInstance().getDefaultFontSize().toString());

		// Set label font weight
		style.put(MXConstants.FONT_WEIGHT, DEFAULT_LABEL_FONT_WEIGHT);

		// Set label font style
		style.put(MXConstants.FONT_STYLE, DEFAULT_LABEL_FONT_STYLE);

		// Set label line color
		Color lineColorPNDefault = WolfgangProperties.getInstance().getDefaultLabelLineColor();
		style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, getMXColor(lineColorPNDefault));
		
		// Set label line style
		style.put(MXConstants.LABEL_LINE_STYLE, DEFAULT_LINE_STYLE);

		// Set label line shape
		style.put(MXConstants.LABEL_LINE_SHAPE, DEFAULT_LINE_SHAPE);

		// Set label line width
		style.put(MXConstants.LABEL_LINE_WIDTH, Double.toString(DEFAULT_LINE_WIDTH));
	}

	private static void extractAnnotationStyleFromGraphics(AnnotationGraphics annotationGraphics, Hashtable<String, Object> style) throws IOException, PropertyException {
		Validate.notNull(annotationGraphics);
		
		// Set visibility
		if (annotationGraphics.isVisible()) {
			style.put(mxConstants.STYLE_NOLABEL, "0");
		} else {
			style.put(mxConstants.STYLE_NOLABEL, "1");
		}
		
		// Set label background color
		String labelBGColor = annotationGraphics.getFill().getColor();
		if (labelBGColor != null) {
			if (labelBGColor.equals("transparent")) {
				style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "none");
			} else {
				style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, labelBGColor);
			}
		}
		
		// Set label gradient rotation
		GradientRotation gradientRotation = annotationGraphics.getFill().getGradientRotation();
		if (gradientRotation != null)
			style.put(MXConstants.LABEL_GRADIENT_ROTATION, gradientRotation);

		// Set label gradient color
		String labelGradientColor = annotationGraphics.getFill().getGradientColor();
		if (labelGradientColor != null){
		if(labelGradientColor.equals("transparent")){
			style.put(MXConstants.LABEL_GRADIENTCOLOR, "none");
		} else {
			style.put(MXConstants.LABEL_GRADIENTCOLOR, labelGradientColor);
		}
		}
		
		// Set label image
		URI image = annotationGraphics.getFill().getImage();
		if (image != null)
			style.put(MXConstants.LABEL_IMAGE, image);
		
		// Set label font align
		Align fontAlign = annotationGraphics.getFont().getAlign();
		if(fontAlign != null)
			style.put(mxConstants.STYLE_ALIGN, fontAlign);
		
		// Set label font family
		String fontFamily = annotationGraphics.getFont().getFamily();
		if(fontFamily != null)
			style.put(mxConstants.STYLE_FONTFAMILY, fontFamily);
		
		// Set label font size
		String fontSize = annotationGraphics.getFont().getSize();
		if(fontSize != null)
			style.put(mxConstants.STYLE_FONTSIZE, fontSize);
		
		// Set label font weight
		String fontWeight = annotationGraphics.getFont().getWeight();
		if(fontWeight != null)
			style.put(MXConstants.FONT_WEIGHT, fontWeight);
		
		// Set label font style
		String fontStyle = annotationGraphics.getFont().getStyle();
		if(fontStyle != null){
			style.put(MXConstants.FONT_STYLE, fontStyle);
		}
		
		// Set label font decoration
		Decoration fontDecoration = annotationGraphics.getFont().getDecoration();
		if (fontDecoration != null)
			style.put(MXConstants.FONT_DECORATION, fontDecoration);
		
		// Set label font rotation
		Double fontRotation = annotationGraphics.getFont().getRotation();
		if(fontRotation != null)
			style.put(MXConstants.FONT_ROTATION_DEGREE, fontRotation);
		
//		annotationGraphics.setFont(new Font(fontAlign, fontDecoration, fontFamily, font.getRotation(), fontSize, fontStyle, fontWeight));
		
		
		// Set label line color
		String lineColor = annotationGraphics.getLine().getColor();
		if(lineColor != null){
			if(lineColor.equals("transparent")){
				style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
			} else {
				style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, lineColor);
			}
		}

		// Set label line style
		Style lineStyle = annotationGraphics.getLine().getStyle();
		if(lineStyle != null)
			style.put(MXConstants.LABEL_LINE_STYLE, lineStyle);
		
		// Set label line shape
		Shape lineShape = annotationGraphics.getLine().getShape();
		if(lineShape != null)
			style.put(MXConstants.LABEL_LINE_SHAPE, lineShape);

		// Set label line width
		Double lineWidth = annotationGraphics.getLine().getWidth();
		if(lineWidth != null)
			style.put(MXConstants.LABEL_LINE_WIDTH, Double.toString(lineWidth));
		
//		annotationGraphics.setLine(new Line(lineColorPN, lineShape, lineStyle, line.getWidth()));
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
