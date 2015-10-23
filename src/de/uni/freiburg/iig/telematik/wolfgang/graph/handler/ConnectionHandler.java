/**
 * $Id: mxConnectionHandler.java,v 1.1 2012/11/15 13:26:44 gaudenz Exp $
 * Copyright (c) 2008, Gaudenz Alder
 */
package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
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
	private int xPosition;
	private int yPosition;

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
			/**
			 * 
			 */
			public void update(MouseEvent e, mxCellState targetState, double x, double y) {

				mxGraph graph = graphComponent.getGraph();
				mxICell cell = (mxICell) previewState.getCell();

				mxRectangle dirty = graphComponent.getGraph().getPaintBounds(new Object[] { previewState.getCell() });

				if (cell.getTerminal(false) != null) {
					cell.getTerminal(false).removeEdge(cell, false);
				}

				if (targetState != null) {
					((mxICell) targetState.getCell()).insertEdge(cell, false);
				}

				mxGeometry geo = graph.getCellGeometry(previewState.getCell());
				geo.setTerminalPoint(startPoint, true);

				try {
					int placeSize = EditorProperties.getInstance().getDefaultPlaceSize() / 2;
					int transitionWidth = EditorProperties.getInstance().getDefaultTransitionWidth() / 2;
					int transitionHeight = EditorProperties.getInstance().getDefaultTransitionHeight() / 2;
					if (sourceState != null)
						if (sourceState.getCell() instanceof PNGraphCell) {
							PNGraphCell pnCell = ((PNGraphCell) sourceState.getCell());
							switch (pnCell.getType()) {
							case ARC:
								break;
							case PLACE:
								x = x < transitionWidth ? transitionWidth : x;
								y = y < transitionHeight ? transitionHeight : y;
								break;
							case TRANSITION:
								x = x < placeSize ? placeSize : x;
								y = y < placeSize ? placeSize : y;
								break;
							default:
								break;
							}
						}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(getGraphComponent(), "Nodesizes could not be loaded from WolfgangProperties", "Wolfgang Property Exception", JOptionPane.ERROR_MESSAGE);
				}
				// set xPosition and yPosition for creation of Mousevent in
				// mousereleased()
				xPosition = (int) x;
				yPosition = (int) y;

				geo.setTerminalPoint(transformScreenPoint(x, y), false);
				revalidate(previewState);
				
				// Neccesary?
				// revalidate(graph.getView().getState(graph.getDefaultParent()));
				fireEvent(new mxEventObject(mxEvent.CONTINUE, "event", e, "x", x, "y", y));

				// Repaints the dirty region
				// TODO: Cache the new dirty region for next repaint
				Rectangle tmp = getDirtyRect(dirty);

				if (tmp != null) {
					graphComponent.getGraphControl().repaint(tmp);
				} else {
					graphComponent.getGraphControl().repaint();
				}
			}

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
						try {
							result = ((PNGraph) graphComponent.getGraph()).addNewFlowRelation((PNGraphCell) src, (PNGraphCell) trg);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(graphComponent), "Cannot insert flow relation.\nReason: " + ex.getMessage(), "Internal Error",
									JOptionPane.ERROR_MESSAGE);
						}
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

		e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), xPosition, yPosition, e.getClickCount(), false);
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
								try {
									targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewTransition(graphComponent.getPointForEvent(e));
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(graphComponent), "Cannot insert transition.\nReason: " + ex.getMessage(), "Internal Error",
											JOptionPane.ERROR_MESSAGE);
								}
								break;
							case TRANSITION:
								try {
									targetCell = (PNGraphCell) ((PNGraph) graphComponent.getGraph()).addNewPlace(graphComponent.getPointForEvent(e));
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(graphComponent), "Cannot insert place.\nReason: " + ex.getMessage(), "Internal Error",
											JOptionPane.ERROR_MESSAGE);
								}
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
