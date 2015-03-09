package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.component.ColorChooserPanel;
import de.invation.code.toval.graphic.component.ColorChooserPanel.ColorMode;
import de.invation.code.toval.graphic.component.EnumComboBox;
import de.invation.code.toval.graphic.component.FontComboBox;
import de.invation.code.toval.graphic.component.FontComboBox.DisplayMode;
import de.invation.code.toval.graphic.component.RestrictedTextField;
import de.invation.code.toval.graphic.component.RestrictedTextField.Restriction;
import de.invation.code.toval.graphic.dialog.AbstractDialog;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;

public class PropertySettingDialog extends AbstractDialog {

	private static final long serialVersionUID = 1855588446219004779L;

	private EnumComboBox<IconSize> comboIconSize;
	private RestrictedTextField txtDefPlaceSize;
	private RestrictedTextField txtDefTransitionWidth;
	private RestrictedTextField txtDefTransitionHeight;
	private RestrictedTextField txtDefTokenSize;
	private RestrictedTextField txtDefTokenDistance;
	private RestrictedTextField txtDefVertLabelOffset;
	private RestrictedTextField txtDefHorizLabelOffset;
	private RestrictedTextField txtGridSize;
	
	private ColorChooserPanel colDefLabelBGColor;
	private ColorChooserPanel colDefLabelLineColor;
	private ColorChooserPanel colDefPlaceColor;
	private ColorChooserPanel colDefTransitionColor;
	private ColorChooserPanel colDefLineColor;
	private ColorChooserPanel colDefGradientColor;
	private ColorChooserPanel colBGColor;
	private ColorChooserPanel colGridColor;
	
	private JCheckBox chckGridVisibility;
	private JCheckBox chckSnapToGrid;
	
	private EnumComboBox<GradientRotation> comboGradientRotation;
	private FontComboBox comboFontFamily;
	private RestrictedTextField txtDefFontSize;
	private RestrictedTextField txtDefZoomStep;

