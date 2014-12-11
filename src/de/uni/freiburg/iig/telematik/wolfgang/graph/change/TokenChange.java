package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class TokenChange extends mxAtomicGraphModelChange {

	protected String name;
	protected Multiset value, previous;
	private PNGraph graph;

	public TokenChange() {
		this(null, null, null);
	}

	public TokenChange(PNGraph graph, String name, Multiset<String> multiSet) {
		this.graph = graph;
		this.name = name;
		this.value = multiSet;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}

	public Object getName() {
		return name;
	}

	public void setValue(Multiset value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(Multiset value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
	}

	protected Multiset valueForCellChanged(String name, Multiset value) {
		Multiset<String> oldValue = graph.getPlaceStateForCell(name, null);
		graph.updatePlaceState(name, value);
		return oldValue;
	}

}
