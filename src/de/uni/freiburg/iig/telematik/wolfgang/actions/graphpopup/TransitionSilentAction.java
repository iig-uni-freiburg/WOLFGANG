package de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup;

import java.awt.event.ActionEvent;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TransitionSilentChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;

public class TransitionSilentAction extends AbstractPNEditorAction {
	private boolean silent;

	public TransitionSilentAction(PNEditorComponent editor, String layoutName, boolean setSilent) throws ParameterException {
		super(editor, layoutName);
		silent = setSilent;
	}

	private static final long serialVersionUID = 1728027231812006823L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		PNGraph graph = getEditor().getGraphComponent().getGraph();
		PNGraphCell selectedCell = (PNGraphCell) graph.getSelectionCell();
		Object[] cells = graph.getSelectionCells();
		for (Object cell : cells) {
			if (cell instanceof PNGraphCell) {
				if (((PNGraphCell) cell).getType().equals(PNComponent.TRANSITION)) {
					selectedCell = (PNGraphCell) cell;
					if (selectedCell != null && selectedCell.getType().toString() == "TRANSITION") {
						((mxGraphModel) graph.getModel()).beginUpdate();
						((mxGraphModel) graph.getModel()).execute(new TransitionSilentChange((PNGraph) graph, selectedCell.getId(), silent));
						if (silent) {
							graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#00000", new Object[]{selectedCell});
							graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#00000", new Object[]{selectedCell});
							graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, "#00000", new Object[]{selectedCell});
							graph.setCellStyles(mxConstants.STYLE_NOLABEL, "1", new Object[]{selectedCell});
							graph.setCellStyles(MXConstants.SILENT, "1", new Object[]{selectedCell});
						} else {
							graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(EditorProperties.getInstance().getDefaultTransitionColor()));
							graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(EditorProperties.getInstance().getDefaultTransitionColor()));
							graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(EditorProperties.getInstance().getDefaultLineColor()));
							graph.setCellStyles(mxConstants.STYLE_NOLABEL, "0");
							graph.setCellStyles(MXConstants.SILENT, "0", new Object[]{selectedCell});

						}
						((mxGraphModel) graph.getModel()).endUpdate();
					}
				}
			}
		}

	}

}
