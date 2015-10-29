package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;


import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.swing.util.mxCellOverlay;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangPropertyAdapter;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.ConnectionHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.GraphHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.GraphTransferHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.PNCellHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.PNEdgeHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.PNElbowEdgeHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.handler.PNVertexHandler;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.ConnectorShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.DefaultTextShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.EllipseShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.HtmlTextShape;
import de.uni.freiburg.iig.telematik.wolfgang.graph.shape.RectangleShape;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;

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

	@Override
	protected GraphHandler createGraphHandler() {
		// TODO Auto-generated method stub
		return new GraphHandler(this);
	}

	private static final long serialVersionUID = 1411737962538427287L;

	private EditorPopupMenu popupMenu = null;

	private JPopupMenu pmnTransition;

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
			final PNGraphCell cell = getGraph().getNodeCell(n);
			mxCellOverlay overlay = null;

			try {
				ImageIcon playIconImage = IconFactory.getIcon("playred");
				Image image = playIconImage.getImage();
				BufferedImage bi = toBufferedImage(image);
				// image.ge
				String label = getGraph().getNetContainer().getPetriNet().getTransition(cell.getId()).getLabel();
				BufferedImage img = drawLabelonPlayIcon((BufferedImage) bi, label);
				BufferedImage blankImage = new BufferedImage((int)cell.getGeometry().getWidth(), (int)cell.getGeometry().getHeight(),BufferedImage.TYPE_INT_ARGB);
				Graphics blankGraphics = blankImage.getGraphics();
				blankGraphics.drawImage(img, (int)cell.getGeometry().getWidth()/2-img.getWidth()/2, (int)cell.getGeometry().getHeight()/2-img.getHeight()/2, null);
				overlay = new mxCellOverlay(new ImageIcon(blankImage), null);
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
					} catch (PNException e) {
						JOptionPane.showMessageDialog(getGraphComponent(), "Petri Net Exception \nReason: " + e.getMessage(), "Petri Net Exception", JOptionPane.ERROR_MESSAGE);
					}
					marker.setVisible(false);
					highlightEnabledTransitions();

				}
			});
			addCellOverlay(cell, overlay);

		}

	}

	private BufferedImage drawLabelonPlayIcon(BufferedImage old, String string) {
		int w = old.getWidth();
		int h = old.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(old, 0, 0, null);
		g2d.setPaint(Color.BLACK);
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 12));
		String s = string;
		FontMetrics fm = g2d.getFontMetrics();
		while (old.getWidth() + 2 < g2d.getFontMetrics().stringWidth(s)) {
			int size = g2d.getFont().getSize();
			g2d.setFont(new Font("TimesRoman", Font.BOLD, size - 1));
		}
		int y = fm.getHeight() + 5;
		g2d.setColor(Color.RED);
		g2d.fillRect(0, y - 10, fm.stringWidth(s), 12);
		g2d.setColor(Color.BLACK);
		g2d.drawString(s, 1, y);
		g2d.dispose();

		return img;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	protected void unhighlightArcs() {
		Collection<AbstractFlowRelation> flowrelations = (Collection<AbstractFlowRelation>) getGraph().getNetContainer().getPetriNet().getFlowRelations();
		for (AbstractFlowRelation fr : flowrelations) {
			PNGraphCell cell = getGraph().getNodeCell(fr.getName());
			mxCellMarker marker = getCellMarker(cell);
			marker.highlight(graph.getView().getState(cell), Color.ORANGE);
		}

	}

	private void highlightArcs() {

		Collection<AbstractFlowRelation> flowrelations = (Collection<AbstractFlowRelation>) getGraph().getNetContainer().getPetriNet().getFlowRelations();
		for (AbstractFlowRelation fr : flowrelations) {
			PNGraphCell cell = getGraph().getNodeCell(fr.getName());
			mxCellMarker marker = getCellMarker(cell);
			marker.highlight(graph.getView().getState(cell), Color.MAGENTA);
			marker.setVisible(true);

		}
	}
	
	public void markPath(List<String> path, Color color) {
		mxCellMarker marker = new mxCellMarker(getGraphComponent());
		for (String s:path){
			getCellMarker(getGraph().getNodeCell(s)).highlight(getGraph().getView().getState(getGraph().getNodeCell(s)), color);
			getCellMarker(getGraph().getNodeCell(s)).setVisible(true);
			//PNGraphCell cell = getGraph().getNodeCell(s);
			//marker.highlight(getGraph().getView().getState(cell), color);
			//marker.setVisible(true);
		}
		
	}

	private mxCellMarker getCellMarker(PNGraphCell cell) {
		if (!markerReference.containsKey(cell.getId()))
			markerReference.put(cell.getId(), new mxCellMarker(this));
		return markerReference.get(cell.getId());
	}

	public void highlightPath() {
		Set<?> nodes = getGraph().getNetContainer().getPetriNet().getNodes();
		for (Object n : nodes) {
			String s = n.toString();
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
		setGridStyle(mxGraphComponent.GRID_STYLE_LINE);
		setBackgroundColor();
		setGridColor();
		setGridVisibility();
		addWGPropertiesListener();
		setToolTips(true);
		setBorder(null);
		getGraphControl().addMouseListener(new GCMouseAdapter());
		addMouseWheelListener(new GCMouseWheelListener());
		// addKeyListener(new GCKeyListener());
		getConnectionHandler().setCreateTarget(true);
	}

	private void addWGPropertiesListener() {
		try {
			EditorProperties.getInstance().addListener(new WolfgangPropertyAdapter() {

				@Override
				public void backgroundColorChanged(Color backgroundColor) {
					setBackgroundColor();
					refresh();
				}

				@Override
				public void gridColorChanged(Color gridColor) {
					setGridColor();
					refresh();
				}

				@Override
				public void gridVisibilityChanged(boolean gridVisibility) {
					setGridVisibility();
					refresh();
				}

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setBackgroundColor() {
		try {
			getViewport().setBackground(EditorProperties.getInstance().getBackgroundColor());
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setGridColor() {
		try {
			setGridColor(EditorProperties.getInstance().getGridColor());
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setGridVisibility() {
		try {
			setGridVisible(EditorProperties.getInstance().getGridVisibility());
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void setTransitionPopupMenu(JPopupMenu pmn) {
		this.pmnTransition = pmn;
	}

	public JPopupMenu getTransitionPopupMenu() {
		return pmnTransition;
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
		//Fixes continual selection
		getGraph().setSelectionCell(null);
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
					getGraph().setSelectionCells(getGraph().getSelectionCells());
//					getGraph().invoke(PNGraphComponent.this, new mxEventObject(mxEvent.CHANGE));
//					getSelectionCellsHandler().refresh();
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
		
		for (AbstractTransition transistion : getGraph().getNetContainer().getPetriNet().getTransitions()) {
			PNGraphCell cell = getGraph().getNodeCell(transistion.getName());
			removeCellOverlays(cell);
		}

	}

}
