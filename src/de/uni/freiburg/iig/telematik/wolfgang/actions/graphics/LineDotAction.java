package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class LineDotAction extends AbstractPNEditorGraphicsAction {

	public LineDotAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "gradient_diagonal", IconFactory.getIcon("dot"));
		setButtonScale(2,3);
		setIconImage(getIcon().getImage());
	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(MXConstants.LABEL_LINE_STYLE, "dot");
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(MXConstants.LINE_STYLE, "dot");
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {}

}