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
	public static final String DEFAULT_GRADIENT_DIRECTION = "";
	public static final String DEFAULT_FONT_FAMILY = "Dialog";
	public static final int DEFAULT_FONT_SIZE = 11;
	public static final double DEFAULT_ZOOM_STEP = 0.2;
	public static final boolean DEFAULT_SNAP_TO_GRID = true;
	
	protected static final String propertyFileName = "WolfgangProperties";
	
	private static WolfgangProperties instance = null;
	
	private Set<WolfgangPropertyChangeListener> listeners = new HashSet<WolfgangPropertyChangeListener>();

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
	
	// ------- Icon Size --------------------------------------------------------------------

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
	
	public void setIconSize(IconSize size) throws PropertyException, ParameterException {
		Validate.notNull(size);
		setProperty(WolfgangProperty.ICON_SIZE, size.toString());
	}
	
	
	// ------- Default Place size -----------------------------------------------------------
	
	public void setDefaultPlaceSize(Integer fontSize){
		Validate.positive(fontSize);
		setProperty(WolfgangProperty.DEFAULT_PLACE_SIZE, fontSize.toString());
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
	
	public void setDefaultTransitionWidth(Integer fontSize){
		Validate.positive(fontSize);
		setProperty(WolfgangProperty.DEFAULT_TRANSITION_WIDTH, fontSize.toString());
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
	
	public void setDefaultTransitionHeight(Integer fontSize){
		Validate.positive(fontSize);
		setProperty(WolfgangProperty.DEFAULT_TRANSITION_HEIGHT, fontSize.toString());
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
	
	public void setDefaultVerticalLabelOffset(Integer fontSize){
		Validate.notNull(fontSize);
		setProperty(WolfgangProperty.DEFAULT_VERTICAL_LABEL_OFFSET, fontSize.toString());
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
	
	public void setDefaultHorizontalLabelOffset(Integer fontSize){
		Validate.notNull(fontSize);
		setProperty(WolfgangProperty.DEFAULT_HORIZONTAL_LABEL_OFFSET, fontSize.toString());
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
	
	public void setDefaultTokenSize(Integer fontSize){
		Validate.positive(fontSize);
		setProperty(WolfgangProperty.DEFAULT_TOKEN_SIZE, fontSize.toString());
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
	
	public void setDefaultTokenDistance(Integer tokenDistance){
		Validate.positive(tokenDistance);
		setProperty(WolfgangProperty.DEFAULT_TOKEN_DISTANCE, tokenDistance.toString());
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
	
	public void setDefaultFontSize(Integer fontSize){
		Validate.positive(fontSize);
		setProperty(WolfgangProperty.DEFAULT_FONT_SIZE, fontSize.toString());
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
	
	public void setDefaultZoomStep(Double zoomStep){
		Validate.probability(zoomStep);
		setProperty(WolfgangProperty.DEFAULT_ZOOM_STEP, zoomStep.toString());
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
	
	public void setDefaultPlaceColor(Color nodeColor) {
		if(nodeColor == null){
			setProperty(WolfgangProperty.DEFAULT_PLACE_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_PLACE_COLOR, nodeColor.getRGB());
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
	
	public void setDefaultTransitionColor(Color nodeColor) {
		if(nodeColor == null){
			setProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_TRANSITION_COLOR, nodeColor.getRGB());
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
	
	public void setDefaultLabelBackgroundColor(Color nodeColor) {
		if(nodeColor == null){
			setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, nodeColor.getRGB());
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
	
	public void setDefaultGradientColor(Color gradientColor) {
		if(gradientColor == null){
			setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR, gradientColor.getRGB());
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
	
	public void setDefaultGradientDirection(GradientRotation gradientDirection) {
		Validate.notNull(gradientDirection);
		setProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION, gradientDirection.toString());
	}
	
	public GradientRotation getDefaultGradientDirection() throws PropertyException{
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION);
		if (propertyValue == null || propertyValue.equals("")) {
			return null;
		}
		try {
			GradientRotation result = GradientRotation.valueOf(propertyValue);
			return result;
		} catch (Exception e) {
			throw new PropertyException(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION, propertyValue);
		}
	}
	
	
	// ------- Default Line Color -----------------------------------------------------------
	
	public void setDefaultLineColor(Color lineColor) {
		if(lineColor == null){
			setProperty(WolfgangProperty.DEFAULT_LINE_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_LINE_COLOR, lineColor.getRGB());
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
		setProperty(WolfgangProperty.DEFAULT_FONT_FAMILY, fontFamily);
	}
	
	public String getDefaultFontFamily(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_FONT_FAMILY);
		Validate.notNull(propertyValue);
		return propertyValue;
	}
	
	
	// ------- Default Label Line Color -----------------------------------------------------
	
	public void setDefaultLabelLineColor(Color labelLineColor) {
		if(labelLineColor == null){
			setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, "");
		} else {
			setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, labelLineColor.getRGB());
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
	
	public void setBackgroundColor(Color labelLineColor) {
		if(labelLineColor == null){
			setProperty(WolfgangProperty.BACKGROUD_COLOR, "");
		} else {
			setProperty(WolfgangProperty.BACKGROUD_COLOR, labelLineColor.getRGB());
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
	
	// ------- Grid Color -------------------------------------------------------------------
	
	// ------- Grid Visibility --------------------------------------------------------------
	
	// ------- Snap to Grid -----------------------------------------------------------------
	
	public void setSnapToGrid(Boolean snapToGrid) {
		Validate.notNull(snapToGrid);
		setProperty(WolfgangProperty.SNAP_TO_GRID, snapToGrid);
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
		
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION.toString(), DEFAULT_GRADIENT_DIRECTION);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_FAMILY.toString(), DEFAULT_FONT_FAMILY);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_SIZE.toString(), String.valueOf(DEFAULT_FONT_SIZE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_ZOOM_STEP.toString(), String.valueOf(DEFAULT_ZOOM_STEP));
		
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
