package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public interface PNGraphListener {
	
	public void placeAdded(AbstractPlace place);
	
	public void transitionAdded(AbstractTransition transition);
	
	public void relationAdded(AbstractFlowRelation relation);
	
	public void placeRemoved(AbstractPlace place);
	
	public void transitionRemoved(AbstractTransition transition);
	
	public void relationRemoved(AbstractFlowRelation relation);
	
	public void componentsSelected(Set<PNGraphCell> selectedComponents);

}
