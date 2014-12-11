package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public class TokenMouseWheelChange extends mxAtomicGraphModelChange {

	protected PNGraphCell cell;
	int value;
	protected int previous;
	private PNGraph graph;

	public TokenMouseWheelChange() {
		this(null, null, (Integer) null);
	}

	public TokenMouseWheelChange(PNGraph graph, PNGraphCell cell, int i) {
		this.graph = graph;
		this.cell = cell;
		this.value = i;
		this.previous = this.value;
	}

	public void setCell(PNGraphCell value) {
		cell = (PNGraphCell) value;
	}

	public Object getCell() {
		return cell;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(int value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(cell, previous);
	}

	protected int valueForCellChanged(PNGraphCell cell, int previous) {
		int oldValue = (previous == 1) ? -1 : 1;

		graph.inOrDecrementPlaceState(cell, previous);

		return oldValue;
	}

}
