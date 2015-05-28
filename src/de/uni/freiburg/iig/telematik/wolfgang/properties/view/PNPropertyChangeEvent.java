package de.uni.freiburg.iig.telematik.wolfgang.properties.view;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;


public class PNPropertyChangeEvent extends EventObject {

	private static final long serialVersionUID = -4892583337247587700L;
	private PNProperty property = null;
	private PNComponent fieldType = null;
	private String name = null;
	private Object oldValue = null;
	private Object newValue = null;

	public PNPropertyChangeEvent(Object source, PNComponent fieldType, String name, PNProperty property, Object oldValue, Object newValue) {
		super(source);
		this.fieldType = fieldType;
		this.property = property;
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getName() {
		return name;
	}

	public PNProperty getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public PNComponent getFieldType() {
		return fieldType;
	}

}
