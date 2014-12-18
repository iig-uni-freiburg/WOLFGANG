package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Color;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;

public class PNElbowEdgeHandler extends mxElbowEdgeHandler {

	public PNElbowEdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean isHandleVisible(int index) {
		// TODO Auto-generated method stub
		 return !isLabel(index) || (isLabelMovable()&& ((PNGraph) getGraphComponent().getGraph()).isLabelSelected());
	}
	@Override
	protected Color getHandleFillColor(int index)
	{
		if (isLabel(index))
		{
			return MXConstants.LABEL_HANDLE_FILLCOLOR;
		}

		return MXConstants.HANDLE_FILLCOLOR;
	}

	@Override
	/**
	 * 
	 */
	public Color getSelectionColor()
	{
		return MXConstants.VERTEX_SELECTION_COLOR;
	}
		
}
