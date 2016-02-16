package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.PTNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.properties.check.AbstractPropertyCheckView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.check.WFNetPropertyCheckView;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PTProperties;

public class PTNetEditorComponent extends PNEditorComponent {

	private static final long serialVersionUID = -5130690639223735136L;

	public PTNetEditorComponent() {
		super();
	}

	public PTNetEditorComponent(GraphicalPTNet netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public PTNetEditorComponent(GraphicalPTNet netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	public PTNetEditorComponent(GraphicalPTNet netContainer) {
		super(netContainer);
	}

	@Override
	protected GraphicalPTNet createNetContainer() {
		return new GraphicalPTNet(new PTNet(), new PTGraphics());
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties createPNProperties() {
		return new PTProperties(getNetContainer());
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		return new PTGraphComponent(new PTGraph(getNetContainer(), getPNProperties()));

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
	
	

}
