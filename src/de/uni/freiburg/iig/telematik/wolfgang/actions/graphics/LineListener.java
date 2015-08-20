package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;

public interface LineListener {
	public void lineShapeChanged(Shape line);

	public void lineStyleChanged(Style solid);

	public void lineWeightChanged(String strokeWeight);

	public void lineColorChanged(Color color);

}
