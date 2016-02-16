package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.io.IOException;

import javax.swing.JPopupMenu;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.wolfgang.graph.RTPNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.RTPNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.RTPNProperties;

public class RTPNEditorComponent extends AbstractRTPNEditorComponent{
	
	private static final long serialVersionUID = -3872996107747717296L;

	public RTPNEditorComponent() {
		super();
	}

	public RTPNEditorComponent(GraphicalTimedNet netContainer) {
		super(netContainer);
	}

	public RTPNEditorComponent(GraphicalTimedNet netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public RTPNEditorComponent(GraphicalTimedNet netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}
	
	@Override
	public EditorPopupMenu getPopupMenu() {
		try {
			return new EditorPopupMenu(this);
		} catch (ParameterException | PropertyException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JPopupMenu getTransitionPopupMenu() {
		try {
			return new TransitionPopupMenu(this);
		} catch (ParameterException | PropertyException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected RTPNGraphComponent createGraphComponent() {
		return new RTPNGraphComponent(new RTPNGraph((GraphicalTimedNet)getNetContainer(), (RTPNProperties) getPNProperties()));

	}
}
