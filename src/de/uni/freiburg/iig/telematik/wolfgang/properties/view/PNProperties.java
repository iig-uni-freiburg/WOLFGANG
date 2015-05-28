package de.uni.freiburg.iig.telematik.wolfgang.properties.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

/**
 * This class allows to change properties of an underlying Petri net<br>
 * and allows to add listeners that are notified about changes.<br>
 * <br>
 * All functionality ids defined in an abstract way.<br>
 * Subclasses have to override methods to add specific behavior<br>
 * dependent on the concrete Petri net type (P/T-Net, CPN, IFNet).
 * 
 * @author Thomas Stocker
 */
public abstract class PNProperties {

	private static final Integer MAX_NODE_SIZE = 501;
	/**
	 * The change support which allows to notify all registered listeners about
	 * changes.
	 */
	protected PNChangeSupport changeSupport = new PNChangeSupport();
	/**
	 * The net container, where changes are applied on.<br>
	 * Note: This field is private, because subclasses should use
	 * {@link #getNetContainer()}, which returns a net container of the expected
	 * type.
	 */
	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = null;
	private PropertiesView propertiesView;

	/**
	 * Creates a new PNProperties object with the given net container.<br>
	 * The net container is used as underlying Petri net where changes are
	 * applied on.<br>
	 * Subclasses should provide constructors with adjusted net container types<br>
	 * based on the concrete Petri net type they consider.
	 * 
	 * @param netContainer
	 *            The net container. @ If the given net container is
	 *            <code>null</code>.
	 */
	public PNProperties(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer) {
		Validate.notNull(netContainer);
		this.netContainer = netContainer;
	}

	// ------- Net Container
	// -------------------------------------------------------------------------

