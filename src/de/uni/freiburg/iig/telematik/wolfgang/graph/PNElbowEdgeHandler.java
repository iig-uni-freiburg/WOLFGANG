package de.uni.freiburg.iig.telematik.wolfgang.graph;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.view.mxCellState;

public class PNElbowEdgeHandler extends mxElbowEdgeHandler {

	public PNElbowEdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean isHandleVisible(int index) {
		System.out.println(index + "handles");
		if(((PNGraph)graphComponent.getGraph()).isLabelSelected() && isLabel(index))
			return false;
		return super.isHandleVisible(index);
	}
}
