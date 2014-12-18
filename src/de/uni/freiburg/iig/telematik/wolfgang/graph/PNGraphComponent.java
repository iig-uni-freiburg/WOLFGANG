package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.w3c.dom.Document;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.swing.util.mxCellOverlay;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.ConnectionHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.PNEdgeHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.GraphTransferHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.ConnectorShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.DefaultTextShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.EllipseShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.HtmlTextShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.RectangleShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;

public abstract class PNGraphComponent extends mxGraphComponent {

	@Override
	/**
	 * 
	 * @param event
	 * @return Returns true if the given event should toggle selected cells.
	 */
	public boolean isToggleEvent(MouseEvent event) {
		// NOTE: IsMetaDown always returns true for right-clicks on the Mac, so
		// toggle selection for left mouse buttons requires CMD key to be
		// pressed,
		// but toggle for right mouse buttons requires CTRL to be pressed.
		return (event != null) ? ((mxUtils.IS_MAC) ? ((SwingUtilities.isLeftMouseButton(event) && event.isMetaDown()) || (SwingUtilities.isRightMouseButton(event) && event.isControlDown() || event
				.isShiftDown())) : event.isControlDown()) : false;
	}

	private static final long serialVersionUID = 1411737962538427287L;

	private EditorPopupMenu popupMenu = null;

	private TransitionPopupMenu transitionPopupMenu;

	private Map<String, mxCellMarker> markerReference = new HashMap<String, mxCellMarker>();

	public PNGraphComponent(PNGraph graph) {
		super(graph);
		initialize();
		getCanvas().putShape(mxConstants.SHAPE_RECTANGLE, new RectangleShape());
		getCanvas().putShape(mxConstants.SHAPE_ELLIPSE, new EllipseShape());
		getCanvas().putTextShape(mxGraphics2DCanvas.TEXT_SHAPE_DEFAULT, new DefaultTextShape());
		getCanvas().putTextShape(mxGraphics2DCanvas.TEXT_SHAPE_HTML, new HtmlTextShape());
		getCanvas().putShape(mxConstants.SHAPE_CONNECTOR, new ConnectorShape());

	}

	public void highlightEnabledTransitions() {
		Set<String> nameSet = null;

		nameSet = PNUtils.getNameSetFromTransitions(getGraph().getNetContainer().getPetriNet().getEnabledTransitions(), true);

		removeCellOverlays();
		getGraph().setCellsSelectable(false);
		for (final String n : nameSet) {
			final PNGraphCell cell = getGraph().nodeReferences.get(n);
			Rectangle geo = cell.getGeometry().getRectangle();
			// enabledTransitionsPanel =
			mxCellOverlay overlay = null;

			try {
				overlay = new mxCellOverlay(IconFactory.getIcon("playred"), null);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "playred-Icon could not be assinged as cell-overlay. \nReason: " + e1.getMessage(), "" + e1.getClass(), JOptionPane.ERROR);
			}
			overlay.setAlign(mxConstants.ALIGN_CENTER);
			overlay.setVerticalAlign(mxConstants.ALIGN_MIDDLE);
			final mxCellMarker marker = new mxCellMarker(getGraphComponent());

			overlay.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent arg0) {

				}

				@Override
				public void mousePressed(MouseEvent arg0) {

				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					marker.setVisible(false);
					// unhighlightArcs();

				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// highlightArcs();
					marker.setVisible(true);
					marker.highlight(graph.getView().getState(cell), Color.BLUE);

				}

