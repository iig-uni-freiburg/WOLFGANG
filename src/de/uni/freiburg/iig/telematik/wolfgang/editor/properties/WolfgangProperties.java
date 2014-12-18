package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

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

	public static final int DEFAULT_PLACE_SIZE = 30;
	public static final int DEFAULT_TRANSITION_WIDTH = 30;
	public static final int DEFAULT_TRANSITION_HEIGHT = 30;
	public static final int DEFAULT_TOKEN_SIZE = 8;
	public static final int DEFAULT_TOKEN_DISTANCE = 1;
	public static final int DEFAULT_VERTICAL_LABEL_OFFSET = 30;
	public static final int DEFAULT_HORIZONTAL_LABEL_OFFSET = 1;
	public static final String DEFAULT_LABEL_BG_COLOR = "none" ;
	public static final String DEFAULT_LABEL_LINE_COLOR = "none";
	public static final String DEFAULT_NODE_COLOR = "#B6CAE4";
	public static final String DEFAULT_LINE_COLOR = "#000000";
	public static final String DEFAULT_GRADIENT_COLOR = "";
	public static final String DEFAULT_GRADIENT_DIRECTION = "";
	public static final String DEFAULT_FONT_FAMILY = "Dialog";
	public static final int DEFAULT_FONT_SIZE = 11;
	public static final double DEFAULT_ZOOM_STEP = 0.2;
	
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
	
	private void removeProperty(WolfgangProperty property){
		props.remove(property.toString());
	}
	
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
	
	
	// ------- Default Node Color -----------------------------------------------------------
	
	public void setDefaultNodeColor(String nodeColor) {
		Validate.notNull(nodeColor);
		setProperty(WolfgangProperty.DEFAULT_NODE_COLOR, nodeColor);
	}
	
	public String getDefaultNodeColor(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_NODE_COLOR);
		Validate.notNull(propertyValue);
		return propertyValue;
	}
	
	
	// ------- Default Label Background Color -----------------------------------------------
	
	public void setDefaultLabelBackgroundColor(String nodeColor) {
		Validate.notNull(nodeColor);
		setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR, nodeColor);
	}
	
	public String getDefaultLabelBackgroundColor(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR);
		Validate.notNull(propertyValue);
		return propertyValue;
	}
	
	
	// ------- Default Gradient Color -------------------------------------------------------
	
	public void setDefaultGradientColor(String gradientColor) {
		Validate.notNull(gradientColor);
		setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR, gradientColor);
	}
	
	public String getDefaultGradientColor(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR);
		Validate.notNull(propertyValue);
		if(propertyValue.isEmpty())
			return null;
		return propertyValue;
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
	
	public void setDefaultLineColor(String lineColor) {
		Validate.notNull(lineColor);
		setProperty(WolfgangProperty.DEFAULT_LINE_COLOR, lineColor);
	}
	
	public String getDefaultLineColor(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LINE_COLOR);
		Validate.notNull(propertyValue);
		return propertyValue;
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
	
	public void setDefaultLabelLineColor(String labelLineColor) {
		Validate.notNull(labelLineColor);
		setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR, labelLineColor);
	}
	
	public String getDefaultLabelLineColor(){
		String propertyValue = getProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR);
		Validate.notNull(propertyValue);
		return propertyValue;
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
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LABEL_BG_COLOR.toString(), DEFAULT_LABEL_BG_COLOR);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LABEL_LINE_COLOR.toString(), DEFAULT_LABEL_LINE_COLOR);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_NODE_COLOR.toString(), DEFAULT_NODE_COLOR);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_LINE_COLOR.toString(), DEFAULT_LINE_COLOR);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_GRADIENT_COLOR.toString(), DEFAULT_GRADIENT_COLOR);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_GRADIENT_DIRECTION.toString(), DEFAULT_GRADIENT_DIRECTION);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_FAMILY.toString(), DEFAULT_FONT_FAMILY);
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_FONT_SIZE.toString(), String.valueOf(DEFAULT_FONT_SIZE));
		defaultProperties.setProperty(WolfgangProperty.DEFAULT_ZOOM_STEP.toString(), String.valueOf(DEFAULT_ZOOM_STEP));
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
