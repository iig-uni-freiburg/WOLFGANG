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
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TransitionSilentChange;

public class TransitionSilentAction extends AbstractPNEditorAction {
	private boolean silent;

	public TransitionSilentAction(PNEditorComponent editor, String layoutName, boolean setSilent) throws ParameterException {
		super(editor, layoutName);
		silent = setSilent;
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
				layout = new mxHierarchicalLayout(graph);
			} else if (ident.equals("horizontalHierarchical")) {
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			} else if (ident.equals("verticalTree")) {
				layout = new mxCompactTreeLayout(graph, false);
			} else if (ident.equals("horizontalTree")) {
				layout = new mxCompactTreeLayout(graph, true);
			} else if (ident.equals("parallelEdges")) {
				layout = new mxParallelEdgeLayout(graph);
			} else if (ident.equals("placeEdgeLabels")) {
				layout = new mxEdgeLabelLayout(graph);
			} else if (ident.equals("organicLayout")) {
				layout = new mxOrganicLayout(graph);
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
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		if (selectedCell != null) {
			((mxGraphModel) graph.getModel()).beginUpdate();
			((mxGraphModel) graph.getModel()).execute(new TransitionSilentChange((PNGraph) graph, selectedCell.getId(), silent));
			if (silent) {
				graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#00000");
				graph.setCellStyles(mxConstants.STYLE_NOLABEL, "1");
			} else {
				graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(WolfgangProperties.getInstance().getDefaultTransitionColor()));
				graph.setCellStyles(mxConstants.STYLE_NOLABEL, "0");
			}
			((mxGraphModel) graph.getModel()).endUpdate();
		}

	}

}
