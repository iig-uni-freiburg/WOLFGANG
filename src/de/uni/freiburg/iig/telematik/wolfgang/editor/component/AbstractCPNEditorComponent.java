package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.wolfgang.properties.CPNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties;

public abstract class AbstractCPNEditorComponent extends PNEditorComponent {

	private static final long serialVersionUID = 7463202384539027183L;

	public AbstractCPNEditorComponent() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public AbstractCPNEditorComponent(AbstractGraphicalCPN netContainer) {
		super(netContainer);
	}

	@Override
	protected PNProperties createPNProperties() {
		//TODO:		return new CPNProperties(getNetContainer());
		return null;
	}

	@SuppressWarnings("rawtypes") 
	protected String getArcConstraint(AbstractFlowRelation relation) {
		// TODO: Do something
		return null;
	}

	@Override
	protected PNProperties getPNProperties() {
		return (CPNProperties) super.getPNProperties();
	}
	
}
