package de.uni.freiburg.iig.telematik.wolfgang.properties;

import java.util.HashSet;
import java.util.Set;

public class PNChangeSupport {
	
	private Set<PNPropertiesListener> listeners = new HashSet<PNPropertiesListener>();
	
	public void addListener(PNPropertiesListener listener){
		listeners.add(listener);
	} 
	public void removeListener(PNPropertiesListener listener){
		listeners.remove(listener);
	} 
	public void fireChangeEvent(Object source, PNPropertyChangeEvent event){
		for(PNPropertiesListener listener : listeners){
			if(source != listener)
				listener.propertyChange(event);
		}
	}

}
