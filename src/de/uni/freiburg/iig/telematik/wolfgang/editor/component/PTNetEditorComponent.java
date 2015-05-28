package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.AbstractPropertyCheckAction;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.PTNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PTProperties;

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
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public JPopupMenu getTransitionPopupMenu() {
		try {
			return new TransitionPopupMenu(this);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new PTNetToolBar(this, JToolBar.HORIZONTAL);
	}
	
	@Override
	protected void setPropertyChecksUnknown() {
		((PTNetToolBar)getEditorToolbar()).getCheckBoundednessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		((PTNetToolBar)getEditorToolbar()).getCheckWFNetAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		getPropertyCheckView().updateWFNetProperties(new WFNetProperties());
	}

	@Override
	public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
		setPropertyChecksUnknown();		
	}

	@Override
	public void placeCapacityChanged(String placeName, String color, int newCapacity) {
		setPropertyChecksUnknown();	
	}

	@Override
	public void constraintChanged(String flowRelation, Multiset constraint) {
		setPropertyChecksUnknown();		
	}

	@Override
	protected NetCheckingProperties createPropertyCheckProperties() {
		return new WFNetProperties();
	}
	
	

}
