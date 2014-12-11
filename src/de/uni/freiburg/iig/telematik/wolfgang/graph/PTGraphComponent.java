package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxPoint;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenMouseWheelChange;

public class PTGraphComponent extends PNGraphComponent {

	@Override
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		getTransitionPopupMenu().show(PTGraphComponent.this, pt.x, pt.y);
		return false;
	}

	private boolean isExecution;

	@Override
	protected boolean singleClickOnTransition(PNGraphCell cell, MouseEvent e) {
		isExecution = getGraph().isExecution();
		if (isExecution) {

			try {
				getGraph().fireTransition(cell);
			} catch (ParameterException e1) {
				JOptionPane.showMessageDialog(null, "Parameter Exception \nReason: " + e1.getMessage(), "Parameter Exception", JOptionPane.ERROR);
			} catch (PNException e1) {
				JOptionPane.showMessageDialog(null, "Petri Net Exception \nReason: " + e1.getMessage(), "Petri Net Exception", JOptionPane.ERROR);
			}
			highlightEnabledTransitions();

		}
		return true;
	}

	private static final long serialVersionUID = -1698182711658593407L;

	public PTGraphComponent(PTGraph graph) {
		super(graph);
	}

	@Override
	public PTGraph getGraph() {
		return (PTGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		String tokens = JOptionPane.showInputDialog(PTGraphComponent.this, "Input new amount of tokens");
		if(tokens != null){
		try {
			Validate.notNegativeInteger(tokens);
			Multiset<String> multiSet = new Multiset<String>();
			multiSet.setMultiplicity("black", new Integer(tokens));
			((mxGraphModel) getGraph().getModel()).execute(new TokenChange((PNGraph) getGraph(), cell.getId(), multiSet));
		} catch (ParameterException ex) {
			JOptionPane.showMessageDialog(PTGraphComponent.this, "Input is not a positive integer.", "Invalid parameter", JOptionPane.ERROR_MESSAGE);
		}
		return true;
		}
		return false;
	}

	@Override
	protected boolean doubleClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
		String weight = JOptionPane.showInputDialog(PTGraphComponent.this, "Input new arc weight");
		if(weight != null){
		try {
			Validate.positiveInteger(weight);
		} catch (ParameterException ex) {
			JOptionPane.showMessageDialog(PTGraphComponent.this, "Input is not a positive integer.", "Invalid parameter", JOptionPane.ERROR_MESSAGE);
		}

		if (weight != null) {
			try {
				getGraph().getPNProperties().setArcWeight(this, cell.getId(), weight);
			} catch (ParameterException e2) {
				JOptionPane.showMessageDialog(PTGraphComponent.this, "Cannot set arc weight.\n Reason: " + e2.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
		return true;
		}
		return false;
	}

	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		isExecution = getGraph().isExecution();
		if (!isExecution)
			((mxGraphModel) getGraph().getModel()).execute(new TokenMouseWheelChange((PNGraph) getGraph(), cell, e.getWheelRotation()));

		return true;
	}

	/**
	 * Resets the control points of the given edge.
	 */
	public Object resetEdge(Object edge) {
		mxGeometry geo = getGraph().getModel().getGeometry(edge);

		if (geo != null) {
			// Resets the control points
			List<mxPoint> points = geo.getPoints();

			if (points != null && !points.isEmpty()) {
				geo = (mxGeometry) geo.clone();
				geo.setPoints(null);
				getGraph().getModel().setGeometry(edge, geo);
			}
		}

		return edge;
	}

}
