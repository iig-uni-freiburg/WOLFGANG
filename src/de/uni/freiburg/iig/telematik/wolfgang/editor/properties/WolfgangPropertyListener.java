package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.Color;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

public interface WolfgangPropertyListener {

	public void iconSizeChanged(IconSize size);
	
	public void defaultPlaceSizeChanged(int defaultPlaceSize);
	
	public void defaultTransitionWidthChanged(int defaultTransitionWidth);
	
	public void defaultTransitionHeightChanged(int defaultTransitionHeight);
	
	public void defaultTokenSizeChanged(int defaultTokenSize);
	
	public void defaultTokenDistanceChanged(int defaultTokenDistance);
	
	public void defaultVerticalLabelOffsetChanged(int defaultVerticalLabelOffset);
	
	public void defaultHorizuntalLabelOffsetChanged(int defaultHorizontalLabelOffset);
	
	public void defaultLabelBGColorChanged(Color defaultLabelBGColor);
	
	public void defaultLabelLineColorChanged(Color defaultLabelLineColor);
	
	public void defaultPlaceColorChanged(Color defaultPlaceColor);
	
	public void defaultTransitionColorChanged(Color defaultTransitionColor);
	
	public void defaultLineColorChanged(Color defaultLineColor);
	
	public void defaultGradientColorChanged(Color defaultGradientColor);

	public void defaultGradientDirectionChanged(GradientRotation defaultGradientDirection);
	
	public void defaultFontFamilyChanged(String defaultFontFamily);
	
	public void defaultFontSizeChanged(int defaultFontSize);
	
	public void defaultZoomStepChanged(double defaultZoomStep);
	
	public void backgroundColorChanged(Color backgroundColor);
	
	public void gridSizeChanged(int gridSize);
	
	public void gridColorChanged(Color gridColor);
	
	public void gridVisibilityChanged(boolean gridVisibility);
	
	public void snapToGridChanged(boolean snapToGrid);
	
	public void showUpdateNotificationChanged(boolean showUpdateNotification);
	
	public void showFileExtensionAssociationChanged(boolean showFileExtensionAssociation);
	
	public void requireNetTypeChanged(boolean requireNetType);
	
	public void pnValidationChanged(boolean pnValidation);
	
}
