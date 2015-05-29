package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.ThreadedBoundednessChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class BoundednessCheckLabel extends PNPropertyCheckLabel<BoundednessCheckResult> {
	
	public BoundednessCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	private static final long serialVersionUID = 7128838444258623686L;

	@Override
	protected ThreadedBoundednessChecker createNewExecutor() {
		return new ThreadedBoundednessChecker(new BoundednessCheckGenerator(editorComponent.getNetContainer().getPetriNet()));
	}

	@Override
	protected void setGraphicsFinished() {
		super.setGraphicsFinished();
	}

	@Override
	protected void setPropertyHolds(BoundednessCheckResult result) {
		this.propertyHolds = result.getBoundedness() == Boundedness.BOUNDED;
	}
	
}
