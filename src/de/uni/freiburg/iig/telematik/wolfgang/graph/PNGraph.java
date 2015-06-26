package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.graphic.misc.CircularPointGroup;
import de.invation.code.toval.graphic.misc.PColor;
import de.invation.code.toval.graphic.util.GraphicUtils;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListener;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.RelationChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangPropertyAdapter;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.StyleChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNPropertiesListener;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperty;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNPropertyChangeEvent;

public abstract class PNGraph extends mxGraph implements PNPropertiesListener, mxIEventListener, PNStructureListener {

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PNProperties properties = null;

	private boolean labelSelected = false;
	private boolean isExecution = false;
	protected boolean hideContraintsAsTokens = true;
	private boolean containedGraphics = false;

	private PNGraphChangeHandler changeHandler;

	protected PNGraphListenerSupport graphListenerSupport = new PNGraphListenerSupport();

	public PNGraph(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer, PNProperties properties) {
		super();
		addWGPropertiesListener();

		try {
			setGridSize(EditorProperties.getInstance().getGridSize());
		} catch (PropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Validate.notNull(netContainer);
		Validate.notNull(properties);
		this.netContainer = netContainer;
		this.netContainer.getPetriNet().addStructureListener(this);
		this.properties = properties;
		this.properties.addPNPropertiesListener(this);
		this.getSelectionModel().addListener(mxEvent.CHANGE, this);

		this.addListener(mxEvent.RESIZE_CELLS, this);

		this.getModel().addListener(mxEvent.CHANGE, this);
		this.getModel().addListener(mxEvent.UNDO, this);
		changeHandler = new PNGraphChangeHandler(this);
		setCellsBendable(true);

		setHtmlLabels(true);
		setAllowDanglingEdges(false);
		setMultigraph(true);
		setCellsEditable(false);
		setDisconnectOnMove(false);
		setExtendParents(false); // disables extending parents after adding
		setVertexLabelsMovable(true);

		try {
			initialize();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addWGPropertiesListener() {
		try {
			EditorProperties.getInstance().addListener(new WolfgangPropertyAdapter() {

				@Override
				public void gridSizeChanged(int gridSize) {
					try {
						setGridSize(EditorProperties.getInstance().getGridSize());
					} catch (PropertyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					refresh();
				}

				@Override
				public void defaultTokenSizeChanged(int defaultTokenSize) {
					refresh();
				}

				@Override
				public void defaultTokenDistanceChanged(int defaultTokenDistance) {
					refresh();
				}

			});
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void initialize() throws PropertyException, IOException {
		// Check if net contains Graphical Info and keep that information
		if (netContainer.getPetriNetGraphics().getPlaceGraphics().size() > 0 || netContainer.getPetriNet().isEmpty()) {
			containedGraphics = true;
		}

		// Check if net container is empty.
		// If not, add all PN components to the graph.
		if (!netContainer.getPetriNet().isEmpty()) {
			getModel().beginUpdate();

			for (AbstractPlace place : getNetContainer().getPetriNet().getPlaces()) {
				if (netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()) == null)
					netContainer.getPetriNetGraphics().getPlaceGraphics().put(place.getName(), new NodeGraphics());
				if (netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(place.getName()) == null)
					netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().put(place.getName(), new AnnotationGraphics());

				addPlaceCell(
						place.getName(),
						MXConstants.extractNodeStyleFromGraphics(PNComponent.PLACE, netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()), netContainer.getPetriNetGraphics()
								.getPlaceLabelAnnotationGraphics().get(place.getName())));
			}
			for (AbstractTransition transition : getNetContainer().getPetriNet().getTransitions()) {
				if (netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()) == null)
					netContainer.getPetriNetGraphics().getTransitionGraphics().put(transition.getName(), new NodeGraphics());
				if (netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(transition.getName()) == null)
					netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().put(transition.getName(), new AnnotationGraphics());

				addTransitionCell(
						transition.getName(),
						MXConstants.extractNodeStyleFromGraphics(PNComponent.TRANSITION, netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()), netContainer
								.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(transition.getName())));
			}
			for (AbstractFlowRelation relation : getNetContainer().getPetriNet().getFlowRelations()) {
				if (netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()) == null)
					netContainer.getPetriNetGraphics().getArcGraphics().put(relation.getName(), new ArcGraphics());
				if (netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(relation.getName()) == null)
					netContainer.getPetriNetGraphics().getArcAnnotationGraphics().put(relation.getName(), new AnnotationGraphics());

				addArcCell(
						relation.getName(),
						MXConstants.extractArcStyleFromGraphics(netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()), netContainer.getPetriNetGraphics()
								.getArcAnnotationGraphics().get(relation.getName())));
			}
			getModel().endUpdate();
		}

	}

	public void setLabelSelected(boolean selected) {
		this.labelSelected = selected;
	}

	public boolean isLabelSelected() {
		return labelSelected;
	}

	public void addPNGraphListener(PNGraphListener listener) {
		graphListenerSupport.addPNGraphListener(listener);
	}

	public void removePNGraphListener(PNGraphListener listener) {
		graphListenerSupport.removePNGraphListener(listener);
	}

	private void ensureValidPlaceSize() {
		for (PNGraphCell selectedCell : getSelectedGraphCells()) {
			if (selectedCell.getType() == PNComponent.PLACE) {
				Rectangle bounds = selectedCell.getGeometry().getRectangle();
				if (bounds.getHeight() == bounds.getWidth()) {
					return;
				}
				int tagetSize = (int) Math.round(Math.min(bounds.getWidth(), bounds.getHeight()));
				mxRectangle targetBounds = getView().getState(selectedCell).getBoundingBox();
				targetBounds.setWidth(tagetSize);
				targetBounds.setHeight(tagetSize);
				resizeCell(selectedCell, targetBounds);
				setSelectionCell(selectedCell);
			}
		}
	}

