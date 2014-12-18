package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Color;
import java.awt.Rectangle;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.view.mxCellState;

public class PNCellHandler extends mxCellHandler {

	@Override
	public void setHandlesVisible(boolean handlesVisible) {
		// TODO Auto-generated method stub
		super.setHandlesVisible(handlesVisible);
	}

	@Override
	public boolean isHandlesVisible() {
		// TODO Auto-generated method stub
		return super.isHandlesVisible();
	}

	@Override
	protected boolean isHandleVisible(int index) {
		System.out.println(index + "handles");
		if(((PNGraph)graphComponent.getGraph()).isLabelSelected() && isLabel(index))
			return false;
//		return super.isHandleVisible(index);
		return false;
	}

	public PNCellHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Color getHandleFillColor(int index) {
		if (isLabel(index))
		{
			return mxSwingConstants.LABEL_HANDLE_FILLCOLOR;
		}

		return mxSwingConstants.HANDLE_FILLCOLOR;
//		return super.getHandleFillColor(index);
	}


}
