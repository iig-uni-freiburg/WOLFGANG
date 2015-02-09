package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;


import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;

public class GraphTransferHandler extends mxGraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521142091923475876L;

	@Override
	/**
	 * Checks if the mxmxGraphTransferable data flavour is supported and calls
	 * importmxGraphTransferable if possible.
	 */
	public boolean importData(JComponent c, Transferable t) {
		boolean result = false;

		if (isLocalDrag()) {
			// Enables visual feedback on the Mac
			result = true;
		} else {

			updateImportCount(t);

			if (c instanceof PNGraphComponent) {
				PNGraphComponent graphComponent = (PNGraphComponent) c;

				if (graphComponent.isEnabled() && t.isDataFlavorSupported(mxGraphTransferable.dataFlavor)) {
					mxGraphTransferable gt;
					try {
						gt = (mxGraphTransferable) t.getTransferData(mxGraphTransferable.dataFlavor);

						if (gt.getCells() != null) {
							result = importmxGraphTransferable(graphComponent, gt);
						}
					} catch (UnsupportedFlavorException e) {
						JOptionPane.showMessageDialog(graphComponent, "Error during transfer \nReason:" + e.getMessage(), "Unsupported Flavor Exception", JOptionPane.ERROR);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(graphComponent, "Error during transfer \nReason:" + e.getMessage(), "IO Exception", JOptionPane.ERROR);

					}
				}
			}
		}

		return result;
	}

	/**
	 * Returns true if the cells have been imported using importCells.
	 */
	protected boolean importmxGraphTransferable(PNGraphComponent graphComponent, mxGraphTransferable gt) {
		boolean result = false;

		PNGraph graph = graphComponent.getGraph();
		double scale = graph.getView().getScale();
		mxRectangle bounds = gt.getBounds();
		double dx = 0, dy = 0;

		// Computes the offset for the placement of the imported cells
		if (location != null && bounds != null) {
			mxPoint translate = graph.getView().getTranslate();

			dx = location.getX() - (bounds.getX() + translate.getX()) * scale;
			dy = location.getY() - (bounds.getY() + translate.getY()) * scale;

			// Keeps the cells aligned to the grid
			dx = graph.snap(dx / scale);
			dy = graph.snap(dy / scale);
		} else {
			int gs = graph.getGridSize();

			dx = importCount * gs;
			dy = importCount * gs;
		}

		if (offset != null) {
			dx += offset.x;
			dy += offset.y;
		}


			importCells(graphComponent, gt, dx, dy);

		location = null;
		offset = null;
		result = true;

		// Requests the focus after an import
		graphComponent.requestFocus();

		return result;
	}
	
	/**
	 * Gets a drop target using getDropTarget and imports the cells.<br>
	 * <ul>
	 * <li>Case 1: Drag 'n' Drop from toolbar</li>
	 * <li>Case 2: Drag from existing node in editor</li>
	 * <li>Case 3: Keyboard shortcut (Strg + arrow)</li>
	 * <li>Case 4: Copy Paste</li>
	 * <ul>
	 * These cases are handles with two different strategies:
	 * <ul>
	 * <li>Strategy A (Case 1-3) {@see #}</li>
	 * <li>Strategy B (Case 4)</li>
	 * <ul>
	 * @throws IOException 
	 * @throws PropertyException 
	 */
	@Override
	protected Object[] importCells(mxGraphComponent graphComponent, mxGraphTransferable gt, double dx, double dy) {
		Validate.type(graphComponent, PNGraphComponent.class);
		Object target = getDropTarget(graphComponent, gt);
		PNGraph graph = ((PNGraphComponent) graphComponent).getGraph();
		Object[] cells = gt.getCells();
		HashMap<String, PNGraphCell> insertedCells = new HashMap<String, PNGraphCell>();
		
		List<PNGraphCell> remainingArcCells = new ArrayList<PNGraphCell>();

		graph.getModel().beginUpdate();
		for (Object object : cells) {
			if (object instanceof PNGraphCell) {
				PNGraphCell cell = (PNGraphCell) object;

				PNGraphCell newCell = null;
				switch (cell.getType()) {
				case PLACE:
					if (cell.getId() == null){
						// Strategy A (Case 1-3)
						newCell = (PNGraphCell) graph.addNewPlace(new mxPoint(dx, dy));
					} else {
						// Strategy B (Case 4)
						Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
						Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
						newCell = (PNGraphCell) graph.addNewPlace(new mxPoint(cell.getGeometry().getCenterX() + dx, cell.getGeometry().getCenterY() + dy), cell.getStyle(), offset, dimension);
						
						if(!cell.getId().equals(cell.getValue())){
							graph.getNetContainer().getPetriNet().getPlace(cell.getId()).setLabel(cell.getValue().toString());
						}
					}
					break;
				case TRANSITION:
					if (cell.getId() == null){
						// Strategy A (Case 1-3)
						newCell = (PNGraphCell) graph.addNewTransition(new mxPoint(dx, dy));
					} else {
						String nodeName = graph.getNewTransitionName();
						if (graph.getNetContainer().getPetriNet().addTransition(nodeName)) {
							AbstractTransition transition = graph.getNetContainer().getPetriNet().getTransition(nodeName);
							if (graph.getNetContainer().getPetriNet().containsTransition(cell.getId())) {
								if (!graph.getNetContainer().getPetriNet().getTransition(cell.getId()).getLabel().equals(cell.getId()))
									transition.setLabel(graph.getNetContainer().getPetriNet().getTransition(cell.getId()).getLabel());
							} else
								transition.setLabel((String) cell.getValue());

							NodeGraphics nodeGraphics = new NodeGraphics();
							AnnotationGraphics annotationGraphics = new AnnotationGraphics();
							Utils.createNodeGraphicsFromStyle(cell.getStyle(), (NodeGraphics)nodeGraphics, annotationGraphics);
							graph.addGraphicalInfoToPNTransition(new mxPoint(cell.getGeometry().getCenterX() + dx, cell.getGeometry().getCenterY() + dy), transition, nodeGraphics, annotationGraphics);
							annotationGraphics.setOffset(new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY()));
							newCell = graph.insertPNTransition(transition, nodeGraphics, annotationGraphics);
						}
					}
					break;

				case ARC:
					
//					if (cell.getId() == null){
//						// Strategy A (Case 1-3)
//						
//					} else {

						PNGraphCell sourceCell = insertedCells.get(cell.getSource().getId());
						PNGraphCell targetCell = insertedCells.get(cell.getTarget().getId());
						if(sourceCell == null || targetCell == null){
							remainingArcCells.add(cell);
							continue;
						}
						
						AbstractFlowRelation relation = null;
						if (sourceCell != null && targetCell != null) {
							if (sourceCell.getType() == PNComponent.PLACE && targetCell.getType() == PNComponent.TRANSITION) {
								relation = graph.getNetContainer().getPetriNet().addFlowRelationPT(sourceCell.getId(), targetCell.getId());
							} else if (sourceCell.getType() == PNComponent.TRANSITION && targetCell.getType() == PNComponent.PLACE) {
								relation = graph.getNetContainer().getPetriNet().addFlowRelationTP(sourceCell.getId(), targetCell.getId());
							}
							ArcGraphics arcGraphics = new ArcGraphics();
							AnnotationGraphics annotationGraphics = new AnnotationGraphics();
							graph.addGraphicalInfoToPNArc(relation, arcGraphics, annotationGraphics);
							Utils.createArcGraphicsFromStyle(cell.getStyle(), arcGraphics, annotationGraphics);
							List<mxPoint> points = cell.getGeometry().getPoints();
							if (arcGraphics != null) {
								Vector<Position> vector = new Vector<Position>();
								if (points != null) {
									if (points.size() >= 0) {
										for (mxPoint p : points) {
											vector.add(new Position(p.getX() + dx, p.getY() + dy));
										}

										arcGraphics.setPositions(vector);

									}
								}
							}
							newCell = graph.insertPNRelation(relation, arcGraphics, annotationGraphics);
						}
					
//					}

					break;
				}

				insertedCells.put(cell.getId(), newCell);
			}
		}
		
		for (PNGraphCell remainingArcCell: remainingArcCells) {
			
		}
		
		graph.getModel().endUpdate();
		if (graph.isSplitEnabled() && graph.isSplitTarget(target, cells)) {
			graph.splitEdge(target, cells, dx, dy);
		} else {
			graph.setSelectionCells(cells);
		}
		if (!insertedCells.isEmpty())
			graph.setSelectionCells(insertedCells.values().toArray());

		return cells;
	}

}
