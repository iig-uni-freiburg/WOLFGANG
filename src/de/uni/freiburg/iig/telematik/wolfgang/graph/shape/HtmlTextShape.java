package de.uni.freiburg.iig.telematik.wolfgang.graph.shape;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.CellRendererPane;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxHtmlTextShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxLightweightLabel;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;

public class HtmlTextShape extends mxHtmlTextShape {
@Override
/**
 * 
 */
public void paintShape(mxGraphics2DCanvas canvas, String text,
		mxCellState state, Map<String, Object> style)
{
	mxLightweightLabel textRenderer = mxLightweightLabel
			.getSharedInstance();
	CellRendererPane rendererPane = canvas.getRendererPane();
	Rectangle rect = state.getLabelBounds().getRectangle();
	Graphics2D g = canvas.getGraphics();

	if (textRenderer != null
			&& rendererPane != null
			&& (g.getClipBounds() == null || g.getClipBounds().intersects(
					rect)))
	{
		double scale = canvas.getScale();
		int x = rect.x;
		int y = rect.y;
		int w = rect.width;
		int h = rect.height;

		boolean horizontal = mxUtils.isTrue(style,
				mxConstants.STYLE_HORIZONTAL, true);
		String degree;

		     degree = (String) style.get(MXConstants.FONT_ROTATION_DEGREE);
		     if (degree != null) {
		        if (degree.equals("90")) {
		            g.rotate(Math.PI / 2, x + w / 2, y + h / 2);
		            g.translate(w / 2 - h / 2, h / 2 - w / 2);
		        } else if (degree.equals("270")) {
		            g.rotate(-Math.PI / 2, x + w / 2, y + h / 2);
		            g.translate(w / 2 - h / 2, h / 2 - w / 2);
		        }

		        if (degree.equals("180")) {
		            g.rotate(Math.PI, x + w / 2, y + h / 2);
		        } else if (degree.equals("360")) {
		            g.rotate(Math.PI * 2, x + w / 2, y + h / 2);
		        }
		    }
		 

		// Replaces the linefeeds with BR tags
		if (isReplaceHtmlLinefeeds())
		{
			text = text.replaceAll("\n", "<br>");
		}

		// Renders the scaled text
		textRenderer.setText(createHtmlDoc(style, text, scale, 0, null));
		textRenderer.setFont(Utils.getFont(style, canvas.getScale()));
		g.scale(scale, scale);
		rendererPane.paintComponent(g, textRenderer, rendererPane,
				(int) (x / scale) + mxConstants.LABEL_INSET,
				(int) (y / scale) + mxConstants.LABEL_INSET,
				(int) (w / scale), (int) (h / scale), true);
	}
}
/**
 * Returns a new, empty DOM document. The head argument can be used to
 * provide an optional HEAD section without the HEAD tags as follows:
 * 
 * <pre>
 * mxUtils.createHtmlDocument(style,  text, 1, 0, "<style type=\"text/css\">.classname { color:red; }</style>")
 * </pre>
 * 
 * @return Returns a new DOM document.
 */
public static String createHtmlDoc(Map<String, Object> style,
		String text, double scale, int width, String head)
{
	StringBuffer css = new StringBuffer();
	css.append("font-family:"
			+ mxUtils.getString(style, mxConstants.STYLE_FONTFAMILY,
					mxConstants.DEFAULT_FONTFAMILIES) + ";");
	css.append("font-size:"
			+ (int) (mxUtils.getInt(style, mxConstants.STYLE_FONTSIZE,
					mxConstants.DEFAULT_FONTSIZE) * scale) + " pt;");

	String color = "#000000";

	if (color != null)
	{
		css.append("color:" + color + ";");
	}
	
	String fontStyle = Utils.getString(style, MXConstants.FONT_STYLE, "normal");
	css.append("font-style:"+fontStyle+";");
	String fontWeight = Utils.getString(style, MXConstants.FONT_WEIGHT, "normal");
	css.append("font-weight:"+fontWeight+";");
	String fontDecoration = Utils.getString(style, MXConstants.FONT_DECORATION);
	if(fontDecoration != null)
		css.append("text-decoration:"+fontDecoration+";");

	String align = mxUtils.getString(style, mxConstants.STYLE_ALIGN,
			mxConstants.ALIGN_LEFT);

	if (align.equals(mxConstants.ALIGN_CENTER))
	{
		css.append("text-align:center;");
	}
	else if (align.equals(mxConstants.ALIGN_RIGHT))
	{
		css.append("text-align:right;");
	}

	if (width > 0)
	{
		// LATER: With max-width support, wrapped text can be measured in 1 step
		css.append("width:" + width + "px;");
	}

	String result = "<html>";

	if (head != null)
	{
		result += "<head>" + head + "</head>";
	}

	return result + "<body style=\"" + css.toString() + "\">" + text
			+ "</body></html>";
}
}
