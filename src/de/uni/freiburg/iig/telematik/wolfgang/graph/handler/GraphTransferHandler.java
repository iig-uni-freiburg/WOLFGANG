package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;


import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxPoint;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;

public class GraphTransferHandler extends mxGraphTransferHandler {


	private static final long serialVersionUID = -8521142091923475876L;

	public GraphTransferHandler() {
		super();
		
		//Workaround JVM 1.7/1.8 Bug regarding flavormap.properties (MacOS/Linux Problem)
		//see  https://java.net/nonav/projects/macosx-port/lists/issues/archive/2011-11/message/524
		mxGraphTransferable.enableImageSupport= false;
	}

	/**
	 * Checks if the mxGraphTransferable data flavour is supported and calls
	 * importmxGraphTransferable if possible.
	 */
	@Override
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
							result = importGraphTransferable(graphComponent, gt);
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
				
				switch (cell.getType()) {
				
				case PLACE:
					PNGraphCell placeCell = null;
					if (cell.getId() == null){
						// Strategy A (Case 1-3)
						try {
							placeCell = (PNGraphCell) graph.addNewPlace(new mxPoint(dx, dy));
						} catch (Exception e) {
							JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(graphComponent), "Cannot insert place.\nReason: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						// Strategy B (Case 4)
						Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
						Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
						placeCell = (PNGraphCell) graph.addNewPlace(new mxPoint(cell.getGeometry().getCenterX() + dx, cell.getGeometry().getCenterY() + dy), cell.getStyle(), offset, dimension);
						
						if(!cell.getId().equals(cell.getValue())){
							graph.getNetContainer().getPetriNet().getPlace(cell.getId()).setLabel(cell.getValue().toString());
						}
					}
					insertedCells.put(cell.getId(), placeCell);
					break;
					
				case TRANSITION:
					PNGraphCell transitionCell = null;
					if (cell.getId() == null){
						// Strategy A (Case 1-3)
						try{
							transitionCell = (PNGraphCell) graph.addNewTransition(new mxPoint(dx, dy));
						} catch (Exception e) {
							JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(graphComponent), "Cannot insert transition.\nReason: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						// Strategy B (Case 4)
						Offset offset = new Offset(cell.getGeometry().getOffset().getX(), cell.getGeometry().getOffset().getY());
						Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
						transitionCell = (PNGraphCell) graph.addNewTransition(new mxPoint(cell.getGeometry().getCenterX() + dx, cell.getGeometry().getCenterY() + dy), cell.getStyle(), offset, dimension);
						
						if(!cell.getId().equals(cell.getValue())){
							graph.getNetContainer().getPetriNet().getTransition(cell.getId()).setLabel(cell.getValue().toString());
						}
						Object silentValue = graph.getCellStyle(cell).get(MXConstants.SILENT);
						if(silentValue == null || silentValue.equals("1"));
							graph.getNetContainer().getPetriNet().getTransition(transitionCell.getId()).setSilent(true);
					}
					insertedCells.put(cell.getId(), transitionCell);
					break;

				case ARC:
					// For arcs, this code is only reachable for case 4
					
					// Remember arc cell for later import
					// -> ensure that source and target cells already have been imported
					remainingArcCells.add(cell);
					break;
				}
			}
		}
		
		for (PNGraphCell remainingArcCell: remainingArcCells) {
			PNGraphCell sourceCell;
			PNGraphCell targetCell;
			// Check if source and target nodes already have been added
			if (remainingArcCell.getSource() != null && remainingArcCell.getTarget() != null) {
				sourceCell = insertedCells.get(remainingArcCell.getSource().getId());
				targetCell = insertedCells.get(remainingArcCell.getTarget().getId());
			}
			
			else
				// Skip import of arc cells with missing source or target cell
				continue;
			
			Offset offset = new Offset(remainingArcCell.getGeometry().getOffset().getX(), remainingArcCell.getGeometry().getOffset().getY());
			PNGraphCell insertedArcCell = graph.addNewFlowRelation(sourceCell, targetCell, offset, remainingArcCell.getGeometry().getPoints(), new mxPoint(dx, dy), remainingArcCell.getStyle());
			insertedCells.put(remainingArcCell.getId(), insertedArcCell);
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
