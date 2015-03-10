package de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands;

import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;

public class SelectAction extends AbstractPNEditorAction {

	private PNComponent type;

	public SelectAction(PNEditorComponent editor, PNComponent type) {
		super(editor);
		this.type =type;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		 graph.selectPNGraphCells(type);		
	}


}
