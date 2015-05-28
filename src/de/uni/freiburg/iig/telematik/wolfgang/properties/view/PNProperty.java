package de.uni.freiburg.iig.telematik.wolfgang.properties.view;

import de.invation.code.toval.graphic.component.RestrictedTextField.Restriction;

public enum PNProperty {
	
	//Place Property Order
	PLACE_LABEL(Restriction.NOT_EMPTY)						{public String toString() {return 
	"label"
																					;}}, 
	PLACE_SIZE(Restriction.POSITIVE_INTEGER)				{public String toString() {return 
	"size"
																					;}}, 
	PLACE_POSITION_X(Restriction.POSITIVE_INTEGER)			{public String toString() {return 
	"x"
																					;}}, 
	PLACE_POSITION_Y(Restriction.POSITIVE_INTEGER)			{public String toString() {return 
	"y"
																					;}},
	PLACE_CAPACITY(Restriction.POSITIVE_INTEGER)						{public String toString() {return 
	"capacity"
																					;}},
																					
	//Transition Property Order
	TRANSITION_LABEL(Restriction.NOT_EMPTY)					{public String toString() {return 
	"label"
																					;}}, 
	TRANSITION_SIZE_X(Restriction.POSITIVE_INTEGER)			{public String toString() {return 
	"width"
																					;}}, 
	TRANSITION_SIZE_Y(Restriction.POSITIVE_INTEGER)			{public String toString() {return 
	"height"
																					;}}, 
	TRANSITION_POSITION_X(Restriction.POSITIVE_INTEGER)		{public String toString() {return 
	"x"
																					;}}, 
	TRANSITION_POSITION_Y(Restriction.POSITIVE_INTEGER)		{public String toString() {return 
	"y"
																					;}}, 
																
	//Arc Property Order
	ARC_WEIGHT(Restriction.POSITIVE_INTEGER) 				{public String toString() {return 
	"weight"																		;}};
	

	Restriction restriction = null;
	
	private PNProperty(Restriction restriction){
		this.restriction = restriction;
	}
	
	public Restriction getRestriction(){
		return restriction;
	}
	

}
