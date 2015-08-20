package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;

public interface FillListener {

	void gradientColorChanged(Color color);

	void fillColorChanged(Color color);

	void gradientDirectionChanged(GradientRotation rotation);

}
