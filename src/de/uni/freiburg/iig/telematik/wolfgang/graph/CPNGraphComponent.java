package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

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
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		getTransitionPopupMenu().show(CPNGraphComponent.this, pt.x, pt.y);
		return false;
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell,this);
		return true;
	}


	@Override
	protected boolean doubleClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell,this);
		return true;
	}
	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		return false;
	}

}
