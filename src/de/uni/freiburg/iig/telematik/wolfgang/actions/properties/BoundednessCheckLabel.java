package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.uni.freiburg.iig.telematik.sepia.util.ThreadedMGCalculator;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class BoundednessCheckLabel extends PropertyCheckLabel {
	
	private static final long serialVersionUID = 7128838444258623686L;

	public BoundednessCheckLabel(PNEditorComponent editorComponent) {
		super(editorComponent);
	}

	@Override
	protected SingleThreadExecutorService createNewExecutor() {
		return ThreadedMGCalculator.getCalculator(editorComponent.getNetContainer().getPetriNet(), false);
	}

	@Override
	protected void setGraphicsFinished() {
		super.setGraphicsFinished();
	}
	
}
