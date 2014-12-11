package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class CPNGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public CPNGraphComponent(CPNGraph cpnGraph) {
		super(cpnGraph);
	}

	@Override
	public CPNGraph getGraph() {
		return (CPNGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell,this);
		return true;
	}


	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell,this);
		return true;
	}
	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		return false;
	}

}
