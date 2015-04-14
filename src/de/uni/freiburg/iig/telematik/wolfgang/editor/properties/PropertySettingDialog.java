package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.invation.code.toval.graphic.dialog.AbstractDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;

public class PropertySettingDialog extends AbstractDialog {

	private static final long serialVersionUID = 1855588446219004779L;
	private WGPropertySettingPanel settingPanel = null;
	
	public PropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent, ButtonPanelLayout.CENTERED);
		setIncludeCancelButton(false);
		settingPanel = new WGPropertySettingPanel();
	}
	
	

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		mainPanel().add(createNewWGPropertySettingPanel(), BorderLayout.CENTER);
	}

	protected Component createNewWGPropertySettingPanel() throws PropertyException, IOException {
		return new WGPropertySettingPanel();
	}

	@Override
	protected void setTitle() {
		setTitle("Edit Wolfgang Properties");
	}

	@Override
	protected void okProcedure() {
		try {
			transferProperties();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(PropertySettingDialog.this, "Cannot store all properties:\nReason: " + e.getMessage(), "Exception during property change", JOptionPane.ERROR_MESSAGE);
			return;
		}
		super.okProcedure();
	}
	
	protected void transferProperties() throws ParameterException, PropertyException, IOException{
		WolfgangProperties.getInstance().setIconSize(settingPanel.getIconSize());
		WolfgangProperties.getInstance().setDefaultPlaceSize(settingPanel.getDefaultPlaceSize());
		WolfgangProperties.getInstance().setDefaultTransitionWidth(settingPanel.getDefaultTransitionWidth());
		WolfgangProperties.getInstance().setDefaultTransitionHeight(settingPanel.getDefaultTransitionHeight());
		WolfgangProperties.getInstance().setDefaultTokenSize(settingPanel.getDefaultTokenSize());
		WolfgangProperties.getInstance().setDefaultTokenDistance(settingPanel.getDefaultTokenDistance());
		WolfgangProperties.getInstance().setDefaultVerticalLabelOffset(settingPanel.getDefaultVerticalLabelOffset());
		WolfgangProperties.getInstance().setDefaultHorizontalLabelOffset(settingPanel.getDefaultHorizontalLabelOffset());
		WolfgangProperties.getInstance().setDefaultLabelBackgroundColor(settingPanel.getDefaultLabelBackgroundColor());
		WolfgangProperties.getInstance().setDefaultLabelLineColor(settingPanel.getDefaultLabelLineColor());
		WolfgangProperties.getInstance().setDefaultPlaceColor(settingPanel.getDefaultPlaceColor());
		WolfgangProperties.getInstance().setDefaultTransitionColor(settingPanel.getDefaultTransitionColor());
		WolfgangProperties.getInstance().setDefaultLineColor(settingPanel.getDefaultLineColor());
		WolfgangProperties.getInstance().setDefaultGradientColor(settingPanel.getDefaultGradientColor());
		WolfgangProperties.getInstance().setDefaultGradientDirection(settingPanel.getDefaultGradientDirection());
		WolfgangProperties.getInstance().setDefaultFontFamily(settingPanel.getDefaultFontFamily());
		WolfgangProperties.getInstance().setDefaultFontSize(settingPanel.getDefaultFontSize());
		WolfgangProperties.getInstance().setDefaultZoomStep(settingPanel.getDefaultZoomStep());
		WolfgangProperties.getInstance().setBackgroundColor(settingPanel.getBackgroundColor());
		WolfgangProperties.getInstance().setGridColor(settingPanel.getGridColor());
		WolfgangProperties.getInstance().setGridSize(settingPanel.getGridSize());
		WolfgangProperties.getInstance().setGridVisibility(settingPanel.getGridVisibility());
		WolfgangProperties.getInstance().setSnapToGrid(settingPanel.getSnapToGrid());
		WolfgangProperties.getInstance().store();
	}

	public static void showDialog(Window parent) throws Exception {
		PropertySettingDialog dialog = new PropertySettingDialog(parent);
		dialog.setUpGUI();
	}

	public static void main(String[] args) throws Exception {
		PropertySettingDialog.showDialog(null);
	}
}
