package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Rectangle;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;

public class PNVertexHandler extends mxVertexHandler {

	public PNVertexHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
		// TODO Auto-generated constructor stub
	}
//	@Override
//	protected boolean isHandleVisible(int index) {
//		System.out.println((((PNGraph)graphComponent.getGraph()).isLabelSelected() && isLabel(index)) + "<--------");
////		if(((PNGraph)graphComponent.getGraph()).isLabelSelected() && isLabel(index))
////			return true;
//		return (!isLabel(index) && !((PNGraph)graphComponent.getGraph()).isLabelSelected()); //|| isLabelMovable();
//
//	}

	@Override
	protected Rectangle[] createHandles() {
		// TODO Auto-generated method stub
		Rectangle[] h = null;

		if (graphComponent.getGraph().isCellResizable(getState().getCell()))
		{
			Rectangle bounds = getState().getRectangle();
			int half = mxConstants.HANDLE_SIZE / 2;

			int left = bounds.x - half;
			int top = bounds.y - half;

			int w2 = bounds.x + (bounds.width / 2) - half;
			int h2 = bounds.y + (bounds.height / 2) - half;

			int right = bounds.x + bounds.width - half;
			int bottom = bounds.y + bounds.height - half;

			h = new Rectangle[9];

			int s = mxConstants.HANDLE_SIZE;
			h[0] = new Rectangle(left, top, s, s);
			h[1] = new Rectangle(w2, top, s, s);
			h[2] = new Rectangle(right, top, s, s);
			h[3] = new Rectangle(left, h2, s, s);
			h[4] = new Rectangle(right, h2, s, s);
			h[5] = new Rectangle(left, bottom, s, s);
			h[6] = new Rectangle(w2, bottom, s, s);
			h[7] = new Rectangle(right, bottom, s, s);
		}
		else
		{
			h = new Rectangle[1];
		}
System.out.println(((PNGraph)getGraphComponent().getGraph()).isLabelSelected());

		int s = mxConstants.LABEL_HANDLE_SIZE;
		mxRectangle bounds = state.getLabelBounds();
		h[h.length - 1] = new Rectangle((int) (bounds.getX()
				+ bounds.getWidth() / 2 - s), (int) (bounds.getY()
				+ bounds.getHeight() / 2 - s), 2 * s, 2 * s);

		return h;
	}
	
}
