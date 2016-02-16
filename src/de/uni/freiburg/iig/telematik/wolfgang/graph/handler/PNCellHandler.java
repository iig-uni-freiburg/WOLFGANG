package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class PNCellHandler extends mxCellHandler {

	public PNCellHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
	}

	@Override
	protected boolean isHandleVisible(int index) {
		 return !isLabel(index) || (isLabelMovable()&& ((PNGraph) getGraphComponent().getGraph()).isLabelSelected());
	}


}
