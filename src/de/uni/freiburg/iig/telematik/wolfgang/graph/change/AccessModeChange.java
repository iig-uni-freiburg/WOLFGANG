package de.uni.freiburg.iig.telematik.wolfgang.graph.change;

import java.util.Set;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;

public class AccessModeChange extends mxAtomicGraphModelChange {

	/**
	 *
	 */
	protected String name;
	Set value;
	protected Set previous;
	private IFNetGraph graph;
	private String color;

	/**
	 * @param newCapacity
	 * @param color
	 * @param string
	 * @param graph2
	 * 
	 */

	/**
	 * 
	 */
	// public TokenChange(PNGraph pnGraph, PNGraphCell cell, Multiset value)
	// {
	// this.graph = pnGraph;
	// this.cell = cell;
	// this.value = value;
	// this.previous = this.value;
	// }

	public AccessModeChange(PNGraph graph, String name, String color, Set am) {
		this.graph = (IFNetGraph) graph;
		this.name = name;
		this.color = color;
		this.value = am;
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
	public void setValue(Set value) {
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
	public void setPrevious(Set value) {
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

	protected Set valueForCellChanged(String name, String color, Set newAM) {
		Set oldValue = graph.getAccessModeforTransition(name, color);
		graph.updateAccessModeTransition(name, color, newAM);
		return oldValue;
	}

}

