package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.ThreadedBoundednessChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.ThreadedMGCalculator;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class BoundednessCheckLabel extends PropertyCheckLabel {
	
	public BoundednessCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	private static final long serialVersionUID = 7128838444258623686L;


	@Override
	protected SingleThreadExecutorService createNewExecutor() {
		return new ThreadedBoundednessChecker(new BoundednessCheckGenerator(editorComponent.getNetContainer().getPetriNet()));
	}

	@Override
	protected void setGraphicsFinished() {
		super.setGraphicsFinished();
	}
	
}
