package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.util.ArrayList;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxGraphModel.mxGeometryChange;
import com.mxgraph.model.mxGraphModel.mxTerminalChange;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;

public class PNGraphChangeHandler {

	private PNGraph graph;

	public PNGraphChangeHandler(PNGraph graph) {
		this.graph = graph;
	}

	public void handleChange(mxEventObject evt) {
		if (evt.getProperties().isEmpty()) {
			graph.getSelectionCell();
		}
		ArrayList<mxAtomicGraphModelChange> changes = (ArrayList<mxAtomicGraphModelChange>) evt.getProperty("changes");
		if (changes != null) {
			for (mxAtomicGraphModelChange change : changes) {
				if (change instanceof mxValueChange) {
					handleValueChange((mxValueChange) change);
				} else if (change instanceof mxTerminalChange) {
					handleTerminalChange((mxTerminalChange) change);
				} else if (change instanceof mxChildChange) {
					handleChildChange((mxChildChange) change);
				} else if (change instanceof mxGeometryChange) {
					handleGeometryChange((mxGeometryChange) change);
				}
			}
		}
	}

	private void handleChildChange(mxChildChange childChange) {
		if (childChange.getChild() instanceof PNGraphCell) {
			PNGraphCell cell = (PNGraphCell) childChange.getChild();
			if (childChange.getPrevious() == null) {
				switch (cell.getType()) {
				case PLACE:
					if (!graph.getNetContainer().getPetriNet().containsPlace(cell.getId())) {
						// Same code as case 4 in class GraphTransferHandler
						Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
						Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
						graph.addNewPlace(new mxPoint(cell.getGeometry().getCenterX(), cell.getGeometry().getCenterY()), cell.getStyle(), offset, dimension);

						if (!cell.getId().equals(cell.getValue())) {
							graph.getNetContainer().getPetriNet().getPlace(cell.getId()).setLabel(cell.getValue().toString());
						}
					}

					break;
				case TRANSITION:
					if (!graph.getNetContainer().getPetriNet().containsTransition(cell.getId())) {

						// Same code as case 4 in class GraphTransferHandler
						Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
						Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
						graph.addNewTransition(new mxPoint(cell.getGeometry().getCenterX(), cell.getGeometry().getCenterY()), cell.getStyle(), offset, dimension);

						if (!cell.getId().equals(cell.getValue())) {
							graph.getNetContainer().getPetriNet().getTransition(cell.getId()).setLabel(cell.getValue().toString());
						}

					}
					break;
				case ARC:
					if (!graph.getNetContainer().getPetriNet().containsFlowRelation(cell.getId())) {

						// Same code as in GraphTransferHandler
						// Check if source and target nodes already have been
						// added
						PNGraphCell sourceCell = (PNGraphCell) cell.getSource();
						PNGraphCell targetCell = (PNGraphCell) cell.getTarget();
						if (sourceCell != null || targetCell != null) {
							Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
							graph.addNewFlowRelation(sourceCell, targetCell, offset, cell.getGeometry().getPoints(), new mxPoint(0.0, 0.0), cell.getStyle());
						}
					}
					break;
				}
			}
			if (childChange.getPrevious() != null && childChange.getParent() == null) {

				switch (cell.getType()) {
				case ARC:
					graph.removeFlowRelation(cell.getId());
					break;
				case PLACE:
					graph.removePlace(cell.getId());
					break;
				case TRANSITION:
					graph.removeTransition(cell.getId());
					break;
				}

			}
		}
	}

	private void handleValueChange(mxValueChange valueChange) {
		PNGraphCell labelCell = (PNGraphCell) valueChange.getCell();

		switch (labelCell.getType()) {
		case ARC:
			graph.setArcLabel(labelCell.getId(), valueChange.getValue() + "");
			break;
		case PLACE:
			graph.getPNProperties().setPlaceLabel(this, labelCell.getId(), (String) valueChange.getValue());
			break;
		case TRANSITION:
			graph.getPNProperties().setTransitionLabel(this, labelCell.getId(), (String) valueChange.getValue());
			break;
		default:
			break;
		}
	}

	private void handleGeometryChange(mxGeometryChange geometryChange) {
		mxGeometry geometry = geometryChange.getGeometry();
		PNGraphCell cell = (PNGraphCell) geometryChange.getCell();

		NodeGraphics nodeGraphics = null;
		AnnotationGraphics annotationGraphics = null;
		ArcGraphics arcGraphics = null;
		switch (cell.getType()) {
		case PLACE:
			nodeGraphics = graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
			annotationGraphics = graph.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
			break;
		case TRANSITION:
			nodeGraphics = graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
			annotationGraphics = graph.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
			break;
		case ARC:
			arcGraphics = graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
			annotationGraphics = graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
			break;
		}

		if (nodeGraphics != null) {
			nodeGraphics.getPosition().setX(geometry.getCenterX());
			nodeGraphics.getPosition().setY(geometry.getCenterY());
		}
		graph.updatePointsInArcGraphics(cell, geometry.getPoints());

		if (annotationGraphics != null) {
			annotationGraphics.getOffset().setX(geometry.getOffset().getX());
			annotationGraphics.getOffset().setY(geometry.getOffset().getY());
		}

	}

	private void handleTerminalChange(mxTerminalChange terminalChange) {
		PNGraphCell arc = (PNGraphCell) terminalChange.getCell();
		PNGraphCell prevArcEnd = (PNGraphCell) terminalChange.getPrevious();
		PNGraphCell newArcEnd = (PNGraphCell) terminalChange.getTerminal();
		if (prevArcEnd != null && newArcEnd != null) {
			if (graph.getNetContainer().getPetriNet().containsFlowRelation(arc.getId())) {
				if (prevArcEnd != newArcEnd)
					graph.removeFlowRelation(arc.getId());
				PNGraphCell sourceCell = (PNGraphCell) arc.getSource();
				PNGraphCell targetCell = (PNGraphCell) arc.getTarget();
				Offset offset = new Offset(arc.getGeometry().getOffset().getX(), arc.getGeometry().getOffset().getY());
				graph.addNewFlowRelation(sourceCell, targetCell, offset, arc.getGeometry().getPoints(), new mxPoint(0.0, 0.0), arc.getStyle());
				graph.removeCells(new Object[] { arc });
				graph.refresh();
				// AbstractFlowRelation relation = null;
				// if (sourceCell.getType() == PNComponent.PLACE &&
				// targetCell.getType() == PNComponent.TRANSITION) {
				// relation =
				// graph.getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(),
				// targetCell.getId());
				// } else if (sourceCell.getType() == PNComponent.TRANSITION &&
				// targetCell.getType() == PNComponent.PLACE) {
				// relation =
				// graph.getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(),
				// targetCell.getId());
				// }
				// PNGraphCell arcCell = arcReferences.get(arc.getId());
				// addArcReference(relation.getName(), arcCell);
				// ArcGraphics arcGraphics =
				// graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(arc.getId());
				// AnnotationGraphics annotationGraphics =
				// graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(arc.getId());
				// addGraphicalInfoToPNArc(relation, arcGraphics,
				// annotationGraphics);
				// graph.removeFlowRelation(arc.getId());
				// arcCell.setId(relation.getName());
				// graphListenerSupport.notifyRelationAdded(relation);
				// graph.setSelectionCell(arcCell);

			}
		}
	}

}
