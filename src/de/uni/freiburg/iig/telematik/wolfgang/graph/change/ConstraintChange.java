package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class ConstraintChange extends mxAtomicGraphModelChange {

	private String name;
	private Multiset value, previous;
	private PNGraph graph;

	public ConstraintChange() {
		this(null, null, null);
	}

	public ConstraintChange(PNGraph graph, String name, Multiset<String> multiSet) {
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

	protected Multiset valueForCellChanged(String flowRelation, Multiset constraint) {
		Multiset<String> oldValue = graph.getConstraintforArc(flowRelation);
		graph.updateConstraint(flowRelation, constraint);

		return oldValue;
	}

}