	private Set<PNGraphCell> getSelectedGraphCells() {
		Set<PNGraphCell> selectedCells = new HashSet<PNGraphCell>();
		for (Object selectedObject : getSelectionCells()) {
			if (selectedObject instanceof PNGraphCell) {
				selectedCells.add((PNGraphCell) selectedObject);
			}
		}
		return selectedCells;
	}

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	protected PNProperties getPNProperties() {
		return properties;
	}

	/**
	 * Adds anew place with default style.
	 * 
	 * @param point
	 * @throws IOException
	 * @throws PropertyException
	 */
	public PNGraphCell addNewPlace(mxPoint point) throws PropertyException, IOException {
		Offset offset = new Offset(EditorProperties.getInstance().getDefaultHorizontalLabelOffset(), EditorProperties.getInstance().getDefaultVerticalLabelOffset());
		Dimension dimension = new Dimension(EditorProperties.getInstance().getDefaultPlaceSize(), EditorProperties.getInstance().getDefaultPlaceSize());
		return addNewPlace(point, MXConstants.getDefaultNodeStyle(PNComponent.PLACE), offset, dimension);
	}

	/**
	 * Adds a new place with existing style.
	 * 
	 * @param point
	 * @param style
	 */
	public PNGraphCell addNewPlace(mxPoint point, String style, Offset offset, Dimension dimension) {
		String nodeName = getNewPlaceName();
		PNGraphCell newPlaceCell = getNodeCell(nodeName);
		if (newPlaceCell != null) {
			nodeName = newPlaceCell.getId();
		}
		if (getNetContainer().getPetriNet().addPlace(nodeName)) {
			NodeGraphics nodeGraphics = Utils.createNodeGraphicsFromStyle(style);
			nodeGraphics.setPosition(new Position(point.getX(), point.getY()));
			nodeGraphics.setDimension(dimension);
			AnnotationGraphics annotationGraphics = Utils.createAnnotationGraphicsFromStyle(style);
			annotationGraphics.setOffset(offset);

			getNetContainer().getPetriNetGraphics().getPlaceGraphics().put(nodeName, nodeGraphics);
			getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().put(nodeName, annotationGraphics);
			if (newPlaceCell == null) {
				newPlaceCell = addPlaceCell(nodeName, style);
			}
			graphListenerSupport.notifyPlaceAdded(getNetContainer().getPetriNet().getPlace(nodeName));
			return newPlaceCell;
		}
		return null;
	}

	/**
	 * Inserts a new place with existing graphic information into the graphical
	 * Petri net.
	 * 
	 * @param nodeName
	 * @param style
	 */
	@SuppressWarnings("rawtypes")
	public PNGraphCell addPlaceCell(String nodeName, String style) {
		AbstractPlace place = getNetContainer().getPetriNet().getPlace(nodeName);
		NodeGraphics nodeGraphics = getNetContainer().getPetriNetGraphics().getPlaceGraphics(nodeName);
		AnnotationGraphics annotationGraphics = getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics(nodeName);
		PNGraphCell newCell = createPlaceCell(place.getName(), place.getLabel(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension().getX(), nodeGraphics
				.getDimension().getY(), style);

		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		// if (nodeGraphics == null || annotationGraphics == null) {
		// mxCellState state = getView().getState(newCell, true);
		// }
		addCell(newCell, getDefaultParent());
		return newCell;
	}

	public PNGraphCell createPlaceCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX - (width / 2), posY - (height / 2), width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.PLACE);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	public PNGraphCell addNewFlowRelation(PNGraphCell sourceCell, PNGraphCell targetCell) throws PropertyException, IOException {
		Offset offset = new Offset(EditorProperties.getInstance().getDefaultHorizontalLabelOffset(), EditorProperties.getInstance().getDefaultVerticalLabelOffset());
		return addNewFlowRelation(sourceCell, targetCell, offset, null, null, MXConstants.getDefaultArcStyle());
	}

