package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar.FillStyle;

public class FillNoFillAction extends AbstractPNEditorGraphicsAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3621631059619148556L;

	public FillNoFillAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "NoFill", IconFactory.getIcon("no_fill"));
		setButtonScale(3, 3);
		setIconImage(getIcon().getImage());
	}

	@Override
	protected void performLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "none");
	}

	@Override
	protected void performNoLabelAction() {
		getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "none");
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().getGraphicsToolbar().setFillStyle(FillStyle.NOFILL);
	}

}