	public PropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent, ButtonPanelLayout.CENTERED);
		setIncludeCancelButton(false);
		initialize();
	}

	private void initialize() throws PropertyException, IOException {
		comboIconSize = new EnumComboBox<IconSize>(IconSize.class);
		comboIconSize.setSelectedItem(WolfgangProperties.getInstance().getIconSize());
		txtDefPlaceSize = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultPlaceSize().toString());
		txtDefTransitionWidth = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultTransitionWidth().toString());
		txtDefTransitionHeight = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultTransitionHeight().toString());
		txtDefTokenSize = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultTokenSize().toString());
		txtDefTokenDistance = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultTokenDistance().toString());
		txtDefVertLabelOffset = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultVerticalLabelOffset().toString());
		txtDefHorizLabelOffset = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultHorizontalLabelOffset().toString());
		txtGridSize = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getGridSize().toString());
		
		colDefLabelBGColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultLabelBackgroundColor());
		colDefLabelLineColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultLabelLineColor());
		colDefPlaceColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultPlaceColor());
		colDefTransitionColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultTransitionColor());
		colDefLineColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultLineColor());
		colDefGradientColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getDefaultGradientColor());
		colBGColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getBackgroundColor());
		colGridColor = new ColorChooserPanel(ColorMode.HEX, WolfgangProperties.getInstance().getGridColor());
		
		comboGradientRotation = new EnumComboBox<GradientRotation>(GradientRotation.class);
		comboGradientRotation.setSelectedItem(WolfgangProperties.getInstance().getDefaultGradientDirection());
		
		comboFontFamily = new FontComboBox(DisplayMode.FONT_FAMILY, WolfgangProperties.getInstance().getDefaultFontFamily());
		comboFontFamily.setSelectedItem(WolfgangProperties.getInstance().getDefaultFontFamily());
		txtDefFontSize = new RestrictedTextField(Restriction.POSITIVE_INTEGER, WolfgangProperties.getInstance().getDefaultFontSize().toString());
		txtDefZoomStep = new RestrictedTextField(Restriction.POSITIVE_DOUBLE, WolfgangProperties.getInstance().getDefaultZoomStep().toString());
		
		chckGridVisibility = new JCheckBox();
		chckGridVisibility.setSelected(WolfgangProperties.getInstance().getGridVisibility());
		chckSnapToGrid = new JCheckBox();
		chckSnapToGrid.setSelected(WolfgangProperties.getInstance().getSnapToGrid());
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
			WolfgangProperties.getInstance().setIconSize(comboIconSize.getSelectedItem());
			WolfgangProperties.getInstance().setDefaultPlaceSize(Integer.valueOf(txtDefPlaceSize.getText()));
			WolfgangProperties.getInstance().setDefaultTransitionWidth(Integer.valueOf(txtDefTransitionWidth.getText()));
			WolfgangProperties.getInstance().setDefaultTransitionHeight(Integer.valueOf(txtDefTransitionHeight.getText()));
			WolfgangProperties.getInstance().setDefaultTokenSize(Integer.valueOf(txtDefTokenSize.getText()));
			WolfgangProperties.getInstance().setDefaultTokenDistance(Integer.valueOf(txtDefTokenDistance.getText()));
			WolfgangProperties.getInstance().setDefaultVerticalLabelOffset(Integer.valueOf(txtDefVertLabelOffset.getText()));
			WolfgangProperties.getInstance().setDefaultHorizontalLabelOffset(Integer.valueOf(txtDefHorizLabelOffset.getText()));
			WolfgangProperties.getInstance().setDefaultLabelBackgroundColor(colDefLabelBGColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultLabelLineColor(colDefLabelLineColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultPlaceColor(colDefPlaceColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultTransitionColor(colDefTransitionColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultLineColor(colDefLineColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultGradientColor(colDefGradientColor.getChosenColor());
			WolfgangProperties.getInstance().setDefaultGradientDirection(comboGradientRotation.getSelectedItem());
			WolfgangProperties.getInstance().setDefaultFontFamily(comboFontFamily.getSelectedItem().toString());
			WolfgangProperties.getInstance().setDefaultFontSize(Integer.valueOf(txtDefFontSize.getText()));
			WolfgangProperties.getInstance().setDefaultZoomStep(Double.valueOf(txtDefZoomStep.getText()));
			WolfgangProperties.getInstance().setBackgroundColor(colBGColor.getChosenColor());
			WolfgangProperties.getInstance().setGridColor(colGridColor.getChosenColor());
			WolfgangProperties.getInstance().setGridSize(Integer.valueOf(txtGridSize.getText()));
			WolfgangProperties.getInstance().setGridVisibility(chckGridVisibility.isSelected());
			WolfgangProperties.getInstance().setSnapToGrid(chckSnapToGrid.isSelected());
			WolfgangProperties.getInstance().store();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(PropertySettingDialog.this, "Cannot store all properties:\nReason: " + e.getMessage(), "Exception during property change", JOptionPane.ERROR_MESSAGE);
			return;
		}
		super.okProcedure();
	}

	public static void showDialog(Window parent) throws Exception{
		PropertySettingDialog dialog = new PropertySettingDialog(parent);
		dialog.setUpGUI();
	}

	private class WGPropertySettingPanel extends JPanel {

		private static final long serialVersionUID = 1066469038019795225L;

		public WGPropertySettingPanel() throws PropertyException, IOException {
			super(new SpringLayout());
			initialize();
			add(new JLabel("Icon Size:", JLabel.RIGHT));
			add(comboIconSize);
			add(new JLabel("Default Place Size:", JLabel.RIGHT));
			add(txtDefPlaceSize);
			add(new JLabel("Default Transition Width:", JLabel.RIGHT));
			add(txtDefTransitionWidth);
			add(new JLabel("Default Transition Height:", JLabel.RIGHT));
			add(txtDefTransitionHeight);
			add(new JLabel("Default Token Size:", JLabel.RIGHT));
			add(txtDefTokenSize);
			add(new JLabel("Default Token Distance:", JLabel.RIGHT));
			add(txtDefTokenDistance);
			add(new JLabel("Default Vertical Label Offset:", JLabel.RIGHT));
			add(txtDefVertLabelOffset);
			add(new JLabel("Default Horizontal Label Offset:", JLabel.RIGHT));
			add(txtDefHorizLabelOffset);
			
			add(new JLabel("Default Label BG Color:", JLabel.RIGHT));
			add(colDefLabelBGColor);
			add(new JLabel("Default Label Line Color:", JLabel.RIGHT));
			add(colDefLabelLineColor);
			add(new JLabel("Default Place Color:", JLabel.RIGHT));
			add(colDefPlaceColor);
			add(new JLabel("Default Transition Color:", JLabel.RIGHT));
			add(colDefTransitionColor);
			add(new JLabel("Default Line Color:", JLabel.RIGHT));
			add(colDefLineColor);
			add(new JLabel("Default Gradient Color:", JLabel.RIGHT));
			add(colDefGradientColor);

			add(new JLabel("Default Font Family:", JLabel.RIGHT));
			add(comboFontFamily);
			add(new JLabel("Default Font Size:", JLabel.RIGHT));
			add(txtDefFontSize);
			add(new JLabel("Default Zoom Step:", JLabel.RIGHT));
			add(txtDefZoomStep);
			
			add(new JLabel("Background Color:", JLabel.RIGHT));
			add(colBGColor);
			
			add(new JLabel("Grid Size:", JLabel.RIGHT));
			add(txtGridSize);
			add(new JLabel("Grid Color:", JLabel.RIGHT));
			add(colGridColor);
			add(new JLabel("Grid Visibility:", JLabel.RIGHT));
			add(chckGridVisibility);
			add(new JLabel("Snap To Grid:", JLabel.RIGHT));
			add(chckSnapToGrid);

			SpringUtilities.makeCompactGrid(this, 22, 2, 5, 5, 5, 5);
		}

	}
	
	public static void main(String[] args) throws Exception {
		PropertySettingDialog.showDialog(null);
	}
}
