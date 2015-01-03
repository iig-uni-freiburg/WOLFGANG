package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;

public class IFNetGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public IFNetGraphComponent(IFNetGraph IFNetGraph) {
		super(IFNetGraph);
	}

	@Override
	public IFNetGraph getGraph() {
		return (IFNetGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell, this);
		return true;
	}

	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell, this);
		return true;
	}

	@Override
	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		getGraph().newTokenConfigurer(cell, this);
		return true;
	}

	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		// TODO decrementing or incrementing tokennumber of selected color with
		// mousewheel
		return false;
	}

	@Override
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		getTransitionPopupMenu().show(IFNetGraphComponent.this, pt.x, pt.y);
		return false;
	}

	@Override
	protected boolean rightClickOnArc(PNGraphCell cell, MouseEvent e) {
		return false;
	}

}
