package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.AbstractPropertyCheckAction;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.CPNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CPNToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties;

public class CPNEditorComponent extends AbstractCPNEditorComponent {

	private static final long serialVersionUID = 7463202384539027183L;

	public CPNEditorComponent() {
		super();
	}

	public CPNEditorComponent(GraphicalCPN netContainer) {
		super(netContainer);
	}

	public CPNEditorComponent(GraphicalCPN netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public CPNEditorComponent(GraphicalCPN netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	@Override
	public GraphicalCPN getNetContainer() {
		return (GraphicalCPN) super.getNetContainer();
	}

	@Override
	public GraphicalCPN createNetContainer() {
		return new GraphicalCPN(new CPN(), new CPNGraphics());
	}

	@Override
	protected PNProperties createPNProperties() {
		return new CPNProperties(getNetContainer());
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected CPNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		return new CPNGraphComponent(new CPNGraph(getNetContainer(), getPNProperties()));
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
		return new CPNToolBar(this, JToolBar.HORIZONTAL);
	}

	@Override
	protected void setPropertyChecksUnknown() {
		((CPNToolBar)getEditorToolbar()).getCheckValidityAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor );
//		((CPNToolBar)getEditorToolbar()).getCheckSoundnessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		((CPNToolBar)getEditorToolbar()).getCheckBoundednessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		((CPNToolBar)getEditorToolbar()).getCheckCWNStructureAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		((CPNToolBar)getEditorToolbar()).getCheckCWNSoundnessAction().setFillColor(AbstractPropertyCheckAction.PropertyUnknownColor);
		getPropertyCheckView().updateCWNProperties(new CWNProperties());
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
	protected CWNProperties createPropertyCheckProperties() {
		return new CWNProperties();
	}

}
