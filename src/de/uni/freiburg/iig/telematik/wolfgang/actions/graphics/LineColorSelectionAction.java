package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JColorChooser;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class LineColorSelectionAction extends AbstractPNEditorGraphicsAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5429471026589821624L;

	public LineColorSelectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "FillColor", IconFactory.getIcon("fill"));
		setLineColor(EditorProperties.getInstance().getDefaultLineColor(), MXConstants.DEFAULT_LINE_WIDTH, MXConstants.DEFAULT_LINE_STYLE, false);
	}

	@Override
	protected void performLabelAction() {
		Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) {
			getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));
		}
	}

	@Override
	protected void performNoLabelAction() {
		Color newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) {
			getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));
		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {}
}
