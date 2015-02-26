package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.Color;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.invation.code.toval.properties.AbstractProperties;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;


public class WolfgangProperties extends AbstractProperties{
	
	public static final IconSize ICON_SIZE = IconSize.MEDIUM;

	public static final int DEFAULT_PLACE_SIZE = 45;
	public static final int DEFAULT_TRANSITION_WIDTH = 45;
	public static final int DEFAULT_TRANSITION_HEIGHT = 45;
	public static final int DEFAULT_TOKEN_SIZE = 8;
	public static final int DEFAULT_TOKEN_DISTANCE = 1;
	public static final int DEFAULT_VERTICAL_LABEL_OFFSET = 30;
	public static final int DEFAULT_HORIZONTAL_LABEL_OFFSET = 1;
	public static final Color DEFAULT_LABEL_BG_COLOR = null;
	public static final Color DEFAULT_LABEL_LINE_COLOR = null;
	public static final Color DEFAULT_PLACE_COLOR = Color.decode("0xB6CAE4");
	public static final Color DEFAULT_TRANSITION_COLOR = Color.decode("0xB6CAE4");
	public static final Color DEFAULT_LINE_COLOR = Color.decode("0x000000");
	public static final Color DEFAULT_GRADIENT_COLOR = null;
	public static final GradientRotation DEFAULT_GRADIENT_DIRECTION = GradientRotation.HORIZONTAL;
	public static final String DEFAULT_FONT_FAMILY = "Dialog";
	public static final int DEFAULT_FONT_SIZE = 11;
	public static final double DEFAULT_ZOOM_STEP = 0.2;
	
	public static final Color DEFAULT_BACKGROUND_COLOR = MXConstants.blueBG;
	public static final int DEFAULT_GRID_SIZE = 5;
	public static final Color DEFAULT_GRID_COLOR = MXConstants.bluehigh;
	public static final boolean DEFAULT_GRID_VISIBILITY = true;
	
	public static final boolean DEFAULT_SNAP_TO_GRID = true;
	
	protected static final String propertyFileName = "WolfgangProperties";
	
	private static WolfgangProperties instance = null;
	
	private Set<WolfgangPropertyListener> listeners = new HashSet<WolfgangPropertyListener>();

	public WolfgangProperties() throws IOException {
		try {
			load(propertyFileName);
		} catch (IOException e) {
			// Create new property file.
			loadDefaultProperties();
			store();
		}
	}
	
	public static WolfgangProperties getInstance() throws IOException {
		if(instance == null){
			instance = new WolfgangProperties();
		}
		return instance;
	}
	
	//------- Property setting -------------------------------------------------------------
	
	private void setProperty(WolfgangProperty property, Object value){
		props.setProperty(property.toString(), value.toString());
	}
	
	private String getProperty(WolfgangProperty property){
		return props.getProperty(property.toString());
	}
	
//	private void removeProperty(WolfgangProperty property){
//		props.remove(property.toString());
//	}
	
	
	// ------- Listeners --------------------------------------------------------------------
	
	public boolean addListener(WolfgangPropertyListener listener){
		return listeners.add(listener);
	}
	
	public boolean removeListener(WolfgangPropertyListener listener){
		return listeners.remove(listener);
	}
	
	// ------- Icon Size --------------------------------------------------------------------

	public void setIconSize(IconSize size) throws PropertyException, ParameterException {
		Validate.notNull(size);
		if(!size.equals(getIconSize())){
			setProperty(WolfgangProperty.ICON_SIZE, size.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.iconSizeChanged(size);
			}
		}
	}
	
	public IconSize getIconSize() throws PropertyException {
		String propertyValue = getProperty(WolfgangProperty.ICON_SIZE);
		if (propertyValue == null || propertyValue.equals(""))
			throw new PropertyException(WolfgangProperty.ICON_SIZE, propertyValue);
		try {
			IconSize result = IconSize.valueOf(propertyValue);
			return result;
		} catch (Exception e) {
			throw new PropertyException(WolfgangProperty.ICON_SIZE, propertyValue);
		}
	}
	
	
	// ------- Default Place size -----------------------------------------------------------
	
	public void setDefaultPlaceSize(Integer placeSize) throws PropertyException{
		Validate.positive(placeSize);
		if(!placeSize.equals(getDefaultPlaceSize())){
			setProperty(WolfgangProperty.DEFAULT_PLACE_SIZE, placeSize.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultPlaceSizeChanged(placeSize);
			}
		}
	}
	