	/**
	 * Returns the net container.<br>
	 * Subclasses should override this method and adjust the return type based
	 * on the concrete net container type.
	 * 
	 * @return The net container.
	 */
	protected AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		return netContainer;
	}

	// ------- Places
	// --------------------------------------------------------------------------------

	/**
	 * Returns the names of all places.<br>
	 * This is especially helpful for the {@link PropertiesView} to create
	 * enough editing fields.
	 * 
	 * @return A sorted list of all place names.
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getPlaceNames() {
		List<String> result = new ArrayList<String>();
		for (AbstractPlace place : getNetContainer().getPetriNet().getPlaces()) {
			result.add(place.getName());
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns all place properties which are managed by this properties class.<br>
	 * The abstract implementation returns {@link PNProperty#PLACE_LABEL} and
	 * {@link PNProperty#PLACE_SIZE}.<br>
	 * Subclasses may override this method to add net specific properties.
	 * 
	 * @return A set of PN properties.
	 * @see PNProperty
	 */
	public Set<PNProperty> getPlaceProperties() {
		Set<PNProperty> result = new HashSet<PNProperty>();
		result.add(PNProperty.PLACE_LABEL);
		result.add(PNProperty.PLACE_SIZE);
		result.add(PNProperty.PLACE_POSITION_X);
		result.add(PNProperty.PLACE_POSITION_Y);
		return result;
	}

	/**
	 * Returns the value of a place property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for transitions and arcs.<br>
	 * The abstract implementation returns values for
	 * {@link PNProperty#PLACE_LABEL} and {@link PNProperty#PLACE_SIZE}.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getPlaceProperties()}.
	 * 
	 * @param name
	 *            The name of the place whose property is requested.
	 * @param property
	 *            The property which is requested.
	 * @return The value of the desired property of the place with the given
	 *         name or<br>
	 *         <code>null</code> if this property class does not contain a value
	 *         for the desired property wrt to the given place. @ If one of the
	 *         given parameters is <code>null</code> or the net does not contain
	 *         a place with the given name.
	 * @see PNProperty
	 */
	@SuppressWarnings("incomplete-switch")
	protected String getPlaceProperty(String name, PNProperty property) {
		Validate.notNull(property);
		switch (property) {
		case PLACE_LABEL:
			return getPlaceLabel(name);
		case PLACE_SIZE:
			return getPlaceSize(name).toString();
		case PLACE_POSITION_X:
			return getPlacePositionX(name).toString();
		case PLACE_POSITION_Y:
			return getPlacePositionY(name).toString();
		}
		return null;
	}

	/**
	 * Sets the value of a place property.<br>
	 * Based on the given property, the method chooses an appropriate method for
	 * changing the property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for transitions and arcs.<br>
	 * The abstract implementation sets values for
	 * {@link PNProperty#PLACE_LABEL} and {@link PNProperty#PLACE_SIZE}.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getPlaceProperties()}.<br>
	 * <br>
	 * This method notifies all listeners about the property change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a property change.
	 * @param name
	 *            The name of the place whose property is changed.
	 * @param property
	 *            The property which is changed.
	 * @param value
	 *            The new value for the place property.
	 * @return <code>true</code> if the value was successfully set;<br>
	 *         <code>false</code> otherwise. @ If the given place name is
	 *         <code>null</code> or the net does not contain a place with the
	 *         given name.
	 */
	@SuppressWarnings("incomplete-switch")
	protected boolean setPlaceProperty(Object sender, String name, PNProperty property, String value) {
		switch (property) {
		case PLACE_LABEL:
			setPlaceLabel(sender, name, value);
			return true;
		case PLACE_SIZE:
			setPlaceSize(sender, name, value);
			return true;
		case PLACE_POSITION_X:
			setPlacePositionX(
					sender,
					name,
					getValueInCanvas(
							value, 
							getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(name).getDimension().getX(), 
							getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(name).getPosition().getX()));
			return true;
		case PLACE_POSITION_Y:
			setPlacePositionY(
							sender,
							name,
							getValueInCanvas(
									value, 
									getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(name).getDimension().getY(), 
									getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(name).getPosition().getY()));
			return true;
		}
		return false;
	}

	/**
	 * Returns the label of a place.
	 * 
	 * @param placeName
	 *            The name of the place whose label is requested.
	 * @return The label of the place with the given name. @ If the given place
	 *         name is <code>null</code> or the net does not contain a place
	 *         with the given name.
	 */
	public String getPlaceLabel(String placeName) {
		Validate.notNull(placeName);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		return getNetContainer().getPetriNet().getPlace(placeName).getLabel();
	}

	/**
	 * Sets the label of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a label change.
	 * @param placeName
	 *            The name of the place whose label is changed.
	 * @param label
	 *            The new label for the place. @ ParameterException If the given
	 *            place name is <code>null</code> or the net does not contain a
	 *            place with the given name.
	 */
	public void setPlaceLabel(Object sender, String placeName, String label) {
		Validate.notNull(label);
		Validate.notNull(placeName);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		String oldLabel = getNetContainer().getPetriNet().getPlace(placeName).getLabel();
		getNetContainer().getPetriNet().getPlace(placeName).setLabel(label);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.PLACE, placeName, PNProperty.PLACE_LABEL, oldLabel, label);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Returns the size of a place.
	 * 
	 * @param placeName
	 *            The name of the place whose size is requested.
	 * @return The size of the place with the given name. @ If the given place
	 *         name is <code>null</code> or the net does not contain a place
	 *         with the given name.
	 */
	public Integer getPlaceSize(String placeName) {
		Validate.notNull(placeName);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		return (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
	}

	/**
	 * Returns the size of a place.
	 * 
	 * @param placeName
	 *            The name of the place whose size is requested.
	 * @return The size of the place with the given name. @ If the given place
	 *         name is <code>null</code> or the net does not contain a place
	 *         with the given name.
	 */
	public Integer getPlacePositionX(String placeName) {
		Validate.notNull(placeName);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		return (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().getX();
	}

	/**
	 * Returns the size of a place.
	 * 
	 * @param placeName
	 *            The name of the place whose size is requested.
	 * @return The size of the place with the given name. @ If the given place
	 *         name is <code>null</code> or the net does not contain a place
	 *         with the given name.
	 */
	public Integer getPlacePositionY(String placeName) {
		Validate.notNull(placeName);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		return (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().getY();
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param size
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 * @see {@link #setPlaceSize(Object, String, Integer)}
	 */
	public void setPlaceSize(Object sender, String placeName, String size) {
		Validate.positiveInteger(size);
		setPlaceSize(sender, placeName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param size
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 */
	public void setPlaceSize(Object sender, String placeName, Integer size) {
		Validate.notNull(placeName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		Validate.smaller(size, MAX_NODE_SIZE);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().getX();
		getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().setX(size);
		getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getDimension().setY(size);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.PLACE, placeName, PNProperty.PLACE_SIZE, oldSize, size);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param size
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 * @see {@link #setPlaceSize(Object, String, Integer)}
	 */
	public void setPlacePositionX(Object sender, String placeName, String size) {
		Validate.positiveInteger(size);
		setPlacePositionX(sender, placeName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param position
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 */
	public void setPlacePositionX(Object sender, String placeName, Integer position) {
		Validate.notNull(placeName);
		Validate.notNull(position);
		Validate.bigger(position, -1);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");
		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().getX();
		getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().setX(position);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.PLACE, placeName, PNProperty.PLACE_POSITION_X, oldSize, position);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param size
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 * @see {@link #setPlaceSize(Object, String, Integer)}
	 */
	public void setPlacePositionY(Object sender, String placeName, String size) {
		Validate.positiveInteger(size);
		setPlacePositionY(sender, placeName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a place.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param placeName
	 *            The name of the place whose size is changed.
	 * @param position
	 *            The new size for the place. @ If the given place size is
	 *            invalid or the net does not contain a place with the given
	 *            name.
	 */
	public void setPlacePositionY(Object sender, String placeName, Integer position) {
		Validate.notNull(placeName);
		Validate.notNull(position);
		Validate.bigger(position, -1);
		if (!getNetContainer().getPetriNet().containsPlace(placeName))
			throw new ParameterException("Unknown Place");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().getY();
		getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(placeName).getPosition().setY(position);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.PLACE, placeName, PNProperty.PLACE_POSITION_Y, oldSize, position);
		changeSupport.fireChangeEvent(this, event);
	}

	// ------- Transitions
	// ---------------------------------------------------------------------------

	/**
	 * Returns the names of all transitions.<br>
	 * This is especially helpful for the {@link PropertiesView} to create
	 * enough editing fields.
	 * 
	 * @return A sorted list of all transition names.
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getTransitionNames() {
		List<String> result = new ArrayList<String>();
		for (AbstractTransition transition : getNetContainer().getPetriNet().getTransitions()) {
			result.add(transition.getName());
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns all transition properties which are managed by this properties
	 * class.<br>
	 * The abstract implementation returns {@link PNProperty.TRANSITION_LABEL}
	 * and {@link PNProperty.TRANSITION_SIZE}.<br>
	 * Subclasses may override this method to add net specific properties.
	 * 
	 * @return A set of PN properties.
	 * @see PNProperty
	 */
	public Set<PNProperty> getTransitionProperties() {
		Set<PNProperty> result = new HashSet<PNProperty>();
		result.add(PNProperty.TRANSITION_LABEL);
		result.add(PNProperty.TRANSITION_SIZE_X);
		result.add(PNProperty.TRANSITION_SIZE_Y);
		result.add(PNProperty.TRANSITION_POSITION_X);
		result.add(PNProperty.TRANSITION_POSITION_Y);
		return result;
	}

	/**
	 * Returns the value of a transition property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for places and arcs.<br>
	 * The abstract implementation returns values for
	 * {@link PNProperty.TRANSITION_LABEL} and
	 * {@link PNProperty.TRANSITION_SIZE}.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getTransitionProperties()}.
	 * 
	 * @param name
	 *            The name of the transition whose property is requested.
	 * @param property
	 *            The property which is requested.
	 * @return The value of the desired property of the transition with the
	 *         given name or<br>
	 *         <code>null</code> if this property class does not contain a value
	 *         for the desired property wrt to the given transition. @ If one of
	 *         the given parameters is <code>null</code> or the net does not
	 *         contain a transition with the given name.
	 * @see PNProperty
	 */
	@SuppressWarnings("incomplete-switch")
	protected String getTransitionProperty(String name, PNProperty property) {
		switch (property) {
		case TRANSITION_LABEL:
			return getTransitionLabel(name);
		case TRANSITION_SIZE_X:
			return getTransitionSizeX(name).toString();
		case TRANSITION_SIZE_Y:
			return getTransitionSizeY(name).toString();
		case TRANSITION_POSITION_X:
			return getTransitionPositionX(name).toString();
		case TRANSITION_POSITION_Y:
			return getTransitionPositionY(name).toString();
		}
		return null;
	}

	/**
	 * Sets the value of a transition property.<br>
	 * Based on the given property, the method chooses an appropriate method for
	 * changing the property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for places and arcs.<br>
	 * The abstract implementation sets values for
	 * {@link PNProperty.TRANSITION_LABEL} and
	 * {@link PNProperty.TRANSITION_SIZE}.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getTransitionProperties()}.<br>
	 * <br>
	 * This method notifies all listeners about the property change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a property change.
	 * @param name
	 *            The name of the transition whose property is changed.
	 * @param property
	 *            The property which is changed.
	 * @param value
	 *            The new value for the transition property.
	 * @return <code>true</code> if the value was successfully set;<br>
	 *         <code>false</code> otherwise. @ If the given transition name is
	 *         <code>null</code> or the net does not contain a transition with
	 *         the given name.
	 */
	@SuppressWarnings("incomplete-switch")
	protected boolean setTransitionProperty(Object sender, String name, PNProperty property, String value) {
		NodeGraphics nodeGraphics;
		switch (property) {
		case TRANSITION_LABEL:
			setTransitionLabel(sender, name, value);
			return true;
		case TRANSITION_SIZE_X:
			setTransitionSizeX(sender, name, value);
			return true;
		case TRANSITION_SIZE_Y:
			setTransitionSizeY(sender, name, value);
			return true;
		case TRANSITION_POSITION_X:
			setTransitionPositionX(
					sender,
					name,
					getValueInCanvas(
							value, 
							getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(name).getDimension().getX(), 
							getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(name).getPosition().getX()));
			return true;
		case TRANSITION_POSITION_Y:
			setTransitionPositionY(
					sender,
					name,
					getValueInCanvas(
							value, 
							getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(name).getDimension().getY(), 
							getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(name).getPosition().getY()));
			return true;
		}
		return false;
	}

	private String getValueInCanvas(String value, double dim, double position) {
		double minValue = dim / 2;
		if (new Integer(value) < minValue) {
			value = position + "";
		}
		return value;
	}

	/**
	 * Returns the label of a transition.
	 * 
	 * @param transitionName
	 *            The name of the transition whose label is requested.
	 * @return The label of the transition with the given name. @ If the given
	 *         transition name is <code>null</code> or the net does not contain
	 *         a transition with the given name.
	 */
	public String getTransitionLabel(String transitionName) {
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		return getNetContainer().getPetriNet().getTransition(transitionName).getLabel();
	}

	/**
	 * Sets the label of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a label change.
	 * @param transitionName
	 *            The name of the transition whose label is changed.
	 * @param label
	 *            The new label for the transition. @ ParameterException If the
	 *            given transition name is <code>null</code> or the net does not
	 *            contain a transition with the given name.
	 */
	public void setTransitionLabel(Object sender, String transitionName, String label) {
		Validate.notNull(label);
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		String oldLabel = getNetContainer().getPetriNet().getTransition(transitionName).getLabel();
		getNetContainer().getPetriNet().getTransition(transitionName).setLabel(label);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.TRANSITION, transitionName, PNProperty.TRANSITION_LABEL, oldLabel, label);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Returns the size of a transition.
	 * 
	 * @param transitionName
	 *            The name of the transition whose size is requested.
	 * @return The size of the transition with the given name. @ If the given
	 *         transition name is <code>null</code> or the net does not contain
	 *         a transition with the given name.
	 */
	public Integer getTransitionSizeX(String transitionName) {
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		return (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
	}

	/**
	 * Returns the size of a transition.
	 * 
	 * @param transitionName
	 *            The name of the transition whose size is requested.
	 * @return The size of the transition with the given name. @ If the given
	 *         transition name is <code>null</code> or the net does not contain
	 *         a transition with the given name.
	 */
	public Integer getTransitionSizeY(String transitionName) {
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		return (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getY();
	}

	/**
	 * Returns the size of a transition.
	 * 
	 * @param transitionName
	 *            The name of the transition whose size is requested.
	 * @return The size of the transition with the given name. @ If the given
	 *         transition name is <code>null</code> or the net does not contain
	 *         a transition with the given name.
	 */
	public Integer getTransitionPositionX(String transitionName) {
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		return (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().getX();
	}

	/**
	 * Returns the size of a transition.
	 * 
	 * @param transitionName
	 *            The name of the transition whose size is requested.
	 * @return The size of the transition with the given name. @ If the given
	 *         transition name is <code>null</code> or the net does not contain
	 *         a transition with the given name.
	 */
	public Integer getTransitionPositionY(String transitionName) {
		Validate.notNull(transitionName);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");
		return (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().getY();
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 * @see {@link #setTransitionSize(Object, String, Integer)}
	 */
	public void setTransitionSizeX(Object sender, String transitionName, String size) {
		Validate.positiveInteger(size);
		setTransitionSizeX(sender, transitionName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 */
	public void setTransitionSizeX(Object sender, String transitionName, Integer size) {
		Validate.notNull(transitionName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		Validate.smaller(size, MAX_NODE_SIZE);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getX();
		getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().setX(size);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.TRANSITION, transitionName, PNProperty.TRANSITION_SIZE_X, oldSize, size);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 * @see {@link #setTransitionSize(Object, String, Integer)}
	 */
	public void setTransitionSizeY(Object sender, String transitionName, String size) {
		Validate.positiveInteger(size);
		setTransitionSizeY(sender, transitionName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 */
	public void setTransitionSizeY(Object sender, String transitionName, Integer size) {
		Validate.notNull(transitionName);
		Validate.notNull(size);
		Validate.bigger(size, 0);
		Validate.smaller(size, MAX_NODE_SIZE);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().getY();
		getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getDimension().setY(size);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.TRANSITION, transitionName, PNProperty.TRANSITION_SIZE_Y, oldSize, size);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 * @see {@link #setTransitionSize(Object, String, Integer)}
	 */
	public void setTransitionPositionX(Object sender, String transitionName, String size) {
		Validate.positiveInteger(size);
		setTransitionPositionX(sender, transitionName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param position
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 */
	public void setTransitionPositionX(Object sender, String transitionName, Integer position) {
		Validate.notNull(transitionName);
		Validate.notNull(position);
		Validate.bigger(position, -1);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().getX();
		getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().setX(position);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.TRANSITION, transitionName, PNProperty.TRANSITION_POSITION_X, oldSize, position);
		changeSupport.fireChangeEvent(this, event);
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param size
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 * @see {@link #setTransitionSize(Object, String, Integer)}
	 */
	public void setTransitionPositionY(Object sender, String transitionName, String size) {
		Validate.positiveInteger(size);
		setTransitionPositionY(sender, transitionName, Integer.parseInt(size));
	}

	/**
	 * Sets the size of a transition.<br>
	 * This method notifies all listeners about the label change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a size change.
	 * @param transitionName
	 *            The name of the transition whose size is changed.
	 * @param position
	 *            The new size for the transition. @ If the given transition
	 *            size is invalid or the net does not contain a transition with
	 *            the given name.
	 */
	public void setTransitionPositionY(Object sender, String transitionName, Integer position) {
		Validate.notNull(transitionName);
		Validate.notNull(position);
		Validate.bigger(position, -1);
		if (!getNetContainer().getPetriNet().containsTransition(transitionName))
			throw new ParameterException("Unknown Transition");

		Integer oldSize = (int) getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().getY();
		getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(transitionName).getPosition().setY(position);
		PNPropertyChangeEvent event = new PNPropertyChangeEvent(sender, PNComponent.TRANSITION, transitionName, PNProperty.TRANSITION_POSITION_Y, oldSize, position);
		changeSupport.fireChangeEvent(this, event);
	}

	// ------- Arcs
	// ----------------------------------------------------------------------------------

	/**
	 * Returns the names of all arcs.<br>
	 * This is especially helpful for the {@link PropertiesView} to create
	 * enough editing fields.
	 * 
	 * @return A sorted list of all arc names.
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getArcNames() {
		List<String> result = new ArrayList<String>();
		for (AbstractFlowRelation arc : getNetContainer().getPetriNet().getFlowRelations()) {
			result.add(arc.getName());
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns all transition properties which are managed by this properties
	 * class.<br>
	 * The abstract implementation returns an empty set.<br>
	 * Subclasses may override this method to add net specific properties (e.g.
	 * arc weight).
	 * 
	 * @return A set of PN properties.
	 * @see PNProperty
	 */
	public Set<PNProperty> getArcProperties() {
		return new HashSet<PNProperty>();
	}

	/**
	 * Returns the value of an arc property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for places and transitions.<br>
	 * The abstract implementation always returns <code>null></code>.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getArcProperties()}.
	 * 
	 * @param name
	 *            The name of the arc whose property is requested.
	 * @param property
	 *            The property which is requested.
	 * @return The value of the desired property of the arc with the given name
	 *         or<br>
	 *         <code>null</code> if this property class does not contain a value
	 *         for the desired property wrt to the given arc. @ If one of the
	 *         given parameters is <code>null</code> or the net does not contain
	 *         an arc with the given name.
	 * @see PNProperty
	 */
	protected String getArcProperty(String name, PNProperty property) {
		return null;
	}

	/**
	 * Sets the value of an arc property.<br>
	 * Based on the given property, the method chooses an appropriate method for
	 * changing the property.<br>
	 * The set of considered PNProperties is necessarily incomplete, since there
	 * are also properties for places and transitions.<br>
	 * The abstract implementation does not set any value and always returns
	 * <code>false</code>.<br>
	 * Generally, the behavior of this method should be synchronized with the
	 * method {@link #getArcProperties()}.<br>
	 * <br>
	 * This method notifies all listeners about the property change.<br>
	 * Note: The sender of the change request is not notified!
	 * 
	 * @param sender
	 *            The object which is requesting a property change.
	 * @param name
	 *            The name of the arc whose property is changed.
	 * @param property
	 *            The property which is changed.
	 * @param value
	 *            The new value for the arc property.
	 * @return <code>true</code> if the value was successfully set;<br>
	 *         <code>false</code> otherwise. @ If the given arc name is
	 *         <code>null</code> or the net does not contain an arc with the
	 *         given name.
	 */
	protected boolean setArcProperty(Object sender, String name, PNProperty property, String value) {
		return false;
	}

	// ------- Value Getting and Setting
	// ------------------------------------------------------------

	public String getValue(PNComponent fieldType, String name, PNProperty property) {
		switch (fieldType) {
		case PLACE:
			return getPlaceProperty(name, property);
		case TRANSITION:
			return getTransitionProperty(name, property);
		case ARC:
			return getArcProperty(name, property);
		}
		return null;
	}

	public void setValue(Object sender, PNComponent fieldType, String name, PNProperty property, String value) {
		switch (fieldType) {
		case PLACE:
			setPlaceProperty(sender, name, property, value);
			break;
		case TRANSITION:
			setTransitionProperty(sender, name, property, value);
			break;
		case ARC:
			setArcProperty(sender, name, property, value);
			break;
		}
	}

	// ------- Listener Support
	// ----------------------------------------------------------------------

	/**
	 * Adds a new {@link PNPropertiesListener}.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addPNPropertiesListener(PNPropertiesListener listener) {
		changeSupport.addListener(listener);
	}

	/**
	 * Removes a {@link PNPropertiesListener}.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removePNPropertiesListener(PNPropertiesListener listener) {
		changeSupport.removeListener(listener);
	}

	// ------- Private classes
	// -----------------------------------------------------------------------

	/**
	 * Enumeration for Petri net components, i.e. places, transitions and arcs.
	 */
	public enum PNComponent {
		PLACE, TRANSITION, ARC;
	}

	public PropertiesView getPropertiesView() {
		return propertiesView;
	}

	public void setPropertiesView(PropertiesView propertiesView) {
		this.propertiesView = propertiesView;
	}

}
