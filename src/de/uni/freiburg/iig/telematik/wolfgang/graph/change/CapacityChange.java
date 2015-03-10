package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class CapacityChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	private String name;
	private int value;
	private int previous;
	private PNGraph graph;
	private String color;

	/**
	 * @param newCapacity
	 * @param color
	 * @param string
	 * @param graph2
	 * 
	 */

	public CapacityChange(PNGraph graph, String name, String color, int newCapacity) {
		this.graph = graph;
		this.name = name;
		this.color = color;
		this.value = newCapacity;
		this.previous = this.value;
	}

	/**
	 * 
	 */
	public void setCell(String value) {
		name = value;
	}

	/**
	 * @return the cell
	 */
	public Object getName() {
		return name;
	}

	/**
	 * 
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 */
	public void setPrevious(int value) {
		previous = value;
	}

	/**
	 * @return the previous
	 */
	public Object getPrevious() {
		return previous;
	}

	/**
	 * Changes the root of the model.
	 */
	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, color, previous);

	}

	protected int valueForCellChanged(String name, String color, int newCapacity) {
		int oldValue = graph.getCapacityforPlace(name, color);
		graph.updatePlaceCapacity(name, color, newCapacity);
		return oldValue;
	}

}
