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

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenMouseWheelChange;
import de.uni.freiburg.iig.telematik.wolfgang.menu.PTPlaceConfigurerDialog;

public class RTPNGraphComponent extends PNGraphComponent {
	
	private boolean isExecution;

	public RTPNGraphComponent(RTPNGraph graph) {
		super(graph);
	}
	
	@Override
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		getTransitionPopupMenu().show(RTPNGraphComponent.this, pt.x, pt.y);
		return false;
	}
	
	@Override
	public RTPNGraph getGraph() {
		return (RTPNGraph) super.getGraph();
	}
	
	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		PTPlaceConfigurerDialog.showDialog(SwingUtilities.getWindowAncestor(RTPNGraphComponent.this), cell.getId(), getGraph());
		return true;
	}

	@Override
	protected boolean doubleClickOnArcLabel(PNGraphCell cell, MouseEvent e) {
		String weight = JOptionPane.showInputDialog(RTPNGraphComponent.this, "Input new arc weight", "Arc Weight", JOptionPane.QUESTION_MESSAGE);

		if (weight != null) {
			try {
				getGraph().getPNProperties().setArcWeight(this, cell.getId(), weight);
			} catch (ParameterException e2) {
				JOptionPane.showMessageDialog(RTPNGraphComponent.this, "Cannot set arc weight.\n Reason: " + e2.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
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
