package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;

public class PNVertexHandler extends mxVertexHandler {

	public PNVertexHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean isHandleVisible(int index) {
		// TODO Auto-generated method stub
		return !isLabel(index) || (isLabelMovable() && ((PNGraph) getGraphComponent().getGraph()).isLabelSelected());
	}

	@Override
	protected void resizeCell(MouseEvent e) {
		mxGraph graph = graphComponent.getGraph();
		double scale = graph.getView().getScale();

		Object cell = state.getCell();
		mxGeometry geometry = graph.getModel().getGeometry(cell);

		if (geometry != null) {
			double dx = (e.getX() - first.x) / scale;
			double dy = (e.getY() - first.y) / scale;

			if (isLabel(index)) {
				geometry = (mxGeometry) geometry.clone();

				if (geometry.getOffset() != null) {
					dx += geometry.getOffset().getX();
					dy += geometry.getOffset().getY();
				}

				if (gridEnabledEvent) {
					dx = graph.snap(dx);
					dy = graph.snap(dy);
				}

				geometry.setOffset(new mxPoint(dx, dy));
				graph.getModel().setGeometry(cell, geometry);
			} else {

				// keeps aspect ratio
				double size = Math.min(dx, dy);
				dx = size;
				dy = size;
				//

				mxRectangle bounds = union(geometry, dx, dy, index);
				Rectangle rect = bounds.getRectangle();

				// Snaps new bounds to grid (unscaled)
				if (gridEnabledEvent) {
					int x = (int) graph.snap(rect.x);
					int y = (int) graph.snap(rect.y);
					rect.width = (int) graph.snap(rect.width - x + rect.x);
					rect.height = (int) graph.snap(rect.height - y + rect.y);
					rect.x = x;
					rect.y = y;
				}
				// Checks if resized cells lies in canvas
				if (rect.getX() >= 0 && rect.getY() >= 0)
					graph.resizeCell(cell, new mxRectangle(rect));
			}
		}
	}

	@Override
	protected Color getHandleFillColor(int index) {
		if (isLabel(index)) {
			return MXConstants.LABEL_HANDLE_FILLCOLOR;
		}

		return MXConstants.HANDLE_FILLCOLOR;
	}

	@Override
	/**
	 * 
	 */
	public Color getSelectionColor() {
		return MXConstants.VERTEX_SELECTION_COLOR;
	}

}
