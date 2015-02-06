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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxGraphModel.mxGeometryChange;
import com.mxgraph.model.mxGraphModel.mxTerminalChange;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.util.mxUndoableEdit;
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
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.StyleChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNPropertiesListener;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperty;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNPropertyChangeEvent;

public abstract class PNGraph extends mxGraph implements PNPropertiesListener, mxIEventListener {

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PNProperties properties = null;
	@SuppressWarnings("rawtypes")
	public Map<String, PNGraphCell> nodeReferences = new HashMap<String, PNGraphCell>();
	public Map<String, PNGraphCell> arcReferences = new HashMap<String, PNGraphCell>();

	private Set<PNGraphListener> listeners = new HashSet<PNGraphListener>();

	private boolean labelSelected = false;
	private boolean isExecution = false;
	protected boolean hideContraintsAsTokens = true;
	private Set<String> nameSetFromTransitions;
	private boolean containedGraphics = false;
	private Map<String, mxCellMarker> markerReference = new HashMap<String, mxCellMarker>();

	
	public PNGraph(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> netContainer, PNProperties properties) {
		super();
		setGridSize(5);

		Validate.notNull(netContainer);
		Validate.notNull(properties);
		this.netContainer = netContainer;
		this.properties = properties;
		this.properties.addPNPropertiesListener(this);
		this.getSelectionModel().addListener(mxEvent.CHANGE, this);

		this.addListener(mxEvent.RESIZE_CELLS, this);

		this.getModel().addListener(mxEvent.CHANGE, this);
		this.getModel().addListener(mxEvent.UNDO, this);
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

	// @Override
	// public void setModel(mxIGraphModel value) {
	// // TODO Auto-generated method stub
	// super.setModel(value);
	// }

	@SuppressWarnings("rawtypes")
	private void initialize() throws PropertyException, IOException {
		// Check if net container is empty.
		// If not, add all PN components to the graph.

		// Check if net contains Graphical Info and keep that information
		if (netContainer.getPetriNetGraphics().getPlaceGraphics().size() > 0 || netContainer.getPetriNet().isEmpty()) {
			containedGraphics = true;
		}
		if (!netContainer.getPetriNet().isEmpty()) {
			getModel().beginUpdate();

			for (AbstractPlace place : getNetContainer().getPetriNet().getPlaces()) {
				if (netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()) == null)
					netContainer.getPetriNetGraphics().getPlaceGraphics().put(place.getName(), new NodeGraphics());
				if (netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(place.getName()) == null)
					netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().put(place.getName(), new AnnotationGraphics());

				insertPNPlace(place, netContainer.getPetriNetGraphics().getPlaceGraphics().get(place.getName()),
						netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(place.getName()));
			}
			for (AbstractTransition transition : getNetContainer().getPetriNet().getTransitions()) {
				if (netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()) == null)
					netContainer.getPetriNetGraphics().getTransitionGraphics().put(transition.getName(), new NodeGraphics());
				if (netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(transition.getName()) == null)
					netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().put(transition.getName(), new AnnotationGraphics());
				insertPNTransition(transition, netContainer.getPetriNetGraphics().getTransitionGraphics().get(transition.getName()), netContainer.getPetriNetGraphics()
						.getTransitionLabelAnnotationGraphics().get(transition.getName()));
			}
			for (AbstractFlowRelation relation : getNetContainer().getPetriNet().getFlowRelations()) {
				if (netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()) == null)
					netContainer.getPetriNetGraphics().getArcGraphics().put(relation.getName(), new ArcGraphics());

				if (netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(relation.getName()) == null)
					netContainer.getPetriNetGraphics().getArcAnnotationGraphics().put(relation.getName(), new AnnotationGraphics());
				insertPNRelation(relation, netContainer.getPetriNetGraphics().getArcGraphics().get(relation.getName()),
						netContainer.getPetriNetGraphics().getArcAnnotationGraphics().get(relation.getName()));
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
		listeners.add(listener);
	}

	public void removePNGraphListener(PNGraphListener listener) {
		listeners.remove(listener);
	}

