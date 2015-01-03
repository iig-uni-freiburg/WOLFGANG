package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.wolfgang.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties;


	public abstract class AbstractIFNetEditorComponent extends PNEditorComponent {

		private static final long serialVersionUID = 7463202384539027183L;

		public AbstractIFNetEditorComponent() {
			super();
		}

		@SuppressWarnings("rawtypes")
		public AbstractIFNetEditorComponent(AbstractGraphicalIFNet netContainer) {
			super(netContainer);
		}
		
		public AbstractIFNetEditorComponent(AbstractGraphicalCPN netContainer, boolean askForLayout) {
			super(netContainer, askForLayout);
		}

		public AbstractIFNetEditorComponent(AbstractGraphicalCPN netContainer, LayoutOption layoutOption) {
			super(netContainer, layoutOption);
		}

		@Override
		protected PNProperties createPNProperties() {
			//TODO:		return new IFNetProperties(getNetContainer());
			return null;
		}

		@SuppressWarnings("rawtypes") 
		protected String getArcConstraint(AbstractFlowRelation relation) {
			// TODO: Do something
			return null;
		}

		@Override
		protected PNProperties getPNProperties() {
			return (IFNetProperties) super.getPNProperties();
		}

}
