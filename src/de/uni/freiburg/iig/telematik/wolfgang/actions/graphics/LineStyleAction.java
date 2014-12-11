package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

@SuppressWarnings("serial")
public class LineStyleAction extends AbstractPNEditorGraphicsAction {
	private Image dot;
	private Image dash;
	private Image solid;
	int iterator = 1;

	public LineStyleAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Line", IconFactory.getIcon("solid"));
		solid = getIcon().getImage();
		dash = IconFactory.getIcon("dash").getImage();
		dot = IconFactory.getIcon("dot").getImage();
	}

	public void setDashIconImage() {
		getIcon().setImage(dash);

	}

	public void setDotconImage() {
		getIcon().setImage(dot);

	}

	public void setSolidIconImage() {
		getIcon().setImage(solid);
	}

	@Override
	protected void performLabelAction() {
		if (getIcon().getImage() == dot) {

			getGraph().setCellStyles(MXConstants.LABEL_LINE_STYLE, "solid");

			getIcon().setImage(solid);
		} else if (getIcon().getImage() == solid) {
			getGraph().setCellStyles(MXConstants.LABEL_LINE_STYLE, "dash");
			getIcon().setImage(dash);
		} else if (getIcon().getImage() == dash) {

			getGraph().setCellStyles(MXConstants.LABEL_LINE_STYLE, "dot");
			getIcon().setImage(dot);
		}
	}

	@Override
	protected void performNoLabelAction() {
		if (getIcon().getImage() == dot) {

			getGraph().setCellStyles(MXConstants.LINE_STYLE, "solid");

			getIcon().setImage(solid);
		} else if (getIcon().getImage() == solid) {

			getGraph().setCellStyles(MXConstants.LINE_STYLE, "dash");
			getIcon().setImage(dash);
		} else if (getIcon().getImage() == dash) {

			getGraph().setCellStyles(MXConstants.LINE_STYLE, "dot");
			getIcon().setImage(dot);
		}
	}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub

	}

}