package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class WFNetCheckLabel extends PNPropertyCheckLabel<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2534069643060024803L;

	public WFNetCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	@Override
	protected void startExecutor() throws Exception {
		WFNetProperties result = WFNetChecker.checkWFNetStructure((PTNet) editorComponent.getNetContainer().getPetriNet());
		editorComponent.getPropertyCheckView().updateFieldContent(result, result.exception);
//		editorComponent.getPropertyCheckView().setUpGui();
//		editorComponent.getPropertyCheckView().updateFieldContent(result, result.exception);

		switch (result.hasWFNetStructure) {
		case FALSE:
			setPropertyHolds(false);
			setGraphicsFinished();
			break;
		case TRUE:
			setPropertyHolds(true);
			setGraphicsFinished();
			break;
		case UNKNOWN:
			setGraphicsCancelled();
			break;
		default:
			break;
		}
	}

	@Override
	protected void setGraphicsFinished() {
		super.setGraphicsFinished();
	}

	@Override
	protected void setPropertyHolds(Boolean result) {
		this.propertyHolds = result;
	}

	@Override
	protected AbstractThreadedPNPropertyChecker<?, ?, ?, ?, ?, ?, Boolean, ?> createNewExecutor() {
		return null;
	}

}
