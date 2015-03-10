package de.uni.freiburg.iig.telematik.wolfgang.properties;

import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;

/**
 * This interface defines methods that allow classes to notify listeners about
 * changes in a underlying Petri net.<br>
 * Besides property changes, the interface also defines methods for the
 * notification about structural changes,<br>
 * i.e. added or removed components (places, transitions and arcs).
 * 
 * @author Thomas Stocker
 */
public interface PNPropertiesListener {

	/**
	 * This method notifies a PNPropertiesListener that a PN property was
	 * changed.
	 * 
	 * @param event
	 *            The property change event.
	 * @see PNPropertyChangeEvent
	 */
	public void propertyChange(PNPropertyChangeEvent event);

	/**
	 * This method notifies a PNPropertiesListener that a PN component was
	 * added.<br>
	 * This can be a place, transition or arc.<br>
	 * 
	 * @param component
	 *            The added component.
	 * @param name
	 *            The name of the added component.
	 * @see PNComponent
	 */
	public void componentAdded(PNComponent component, String name);

	/**
	 * This method notifies a PNPropertiesListener that a PN component was
	 * removed.<br>
	 * This can be a place, transition or arc.<br>
	 * 
	 * @param component
	 *            The removed component.
	 * @param name
	 *            The name of the removed component.
	 * @see PNComponent
	 */
	public void componentRemoved(PNComponent component, String name);

}
