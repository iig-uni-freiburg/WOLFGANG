package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class FillGradientRotationDiagonal extends AbstractPNEditorGraphicsAction {

	public FillGradientRotationDiagonal(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_diagonal", IconFactory.getIcon("gradient-diagonal"));
		setButtonScale(3, 3);		
		setIconImage(getIcon().getImage());

	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());

	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, GradientRotation.DIAGONAL.toString());

	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {}

}