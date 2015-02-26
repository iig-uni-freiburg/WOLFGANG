package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.Color;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

public class WolfgangPropertyAdapter implements WolfgangPropertyListener {

	@Override
	public void iconSizeChanged(IconSize size) {}

	@Override
	public void defaultPlaceSizeChanged(int defaultPlaceSize) {}

	@Override
	public void defaultTransitionWidthChanged(int defaultTransitionWidth) {}

	@Override
	public void defaultTransitionHeightChanged(int defaultTransitionHeight) {}

	@Override
	public void defaultTokenSizeChanged(int defaultTokenSize) {}

	@Override
	public void defaultTokenDistanceChanged(int defaultTokenDistance) {}

	@Override
	public void defaultVerticalLabelOffsetChanged(int defaultVerticalLabelOffset) {}

	@Override
	public void defaultHorizuntalLabelOffsetChanged(int defaultHorizontalLabelOffset) {}

	@Override
	public void defaultLabelBGColorChanged(Color defaultLabelBGColor) {}

	@Override
	public void defaultLabelLineColorChanged(Color defaultLabelLineColor) {}

	@Override
	public void defaultPlaceColorChanged(Color defaultPlaceColor) {}

	@Override
	public void defaultTransitionColorChanged(Color defaultTransitionColor) {}

	@Override
	public void defaultLineColorChanged(Color defaultLineColor) {}

	@Override
	public void defaultGradientColorChanged(Color defaultGradientColor) {}

	@Override
	public void defaultGradientDirectionChanged(GradientRotation defaultGradientDirection) {}

	@Override
	public void defaultFontFamilyChanged(String defaultFontFamily) {}

	@Override
	public void defaultFontSizeChanged(int defaultFontSize) {}

	@Override
	public void defaultZoomStepChanged(double defaultZoomStep) {}

	@Override
	public void backgroundColorChanged(Color backgroundColor) {}

	@Override
	public void gridSizeChanged(int gridSize) {}

	@Override
	public void gridColorChanged(Color gridColor) {}

	@Override
	public void gridVisibilityChanged(boolean gridVisibility) {}

	@Override
	public void snapToGridChanged(boolean snapToGrid) {}

}
