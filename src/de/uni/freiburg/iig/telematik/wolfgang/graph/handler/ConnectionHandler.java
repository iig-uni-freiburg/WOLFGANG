/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;

/**
 * Connection handler creates new connections between cells. This control is
 * used to display the connector icon, while the preview is used to draw the
 * line.
 * 
 * mxEvent.CONNECT fires between begin- and endUpdate in mouseReleased. The
 * <code>cell</code> property contains the inserted edge, the <code>event</code>
 * and <code>target</code> properties contain the respective arguments that were
 * passed to mouseReleased.
 */
public class ConnectionHandler extends mxConnectionHandler {

	public ConnectionHandler(PNGraphComponent arg0) {
		super(arg0);
	}

	private PNGraphCell getSource() {
		return (PNGraphCell) source.getCell();
	}

	private PNGraphComponent getGraphComponent() {
		return (PNGraphComponent) graphComponent;
	}

	@Override
	protected mxConnectPreview createConnectPreview() {
		return new mxConnectPreview(getGraphComponent()) {
			@Override
			public Object stop(boolean commit, MouseEvent e) {
				Object result = (sourceState != null) ? sourceState.getCell() : null;

				if (previewState != null) {
					PNGraph graph = getGraphComponent().getGraph();

					graph.getModel().beginUpdate();
					mxICell cell = (mxICell) previewState.getCell();
					Object src = cell.getTerminal(true);
					Object trg = cell.getTerminal(false);

					if (src != null) {
						((mxICell) src).removeEdge(cell, true);
					}

					if (trg != null) {
						((mxICell) trg).removeEdge(cell, false);
					}

					if (commit) {

						result = ((PNGraph) graphComponent.getGraph()).addNewFlowRelation((PNGraphCell) src, (PNGraphCell) trg);

					}
					fireEvent(new mxEventObject(mxEvent.STOP, "event", e, "commit", commit, "cell", (commit) ? result : null));

					// Clears the state before the model commits
					if (previewState != null) {
						Rectangle dirty = getDirtyRect();
						graph.getView().clear(cell, false, true);
						previewState = null;

						if (!commit && dirty != null) {
							getGraphComponent().getGraphControl().repaint(dirty);
						}
					}
					graph.getModel().endUpdate();
					// Repaint to avoid graph-artifacts of unallowed connections
					graph.repaint();
				}

				sourceState = null;
				startPoint = null;

				return result;
			}

		};
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseReleased(MouseEvent e) {

		if (isActive()) {
			if (error == null && first != null) {
				PNGraph graph = getGraphComponent().getGraph();
				double dx = first.getX() - e.getX();
				double dy = first.getY() - e.getY();

				PNGraphCell targetCell = null;
				Object edgeCell = null;

				if (connectPreview.isActive() && (marker.hasValidState() || isCreateTarget() || graph.isAllowDanglingEdges())) {
					graph.getModel().beginUpdate();

					try {

						if (!marker.hasValidState() && isCreateTarget()) {

							switch (getSource().getType()) {
							case PLACE:
								targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewTransition(graphComponent.getPointForEvent(e));
								break;
							case TRANSITION:
								targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewPlace(graphComponent.getPointForEvent(e));
								break;
							}

							mxCellState targetState = graph.getView().getState(targetCell, true);
							connectPreview.update(e, targetState, e.getX(), e.getY());
						}

						edgeCell = connectPreview.stop(graphComponent.isSignificant(dx, dy), e);

						if (edgeCell != null) {
							eventSource.fireEvent(new mxEventObject(mxEvent.CONNECT, "cell", edgeCell, "event", e, "target", targetCell));
						}

						e.consume();
					} finally {
						graph.getModel().endUpdate();
						if (targetCell != null) {
							((PNGraph) graphComponent.getGraph()).setSelectionCell(targetCell);
						} else {
							((PNGraph) graphComponent.getGraph()).setSelectionCell(edgeCell);
						}

					}
				}

			}
		}

		reset();
	}

	@Override
	/**
	 * 
	 */
	public void paint(Graphics g) {
		if (bounds != null) {
			if (connectIcon != null) {
				g.drawImage(connectIcon.getImage(), bounds.x, bounds.y, bounds.width, bounds.height, null);
			} else if (handleEnabled) {
				g.setColor(Color.BLACK);
				g.draw3DRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, true);
				g.setColor(Color.LIGHT_GRAY);
				g.fill3DRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2, true);
				g.setColor(Color.BLUE);
				g.drawRect(bounds.x + bounds.width / 2 - 1, bounds.y + bounds.height / 2 - 1, 1, 1);
			}
		}
	}

}
