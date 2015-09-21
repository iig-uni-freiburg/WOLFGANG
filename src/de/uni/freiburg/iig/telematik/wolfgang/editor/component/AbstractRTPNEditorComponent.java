package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import javax.swing.JToolBar;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.PTNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.properties.check.AbstractPropertyCheckView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.check.WFNetPropertyCheckView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.RTPNProperties;

public abstract class AbstractRTPNEditorComponent extends PNEditorComponent{
	
	private static final long serialVersionUID = -2676834058650276940L;


	public AbstractRTPNEditorComponent() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public AbstractRTPNEditorComponent(AbstractGraphicalTimedNet netContainer) {
		super(netContainer);
	}

	public AbstractRTPNEditorComponent(AbstractGraphicalTimedNet netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public AbstractRTPNEditorComponent(AbstractGraphicalTimedNet netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	@Override
	protected RTPNProperties getPNProperties() {
		return (RTPNProperties) super.getPNProperties();
	}
	
	@Override
	protected AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new PTNetToolBar(this, JToolBar.HORIZONTAL);
	}

	@Override
	public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
		resetPropertyCheckView();		
	}

	@Override
	public void placeCapacityChanged(String placeName, String color, int newCapacity) {
		resetPropertyCheckView();
		getPNProperties().setPlaceCapacity(this, placeName, newCapacity);
	}

	@Override
	public void constraintChanged(String flowRelation, Multiset constraint) {
		resetPropertyCheckView();		
	}

	@Override
	protected AbstractPropertyCheckView createPropertyCheckView() {
		WFNetPropertyCheckView wfNetPropertyCheck = new WFNetPropertyCheckView();
		wfNetPropertyCheck.setUpGui();
		return wfNetPropertyCheck;
	}
	
	@Override
	protected GraphicalTimedNet createNetContainer() {
		return new GraphicalTimedNet(new TimedNet(), new TimedNetGraphics());
	}
	
	@Override
	protected RTPNProperties createPNProperties() {
		return new RTPNProperties(getNetContainer());
	}
	
	@Override
	public GraphicalTimedNet getNetContainer() {
		return (GraphicalTimedNet) super.getNetContainer();
	}

}
