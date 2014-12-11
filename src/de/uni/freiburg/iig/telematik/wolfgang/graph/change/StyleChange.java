package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import java.util.HashMap;

import com.mxgraph.io.graphml.mxGraphMlUtils;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxGraphModel.mxStyleChange;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;

public class StyleChange extends mxStyleChange {
	private String key;
	private PNGraph graph;

	public StyleChange() {
		this(null, null, null, null);
	}

	public StyleChange(PNGraph pnGraph, Object cell, String style, String key) {
		super((mxGraphModel) pnGraph.getModel(), cell, style);
		this.graph = pnGraph;
		this.key = key;
	}

	public void execute() {
		style = previous;
		previous = styleForCellChanged(cell, previous);
		HashMap<String, Object> styleMap = mxGraphMlUtils.getStyleMap(style, "=");
		Object value = styleMap.get(key);
		Utils.updateGraphics(
				graph,
				((PNGraphCell) cell),
				key,
				value,
				key.contains("label") || key.equals(mxConstants.STYLE_FONTSIZE) || key.equals(mxConstants.STYLE_FONTFAMILY) || key.equals(mxConstants.STYLE_ALIGN)
						|| key.equals(mxConstants.STYLE_NOLABEL));

	}

	/**
	 * Inner callback to update the style of the given mxCell using
	 * mxCell.setStyle and return the previous style.
	 */
	protected String styleForCellChanged(Object cell, String style) {
		String previous = model.getStyle(cell);
		((mxICell) cell).setStyle(style);
		return previous;
	}

}
