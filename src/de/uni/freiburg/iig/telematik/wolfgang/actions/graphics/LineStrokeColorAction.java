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
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class LineStrokeColorAction extends AbstractPNEditorGraphicsAction {
	private Color newColor;

	public LineStrokeColorAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "StokeColor", IconFactory.getIcon("border_color"));
		setFillColor(EditorProperties.getInstance().getDefaultLineColor());
	}


	@Override
	protected void performLabelAction() {
		newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) 
			getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(newColor));
	}

	@Override
	protected void performNoLabelAction() {
		newColor = JColorChooser.showDialog(getEditor().getGraphComponent(), "Stroke Color", null);
		if (newColor != null) 
				getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(newColor));			
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		setFillColor(newColor);
	}
}