	private void notifyPlaceAdded(AbstractPlace place) {
		for (PNGraphListener listener : listeners) {
			listener.placeAdded(place);
		}
	}

	private void notifyTransitionAdded(AbstractTransition transition) {
		for (PNGraphListener listener : listeners) {
			listener.transitionAdded(transition);
		}
	}

	private void notifyRelationAdded(AbstractFlowRelation relation) {
		for (PNGraphListener listener : listeners) {
			listener.relationAdded(relation);
		}
	}

	private void notifyPlaceRemoved(AbstractPlace place) {
		for (PNGraphListener listener : listeners) {
			listener.placeRemoved(place);
		}
	}

	private void notifyTransitionRemoved(AbstractTransition transition) {
		for (PNGraphListener listener : listeners) {
			listener.transitionRemoved(transition);
		}
	}

	private void notifyRelationRemoved(AbstractFlowRelation relation) {
		for (PNGraphListener listener : listeners) {
			listener.relationRemoved(relation);
		}
	}

	private void notifyComponentsSelected() {
		Set<PNGraphCell> selectedCells = getSelectedGraphCells();
		for (PNGraphListener listener : listeners) {
			listener.componentsSelected(selectedCells);
		}
	}
	
	protected void notifyTransitionFired(PNGraphCell cell) {
		Set<PNGraphCell> selectedCells = new HashSet<PNGraphCell>();
		selectedCells.add(cell);
		for (PNGraphListener listener : listeners) {
			listener.componentsSelected(selectedCells);
		}
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
		Set<PNGraphCell> placeCells = new HashSet<PNGraphCell>();
		for (Object selectedObject : getSelectionCells()) {
			if (selectedObject instanceof PNGraphCell) {
				placeCells.add((PNGraphCell) selectedObject);
			}
		}
		return placeCells;
	}

	@SuppressWarnings("rawtypes")
	public void addNodeReference(AbstractPNNode pnNode, PNGraphCell cell) {
		nodeReferences.put(pnNode.getName(), cell);
	}

	@SuppressWarnings("rawtypes")
	void addArcReference(String string, PNGraphCell cell) {
		arcReferences.put(string, cell);
	}

	@SuppressWarnings("rawtypes")
	PNGraphCell getCell(AbstractPNNode pnNode) {
		for (Entry<String, PNGraphCell> nodeset : nodeReferences.entrySet()) {
			if (nodeset.getKey().equals(pnNode.getName())) {
				return nodeset.getValue();
			}
		}
		return nodeReferences.get(pnNode);
	}

	public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	protected PNProperties getPNProperties() {
		return properties;
	}

