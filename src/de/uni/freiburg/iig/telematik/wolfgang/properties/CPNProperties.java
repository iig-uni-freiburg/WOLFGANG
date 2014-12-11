package de.uni.freiburg.iig.telematik.wolfgang.properties;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;

public class CPNProperties extends PNProperties {
	
	/**
	 * Creates a new PTProperties object with the given graphical P/T-Net.
	 * @param graphicalCPN The graphical P/T-Net to use.
	 * @ ParameterException If the given net container is <code>null</code>.
	 */
	public CPNProperties(GraphicalCPN graphicalCPN) {
		super(graphicalCPN);
	}
	
	//------- Net Container -------------------------------------------------------------------------
	
	/**
	 * Overrides super{@link #getNetContainer()} and returns a {@link GraphicalCPN}.
	 */
	@Override
	protected GraphicalCPN getNetContainer() {
		return (GraphicalCPN) super.getNetContainer();
	}
	
	
	//------- Arcs ----------------------------------------------------------------------------------
	
	/**
	 * Overrides super{@link #getArcProperties()} and adds property {@link PNProperty#ARC_WEIGHT}.
	 */
	@Override
	public Set<PNProperty> getArcProperties(){
		Set<PNProperty> result = super.getArcProperties();
		result.add(PNProperty.ARC_WEIGHT);
		return result;
	}
	
	/**
	 * Overrides super{@link #getArcProperty(String, PNProperty)} and additionally considers property {@link PNProperty#ARC_WEIGHT}.<br>
	 * This method calls {@link #getArcWeight(String)}
	 * @see #getArcWeight(String)
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected String getArcProperty(String name, PNProperty property)  {
		String superResult = super.getArcProperty(name, property);
		if(superResult == null){
//			switch (property) {
//			case ARC_WEIGHT:
//				return getArcWeight(name).toString();
//			}
			return null;
		} else {
			return superResult;
		}
	}

	/**
	 * Overrides super{@link #setArcProperty(Object, String, PNProperty, String)} and additionally considers property {@link PNProperty#ARC_WEIGHT}.<br>
	 * This method calls {@link #setArcWeight(Object, String, String)}.
	 * @see #setArcWeight(Object, String, String).
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected boolean setArcProperty(Object sender, String name, PNProperty property, String value)  {
		if(!super.setArcProperty(sender, name, property, value)){
			switch (property) {
			case ARC_WEIGHT:
//				setArcWeight(sender, name, value);
				return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
//	/**
//	 * Returns an arc weight.
//	 * @param arcName The name of the arc whose weight is requested.
//	 * @return The weight of the arc with the given name.
//	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
//	 */
//	public Integer getArcWeight(String arcName)  {
//		Validate.notNull(arcName);
//		if(!getNetContainer().getPetriNet().containsFlowRelation(arcName))
//			throw new ParameterException("Unknown Arc");
//		return getNetContainer().getPetriNet().getFlowRelation(arcName).getWeight();
//	}
	
	/**
	 * Sets the weight of an arc.<br>
	 * This method notifies all listeners about the arc weight change.<br>
	 * Note: The sender of the change request is not notified!
	 * @param sender The object which is requesting an arc weight change.
	 * @param arcName The name of the arc whose weight is changed.
	 * @param wight The new weight for the arc.
	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 * @see #setArcWeight(Object, String, Integer)
	 */
//	public void setArcWeight(Object sender, String arcName, String weight) {
//		Validate.positiveInteger(weight);
//		setArcWeight(sender, arcName, Integer.parseInt(weight));
//	}
	
//	/**
//	 * Sets the weight of an arc.<br>
//	 * This method notifies all listeners about the arc weight change.<br>
//	 * Note: The sender of the change request is not notified!
//	 * @param sender The object which is requesting an arc weight change.
//	 * @param arcName The name of the arc whose weight is changed.
//	 * @param wight The new weight for the arc.
//	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
//	 */
//	public void setArcWeight(Object sender, String arcName, Integer weight) {
//		Validate.notNull(arcName);
//		Validate.notNull(weight);
//		Validate.bigger(weight, 0);
//		if(!getNetContainer().getPetriNet().containsFlowRelation(arcName))
//			throw new ParameterException("Unknown Arc");
//		
//		Integer oldWeight = getArcWeight(arcName);
//		getNetContainer().getPetriNet().getFlowRelation(arcName).setWeight(weight);
//		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.ARC, arcName, PNProperty.ARC_WEIGHT, oldWeight , weight);
//		changeSupport.fireChangeEvent(this, event);
//	}

	@Override
	public Set<PNProperty> getPlaceProperties() {
		Set<PNProperty> result =  super.getPlaceProperties();
		result.add(PNProperty.PLACE_CAPACITY);
		return result;
	}

	@Override
	protected String getPlaceProperty(String name, PNProperty property)
			 {
		Validate.notNull(property);
		String result = super.getPlaceProperty(name, property);
		switch(property){
		case PLACE_CAPACITY:
			return getPlaceCapacity(name).toString();
		}
		return result;
	}

	/**
	 * Returns an arc weight.
	 * @param placeName The name of the arc whose weight is requested.
	 * @return The weight of the arc with the given name.
	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 */
	public Integer getPlaceCapacity(String placeName)  {
		Validate.notNull(placeName);
		if(!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		return getNetContainer().getPetriNet().getPlace(placeName).getCapacity();
	}
	
	/**
	 * Sets the weight of an arc.<br>
	 * This method notifies all listeners about the arc weight change.<br>
	 * Note: The sender of the change request is not notified!
	 * @param sender The object which is requesting an arc weight change.
	 * @param arcName The name of the arc whose weight is changed.
	 * @param wight The new weight for the arc.
	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 * @see #setArcWeight(Object, String, Integer)
	 */
	public void setPlaceCapacity(Object sender, String arcName, String weight) {
		Validate.positiveInteger(weight);
		setPlaceCapacity(sender, arcName, Integer.parseInt(weight));
	}
	
	/**
	 * Sets the weight of an arc.<br>
	 * This method notifies all listeners about the arc weight change.<br>
	 * Note: The sender of the change request is not notified!
	 * @param sender The object which is requesting an arc weight change.
	 * @param placeName The name of the arc whose weight is changed.
	 * @param wight The new weight for the arc.
	 * @ If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 */
	public void setPlaceCapacity(Object sender, String placeName, Integer capacity) {
		Validate.notNull(placeName);
		Validate.notNull(capacity);
		Validate.bigger(capacity, -2);
		if(!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		
		Integer oldWeight = getPlaceCapacity(placeName);
		getNetContainer().getPetriNet().getPlace(placeName).setCapacity(capacity);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.PLACE, placeName, PNProperty.PLACE_CAPACITY, oldWeight , capacity);
		changeSupport.fireChangeEvent(this, event);
	}

	@Override
	protected boolean setPlaceProperty(Object sender, String name,
			PNProperty property, String value)  {
		// TODO Auto-generated method stub
		boolean result = super.setPlaceProperty(sender, name, property, value);
		switch(property){
		case PLACE_CAPACITY:
			setPlaceCapacity(sender, name, value);
			return true;
			}
		return result;
	}
	
	
	
}
