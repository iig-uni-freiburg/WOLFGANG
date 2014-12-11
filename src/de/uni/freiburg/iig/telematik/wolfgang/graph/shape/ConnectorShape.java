package de.uni.freiburg.iig.telematik.wolfgang.graph.shape;

import java.awt.Color;
import java.awt.Paint;
import java.util.Map;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxConnectorShape;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;

public class ConnectorShape extends mxConnectorShape {

	@Override
	/**
	 * Configures the graphics object ready to paint.
	 * @param canvas the canvas to be painted to
	 * @param state the state of cell to be painted
	 * @param background whether or not this is the background stage of 
	 * 			the shape paint
	 * @return whether or not the shape is ready to be drawn
	 */
	protected boolean configureGraphics(mxGraphics2DCanvas canvas, mxCellState state, boolean background) {
		Map<String, Object> style = state.getStyle();
		double scale = canvas.getScale();
		if (background) {
			// Paints the background of the shape
			Paint fillPaint = (Paint) (hasGradient(canvas, state) ? Utils.createFillPaint(getGradientBounds(canvas, state), style) : null);

			if (fillPaint != null) {
				canvas.getGraphics().setPaint(fillPaint);

				return true;
			} else {
				Color color = getFillColor(canvas, state);
				canvas.getGraphics().setColor(color);
				return color != null;
			}
		} else {
			canvas.getGraphics().setPaint(null);
			Color color = getStrokeColor(canvas, state);
			canvas.getGraphics().setColor(color);
			canvas.getGraphics().setStroke(Utils.createStroke(style, scale));

			return color != null;
		}
	}
	
}
