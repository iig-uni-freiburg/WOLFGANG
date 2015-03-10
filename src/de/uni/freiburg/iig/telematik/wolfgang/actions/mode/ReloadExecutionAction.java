package de.uni.freiburg.iig.telematik.wolfgang.actions.mode;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class ReloadExecutionAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7716993627349722001L;

	public ReloadExecutionAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Exeuctin", IconFactory.getIcon("restart"));

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getGraphComponent().getGraph().getNetContainer().getPetriNet().reset();
		getEditor().getGraphComponent().getGraph().refresh();
		getEditor().getEditorToolbar().setExecutionMode();
	}

}