	public Integer getDefaultPlaceSize() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_PLACE_SIZE);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_PLACE_SIZE, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	
	// ------- Default Transition Width -----------------------------------------------------
	
	public void setDefaultTransitionWidth(Integer transitionWidth) throws PropertyException{
		Validate.positive(transitionWidth);
		if(!transitionWidth.equals(getDefaultTransitionWidth())){
			setProperty(WolfgangProperty.DEFAULT_TRANSITION_WIDTH, transitionWidth.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultTransitionWidthChanged(transitionWidth);
			}
		}
	}
	
	public Integer getDefaultTransitionWidth() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_TRANSITION_WIDTH);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_TRANSITION_WIDTH, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	
	// ------- Default Transition Height ----------------------------------------------------
	
	public void setDefaultTransitionHeight(Integer transitionHeight) throws PropertyException{
		Validate.positive(transitionHeight);
		if(!transitionHeight.equals(getDefaultTransitionHeight())){
			setProperty(WolfgangProperty.DEFAULT_TRANSITION_HEIGHT, transitionHeight.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultTransitionHeightChanged(transitionHeight);
			}
		}
	}
	
	public Integer getDefaultTransitionHeight() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_TRANSITION_HEIGHT);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_TRANSITION_HEIGHT, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	// ------- Default Vertical Label Offset ------------------------------------------------
	
	public void setDefaultVerticalLabelOffset(Integer labelOffset) throws PropertyException{
		Validate.notNull(labelOffset);
		if(!labelOffset.equals(getDefaultVerticalLabelOffset())){
			setProperty(WolfgangProperty.DEFAULT_VERTICAL_LABEL_OFFSET, labelOffset.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultVerticalLabelOffsetChanged(labelOffset);
			}
		}
	}
	
	public Integer getDefaultVerticalLabelOffset() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_VERTICAL_LABEL_OFFSET);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_VERTICAL_LABEL_OFFSET, propertyValue);
		}
		return result;
	}
	
	
	// ------- Default Horizontal Label Offset ----------------------------------------------
	
	public void setDefaultHorizontalLabelOffset(Integer labelOffset) throws PropertyException{
		Validate.notNull(labelOffset);
		if(!labelOffset.equals(getDefaultHorizontalLabelOffset())){
			setProperty(WolfgangProperty.DEFAULT_HORIZONTAL_LABEL_OFFSET, labelOffset.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultHorizuntalLabelOffsetChanged(labelOffset);
			}
		}
	}
	
	public Integer getDefaultHorizontalLabelOffset() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_HORIZONTAL_LABEL_OFFSET);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_HORIZONTAL_LABEL_OFFSET, propertyValue);
		}
		return result;
	}
	
	
	// ------- Default Token Size -----------------------------------------------------------
	
	public void setDefaultTokenSize(Integer tokenSize) throws PropertyException{
		Validate.positive(tokenSize);
		if(!tokenSize.equals(getDefaultTokenSize())){
			setProperty(WolfgangProperty.DEFAULT_TOKEN_SIZE, tokenSize.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultTokenSizeChanged(tokenSize);
			}
		}
	}
	
	public Integer getDefaultTokenSize() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_TOKEN_SIZE);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_TOKEN_SIZE, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	
	// ------- Default Token Distance -------------------------------------------------------
	
	public void setDefaultTokenDistance(Integer tokenDistance) throws PropertyException{
		Validate.positive(tokenDistance);
		if(!tokenDistance.equals(getDefaultTokenDistance())){
			setProperty(WolfgangProperty.DEFAULT_TOKEN_DISTANCE, tokenDistance.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultTokenDistanceChanged(tokenDistance);
			}
		}
	}
	
	public Integer getDefaultTokenDistance() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_TOKEN_DISTANCE);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_TOKEN_DISTANCE, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	
	// ------- Default Font Size ------------------------------------------------------------
	
	public void setDefaultFontSize(Integer fontSize) throws PropertyException{
		Validate.positive(fontSize);
		if(!fontSize.equals(getDefaultFontSize())){
			setProperty(WolfgangProperty.DEFAULT_FONT_SIZE, fontSize.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultFontSizeChanged(fontSize);
			}
		}
	}
	
	public Integer getDefaultFontSize() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_FONT_SIZE);
		Integer result = null;
		try{
			result = Integer.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_FONT_SIZE, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	
	// ------- Default Zoom Step ------------------------------------------------------------
	
	public void setDefaultZoomStep(Double zoomStep) throws PropertyException{
		Validate.probability(zoomStep);
		if(!zoomStep.equals(getDefaultZoomStep())){
			setProperty(WolfgangProperty.DEFAULT_ZOOM_STEP, zoomStep.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultZoomStepChanged(zoomStep);
			}
		}
	}
	
	public Double getDefaultZoomStep() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_ZOOM_STEP);
		Double result = null;
		try{
			result = Double.valueOf(propertyValue);
		}catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_ZOOM_STEP, propertyValue);
		}
		Validate.probability(result);
		return result;
	}
	
	
	// ------- Default Place Color -----------------------------------------------------------
	
	public void setDefaultPlaceColor(Color nodeColor) throws PropertyException {
		if(nodeColor == null){
			if(getDefaultPlaceColor() != null){
				setProperty(WolfgangProperty.DEFAULT_PLACE_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultPlaceColorChanged(nodeColor);
				}
			}
		} else {
			if(!nodeColor.equals(getDefaultPlaceColor())){
				setProperty(WolfgangProperty.DEFAULT_PLACE_COLOR, nodeColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultPlaceColorChanged(nodeColor);
				}
			}
		}
	}
	
	public Color getDefaultPlaceColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_PLACE_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_PLACE_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	// ------- Default Transition Color -----------------------------------------------------------
	
	public void setDefaultTransitionColor(Color nodeColor) throws PropertyException {
		if(nodeColor == null){
			if(getDefaultTransitionColor() != null){
				setProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultTransitionColorChanged(nodeColor);
				}
			}
		} else {
			if(!nodeColor.equals(getDefaultTransitionColor())){
				setProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR, nodeColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultTransitionColorChanged(nodeColor);
				}
			}
		}
	}
	
	public Color getDefaultTransitionColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_TRANSITION_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	
	// ------- Default Label Background Color -----------------------------------------------
	
	public void setDefaultLabelBackgroundColor(Color labelBGColor) throws PropertyException {
		if(labelBGColor == null){
			if(getDefaultLabelBackgroundColor() != null){
				setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLabelBGColorChanged(labelBGColor);
				}
			}
		} else {
			if(!labelBGColor.equals(getDefaultLabelBackgroundColor())){
				setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, labelBGColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLabelBGColorChanged(labelBGColor);
				}
			}
		}
	}
	
	public Color getDefaultLabelBackgroundColor() throws PropertyException {
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	
	// ------- Default Gradient Color -------------------------------------------------------
	
	public void setDefaultGradientColor(Color gradientColor) throws PropertyException {
		if(gradientColor == null){
			if(getDefaultGradientColor() != null){
				setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultGradientColorChanged(gradientColor);
				}
			}
		} else {
			if(!gradientColor.equals(getDefaultGradientColor())){
				setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR, gradientColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultGradientColorChanged(gradientColor);
				}
			}
		}
	}
	
	public Color getDefaultGradientColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_GRADIENT_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	
	// ------- Default Gradient Direction ---------------------------------------------------
	
	public void setDefaultGradientDirection(GradientRotation gradientDirection) throws PropertyException {
		Validate.notNull(gradientDirection);
		if(!gradientDirection.equals(getDefaultGradientDirection())){
			setProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION, gradientDirection.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultGradientDirectionChanged(gradientDirection);
			}
		}
	}
	
	public GradientRotation getDefaultGradientDirection() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION);
		if (propertyValue == null || propertyValue.equals("")) {
			return null;
		}
		try {
			GradientRotation result = GradientRotation.getGradientRotation(propertyValue);
			return result;
		} catch (Exception e) {
			throw new PropertyException(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION, propertyValue);
		}
	}
	
	
	// ------- Default Line Color -----------------------------------------------------------
	
	public void setDefaultLineColor(Color lineColor) throws PropertyException {
		if(lineColor == null){
			if(getDefaultLineColor() != null){
				setProperty(WolfgangProperty.DEFAULT_LINE_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLineColorChanged(lineColor);
				}
			}
		} else {
			if(!lineColor.equals(getDefaultLineColor())){
				setProperty(WolfgangProperty.DEFAULT_LINE_COLOR, lineColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLineColorChanged(lineColor);
				}
			}
		}
	}
	
	public Color getDefaultLineColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LINE_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_LINE_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	
	// ------- Default Font Family ----------------------------------------------------------
	
	public void setDefaultFontFamily(String fontFamily) {
		Validate.notNull(fontFamily);
		if(!fontFamily.equals(getDefaultFontFamily())){
			setProperty(WolfgangProperty.DEFAULT_FONT_FAMILY, fontFamily);
			for (WolfgangPropertyListener listener : listeners) {
				listener.defaultFontFamilyChanged(fontFamily);
			}
		}
	}
	
	public String getDefaultFontFamily(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_FONT_FAMILY);
		Validate.notNull(propertyValue);
		return propertyValue;
	}
	
	
	// ------- Default Label Line Color -----------------------------------------------------
	
	public void setDefaultLabelLineColor(Color labelLineColor) throws PropertyException {
		if(labelLineColor == null){
			if(getDefaultLabelLineColor() != null){
				setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLabelLineColorChanged(labelLineColor);
				}
			}
		} else {
			if(!labelLineColor.equals(getDefaultLabelLineColor())){
				setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, labelLineColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.defaultLabelLineColorChanged(labelLineColor);
				}
			}
		}
	}
	
	public Color getDefaultLabelLineColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	
	// ------- Background Color -------------------------------------------------------------
	
	public void setBackgroundColor(Color backgroundColor) throws PropertyException {
		if(backgroundColor == null){
			if(getBackgroundColor() != null){
				setProperty(WolfgangProperty.BACKGROUD_COLOR, "");
				for (WolfgangPropertyListener listener : listeners) {
					listener.backgroundColorChanged(backgroundColor);
				}
			}
		} else {
			if(!backgroundColor.equals(getBackgroundColor())){
				setProperty(WolfgangProperty.BACKGROUD_COLOR, backgroundColor.getRGB());
				for (WolfgangPropertyListener listener : listeners) {
					listener.backgroundColorChanged(backgroundColor);
				}
			}
		}
	}
	
	public Color getBackgroundColor() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.BACKGROUD_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.BACKGROUD_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
	
	// ------- Grid Size --------------------------------------------------------------------
	
	public void setGridSize(Integer gridSize) throws PropertyException {
		Validate.positive(gridSize);
		if (!gridSize.equals(getGridSize())) {
			setProperty(WolfgangProperty.GRID_SIZE, gridSize.toString());
			for (WolfgangPropertyListener listener : listeners) {
				listener.gridSizeChanged(gridSize);
			}
		}
	}

	public Integer getGridSize() throws PropertyException {
		String propertyValue = getProperty(WolfgangProperty.GRID_SIZE);
		Integer result = null;
		try {
			result = Integer.valueOf(propertyValue);
		} catch (Exception e) {
			throw new PropertyException(WolfgangProperty.GRID_SIZE, propertyValue);
		}
		Validate.positive(result);
		return result;
	}
	
	// ------- Grid Color -------------------------------------------------------------------
	
	public void setGridColor(Color gridColor) throws PropertyException {
		if (gridColor == null) {
			if(getGridColor() != null){
				setProperty(WolfgangProperty.GRID_COLOR, "");
				for(WolfgangPropertyListener listener: listeners){
					listener.gridColorChanged(gridColor);
				}
			}
		} else {
			if(!gridColor.equals(getGridColor())){
				setProperty(WolfgangProperty.GRID_COLOR, gridColor.getRGB());
				for(WolfgangPropertyListener listener: listeners){
					listener.gridColorChanged(gridColor);
				}
			}
		}
	}

	public Color getGridColor() throws PropertyException {
		String propertyValue = getProperty(WolfgangProperty.GRID_COLOR);
		Validate.notNull(propertyValue);
		if (propertyValue.isEmpty())
			return null;
		Color color = null;
		try {
			color = Color.decode(propertyValue);
		} catch (Exception e) {
			throw new PropertyException(WolfgangProperty.GRID_COLOR, propertyValue, "Cannot create Color object from property value");
		}
		return color;
	}
		
	// ------- Grid Visibility --------------------------------------------------------------
	
	public void setGridVisibility(Boolean gridVisibility) throws PropertyException {
		Validate.notNull(gridVisibility);
		if(gridVisibility != getGridVisibility()){
			setProperty(WolfgangProperty.GRID_VISIBILITY, gridVisibility);
			for(WolfgangPropertyListener listener: listeners){
				listener.gridVisibilityChanged(gridVisibility);
			}
		}
	}
	
	public boolean getGridVisibility() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.GRID_VISIBILITY);
		if(propertyValue == null)
			throw new PropertyException(WolfgangProperty.GRID_VISIBILITY, propertyValue, "Invalid property value");

		Boolean snapToGrid = null;
		try {
			snapToGrid = Boolean.parseBoolean(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.GRID_VISIBILITY, propertyValue, "Cannot boolean object from property value");
		}
		return snapToGrid;
	}
	
	// ------- Snap to Grid -----------------------------------------------------------------
	
	public void setSnapToGrid(Boolean snapToGrid) throws PropertyException {
		Validate.notNull(snapToGrid);
		if(snapToGrid != getSnapToGrid()){
			setProperty(WolfgangProperty.SNAP_TO_GRID, snapToGrid);
			for(WolfgangPropertyListener listener: listeners){
				listener.snapToGridChanged(snapToGrid);
			}
		}
	}
	
	public boolean getSnapToGrid() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.SNAP_TO_GRID);
		if(propertyValue == null)
			throw new PropertyException(WolfgangProperty.SNAP_TO_GRID, propertyValue, "Invalid property value");

		Boolean snapToGrid = null;
		try {
			snapToGrid = Boolean.parseBoolean(propertyValue);
		} catch(Exception e){
			throw new PropertyException(WolfgangProperty.BACKGROUD_COLOR, propertyValue, "Cannot boolean object from property value");
		}
		return snapToGrid;
	}
	
	// ------- Default Properties -----------------------------------------------------------
	
	@Override
	protected Properties getDefaultProperties(){
		Properties defaultProperties = new Properties();
		defaultProperties.setProperty(WolfgangProperty.ICON_SIZE.toString(), ICON_SIZE.toString());
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_PLACE_SIZE.toString(), String.valueOf(DEFAULT_PLACE_SIZE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_TRANSITION_WIDTH.toString(), String.valueOf(DEFAULT_TRANSITION_WIDTH));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_TRANSITION_HEIGHT.toString(), String.valueOf(DEFAULT_TRANSITION_HEIGHT));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_TOKEN_SIZE.toString(), String.valueOf(DEFAULT_TOKEN_SIZE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_TOKEN_DISTANCE.toString(), String.valueOf(DEFAULT_TOKEN_DISTANCE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_VERTICAL_LABEL_OFFSET.toString(), String.valueOf(DEFAULT_VERTICAL_LABEL_OFFSET));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_HORIZONTAL_LABEL_OFFSET.toString(), String.valueOf(DEFAULT_HORIZONTAL_LABEL_OFFSET));
		
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR.toString(), DEFAULT_LABEL_BG_COLOR == null ? "" : String.valueOf(DEFAULT_LABEL_BG_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR.toString(), DEFAULT_LABEL_LINE_COLOR == null ? "" : String.valueOf(DEFAULT_LABEL_LINE_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_PLACE_COLOR.toString(), DEFAULT_PLACE_COLOR == null ? "" : String.valueOf(DEFAULT_PLACE_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR.toString(), DEFAULT_TRANSITION_COLOR == null ? "" : String.valueOf(DEFAULT_TRANSITION_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LINE_COLOR.toString(), DEFAULT_LINE_COLOR == null ? "" : String.valueOf(DEFAULT_LINE_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR.toString(), DEFAULT_GRADIENT_COLOR == null ? "" : String.valueOf(DEFAULT_GRADIENT_COLOR.getRGB()));
		
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION.toString(), DEFAULT_GRADIENT_DIRECTION == null ? "" : DEFAULT_GRADIENT_DIRECTION.toString());
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_FAMILY.toString(), DEFAULT_FONT_FAMILY);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_SIZE.toString(), String.valueOf(DEFAULT_FONT_SIZE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_ZOOM_STEP.toString(), String.valueOf(DEFAULT_ZOOM_STEP));
		
		defaultProperties.setProperty(WolfgangProperty.BACKGROUD_COLOR.toString(), DEFAULT_BACKGROUND_COLOR == null ? "" : String.valueOf(DEFAULT_BACKGROUND_COLOR.getRGB()));
		
		defaultProperties.setProperty(WolfgangProperty.GRID_SIZE.toString(), String.valueOf(DEFAULT_GRID_SIZE));
		defaultProperties.setProperty(WolfgangProperty.GRID_COLOR.toString(), DEFAULT_GRID_COLOR == null ? "" : String.valueOf(DEFAULT_GRID_COLOR.getRGB()));
		defaultProperties.setProperty(WolfgangProperty.GRID_VISIBILITY.toString(), String.valueOf(DEFAULT_GRID_VISIBILITY));
		defaultProperties.setProperty(WolfgangProperty.SNAP_TO_GRID.toString(), String.valueOf(DEFAULT_SNAP_TO_GRID));
		
		return defaultProperties;
	}
	
	//--------------------------------------------------------------------------------------
	
	public void store() throws IOException {
		try {
			store(propertyFileName);
		} catch (IOException e) {
			throw new IOException("Cannot create/store wolfgang properties file on disk.");
		}
	}

	public boolean getRequestNetType() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getPNValidation() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
