package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class GraphView extends mxGraphView {

	public GraphView(mxGraph graph) {
		super(graph);
	}

	@Override
	/**
	 * Updates the label bounds in the given state.
	 */
	public void updateLabelBounds(mxCellState state) {
		Object cell = state.getCell();
		Map<String, Object> style = state.getStyle();

		if (mxUtils.getString(style, mxConstants.STYLE_OVERFLOW, "").equals("fill")) {
			state.setLabelBounds(new mxRectangle(state));
		} else if (state.getLabel() != null) {
			mxRectangle vertexBounds = (!graph.getModel().isEdge(cell)) ? state : null;

			state.setLabelBounds(mxUtils.getLabelPaintBounds(state.getLabel(), style, graph.isHtmlLabel(cell), state.getAbsoluteOffset(), vertexBounds, scale));
		}
	}

}
