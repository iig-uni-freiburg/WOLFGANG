package de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands;

import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class MoveAction extends AbstractPNEditorAction {

	private int deltaX;
	private int deltaY;

	public MoveAction(PNEditorComponent editor, int dx, int dy) {
		super(editor);
		deltaX = dx;
		deltaY = dy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1728027231812006823L;


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		 graph.moveCells(graph.getSelectionCells(), deltaX, deltaY);		
	}


}
