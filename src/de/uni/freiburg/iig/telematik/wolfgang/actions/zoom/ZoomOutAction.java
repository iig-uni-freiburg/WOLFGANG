package de.uni.freiburg.iig.telematik.wolfgang.actions.zoom;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class ZoomOutAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 7450908146578160638L;
	private mxGraphView view;
	private double currentZoom;
	private double newZoom;

	public ZoomOutAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "Zoom Out", IconFactory.getIcon("zoom_out"));
		view = getEditor().getGraphComponent().getGraph().getView();

	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		currentZoom = view.getScale();
		currentZoom = Math.round(currentZoom*100)/100.0; 
		if (currentZoom > 0 + WolfgangProperties.getInstance().getDefaultZoomStep()) {
			newZoom = currentZoom - WolfgangProperties.getInstance().getDefaultZoomStep();
			newZoom = Math.round(newZoom*100)/100.0;
			getEditor().getGraphComponent().zoomTo(newZoom, getEditor().getGraphComponent().isCenterZoom());
		}
			
		switch(getEditor().getEditorToolbar().getMode()){
		case EDIT:
			break;
		case PLAY:
			getEditor().getGraphComponent().removeCellOverlays();
			getEditor().getGraphComponent().highlightEnabledTransitions();
			break;
		default:
			break;
		}

	}
}