	public PNGraphCell addNewFlowRelation(PNGraphCell sourceCell, PNGraphCell targetCell, Offset offset, List<mxPoint> points, mxPoint referencePoint, String style) {
		AbstractFlowRelation relation = null;
		if (sourceCell.getType() == PNComponent.PLACE && targetCell.getType() == PNComponent.TRANSITION) {
			relation = getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(), targetCell.getId());
		} else if (sourceCell.getType() == PNComponent.TRANSITION && targetCell.getType() == PNComponent.PLACE) {
			relation = getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(), targetCell.getId());
		}
		if (relation != null) {
			PNGraphCell newRelationCell = getNodeCell(relation.getName());
			ArcGraphics arcGraphics = Utils.createArcGraphicsFromStyle(style);
			if (points != null && !points.isEmpty() && referencePoint != null) {
				Vector<Position> vector = new Vector<Position>();
				for (mxPoint p : points) {
					vector.add(new Position(p.getX() + referencePoint.getX(), p.getY() + referencePoint.getY()));
				}
				arcGraphics.setPositions(vector);
			}
			AnnotationGraphics annotationGraphics = Utils.createAnnotationGraphicsFromStyle(style);
			annotationGraphics.setOffset(offset);

			getNetContainer().getPetriNetGraphics().getArcGraphics().put(relation.getName(), arcGraphics);
			getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().put(relation.getName(), annotationGraphics);
			if (newRelationCell == null) {
				newRelationCell = addArcCell(relation.getName(), style);
			}
			graphListenerSupport.notifyRelationAdded(relation);
			return newRelationCell;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public PNGraphCell addArcCell(String arcID, String style) {
		AbstractFlowRelation relation = getNetContainer().getPetriNet().getFlowRelation(arcID);
		ArcGraphics arcGraphics = getNetContainer().getPetriNetGraphics().getArcGraphics(arcID);
		AnnotationGraphics annotationGraphics = getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics(arcID);
		PNGraphCell newCell = createArcCell(arcID, getArcConstraint(relation), style);
		addEdge(newCell, getDefaultParent(), getNodeCell(relation.getSource().getName()), getNodeCell(relation.getTarget().getName()), null);

		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		Vector<Position> positions = arcGraphics.getPositions();
		List<mxPoint> points = new ArrayList<mxPoint>();
		for (Position position : positions) {
			points.add(new mxPoint(position.getX(), position.getY()));
		}
		newCell.getGeometry().setPoints(points);

		return newCell;
	}

	/**
	 * Adds anew transition with default style.
	 */
	public PNGraphCell addNewTransition(mxPoint point) throws PropertyException, IOException {
		Offset offset = new Offset(EditorProperties.getInstance().getDefaultHorizontalLabelOffset(), EditorProperties.getInstance().getDefaultVerticalLabelOffset());
		Dimension dimension = new Dimension(EditorProperties.getInstance().getDefaultTransitionWidth(), EditorProperties.getInstance().getDefaultTransitionHeight());
		return addNewTransition(point, MXConstants.getDefaultNodeStyle(PNComponent.TRANSITION), offset, dimension);
	}

	/**
	 * Adds a new transition with existing style.
	 */
	public PNGraphCell addNewTransition(mxPoint point, String style, Offset offset, Dimension dimension) {
		String nodeName = getNewTransitionName();
		PNGraphCell newTransitionCell = getNodeCell(nodeName);
		if (newTransitionCell != null) {
			nodeName = newTransitionCell.getId();
		}
		if (getNetContainer().getPetriNet().addTransition(nodeName)) {
			NodeGraphics nodeGraphics = Utils.createNodeGraphicsFromStyle(style);
			nodeGraphics.setPosition(new Position(point.getX(), point.getY()));
			nodeGraphics.setDimension(dimension);
			AnnotationGraphics annotationGraphics = Utils.createAnnotationGraphicsFromStyle(style);
			annotationGraphics.setOffset(offset);

			getNetContainer().getPetriNetGraphics().getTransitionGraphics().put(nodeName, nodeGraphics);
			getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().put(nodeName, annotationGraphics);

			if (newTransitionCell == null) {
				newTransitionCell = addTransitionCell(nodeName, style);
			}
			graphListenerSupport.notifyTransitionAdded(getNetContainer().getPetriNet().getTransition(nodeName));
			return newTransitionCell;
		}
		return null;
	}

	/**
	 * Inserts a new place with existing graphic information into the graphical
	 * Petri net.
	 * 
	 * @param nodeName
	 * @param style
	 */
	@SuppressWarnings("rawtypes")
	public PNGraphCell addTransitionCell(String nodeName, String style) {
		AbstractTransition transition = getNetContainer().getPetriNet().getTransition(nodeName);
		NodeGraphics nodeGraphics = getNetContainer().getPetriNetGraphics().getTransitionGraphics(nodeName);
		AnnotationGraphics annotationGraphics = getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics(nodeName);
		PNGraphCell newCell = createTransitionCell(transition.getName(), transition.getLabel(), nodeGraphics.getPosition().getX(), nodeGraphics.getPosition().getY(), nodeGraphics.getDimension()
				.getX(), nodeGraphics.getDimension().getY(), style);

		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		// if (nodeGraphics == null || annotationGraphics == null) {
		// mxCellState state = getView().getState(newCell, true);
		// }
		addCell(newCell, getDefaultParent());
		return newCell;
	}

	public PNGraphCell createTransitionCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX - (width / 2), posY - (height / 2), width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.TRANSITION);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	public PNGraphCell createArcCell(String name, String label, String style) {
		mxGeometry geometry = new mxGeometry();
		geometry.setRelative(true);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.ARC);
		vertex.setId(name);
		vertex.setVertex(false);
		vertex.setEdge(true);
		vertex.setConnectable(true);
		return vertex;
	}

	public abstract void updatePlaceState(String name, Multiset<String> input);

	@Override
	public boolean isCellLocked(Object cell) {
		if (isExecution)
			return true;
		return super.isCellLocked(cell);
	}

	@Override
	public boolean isCellConnectable(Object cell) {
		if (isExecution)
			return false;
		return super.isCellConnectable(cell);
	}

	public boolean isExecution() {
		return isExecution;
	}

	public void setExecution(boolean isExecution) {
		this.isExecution = isExecution;
	}

	@Override
	/**
	 * Constructs a new view to be used in this graph.
	 */
	protected mxGraphView createGraphView() {
		return new GraphView(this);
	}

	@Override
	/**
	 * Returns the tooltip to be used for the given cell.
	 */
	public String getToolTipForCell(Object object) {
		if (object instanceof PNGraphCell) {
			PNGraphCell cell = (PNGraphCell) object;

			switch (cell.getType()) {
			case ARC:
				return getArcToolTip(cell);
			case PLACE:
				return getPlaceToolTip(cell);
			case TRANSITION:
				return getTransitionToolTip(cell);

			}
		}
		return "";
	}

	protected abstract String getPlaceToolTip(PNGraphCell cell);

	protected abstract String getTransitionToolTip(PNGraphCell cell);

	protected abstract String getArcToolTip(PNGraphCell cell);

	protected abstract String getArcConstraint(AbstractFlowRelation relation);

	public abstract Color getTokenColorForName(String name);

	public abstract void updateTokenColor(String name, Color value);

	public abstract Multiset<String> getConstraintforArc(String name);

	public abstract void updateConstraint(String name, Multiset value);

	public abstract void updateTokenConfigurer(String name);
	
	public abstract void updatePlaceCapacity(String name, String color, int newCapacity);

	public abstract int getCapacityforPlace(String name, String color);


	/**
	 * @param id
	 * @param circularPointGroup
	 */
	public abstract Multiset<String> getPlaceStateForCell(String id, CircularPointGroup circularPointGroup);

	/**
	 * Method for incrementing or decrementing the current #AbstractMarking of
	 * the given #AbstractPNPlace
	 * 
	 * @param cell
	 * @param wheelRotation
	 * @return @
	 */

	public abstract AbstractMarking inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation);

	/**
	 * Selects all vertices and/or edges depending on the given boolean
	 * arguments recursively, starting at the given parent or the default parent
	 * if no parent is specified. Use <code>selectAll</code> to select all
	 * cells.
	 */
	public void selectPNGraphCells(final PNComponent type) {

		Collection<Object> cells = mxGraphModel.filterDescendants(getModel(), new mxGraphModel.Filter() {
			public boolean filter(Object cell) {
				return view.getState(cell) != null && model.getChildCount(cell) == 0 && ((PNGraphCell) cell).getType() == type;
			}

		});
		setSelectionCells(cells);
	}

	// Needs to bee overriden for Token-Painting
	@Override
	/**
	 * Draws the cell state with the given label onto the canvas. No
	 * children or descendants are painted here. This method invokes
	 * cellDrawn after the cell, but not its descendants have been
	 * painted.
	 * 
	 * @param canvas Canvas onto which the cell should be drawn.
	 * @param state State of the cell to be drawn.
	 * @param drawLabel Indicates if the label should be drawn.
	 */
	public void drawState(mxICanvas canvas, mxCellState state, boolean drawLabel) {

		Object cell = (state != null) ? state.getCell() : null;

		if (cell != null && cell != view.getCurrentRoot() && cell != model.getRoot() && (model.isVertex(cell) || model.isEdge(cell))) {

			PNGraphCell customcell;
			Object obj = null;
			if (canvas instanceof mxImageCanvas)
				obj = canvas.drawCell(state);
			else
				try {
					obj = drawCell((mxGraphics2DCanvas) canvas, state);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
				} catch (PropertyException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);

				}

			Object lab = null;

			// Holds the current clipping region in case the label will be
			// clipped
			Shape clip = null;
			Rectangle newClip = state.getRectangle();

			// Indirection for image canvas that contains a graphics canvas
			mxICanvas clippedCanvas = (isLabelClipped(state.getCell())) ? canvas : null;

			if (clippedCanvas instanceof mxImageCanvas) {
				clippedCanvas = ((mxImageCanvas) clippedCanvas).getGraphicsCanvas();
				// TODO: Shift newClip to match the image offset
				// Point pt = ((mxImageCanvas) canvas).getTranslate();
				// newClip.translate(-pt.x, -pt.y);
			}

			if (clippedCanvas instanceof mxGraphics2DCanvas) {
				Graphics g = ((mxGraphics2DCanvas) clippedCanvas).getGraphics();
				clip = g.getClip();

				// Ensure that our new clip resides within our old clip
				if (clip instanceof Rectangle) {
					g.setClip(newClip.intersection((Rectangle) clip));
				}
				// Otherwise, default to original implementation
				else {
					g.setClip(newClip);
				}
			}

			if (drawLabel) {
				String label = state.getLabel();

				if (label != null && state.getLabelBounds() != null) {
					Graphics2D g = null;
					if (canvas instanceof mxGraphics2DCanvas) {
						Map<String, Object> style = state.getStyle();
						g = ((mxGraphics2DCanvas) canvas).getGraphics();
						Color color = mxUtils.getColor(state.getStyle(), mxConstants.STYLE_STROKECOLOR);
						g.setColor(color);
						g.setStroke(Utils.createLabelStroke(style, canvas.getScale()));
					}
					lab = canvas.drawLabel(label, state, isHtmlLabel(cell));
					if (g != null)
						g.setStroke(new BasicStroke((float) 2));

				}
			}

			// Restores the previous clipping region
			if (clippedCanvas instanceof mxGraphics2DCanvas) {
				((mxGraphics2DCanvas) clippedCanvas).getGraphics().setClip(clip);
			}

			// Invokes the cellDrawn callback with the object which was created
			// by the canvas to represent the cell graphically
			if (obj != null) {
				cellDrawn(canvas, state, obj, lab);
			}
		}
	}

	public Object drawCell(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException {
		Map<String, Object> style = state.getStyle();
		mxIShape shape = canvas.getShape(style);
		Graphics2D g;
		if (canvas.getGraphics() != null && shape != null) {
			// Creates a temporary graphics instance for drawing this shape
			float opacity = mxUtils.getFloat(style, mxConstants.STYLE_OPACITY, 100);
			Graphics2D previousGraphics = canvas.getGraphics();
			g = ((mxGraphics2DCanvas) canvas).createTemporaryGraphics(style, opacity, state);

			// Paints the shape and restores the graphics object
			shape.paintShape(canvas, state);
			if (state.getCell() instanceof PNGraphCell) {
				PNGraphCell customcell = (PNGraphCell) state.getCell();
				if (customcell.getType() == PNComponent.PLACE) {

					drawAdditionalPlaceGrahpics(canvas, state);

				}
				if (customcell.getType() == PNComponent.TRANSITION) {
					try {
						drawAdditionalTransitionGrahpics(canvas, state);
						drawAdditionalContextToTransition(canvas, state);

					} catch (ParameterException e) {
						JOptionPane.showMessageDialog(null, "Graphic for Access Mode is not avaiable \nReason: " + e.getMessage(), "Parameter Exception", JOptionPane.ERROR);
					} catch (PropertyException e) {
						JOptionPane.showMessageDialog(null, "Graphic for Access Mode is not avaiable \nReason: " + e.getMessage(), "Property Exception", JOptionPane.ERROR);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Graphic for Access Mode is not avaiable \nReason: " + e.getMessage(), "IO Exception", JOptionPane.ERROR);
					}

				}
				if (customcell.getType() == PNComponent.ARC) {
					if (!hideContraintsAsTokens)
						drawAdditionalArcGrahpics(canvas, state);

				}

			}

			g.dispose();
			g = previousGraphics;
		}

		return shape;
	}

	protected abstract void drawAdditionalArcGrahpics(mxGraphics2DCanvas canvas, mxCellState state);

	protected abstract void drawAdditionalTransitionGrahpics(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException;

	protected abstract void drawAdditionalContextToTransition(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException;
	
	protected void drawAdditionalPlaceGrahpics(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException {
		Rectangle temp = state.getRectangle();
		PNGraphCell cell = (PNGraphCell) state.getCell();

		int minDistance = (int) (EditorProperties.getInstance().getDefaultTokenDistance() * getView().getScale());
		int pointDiameter = (int) (EditorProperties.getInstance().getDefaultTokenSize() * getView().getScale());
		CircularPointGroup circularPointGroup = new CircularPointGroup(minDistance, pointDiameter);

		// TODO Making method more general to be able to handle colored marking
		// in cpn
		Multiset<String> placeState = getPlaceStateForCell(cell.getId(), circularPointGroup);
		if (placeState != null) {
			AbstractCPNGraphics cpnGraphics;
			Map<String, Color> colors = null;
			if (getNetContainer().getPetriNetGraphics() instanceof AbstractCPNGraphics) {
				{
					cpnGraphics = (AbstractCPNGraphics) getNetContainer().getPetriNetGraphics();
					colors = cpnGraphics.getColors();
				}
				Set<String> keyset = placeState.support();
				//
				for (String s : keyset) {

					Color color = colors.get(s);
					int number = placeState.multiplicity(s);
					PColor pco;
					if (color != null)
						pco = new PColor(color.getRed(), color.getGreen(), color.getBlue());
					else {
						pco = PColor.black;
					}

					circularPointGroup.addPoints(pco, number);

				}
			}
			int k = placeState.size();
			Point center = new Point(temp.x + temp.width / 2, temp.y + temp.height / 2);
			int requiredWidth = 0;
			if (k == 1)
				requiredWidth = circularPointGroup.getPointDiameter();
			if (k == 2 || k == 3)
				requiredWidth = (circularPointGroup.getPointDiameter() + minDistance) * 2;
			if (k == 4)
				requiredWidth = (circularPointGroup.getPointDiameter() + minDistance * 2) * 2;
			if (k == 2)
				requiredWidth = (circularPointGroup.getPointDiameter() + minDistance) * 2;
			if (k >= 5)
				requiredWidth = circularPointGroup.getRequiredDiameter();
			if (state.getWidth() >= requiredWidth)
				drawPoints(canvas, temp, circularPointGroup, center);
			else
				drawNumbers(cell, k + "", canvas, temp, center);
		}

	}

	private void drawNumbers(PNGraphCell cell, String numbers, mxGraphics2DCanvas canvas, Rectangle temp, Point center) {
		Graphics g = canvas.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		String family = (getCellStyle(cell).get(mxConstants.STYLE_FONTFAMILY) != null) ? getCellStyle(cell).get(mxConstants.STYLE_FONTFAMILY).toString() : mxConstants.DEFAULT_FONTFAMILY;
		g2.setFont(new Font(family, Font.PLAIN, (int) (10 * getView().getScale())));
		g2.setPaint(Color.black);
		drawString(g2, numbers + "\n", center.x - (int) (temp.width * 0.1), center.y - (int) (g.getFontMetrics().getHeight() * 0.8));
	}

	private void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

	protected void drawPoints(mxGraphics2DCanvas canvas, Rectangle temp, CircularPointGroup circularPointGroup, Point center) {
		Graphics g = canvas.getGraphics();
		Iterator<PColor> iter = circularPointGroup.getColors().iterator();
		PColor actColor;
		Set<TokenGraphics> tgSet = new HashSet<TokenGraphics>();

		while (iter.hasNext()) {
			actColor = iter.next();
			g.setColor(new Color(actColor.getRGB()));
			for (de.invation.code.toval.graphic.misc.Position p : circularPointGroup.getCoordinatesFor(actColor)) {
				GraphicUtils.fillCircle(g, (int) (center.getX() + p.getX()), (int) (center.getY() + p.getY()), circularPointGroup.getPointDiameter());
			}
		}
	}

	/**
	 * Sets the positions of place and transition labels according to the<br>
	 * information contained in the corresponding annotation graphics.<br>
	 * This method is called when a graph is created with a non-empty Petri net.
	 */
	// public void updatePositionPropertiesFromCells() {
	// for (PNGraphCell cell : nodeReferences.values()) {
	// mxCellState state = getView().getState(cell);
	// setPositionProperties((PNGraphCell) state.getCell());
	// }
	//
	// }

	private void setPositionProperties(PNGraphCell cell) {
		switch (cell.getType()) {
		case ARC:
			break;
		case PLACE:
			if (cell.getGeometry().getCenterX() >= 0)
				properties.setPlacePositionX(this, cell.getId(), (int) cell.getGeometry().getCenterX());
			if (cell.getGeometry().getCenterY() >= 0)
				properties.setPlacePositionY(this, cell.getId(), (int) cell.getGeometry().getCenterY());
			break;
		case TRANSITION:
			if (cell.getGeometry().getCenterX() >= 0)
				properties.setTransitionPositionX(this, cell.getId(), (int) cell.getGeometry().getCenterX());
			if (cell.getGeometry().getCenterY() >= 0)
				properties.setTransitionPositionY(this, cell.getId(), (int) cell.getGeometry().getCenterY());
			break;
		}
	}

	// ------- Property change
	// handling--------------------------------------------------------------------------------
	// These methods are called when some Petri net properties changed by other
	// classes. ----------------

	/**
	 * This method notifies a PNPropertiesListener that a PN component was
	 * added.<br>
	 * This can be a place, transition or arc.<br>
	 * In the
	 */
	@Override
	public void componentAdded(PNComponent component, String name) {
	}

	@Override
	public void componentRemoved(PNComponent component, String name) {
	}

	@Override
	public void propertyChange(PNPropertyChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue()))
			if (event.getSource() != this) {
				switch (event.getFieldType()) {
				case PLACE:
					handlePlacePropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
					break;
				case TRANSITION:
					handleTransitionPropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
					break;
				case ARC:
					handleArcPropertyChange(event.getName(), event.getProperty(), event.getOldValue(), event.getNewValue());
					break;
				}
				refresh();
			}
	}

	private boolean handlePlacePropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell placeCell = getNodeCell(name);

		mxRectangle bounds;
		switch (property) {
		case PLACE_LABEL:
			getModel().setValue(placeCell, newValue);
			return true;
		case PLACE_SIZE:
			bounds = getView().getState(placeCell).getBoundingBox();
			bounds.setWidth(new Integer((Integer) newValue).doubleValue());
			bounds.setHeight(new Integer((Integer) newValue).doubleValue());
			resizeCell(placeCell, bounds);
			setSelectionCell(placeCell);
			return true;
		case PLACE_POSITION_X:
			moveCells(new Object[] { placeCell }, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue(), 0);
			setSelectionCell(placeCell);
			return true;
		case PLACE_POSITION_Y:
			moveCells(new Object[] { placeCell }, 0, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue());
			setSelectionCell(placeCell);
			return true;
		}
		return false;
	}

	protected boolean handleTransitionPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell transitionCell = getNodeCell(name);
		mxRectangle bounds;
		switch (property) {
		case TRANSITION_LABEL:
			getModel().setValue(transitionCell, newValue);
			return true;
		case TRANSITION_POSITION_X:
			moveCells(new Object[] { transitionCell }, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue(), 0);
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_POSITION_Y:
			moveCells(new Object[] { transitionCell }, 0, new Integer((Integer) newValue).doubleValue() - new Integer((Integer) oldValue).doubleValue());
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_SIZE_X:
			bounds = getView().getState(transitionCell).getBoundingBox();
			bounds.setWidth(new Integer((Integer) newValue).doubleValue());
			bounds.setHeight(transitionCell.getGeometry().getHeight());
			resizeCell(transitionCell, bounds);
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_SIZE_Y:
			bounds = getView().getState(transitionCell).getBoundingBox();
			bounds.setWidth(transitionCell.getGeometry().getWidth());
			bounds.setHeight(new Integer((Integer) newValue).doubleValue());
			resizeCell(transitionCell, bounds);
			setSelectionCell(transitionCell);
			return true;
		}
		return false;
	}

	protected boolean handleArcPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell arcCell = getNodeCell(name);
		switch (property) {
		case ARC_WEIGHT:
			getModel().setValue(arcCell, newValue);
			break;

		}

		return false;
	}

	public void selectPlace(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = getNodeCell(name);
			setSelectionCell(cell);
		}
	}

	public void selectTransition(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = getNodeCell(name);
			setSelectionCell(cell);
		}
	}

	public void selectArc(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = getNodeCell(name);
			setSelectionCell(cell);
		}
	}

	private boolean isCellSelected(String id) {
		PNGraphCell currentSelectionCell = null;
		if (getSelectionCell() instanceof PNGraphCell) {
			currentSelectionCell = (PNGraphCell) getSelectionCell();
		}
		if (currentSelectionCell != null) {
			if (currentSelectionCell.getId() == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void cellsMoved(Object[] cells, double dx, double dy, boolean disconnect, boolean constrain) {
		super.cellsMoved(cells, dx, dy, disconnect, constrain);
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;
				setPositionProperties(cell);

			}
		}
	}

	/**
	 * This method notifies the graph, that some cells have been added.<br>
	 * Note: Only by copy/pase actions on graph canvas!<br>
	 * In case these cells stand for new places or transitions, they have to be
	 * added to the Petri net.
	 */

	@Override
	public void cellsResized(Object[] cells, mxRectangle[] bounds) {
		// TODO Auto-generated method stub
		super.cellsResized(cells, bounds);
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;

				switch (cell.getType()) {
				case ARC:
					break;
				case PLACE:
					properties.setPlaceSize(this, cell.getId(), (int) cell.getGeometry().getWidth());
					break;
				case TRANSITION:
					properties.setTransitionSizeX(this, cell.getId(), (int) cell.getGeometry().getWidth());
					properties.setTransitionSizeY(this, cell.getId(), (int) cell.getGeometry().getHeight());
					break;

				}

			}
		}
	}

	@Override
	public void cellsRemoved(Object[] cells) {
		super.cellsRemoved(cells);

		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;

				switch (cell.getType()) {
				case ARC:
					removeFlowRelation(cell.getId());
					break;
				case PLACE:
					removePlace(cell.getId());
					break;
				case TRANSITION:
					removeTransition(cell.getId());
					break;

				}

			}

		}
	}

	@Override
	/**
	 * Returns true if split is enabled and the given edge may be splitted into
	 * two edges with the given cell as a new terminal between the two.
	 * 
	 * @param target Object that represents the edge to be splitted.
	 * @param cells Array of cells to add into the given edge.
	 * @return Returns true if the given edge may be splitted by the given
	 * cell.
	 */
	public boolean isSplitTarget(Object target, Object[] cells) {
		// since this works only for one cell, this would always hurt the
		// Petri-Net order
		// if (target != null && cells != null && cells.length == 1)
		// {
		// Object src = model.getTerminal(target, true);
		// Object trg = model.getTerminal(target, false);
		//
		// return (model.isEdge(target)
		// && isCellConnectable(cells[0])
		// && getEdgeValidationError(target,
		// model.getTerminal(target, true), cells[0]) == null
		// && !model.isAncestor(cells[0], src) && !model.isAncestor(
		// cells[0], trg));
		// }

		return false;
	}

	@Override
	/**
	 * Returns true if the given target cell is a valid target for source.
	 * This is a boolean implementation for not allowing connections between
	 * certain pairs of vertices and is called by <getEdgeValidationError>.
	 * This implementation returns true if <isValidSource> returns true for
	 * the source and <isValidTarget> returns true for the target.
	 * 
	 * @param source Object that represents the source cell.
	 * @param target Object that represents the target cell.
	 * @return Returns true if the the connection between the given terminals
	 * is valid.
	 */
	public boolean isValidConnection(Object source, Object target) {
		boolean result = isValidSource(source) && isValidTarget(target) && (isAllowLoops() || source != target);
		PNComponent sourceType, targetType;
		if (result && source instanceof PNGraphCell && target instanceof PNGraphCell) {
			sourceType = ((PNGraphCell) source).getType();
			targetType = ((PNGraphCell) target).getType();
			if (sourceType == targetType)
				return false;
		}
		return result;

	}

	protected boolean removeFlowRelation(String name) {
		return netContainer.getPetriNet().removeFlowRelation(name);
	}

	protected boolean removeTransition(String name) {
		return netContainer.getPetriNet().removeTransition(name);
	}

	protected boolean removePlace(String name) {
		return netContainer.getPetriNet().removePlace(name);

	}

	@Override
	public void invoke(Object sender, mxEventObject evt) {

		if (evt.getName().equals(mxEvent.CHANGE)) {
			changeHandler.handleChange(evt);
			if (sender instanceof mxGraphSelectionModel || sender instanceof PNGraphComponent) {
				graphListenerSupport.notifyComponentsSelected(getSelectedGraphCells());
			}
		} else if (evt.getName().equals(mxEvent.RESIZE_CELLS)) {
			ensureValidPlaceSize();
		}
	}

	protected abstract void setArcLabel(String id, String string);

	public void setFontOfSelectedCellLabel(String font) {
		Validate.notNull(font);

		if (font != null && !font.equals("-")) {
			setCellStyles(mxConstants.STYLE_FONTFAMILY, font);
		}
	}

	public void setFontSizeOfSelectedCellLabel(String font) {
		setCellStyles(mxConstants.STYLE_FONTSIZE, font);
	}

	public void setStrokeWeightOfSelectedCell(String strokeWeight) {
		for (Object cell : getSelectionCells()) {
			if (cell instanceof PNGraphCell) {
				String styleKey = (isLabelSelected()) ? MXConstants.LABEL_LINE_WIDTH : mxConstants.STYLE_STROKEWIDTH;
				if(getView().getState(cell)!= null)
				if(getView().getState(cell).getStyle().containsKey(styleKey)){
				String currentStrokeWidth = mxUtils.getString(getView().getState(cell).getStyle(), styleKey).replace(".0", "");
				if (!currentStrokeWidth.equals(strokeWeight)) {
					setCellStyles(styleKey, strokeWeight, new Object[] { cell });
				}
				}
			}
		}
	}

	@Override
	/**
	 * Sets the key to value in the styles of the given cells. This will modify
	 * the existing cell styles in-place and override any existing assignment
	 * for the given key. If no cells are specified, then the selection cells
	 * are changed. If no value is specified, then the respective key is
	 * removed from the styles.
	 * 
	 * @param key String representing the key to be assigned.
	 * @param value String representing the new value for the key.
	 * @param cells Array of cells to change the style for.
	 */
	public Object[] setCellStyles(String key, String value, Object[] cells) {
		if (cells == null) {
			cells = getSelectionCells();
		}

		setCellStyles(this, cells, key, value);

		return cells;
	}

	/**
	 * Assigns the value for the given key in the styles of the given cells, or
	 * removes the key from the styles if the value is null.
	 * 
	 * @param pnGraph
	 *            Model to execute the transaction in.
	 * @param cells
	 *            Array of cells to be updated.
	 * @param key
	 *            Key of the style to be changed.
	 * @param value
	 *            New value for the given key.
	 */
	public static void setCellStyles(PNGraph pnGraph, Object[] cells, String key, String value) {
		if (cells != null && cells.length > 0) {
			pnGraph.getModel().beginUpdate();
			try {
				for (int i = 0; i < cells.length; i++) {
					if (cells[i] != null) {
						String style = mxStyleUtils.setStyle(pnGraph.getModel().getStyle(cells[i]), key, value);
						setStyle(cells[i], style, key, pnGraph);
					}
				}
			} finally {
				pnGraph.getModel().endUpdate();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mxgraph.model.mxIGraphModel#setStyle(Object, String)
	 */
	public static String setStyle(Object cell, String style, String key, PNGraph pnGraph) {
		if (style == null || !style.equals(pnGraph.getModel().getStyle(cell)))

		{
			((mxGraphModel) pnGraph.getModel()).execute(new StyleChange(pnGraph, cell, style, key));
		}

		return style;
	}

	@Override
	/**
	 * Sets the style of the specified cells. If no cells are given, then the
	 * selection cells are changed.
	 * 
	 * @param style String representing the new style of the cells.
	 * @param cells Optional array of <mxCells> to set the style for. Default is the
	 * selection cells.
	 */
	public Object[] setCellStyle(String style, Object[] cells) {
		if (cells == null) {
			cells = getSelectionCells();
		}

		if (cells != null) {
			model.beginUpdate();
			try {
				for (int i = 0; i < cells.length; i++) {
					setStyle(cells[i], style, null, this);
				}
			} finally {
				model.endUpdate();
			}
		}

		return cells;
	}

	public String getNewTransitionName() {
		String prefix = MXConstants.TRANSITION_NAME_PREFIX;
		Integer index = 0;
		while (getNetContainer().getPetriNet().containsTransition(prefix + index)) {
			index++;
		}
		return prefix + index;
	}

	public String getNewPlaceName() {
		String prefix = MXConstants.PLACE_NAME_PREFIX;
		Integer index = 0;
		while (getNetContainer().getPetriNet().containsPlace(prefix + index)) {
			index++;
		}
		return prefix + index;
	}

	public boolean isValidNodeName(String name, PNComponent type) {
		String prefix = null;
		switch (type) {
		case PLACE:
			prefix = MXConstants.PLACE_NAME_PREFIX;
			break;
		case TRANSITION:
			prefix = MXConstants.TRANSITION_NAME_PREFIX;
			break;
		}
		if (!name.startsWith(prefix))
			return false;
		String possibleInteger = name.substring(prefix.length());
		Validate.isInteger(possibleInteger);
		return true;
	}

	public void addWayPoint(PNGraphCell cell, Point pt) {
		if (cell.getType().equals(PNComponent.ARC)) {
			List<mxPoint> points = cell.getGeometry().getPoints();
			if (points != null) {

				// This code enables adding waypoints in between two existing
				// waypoints, and not just adding it at the end of the given
				// line
				if (points.size() == 0) {
					points.add(new mxPoint(pt.getX(), pt.getY()));
				} else {
					double sourceX = cell.getSource().getGeometry().getCenterX();
					double sourceY = cell.getSource().getGeometry().getCenterY();
					double targetX = cell.getTarget().getGeometry().getCenterX();
					double targetY = cell.getTarget().getGeometry().getCenterY();
					points.add(new mxPoint(targetX, targetY));
					points.add(0, new mxPoint(sourceX, sourceY));

					for (int i = 0; i < points.size() - 1; i++) {
						mxPoint p = points.get(i);
						double x1 = p.getX();
						double y1 = p.getY();
						mxPoint p2 = points.get(i + 1);
						double x2 = p2.getX();
						double y2 = p2.getY();
						mxPoint newPoint = new mxPoint(pt.getX(), pt.getY());
						double xP = newPoint.getX();
						double yP = newPoint.getY();
						double comp = Line2D.ptSegDist(x1, y1, x2, y2, xP, yP);
						if (comp <= 5.0 * getView().getScale()) {
							points.add(i + 1, newPoint);
							i = points.size();
						}

					}
					points.remove(points.size() - 1);
					points.remove(0);

				}
			} else {
				points = new ArrayList<mxPoint>();
				points.add(new mxPoint(pt.getX(), pt.getY()));

			}

			cell.getGeometry().setPoints(points);
			updatePointsInArcGraphics(cell, points);
		}
	}

	public void removePoint(PNGraphCell cell, int index) {
		if (cell.getType().equals(PNComponent.ARC)) {
			List<mxPoint> points = cell.getGeometry().getPoints();
			if (points != null && points.size() > 0) {
				cell.getGeometry().getPoints().remove(index - 1);
			}
		}
		updatePointsInArcGraphics(cell, cell.getGeometry().getPoints());
	}

	protected void updatePointsInArcGraphics(PNGraphCell cell, List<mxPoint> points) {
		ArcGraphics arcGraphics = getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
		if (arcGraphics != null) {
			Vector<Position> vector = new Vector<Position>();
			if (points != null) {
				if (points.size() >= 0) {
					for (mxPoint p : points) {
						vector.add(new Position(p.getX(), p.getY()));
					}
					arcGraphics.setPositions(vector);
				}
			}
		}
	}

	public void enterEditingMode() {
		setExecution(false);
		setCellsSelectable(true);
		getNetContainer().getPetriNet().reset();
		refresh();

	}

	public void fireTransition(PNGraphCell cell) throws PNException {
		getNetContainer().getPetriNet().fire(cell.getId());
		graphListenerSupport.notifyTransitionFired(cell);
		refresh();
	}

	public void updateTransitionSilent(String id, boolean setSilent) {
		getNetContainer().getPetriNet().getTransition(id).setSilent(setSilent);

	}

	public boolean getTransitionSilentState(String name) {
		return getNetContainer().getPetriNet().getTransition(name).isSilent();
	}

	public void removeAllArcPoints() {
		for (AbstractFlowRelation fr : getNetContainer().getPetriNet().getFlowRelations()) {
			PNGraphCell arcCell = getNodeCell(fr.getName());
			arcCell.getGeometry().setPoints(new ArrayList<mxPoint>());
			mxPoint point = getView().getState(arcCell).getAbsolutePoint(0);
			ArcGraphics arcGraphics = getNetContainer().getPetriNetGraphics().getArcGraphics().get(arcCell.getId());
			arcGraphics.setPositions(new Vector<Position>());
		}

	}

	public void setTokenOnArcVisibility(boolean b) {
		this.hideContraintsAsTokens = b;
		refresh();

	}

	public boolean containedGraphics() {
		return containedGraphics;
	}

	protected double getDefaultTokenSize() {
		// TODO Auto-generated method stub
		try {
			return EditorProperties.getInstance().getDefaultTokenSize();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Double) null;
	}

	@Override
	public double snap(double value) {
		try {
			if (EditorProperties.getInstance().getSnapToGrid()) {
				return super.snap(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Listeners are not notified when the underlying Petri reports structure
	 * changes (new elements).<br>
	 * This is done in the methods
	 * {@link #addNewPlace(mxPoint, String, Offset, Dimension)},
	 * {@link #addNewTransition(mxPoint, String, Offset, Dimension)} ans
	 * {@link #addNewFlowRelation(PNGraphCell, PNGraphCell, Offset, List, mxPoint, String)}
	 * .
	 */
	@Override
	public void placeAdded(PlaceChangeEvent event) {
	}

	@Override
	public void placeRemoved(PlaceChangeEvent event) {
		graphListenerSupport.notifyPlaceRemoved(event.place);
	}

	/**
	 * Listeners are not notified when the underlying Petri reports structure
	 * changes (new elements).<br>
	 * This is done in the methods
	 * {@link #addNewPlace(mxPoint, String, Offset, Dimension)},
	 * {@link #addNewTransition(mxPoint, String, Offset, Dimension)} ans
	 * {@link #addNewFlowRelation(PNGraphCell, PNGraphCell, Offset, List, mxPoint, String)}
	 * .
	 */
	@Override
	public void transitionAdded(TransitionChangeEvent event) {
	}

	@Override
	public void transitionRemoved(TransitionChangeEvent event) {
		graphListenerSupport.notifyTransitionRemoved(event.transition);
	}

	/**
	 * Listeners are not notified when the underlying Petri reports structure
	 * changes (new elements).<br>
	 * This is done in the methods
	 * {@link #addNewPlace(mxPoint, String, Offset, Dimension)},
	 * {@link #addNewTransition(mxPoint, String, Offset, Dimension)} ans
	 * {@link #addNewFlowRelation(PNGraphCell, PNGraphCell, Offset, List, mxPoint, String)}
	 * .
	 */
	@Override
	public void relationAdded(RelationChangeEvent event) {
	}

	@Override
	public void relationRemoved(RelationChangeEvent event) {
		graphListenerSupport.notifyRelationRemoved(event.relation);
	}

	@Override
	public void structureChanged() {
//		updateViews();
	}

	public PNGraphCell getNodeCell(String name) {
		mxICell superParent = (mxICell) model.getRoot();
		for (int i = 0; i < superParent.getChildCount(); i++) {
			mxICell parent = superParent.getChildAt(i);
			for (int j = 0; j < parent.getChildCount(); j++) {
				PNGraphCell child = (PNGraphCell) parent.getChildAt(j);
				if (child.getId().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

}