				@Override
				public void mouseClicked(MouseEvent arg0) {

					try {
						getGraph().fireTransition(cell);
					} catch (ParameterException e) {
						JOptionPane.showMessageDialog(null, "Parameter Exception \nReason: " + e.getMessage(), "Parameter Exception", JOptionPane.ERROR);
					} catch (PNException e) {
						JOptionPane.showMessageDialog(null, "Petri Net Exception \nReason: " + e.getMessage(), "Petri Net Exception", JOptionPane.ERROR);
					}
					marker.setVisible(false);
					highlightEnabledTransitions();

				}
			});

			addCellOverlay(cell, overlay);

		}

	}

	protected void unhighlightArcs() {
		Collection<AbstractFlowRelation> flowrelations = (Collection<AbstractFlowRelation>) getGraph().getNetContainer().getPetriNet().getFlowRelations();
		for (AbstractFlowRelation fr : flowrelations) {
			PNGraphCell cell = getGraph().arcReferences.get(fr.getName());
			mxCellMarker marker = getCellMarker(cell);
			marker.highlight(graph.getView().getState(cell), Color.ORANGE);
		}

	}

	private void highlightArcs() {

		Collection<AbstractFlowRelation> flowrelations = (Collection<AbstractFlowRelation>) getGraph().getNetContainer().getPetriNet().getFlowRelations();
		for (AbstractFlowRelation fr : flowrelations) {
			PNGraphCell cell = getGraph().arcReferences.get(fr.getName());
			mxCellMarker marker = getCellMarker(cell);
			marker.highlight(graph.getView().getState(cell), Color.MAGENTA);
			marker.setVisible(true);

		}
	}
	


	private mxCellMarker getCellMarker(PNGraphCell cell) {
		if (!markerReference.containsKey(cell.getId()))
			markerReference.put(cell.getId(), new mxCellMarker(this));
		return markerReference.get(cell.getId());
	}

	public void highlightPath() {
		Set<?> nodes = getGraph().getNetContainer().getPetriNet().getNodes();
		System.out.println(nodes);
		for (Object n : nodes) {
			String s = n.toString();
			System.out.println(s);
		}

	}

	protected mxGraphComponent getGraphComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public PNGraph getGraph() {
		return (PNGraph) super.getGraph();
	}

	@Override
	/**
	 * 
	 */
	protected TransferHandler createTransferHandler() {
		return new GraphTransferHandler();
	}

	private void initialize() {
		getViewport().setOpaque(true);
		getViewport().setBackground(MXConstants.blueBG);
		setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
		setGridColor(MXConstants.bluehigh);
		// setGridVisible(true);
		setGridVisible(true);
		setToolTips(true);
		getGraphControl().addMouseListener(new GCMouseAdapter());
		addMouseWheelListener(new GCMouseWheelListener());
		// addKeyListener(new GCKeyListener());
		getConnectionHandler().setCreateTarget(true);
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(GraphResource.class.getResource("default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
	}

	@Override
	protected ConnectionHandler createConnectionHandler() {
		return new ConnectionHandler(this);
	}

	@Override
	/**
	 * 
	 * @param state
	 *            Cell state for which a handler should be created.
	 * @return Returns the handler to be used for the given cell state.
	 */
	public mxCellHandler createHandler(mxCellState state) {
		if (graph.getModel().isVertex(state.getCell())) {
			return new PNVertexHandler(this, state);
		} else if (graph.getModel().isEdge(state.getCell())) {
			mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state, null, null, null);

			if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector || style == mxEdgeStyle.SideToSide || style == mxEdgeStyle.TopToBottom) {
				return new PNElbowEdgeHandler(this, state);
			}

			return new PNEdgeHandler(this, state);
		}

		return new PNCellHandler(this, state);
	}

	public void setPopupMenu(EditorPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public EditorPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setTransitionPopupMenu(TransitionPopupMenu transitionPopupMenu) {
		this.transitionPopupMenu = transitionPopupMenu;
	}

	public TransitionPopupMenu getTransitionPopupMenu() {
		return transitionPopupMenu;
	}

	// ------- MouseListener support
	// ------------------------------------------------------------------

	protected boolean rightClickOnCanvas(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		popupMenu.show(PNGraphComponent.this, pt.x, pt.y);
		return false;
	}

	protected boolean rightClickOnPlace(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean rightClickOnArc(PNGraphCell cell, MouseEvent e) {
		if (getGraph().isLabelSelected())
			rightClickOnArcLabel(cell, e);
		else {
			Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
			mxCellHandler handler = getSelectionCellsHandler().getHandler(cell);
			if (handler instanceof PNEdgeHandler) {
				int index = handler.getIndex();
				int indexbymouse = handler.getIndexAt(pt.x, pt.y);
				if (indexbymouse > 0) {
					getGraph().removePoint(cell, indexbymouse);
				}
			}
		}
		return true;
	}

	protected boolean doubleClickOnCanvas(MouseEvent e) {
		return false;
	}

	protected boolean mouseWheelOnCanvas(MouseEvent e) {
		return false;
	}

	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		return false;
	}

	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		if (getGraph().isLabelSelected())
			doubleClickOnArcLabel(cell, e);
		else {
			Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
			getGraph().addWayPoint(cell, pt);
		}

		return true;
	}

	protected boolean doubleClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean rightClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
		return false;
	}

	protected boolean singleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	private class GCMouseWheelListener implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Object object = getGraph().getSelectionCell();
			PNGraphCell cell = null;
			if (object != null) {
				cell = (PNGraphCell) object;
			}
			boolean refresh = false;

			// Double click on graph component.
			if (object == null) {
				refresh = mouseWheelOnCanvas(e);
			} else {
				switch (cell.getType()) {
				case PLACE:
					refresh = mouseWheelOnPlace(cell, e);
					break;
				}
			}

			if (refresh) {
				mxCellState state = getGraph().getView().getState(cell);
				redraw(state);
			}

		}

	}

	private class GCKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent event) {
			int dx = 0;
			int dy = 0;
			if (event.getKeyChar() == KeyEvent.VK_LEFT) {
				dx--;
			}
			if (event.getKeyChar() == KeyEvent.VK_DOWN) {
				dy++;
			}
			if (event.getKeyChar() == KeyEvent.VK_RIGHT) {
				dx++;
			}
			if (event.getKeyChar() == KeyEvent.VK_UP) {
				dy--;
			}
			graph.moveCells(graph.getSelectionCells(), dx, dy);
		}

	}

	private class GCMouseAdapter extends MouseAdapter {

		@Override
		/**
		 * 
		 */
		public void mousePressed(MouseEvent e) {

			// Handles context menu on the Mac where the trigger is on
			// mousepressed
			// mouseClicked(e.getModifiers());

		}

		@Override
		/**
		 * 
		 */
		public void mouseReleased(MouseEvent e) {
			// Handles context menu on Windows where the trigger is on
			// mousereleased
			// TODO also working on Linux?
			// mouseClicked(e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
				if (e.getWheelRotation() < 0) {
					zoomIn();
				} else {
					zoomOut();
				}
				// displayStatusMessage(String.format(scaleMessageFormat, (int)
				// (100 * getGraph().getView().getScale())));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Object object = getCellAt(e.getX(), e.getY());
			PNGraphCell cell = null;
			if (object != null) {
				cell = (PNGraphCell) object;
			}
			boolean refresh = false;
			if (e.getClickCount() == 1) {
				if (!(e.getModifiers() == 4)) {
					// Double click on graph component.
					if (object == null) {
						refresh = doubleClickOnCanvas(e);
					} else {
						switch (cell.getType()) {
						case PLACE:
							// refresh = doubleClickOnPlace(cell, e);
							break;
						case TRANSITION:
							refresh = singleClickOnTransition(cell, e);
							break;
						case ARC:
							// refresh = doubleClickOnArc(cell, e);
							break;
						}
					}
				}

				if (e.getModifiers() == 4 && !getGraph().isExecution()) {
					// Right click on graph component.
					if (object == null) {
						refresh = rightClickOnCanvas(e);
					} else {
						switch (cell.getType()) {
						case PLACE:
							refresh = rightClickOnPlace(cell, e);
							break;
						case TRANSITION:
							refresh = rightClickOnTransition(cell, e);
							break;
						case ARC:
							refresh = rightClickOnArc(cell, e);
							break;
						}
					}
				} else {
					// Left click on graph component.
					mxCellState cellState = getGraph().getView().getState(cell);
					if (cellState != null) {
						getGraph().setLabelSelected(cellState.getLabelBounds().contains(e.getX(), e.getY()));
					} else {
						getGraph().setLabelSelected(false);
					}
					getGraph().invoke(PNGraphComponent.this, new mxEventObject(mxEvent.CHANGE));
				}
			} else if (e.getClickCount() == 2 && !(e.getModifiers() == 4) && !getGraph().isExecution()) {
				// Double click on graph component.
				if (object == null) {
					refresh = doubleClickOnCanvas(e);
				} else {
					switch (cell.getType()) {
					case PLACE:
						refresh = doubleClickOnPlace(cell, e);
						break;
					case TRANSITION:
						refresh = doubleClickOnTransition(cell, e);
						break;
					case ARC:
						refresh = doubleClickOnArc(cell, e);
						break;
					}
				}
			}

			if (refresh) {
				mxCellState state = getGraph().getView().getState(cell);
				redraw(state);
				refresh();
			}
		}

	}

	public void removeCellOverlays() {
		for (Entry<String, PNGraphCell> cell : getGraph().nodeReferences.entrySet())
			removeCellOverlays(cell.getValue());

	}

}
