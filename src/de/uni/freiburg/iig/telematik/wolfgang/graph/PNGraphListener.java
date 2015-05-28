package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public interface PNGraphListener {
	
	public void placeAdded(AbstractPlace place);
	
	public void transitionAdded(AbstractTransition transition);
	
	public void relationAdded(AbstractFlowRelation relation);
	
	public void placeRemoved(AbstractPlace place);
	
	public void transitionRemoved(AbstractTransition transition);
	
	public void relationRemoved(AbstractFlowRelation relation);
	
	public void markingForPlaceChanged(String placeName, Multiset placeMarking);
	
	public void placeCapacityChanged(String placeName, String color, int newCapacity);
	
	public void constraintChanged(String flowRelation, Multiset constraint);
	
	public void componentsSelected(Set<PNGraphCell> selectedComponents);




}
