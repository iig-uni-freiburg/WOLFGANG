package de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class LayoutAction extends AbstractPNEditorAction {
	mxIGraphLayout layout;

	public LayoutAction(PNEditorComponent editor, String layoutName, boolean animate) throws ParameterException {
		super(editor, layoutName);
		layout = createLayout(layoutName, animate);
	}

	private static final long serialVersionUID = 1728027231812006823L;

	/**
	 * Creates an action that executes the specified layout.
	 * 
	 * @param key
	 *            Key to be used for getting the label from mxResources and also
	 *            to create the layout instance for the commercial graph editor
	 *            example.
	 * @return an action that executes the specified layout
	 */

	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate) {
		mxIGraphLayout layout = null;

		if (ident != null) {
			mxGraph graph = getEditor().getGraphComponent().getGraph();
			if (ident.equals("verticalHierarchical")) {
				layout = new mxHierarchicalLayout(graph) {

					@Override
					public void execute(Object parent) {
						getEditor().getGraphComponent().getGraph().removeAllArcPoints();
						super.execute(parent);
					}

				};
			} else if (ident.equals("horizontalHierarchical")) {
				layout = new mxHierarchicalLayout(graph, JLabel.WEST) {

					@Override
					public void execute(Object parent) {
						getEditor().getGraphComponent().getGraph().removeAllArcPoints();
						super.execute(parent);
					}

				};
			} else if (ident.equals("verticalTree")) {
				layout = new mxCompactTreeLayout(graph, false);
			} else if (ident.equals("horizontalTree")) {
				layout = new mxCompactTreeLayout(graph, true);
			} else if (ident.equals("parallelEdges")) {
				layout = new mxParallelEdgeLayout(graph);
			} else if (ident.equals("placeEdgeLabels")) {
				layout = new mxEdgeLabelLayout(graph);
			} else if (ident.equals("organicLayout")) {
				layout = new mxOrganicLayout(graph) {

					@Override
					public void execute(Object parent) {
						getEditor().getGraphComponent().getGraph().removeAllArcPoints();
						super.execute(parent);
					}

				};
			}
			if (ident.equals("verticalPartition")) {
				layout = new mxPartitionLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return getEditor().getGraphComponent().getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalPartition")) {
				layout = new mxPartitionLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return getEditor().getGraphComponent().getLayoutAreaSize();
					}
				};
			} else if (ident.equals("verticalStack")) {
				layout = new mxStackLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return getEditor().getGraphComponent().getLayoutAreaSize();
					}
				};
			} else if (ident.equals("horizontalStack")) {
				layout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return getEditor().getGraphComponent().getLayoutAreaSize();
					}
				};
			} else if (ident.equals("circleLayout")) {
				layout = new mxCircleLayout(graph) {

					@Override
					public void execute(Object parent) {
						getEditor().getGraphComponent().getGraph().removeAllArcPoints();
						super.execute(parent);
					}

				};
			}
		}

		return layout;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (layout != null) {
			mxGraph graph = getEditor().getGraphComponent().getGraph();
			Object cell = graph.getSelectionCell();

			if (cell == null || graph.getModel().getChildCount(cell) == 0) {
				cell = graph.getDefaultParent();
			}

			graph.getModel().beginUpdate();
			try {
				long t0 = System.currentTimeMillis();
				layout.execute(cell);
			} finally {
				mxMorphing morph = new mxMorphing(getEditor().getGraphComponent(), 20, 1.2, 20);

				morph.addListener(mxEvent.DONE, new mxIEventListener() {

					public void invoke(Object sender, mxEventObject evt) {
						getEditor().getGraphComponent().getGraph().getModel().endUpdate();
					}

				});

				morph.startAnimation();
			}
		}
	}

}
