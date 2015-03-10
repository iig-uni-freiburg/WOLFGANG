package de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class MakeSilentAction extends AbstractPNEditorAction {
	private Image play;
	private Image reset;

	public MakeSilentAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Exeuctin", IconFactory.getIcon("play"));
		play = getIcon().getImage();
		reset = IconFactory.getIcon("restart").getImage();

	}

	public void setExecutionImage() {
		getIcon().setImage(play);

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getEditorToolbar().setExecutionMode();
		getEditor().getGraphComponent().getGraph().clearSelection();
		getEditor().getGraphComponent().getGraph().setExecution(true);
		if (getIcon().getImage() == play) {
			getEditor().getGraphComponent().highlightEnabledTransitions();
			getIcon().setImage(reset);
		} else if (getIcon().getImage() == reset) {
			getEditor().getGraphComponent().getGraph().getNetContainer().getPetriNet().reset();
			getEditor().getGraphComponent().getGraph().refresh();
			getEditor().getGraphComponent().highlightEnabledTransitions();
		}
	}
}
