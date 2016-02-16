package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;

public class PNEdgeHandler extends mxEdgeHandler {

	@Override
	public String getToolTipText(MouseEvent e) {
		return "<html>double-click to add waypoint <br>right-click on waypoint delete it</html>";
	}

	public PNEdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
	}
	
	@Override
	/**
	 * 
	 */
	protected Rectangle createHandle(Point center)
	{
		return createHandle(center, MXConstants.EDGE_HANDLE_SIZE);
	}
	
	@Override
	protected boolean isHandleVisible(int index) {
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
		return MXConstants.LABEL_HANDLE_FILLCOLOR;
	}

}
