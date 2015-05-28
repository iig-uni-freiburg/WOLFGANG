package de.uni.freiburg.iig.telematik.wolfgang.properties.view;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;

public class IFNetProperties extends PNProperties {
	
	/**
	 * Creates a new PTProperties object with the given graphical P/T-Net.
	 * @param GraphicalIFNet The graphical P/T-Net to use.
	 * @throws ParameterException ParameterException If the given net container is <code>null</code>.
	 */
	public IFNetProperties(GraphicalIFNet GraphicalIFNet) throws ParameterException{
		super(GraphicalIFNet);
	}
	
	//------- Net Container -------------------------------------------------------------------------
	
	/**
	 * Overrides super{@link #getNetContainer()} and returns a {@link GraphicalIFNet}.
	 */
	@Override
	protected GraphicalIFNet getNetContainer() {
		return (GraphicalIFNet) super.getNetContainer();
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
	protected String getArcProperty(String name, PNProperty property) throws ParameterException {
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
	protected boolean setArcProperty(Object sender, String name, PNProperty property, String value) throws ParameterException {
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
//	 * @throws ParameterException If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
//	 */
//	public Integer getArcWeight(String arcName) throws ParameterException {
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
	 * @throws ParameterException If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 * @see #setArcWeight(Object, String, Integer)
	 */

	@Override
	public Set<PNProperty> getPlaceProperties() {
		Set<PNProperty> result =  super.getPlaceProperties();
		result.add(PNProperty.PLACE_CAPACITY);
		return result;
	}

	@Override
	protected String getPlaceProperty(String name, PNProperty property)
			throws ParameterException {
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
	 * @throws ParameterException If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 */
	public Integer getPlaceCapacity(String placeName) throws ParameterException {
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
	 * @throws ParameterException If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 * @see #setArcWeight(Object, String, Integer)
	 */
	public void setPlaceCapacity(Object sender, String arcName, String weight) throws ParameterException{
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
	 * @throws ParameterException If the given arc name is <code>null</code> or the net does not contain an arc with the given name.
	 */
	public void setPlaceCapacity(Object sender, String placeName, Integer capacity) throws ParameterException{
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
			PNProperty property, String value) throws ParameterException {
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
