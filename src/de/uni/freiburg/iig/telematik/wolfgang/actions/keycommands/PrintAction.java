package de.uni.freiburg.iig.telematik.wolfgang.actions.keycommands;

import java.awt.event.ActionEvent;

import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class PrintAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 8493344159268498517L;

	protected boolean showDialog, success;

	public PrintAction(PNEditorComponent pnEditor) {
		super(pnEditor);
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		success = false;		
		System.out.println("\n//PRINT NET INFORMATION//\n");
		System.out.println(editor.getNetContainer().getPetriNet());
		System.out.println(editor.getNetContainer().getPetriNetGraphics());
		System.out.println("////////////////////////" + editor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getEnabledTransitions());		
	}
}
