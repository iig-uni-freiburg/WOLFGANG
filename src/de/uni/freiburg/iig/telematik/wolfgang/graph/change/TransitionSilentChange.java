package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class TransitionSilentChange extends mxAtomicGraphModelChange {

	protected String name;
	boolean value;
	protected boolean previous;
	private PNGraph graph;

	public TransitionSilentChange() {
		this(null, null, false);
	}

	public TransitionSilentChange(PNGraph graph, String name, boolean setSilent) {
		this.graph = graph;
		this.name = name;
		this.value = setSilent;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}

	public Object getName() {
		return name;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(boolean value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
	}

	protected boolean valueForCellChanged(String name, boolean value) {
		boolean oldValue = graph.getTransitionSilentState(name);
		graph.updateTransitionSilent(name, value);
		return oldValue;
	}

}
