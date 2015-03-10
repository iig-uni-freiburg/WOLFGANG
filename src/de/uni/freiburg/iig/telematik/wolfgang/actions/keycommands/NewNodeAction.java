package de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxPoint;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class NewNodeAction extends AbstractPNEditorAction {

	private int deltaX;
	private int deltaY;

	public NewNodeAction(PNEditorComponent editor, int dx, int dy) {
		super(editor);
		deltaX = dx;
		deltaY = dy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7020268186996809614L;

	/**
	 * @param source
	 * @param pnGraph
	 * @param centerX
	 * @param centerY
	 * @return
	 * @throws ParameterException 
	 * @throws IOException 
	 * @throws PropertyException 
	 */
	protected PNGraphCell createNewNodeWithEdge(PNGraphCell source, PNGraph pnGraph, double centerX, double centerY) throws ParameterException, PropertyException, IOException {
		PNGraphCell target = null;
		switch (source.getType()) {
		case PLACE:
			target = (PNGraphCell) pnGraph.addNewTransition(new mxPoint(centerX, centerY));
			break;
		case TRANSITION:
			target = (PNGraphCell) pnGraph.addNewPlace(new mxPoint(centerX, centerY));
			break;
		case ARC:
			break;
		}
		if (target != null)
			pnGraph.addNewFlowRelation(source, target);
		return target;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		PNGraphCell source = (PNGraphCell) graph.getSelectionCell();
		PNGraphCell target = null;
		double centerX = source.getGeometry().getCenterX();
		double centerY = source.getGeometry().getCenterY();
		centerX += deltaX;
		centerY += deltaY;
		if (centerX > 0 && centerY > 0) {
			target = createNewNodeWithEdge(source, getEditor().getGraphComponent().getGraph(), centerX, centerY);
			graph.setSelectionCell(target);
		}

	}

}
