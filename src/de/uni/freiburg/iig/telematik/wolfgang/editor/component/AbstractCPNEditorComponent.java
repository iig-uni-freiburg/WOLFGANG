package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.CPNProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties;

public abstract class AbstractCPNEditorComponent extends PNEditorComponent {

	private static final long serialVersionUID = 7463202384539027183L;

	public AbstractCPNEditorComponent() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public AbstractCPNEditorComponent(AbstractGraphicalCPN netContainer) {
		super(netContainer);
	}

	public AbstractCPNEditorComponent(AbstractGraphicalCPN netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public AbstractCPNEditorComponent(AbstractGraphicalCPN netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	@Override
	protected PNProperties createPNProperties() {
		// TODO: return new CPNProperties(getNetContainer());
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