	public Object addNewPlace(mxPoint mxPoint) {
		mxPoint point = mxPoint;
		String prefix = MXConstants.PLACE_NAME_PREFIX;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (getNetContainer().getPetriNet().containsPlace(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;
		if (getNetContainer().getPetriNet().addPlace(nodeName)) {
			AbstractPlace place = getNetContainer().getPetriNet().getPlace(nodeName);
			NodeGraphics nodeGraphics = new NodeGraphics();
			AnnotationGraphics annotationGraphics = new AnnotationGraphics();
			addGraphicalInfoToPNPlace(point, place, nodeGraphics, annotationGraphics);
			newCell = insertPNPlace(place, nodeGraphics, annotationGraphics);
		}
		return newCell;
	}

	/**
	 * @param point
	 * @param place
	 * @param nodeGraphics
	 * @param annotationGraphics
	 * @throws IOException
	 * @throws PropertyException
	 * @throws ParameterException
	 */
	public void addGraphicalInfoToPNPlace(mxPoint point, AbstractPlace place, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		try {
			nodeGraphics.setPosition(new Position(point.getX(), point.getY()));
			nodeGraphics.setDimension(new Dimension(WolfgangProperties.getInstance().getDefaultPlaceSize(), WolfgangProperties.getInstance().getDefaultPlaceSize()));
			annotationGraphics.setOffset(new Offset(WolfgangProperties.getInstance().getDefaultHorizontalLabelOffset(), WolfgangProperties.getInstance().getDefaultVerticalLabelOffset()));
			getNetContainer().getPetriNetGraphics().getPlaceGraphics().put(place.getName(), nodeGraphics);
			getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().put(place.getName(), annotationGraphics);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Inserts a new place with existing graphic information into the graphical Petri net.
	 * @param place
	 * @param nodeGraphics
	 * @param annotationGraphics
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNPlace(AbstractPlace place, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		PNGraphCell newCell = null;
		try {
			double x = (nodeGraphics == null) ? 0 : nodeGraphics.getPosition().getX();
			double y = (nodeGraphics == null) ? 0 : nodeGraphics.getPosition().getY();
			double dimX = (nodeGraphics == null) ? WolfgangProperties.getInstance().getDefaultPlaceSize() : nodeGraphics.getDimension().getX();
			double dimY = (nodeGraphics == null) ? WolfgangProperties.getInstance().getDefaultPlaceSize() : nodeGraphics.getDimension().getY();
			newCell = createPlaceCell(place.getName(), place.getLabel(), x, y, dimX, dimY, getDefaultCellStyles(PNComponent.PLACE, nodeGraphics, annotationGraphics));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
		// if(annotationGraphics == null){
		// annotationGraphics = new AnnotationGraphics();
		// mxPoint point = new mxPoint(0, 0);
		// addGraphicalInfoToPNPlace(point , place, nodeGraphics,
		// annotationGraphics);
		// }
		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		if (nodeGraphics == null || annotationGraphics == null) {
			mxCellState state = getView().getState(newCell, true);
		}
		addCell(newCell, getDefaultParent());
		addNodeReference(place, newCell);
		notifyPlaceAdded(place);
		return newCell;
	}

	/**
	 * Returns the default cell styles for the given PN component type.
	 * @param type
	 * @param cellGraphics
	 * @param annotationGraphics
	 * @return
	 */
	private String getDefaultCellStyles(PNComponent type, AbstractObjectGraphics cellGraphics, AnnotationGraphics annotationGraphics) {
		String style = null;
		try {
			switch (type) {
			case PLACE:
				style = MXConstants.getNodeStyle(PNComponent.PLACE, (NodeGraphics) cellGraphics, annotationGraphics);
				break;
			case TRANSITION:
				style = MXConstants.getNodeStyle(PNComponent.TRANSITION, (NodeGraphics) cellGraphics, annotationGraphics);
				break;
			case ARC:
				style = MXConstants.getArcStyle((ArcGraphics) cellGraphics, annotationGraphics);
				break;
			}
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return style;
	}

	/**
	 * 
	 * @param name
	 *            The place name.
	 * @param label
	 *            The place label.
	 * @param posX
	 *            The x coordinate of the vertex.
	 * @param posY
	 *            The y coordinate of the vertex.
	 * @param width
	 *            The width of the vertex.
	 * @param height
	 *            The width of the vertex.
	 * @param style
	 *            The place style.
	 * @param object
	 * @param map
	 * @return
	 */
	public PNGraphCell createPlaceCell(String name, String label, double posX, double posY, double width, double height, String style) {
		mxGeometry geometry = new mxGeometry(posX - (width / 2), posY - (height / 2), width, height);
		geometry.setRelative(false);
		PNGraphCell vertex = new PNGraphCell(label, geometry, style, PNComponent.PLACE);
		vertex.setId(name);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	public Object addNewFlowRelation(PNGraphCell sourceCell, PNGraphCell targetCell) {
		AbstractFlowRelation relation = null;

		if ((sourceCell != null) && (targetCell != null) && sourceCell.getType() == PNComponent.PLACE && targetCell.getType() == PNComponent.TRANSITION) {
			relation = getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(), targetCell.getId());
		} else if (sourceCell.getType() == PNComponent.TRANSITION && targetCell.getType() == PNComponent.PLACE) {
			relation = getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(), targetCell.getId());
		}
		ArcGraphics arcGraphics = new ArcGraphics();
		AnnotationGraphics annotationGraphics = new AnnotationGraphics();
		if (relation != null)
			addGraphicalInfoToPNArc(relation, arcGraphics, annotationGraphics);
		PNGraphCell newCell = null;
		if (relation != null) {
			newCell = insertPNRelation(relation, arcGraphics, annotationGraphics);
		}
		return newCell;
	}

	/**
	 * @param relation
	 * @param arcGraphics
	 * @param annotationGraphics
	 * @throws ParameterException
	 */
	public void addGraphicalInfoToPNArc(AbstractFlowRelation relation, ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) {
		annotationGraphics.setOffset(new Offset(0, 0));
		String name = relation.getName();
		getNetContainer().getPetriNetGraphics().getArcGraphics().put(name, arcGraphics);
		getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().put(relation.getName(), annotationGraphics);
	}

	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNRelation(AbstractFlowRelation relation, ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) {

		PNGraphCell newCell = createArcCell(relation.getName(), getArcConstraint(relation), getDefaultCellStyles(PNComponent.ARC, arcGraphics, annotationGraphics));
		addEdge(newCell, getDefaultParent(), getCell(relation.getSource()), getCell(relation.getTarget()), null);

		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		Vector<Position> positions = (arcGraphics == null) ? new Vector<Position>() : arcGraphics.getPositions();
		List<mxPoint> points = new ArrayList<mxPoint>();
		for (Position position : positions) {
			points.add(new mxPoint(position.getX(), position.getY()));
		}
		newCell.getGeometry().setPoints(points);

		addArcReference(relation.getName(), newCell);
		notifyRelationAdded(relation);
		return newCell;
	}

	public Object addNewTransition(mxPoint mxPoint) {
		mxPoint point = mxPoint;
		String prefix = MXConstants.TRANSITION_NAME_PREFIX;
		PNGraphCell newCell = null;
		Integer index = 0;
		while (getNetContainer().getPetriNet().containsTransition(prefix + index)) {
			index++;
		}
		String nodeName = prefix + index;

			if (getNetContainer().getPetriNet().addTransition(nodeName)) {
				AbstractTransition transition = getNetContainer().getPetriNet().getTransition(nodeName);
				NodeGraphics nodeGraphicsWithMousePosition = new NodeGraphics();
				AnnotationGraphics annotationGraphics = new AnnotationGraphics();
				addGraphicalInfoToPNTransition(point, transition, nodeGraphicsWithMousePosition, annotationGraphics);
				newCell = insertPNTransition(transition, nodeGraphicsWithMousePosition, annotationGraphics);
			}

		return newCell;

	}

	/**
	 * @param point
	 * @param transition
	 * @param nodeGraphicsWithMousePosition
	 * @param annotationGraphics
	 * @throws IOException
	 * @throws PropertyException
	 *             @
	 */
	public void addGraphicalInfoToPNTransition(mxPoint point, AbstractTransition transition, NodeGraphics nodeGraphicsWithMousePosition, AnnotationGraphics annotationGraphics) {
		try {
			nodeGraphicsWithMousePosition.setPosition(new Position(point.getX(), point.getY()));
			nodeGraphicsWithMousePosition.setDimension(new Dimension(WolfgangProperties.getInstance().getDefaultTransitionWidth(), WolfgangProperties.getInstance().getDefaultTransitionHeight()));
			annotationGraphics.setOffset(new Offset(WolfgangProperties.getInstance().getDefaultHorizontalLabelOffset(), WolfgangProperties.getInstance().getDefaultVerticalLabelOffset()));
			getNetContainer().getPetriNetGraphics().getTransitionGraphics().put(transition.getName(), nodeGraphicsWithMousePosition);
			getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().put(transition.getName(), annotationGraphics);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 
	 * @param transition
	 * @param nodeGraphics
	 * @param annotationGraphics
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PNGraphCell insertPNTransition(AbstractTransition transition, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		// transition.setSilent(true);
		PNGraphCell newCell = null;
		if (transition.isSilent()) {
			Fill fill = new Fill("#0000", null, null, null);
			annotationGraphics.setVisibility(false);
			nodeGraphics.setFill(fill);
		}

		double x = (nodeGraphics == null) ? 0 : nodeGraphics.getPosition().getX();
		double y = (nodeGraphics == null) ? 0 : nodeGraphics.getPosition().getY();
		try {
		double dimX = (nodeGraphics == null) ? WolfgangProperties.getInstance().getDefaultTransitionWidth() : nodeGraphics.getDimension().getX();
		double dimY = (nodeGraphics == null) ? WolfgangProperties.getInstance().getDefaultTransitionHeight() : nodeGraphics.getDimension().getY();

		newCell = createTransitionCell(transition.getName(), transition.getLabel(), x, y, dimX, dimY, getDefaultCellStyles(PNComponent.TRANSITION, nodeGraphics, annotationGraphics));
		
		double offx = annotationGraphics.getOffset().getX();
		double offy = annotationGraphics.getOffset().getY();
		mxPoint offset = new mxPoint(offx, offy);
		newCell.getGeometry().setOffset(offset);

		if (nodeGraphics == null || annotationGraphics == null) {
			mxCellState state = getView().getState(newCell, true);
		}

		addCell(newCell, getDefaultParent());
		addNodeReference(transition, newCell);
		notifyTransitionAdded(transition);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot write Graphicsstyle to FileSystem " + e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Property Exception", JOptionPane.ERROR_MESSAGE);
		}
		return newCell;

	}

	private void makeSilent(PNGraphCell state) {
		// newCell.getStyle();
		// style.put(mxConstants.STYLE_FILLCOLOR, "#000000");
		// style.put(mxConstants.STYLE_STROKECOLOR, "none");
		// style.put(mxConstants.STYLE_NOLABEL, "1");

	}

	/**
	 * 
	 * @param name
	 *            The transition name.
	 * @param label
	 *            The transition label.
	 * @param posX
	 *            The x coordinate of the vertex.
	 * @param posY
	 *            The y coordinate of the vertex.
	 * @param width
	 *            The width of the vertex.
	 * @param height
	 *            The width of the vertex.
	 * @param style
	 *            The transition style.
	 * @return
	 */
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

	public abstract int getCapacityforPlace(String name, String color);

	public abstract void updatePlaceCapacity(String name, String color, int newCapacity);

	public abstract void updateViews();

	/**
	 * @param name
	 * @param circularPointGroup
	 * @return
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
	 * 
	 * @param vertices
	 *            Boolean indicating if vertices should be selected.
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

	protected void drawAdditionalPlaceGrahpics(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException {
		Rectangle temp = state.getRectangle();
		PNGraphCell cell = (PNGraphCell) state.getCell();

		int minDistance = (int) (WolfgangProperties.getInstance().getDefaultTokenDistance() * getView().getScale());
		int pointDiameter = (int) (WolfgangProperties.getInstance().getDefaultTokenSize() * getView().getScale());
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
	 * 
	 * @param pnGraphics
	 *            The Petri net graphics
	 */
	public void updatePositionPropertiesFromCells() {
		for (PNGraphCell cell : nodeReferences.values()) {
			mxCellState state = getView().getState(cell);
			setPositionProperties((PNGraphCell) state.getCell());
		}

	}

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

	// ------- GUI events that represent changes on Petri net components
	// ----------------------------------------//

	protected void addTransitionToPetriNet(String name, String label) {

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
		PNGraphCell placeCell = nodeReferences.get(name);

		mxRectangle bounds;
		double dy;
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
		PNGraphCell transitionCell = nodeReferences.get(name);

		switch (property) {
		case TRANSITION_LABEL:
			getModel().setValue(transitionCell, newValue);
			return true;
		case TRANSITION_POSITION_X:
			transitionCell.getGeometry().setX(new Integer((Integer) newValue).doubleValue());
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_POSITION_Y:
			transitionCell.getGeometry().setY(new Integer((Integer) newValue).doubleValue());
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_SIZE_X:
			transitionCell.getGeometry().setWidth(new Integer((Integer) newValue).doubleValue());
			setSelectionCell(transitionCell);
			return true;
		case TRANSITION_SIZE_Y:
			transitionCell.getGeometry().setHeight(new Integer((Integer) newValue).doubleValue());
			setSelectionCell(transitionCell);
			return true;
		}
		return false;
	}

	protected boolean handleArcPropertyChange(String name, PNProperty property, Object oldValue, Object newValue) {
		PNGraphCell arcCell = arcReferences.get(name);
		switch (property) {
		case ARC_WEIGHT:
			getModel().setValue(arcCell, newValue);
			break;

		}

		return false;
	}

	public void selectPlace(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = nodeReferences.get(name);
			setSelectionCell(cell);
		}
	}

	public void selectTransition(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = nodeReferences.get(name);
			setSelectionCell(cell);
		}
	}

	public void selectArc(String name) {
		if (!isCellSelected(name)) {
			PNGraphCell cell = arcReferences.get(name);
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

	private void removeFlowRelation(String name) {
		AbstractFlowRelation relation = netContainer.getPetriNet().getFlowRelation(name);
		if (relation != null) {
			netContainer.getPetriNet().removeFlowRelation(name);
			netContainer.getPetriNetGraphics().getArcGraphics().remove(name);
			netContainer.getPetriNetGraphics().getArcAnnotationGraphics().remove(name);
			notifyRelationRemoved(relation);
		}
	}

	private void removeTransition(String name) {
		AbstractTransition transition = netContainer.getPetriNet().getTransition(name);
		if (transition != null) {
			Set<AbstractFlowRelation> relations = new HashSet<AbstractFlowRelation>();
			relations.addAll(transition.getIncomingRelations());
			relations.addAll(transition.getOutgoingRelations());
			netContainer.getPetriNet().removeTransition(name);
			netContainer.getPetriNetGraphics().getTransitionGraphics().remove(name);
			netContainer.getPetriNetGraphics().getTransitionLabelAnnotationGraphics().remove(name);
			notifyTransitionRemoved(transition);
			for (AbstractFlowRelation removedRelation : relations) {
				notifyRelationRemoved(removedRelation);
				netContainer.getPetriNetGraphics().getArcGraphics().remove(removedRelation.getName());
				netContainer.getPetriNetGraphics().getArcAnnotationGraphics().remove(removedRelation.getName());
			}
		}
	}

	private void removePlace(String name) {
		AbstractPlace place = netContainer.getPetriNet().getPlace(name);
		if (place != null) {
			Set<AbstractFlowRelation> relations = new HashSet<AbstractFlowRelation>();
			relations.addAll(place.getIncomingRelations());
			relations.addAll(place.getOutgoingRelations());
			netContainer.getPetriNet().removePlace(name);
			netContainer.getPetriNetGraphics().getPlaceGraphics().remove(name);
			netContainer.getPetriNetGraphics().getPlaceLabelAnnotationGraphics().remove(name);
			notifyPlaceRemoved(place);
			for (AbstractFlowRelation removedRelation : relations) {
				notifyRelationRemoved(removedRelation);
				netContainer.getPetriNetGraphics().getArcGraphics().remove(removedRelation.getName());
				netContainer.getPetriNetGraphics().getArcAnnotationGraphics().remove(removedRelation.getName());
			}
		}
	}

	@Override
	public void invoke(Object sender, mxEventObject evt) {
//System.out.println("hey------------------" + evt.getProperties());
//System.out.println(sender + "\n" + evt.getName() + "\n" + evt.getClass());
		if (evt.getName().equals(mxEvent.UNDO)) {
			mxUndoableEdit edit = (mxUndoableEdit) evt.getProperty("edit");

		}
		if (evt.getName().equals(mxEvent.CHANGE)) {
//			System.out.println("IsEmpty: " + evt.getProperties().isEmpty()); //refresh bei Handler auslšsen
			if(evt.getProperties().isEmpty()){
				getSelectionCell();
//				getView().getState(getSelectionCell()).g;
//				getSelectionCell()
			}
			ArrayList<mxAtomicGraphModelChange> changes = (ArrayList<mxAtomicGraphModelChange>) evt.getProperty("changes");
			if (changes != null) {
//				System.out.println("Props: " + evt.getProperties());
				for (mxAtomicGraphModelChange change : changes) {

					if (change instanceof mxValueChange) {
						PNGraphCell labelCell = (PNGraphCell) ((mxValueChange) change).getCell();

						switch (labelCell.getType()) {
						case ARC:
							setArcLabel(this, labelCell.getId(), ((mxValueChange) change).getValue() + "");
							break;
						case PLACE:
							Object value = ((mxValueChange) change).getValue();

							break;
						case TRANSITION:
							properties.setTransitionLabel(this, labelCell.getId(), (String) ((mxValueChange) change).getValue());
							break;
						default:
							break;

						}

					}
					if (change instanceof mxTerminalChange) {
						mxTerminalChange terminalChange = (mxTerminalChange) change;
						PNGraphCell arc = (PNGraphCell) terminalChange.getCell();
						PNGraphCell prevArcEnd = (PNGraphCell) terminalChange.getPrevious();
						PNGraphCell newArcEnd = (PNGraphCell) terminalChange.getTerminal();
						if (prevArcEnd != null && newArcEnd != null) {
							if (prevArcEnd.getType() != newArcEnd.getType()) {
							}

							if (getNetContainer().getPetriNet().containsFlowRelation(arc.getId())) {

								PNGraphCell sourceCell = (PNGraphCell) arc.getSource();
								PNGraphCell targetCell = (PNGraphCell) arc.getTarget();

								AbstractFlowRelation relation = null;

								if (sourceCell.getType() == PNComponent.PLACE && targetCell.getType() == PNComponent.TRANSITION) {
									relation = getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(), targetCell.getId());
								} else if (sourceCell.getType() == PNComponent.TRANSITION && targetCell.getType() == PNComponent.PLACE) {
									relation = getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(), targetCell.getId());
								}
								PNGraphCell arcCell = arcReferences.get(arc.getId());
								addArcReference(relation.getName(), arcCell);
								ArcGraphics arcGraphics = getNetContainer().getPetriNetGraphics().getArcGraphics().get(arc.getId());
								AnnotationGraphics annotationGraphics = getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(arc.getId());
								addGraphicalInfoToPNArc(relation, arcGraphics, annotationGraphics);
								removeFlowRelation(arc.getId());
								arcCell.setId(relation.getName());
								notifyRelationAdded(relation);
								setSelectionCell(arcCell);

							}
						}
					}
					if (change instanceof mxChildChange) {
						mxChildChange childChange = ((mxChildChange) change);
						if (childChange.getChild() instanceof PNGraphCell) {
							PNGraphCell cell = (PNGraphCell) ((mxChildChange) change).getChild();
							if (childChange.getPrevious() == null) {

								switch (cell.getType()) {
								case PLACE:
									if (!getNetContainer().getPetriNet().containsPlace(cell.getId())) {

										if (getNetContainer().getPetriNet().addPlace(cell.getId())) {
											AbstractPlace place = getNetContainer().getPetriNet().getPlace(cell.getId());
											NodeGraphics nodeGraphics = new NodeGraphics();
											AnnotationGraphics annotationGraphics = new AnnotationGraphics();

											addGraphicalInfoToPNPlace(new mxPoint(cell.getGeometry().getCenterX(), cell.getGeometry().getCenterY()), place, nodeGraphics, annotationGraphics);

											Utils.createNodeGraphicsFromStyle(cell.getStyle(), nodeGraphics, annotationGraphics);
											addNodeReference(place, cell);
											notifyPlaceAdded(place);
										}

									}

									break;
								case TRANSITION:
									if (!getNetContainer().getPetriNet().containsTransition(cell.getId())) {

										if (getNetContainer().getPetriNet().addTransition(cell.getId())) {
											AbstractTransition transition = getNetContainer().getPetriNet().getTransition(cell.getId());
											NodeGraphics nodeGraphics = new NodeGraphics();
											AnnotationGraphics annotationGraphics = new AnnotationGraphics();

											addGraphicalInfoToPNTransition(new mxPoint(cell.getGeometry().getCenterX(), cell.getGeometry().getCenterY()), transition, nodeGraphics, annotationGraphics);

											Utils.createNodeGraphicsFromStyle(cell.getStyle(), nodeGraphics, annotationGraphics);
											addNodeReference(transition, cell);
											notifyTransitionAdded(transition);
										}

									}
									break;
								case ARC:
									if (!getNetContainer().getPetriNet().containsFlowRelation(cell.getId())) {

										AbstractFlowRelation relation = null;
										PNGraphCell sourceCell = (PNGraphCell) cell.getSource();
										PNGraphCell targetCell = (PNGraphCell) cell.getTarget();
										if (sourceCell.getType() == PNComponent.PLACE && targetCell.getType() == PNComponent.TRANSITION) {
											relation = getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(), targetCell.getId());
										} else if (sourceCell.getType() == PNComponent.TRANSITION && targetCell.getType() == PNComponent.PLACE) {
											relation = getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(), targetCell.getId());
										}
										ArcGraphics arcGraphics = new ArcGraphics();
										AnnotationGraphics annotationGraphics = new AnnotationGraphics();
										addGraphicalInfoToPNArc(relation, arcGraphics, annotationGraphics);
										Utils.createArcGraphicsFromStyle(cell.getStyle(), arcGraphics, annotationGraphics);
										addArcReference(relation.getName(), cell);
										notifyRelationAdded(relation);

									}
									break;
								}
							} else if (childChange.getPrevious() != null && childChange.getParent() == null) {

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

					if (change instanceof mxGeometryChange) {

						mxGeometryChange geochange = ((mxGeometryChange) change);
						mxGeometry geo = geochange.getGeometry();
						PNGraphCell cell = (PNGraphCell) geochange.getCell();

						NodeGraphics nodeGraphics = null;
						AnnotationGraphics annotationGraphics = null;
						ArcGraphics arcGraphics = null;
						switch (cell.getType()) {
						case PLACE:
							nodeGraphics = getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
							annotationGraphics = getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
							break;
						case TRANSITION:
							nodeGraphics = getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
							annotationGraphics = getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
							break;
						case ARC:
							arcGraphics = getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
							annotationGraphics = getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
							break;
						}

						if (nodeGraphics != null) {
							nodeGraphics.getPosition().setX(geo.getCenterX());
							nodeGraphics.getPosition().setY(geo.getCenterY());
						}
						updatePointsInArcGraphics(cell, geo.getPoints());

						if (annotationGraphics != null) {
							annotationGraphics.getOffset().setX(geo.getOffset().getX());
							annotationGraphics.getOffset().setY(geo.getOffset().getY());
						}

					}
				}
			}
			if (evt.getName().equals(mxEvent.CHANGE)) {
				if (sender instanceof mxGraphSelectionModel || sender instanceof PNGraphComponent) {
					// if(!isExecution)
					notifyComponentsSelected();
				}
			}

		}

		if (evt.getName().equals(mxEvent.RESIZE_CELLS)) {
			ensureValidPlaceSize();
		}
	}

	protected abstract void setArcLabel(PNGraph pnGraph, String id, String string);

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
		if (isLabelSelected()) {
			setCellStyles(MXConstants.LABEL_LINE_WIDTH, strokeWeight);
		} else {
			setCellStyles(mxConstants.STYLE_STROKEWIDTH, strokeWeight);
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

	/**
	 * @param graph
	 * @return
	 */
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

	private void updatePointsInArcGraphics(PNGraphCell cell, List<mxPoint> points) {
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
		refresh();
	}

	public void updateTransitionSilent(String id, boolean setSilent) {
		getNetContainer().getPetriNet().getTransition(id).setSilent(setSilent);

	}

	public boolean getTransitionSilentState(String name) {
		return getNetContainer().getPetriNet().getTransition(name).isSilent();
	}

	public void removeAllArcPoints() {
		for (Entry<String, PNGraphCell> arc : arcReferences.entrySet()) {
			PNGraphCell arcCell = arc.getValue();
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
			return WolfgangProperties.getInstance().getDefaultTokenSize();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Double) null;
	}



}
