package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

@SuppressWarnings("rawtypes")
public class PNGraphListenerSupport {

	private Set<PNGraphListener> listeners = new HashSet<PNGraphListener>();

	public void addPNGraphListener(PNGraphListener listener) {
		listeners.add(listener);
	}

	public void removePNGraphListener(PNGraphListener listener) {
		listeners.remove(listener);
	}

	public void notifyPlaceAdded(AbstractPlace place) {
		for (PNGraphListener listener : listeners) {
			listener.placeAdded(place);
		}
	}

	public void notifyTransitionAdded(AbstractTransition transition) {
		for (PNGraphListener listener : listeners) {
			listener.transitionAdded(transition);
		}
	}

	public void notifyRelationAdded(AbstractFlowRelation relation) {
		for (PNGraphListener listener : listeners) {
			listener.relationAdded(relation);
		}
	}

	public void notifyPlaceRemoved(AbstractPlace place) {
		for (PNGraphListener listener : listeners) {
			listener.placeRemoved(place);
		}
	}

	public void notifyTransitionRemoved(AbstractTransition transition) {
		for (PNGraphListener listener : listeners) {
			listener.transitionRemoved(transition);
		}
	}

	public void notifyRelationRemoved(AbstractFlowRelation relation) {
		for (PNGraphListener listener : listeners) {
			listener.relationRemoved(relation);
		}
	}

	public void notifyComponentsSelected(Set<PNGraphCell> selectedCells) {
		for (PNGraphListener listener : listeners) {
			listener.componentsSelected(selectedCells);
		}
	}

	public void notifyMarkingForPlaceChanged(String placeName, Multiset placeMarking) {
		for (PNGraphListener listener : listeners) {
			listener.markingForPlaceChanged(placeName, placeMarking);
		}
	}

	public void notifyPlaceCapacityChanged(String placeName, String color, int newCapacity) {
		for (PNGraphListener listener : listeners) {
			listener.placeCapacityChanged(placeName,color,newCapacity);
		}
	}

	public void notifyConstraintChanged(String flowRelation, Multiset constraint) {
		for (PNGraphListener listener : listeners) {
			listener.constraintChanged(flowRelation,constraint);
		}
	}

	public void notifyTransitionFired(PNGraphCell cell) {
		Set<PNGraphCell> selectedCells = new HashSet<PNGraphCell>();
		selectedCells.add(cell);
		for (PNGraphListener listener : listeners) {
			listener.componentsSelected(selectedCells);
		}
	}




}
