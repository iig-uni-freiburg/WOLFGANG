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
		java.awt.Image img = getIcon().getImage();
		int size = getIcon().getIconWidth();
		java.awt.Image newimg = img.getScaledInstance(size /3, size /3, java.awt.Image.SCALE_SMOOTH);
		getIcon().setImage(newimg);
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
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		
	}


}