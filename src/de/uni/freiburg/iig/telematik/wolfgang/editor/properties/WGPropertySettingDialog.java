package de.uni.freiburg.iig.telematik.wolfgang.editor.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.invation.code.toval.graphic.dialog.AbstractDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;

public class WGPropertySettingDialog extends AbstractDialog {

	private static final Dimension PREFERED_SIZE = new Dimension(400,400);
	private static final long serialVersionUID = 1855588446219004779L;
	private WGPropertySettingPanel settingPanel = null;
	
	public WGPropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent, ButtonPanelLayout.CENTERED);
		setIncludeCancelButton(false);
		settingPanel = new WGPropertySettingPanel();
		setPreferredSize(PREFERED_SIZE);
	}
	
	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(getPropertySettingPanel());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		mainPanel().add(scrollPane, BorderLayout.CENTER);
	}

	protected JPanel getPropertySettingPanel(){
		return settingPanel;
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
			JOptionPane.showMessageDialog(WGPropertySettingDialog.this, "Cannot store all properties:\nReason: " + e.getMessage(), "Exception during property change", JOptionPane.ERROR_MESSAGE);
			return;
		}
		super.okProcedure();
	}
	
	protected void transferProperties() throws ParameterException, PropertyException, IOException{
		EditorProperties.getInstance().setIconSize(settingPanel.getIconSize());
		EditorProperties.getInstance().setDefaultPlaceSize(settingPanel.getDefaultPlaceSize());
		EditorProperties.getInstance().setDefaultTransitionWidth(settingPanel.getDefaultTransitionWidth());
		EditorProperties.getInstance().setDefaultTransitionHeight(settingPanel.getDefaultTransitionHeight());
		EditorProperties.getInstance().setDefaultTokenSize(settingPanel.getDefaultTokenSize());
		EditorProperties.getInstance().setDefaultTokenDistance(settingPanel.getDefaultTokenDistance());
		EditorProperties.getInstance().setDefaultVerticalLabelOffset(settingPanel.getDefaultVerticalLabelOffset());
		EditorProperties.getInstance().setDefaultHorizontalLabelOffset(settingPanel.getDefaultHorizontalLabelOffset());
		EditorProperties.getInstance().setDefaultLabelBackgroundColor(settingPanel.getDefaultLabelBackgroundColor());
		EditorProperties.getInstance().setDefaultLabelLineColor(settingPanel.getDefaultLabelLineColor());
		EditorProperties.getInstance().setDefaultPlaceColor(settingPanel.getDefaultPlaceColor());
		EditorProperties.getInstance().setDefaultTransitionColor(settingPanel.getDefaultTransitionColor());
		EditorProperties.getInstance().setDefaultLineColor(settingPanel.getDefaultLineColor());
		EditorProperties.getInstance().setDefaultGradientColor(settingPanel.getDefaultGradientColor());
		EditorProperties.getInstance().setDefaultGradientDirection(settingPanel.getDefaultGradientDirection());
		EditorProperties.getInstance().setDefaultFontFamily(settingPanel.getDefaultFontFamily());
		EditorProperties.getInstance().setDefaultFontSize(settingPanel.getDefaultFontSize());
		EditorProperties.getInstance().setDefaultZoomStep(settingPanel.getDefaultZoomStep());
		EditorProperties.getInstance().setBackgroundColor(settingPanel.getBackgroundColor());
		EditorProperties.getInstance().setGridColor(settingPanel.getGridColor());
		EditorProperties.getInstance().setGridSize(settingPanel.getGridSize());
		EditorProperties.getInstance().setGridVisibility(settingPanel.getGridVisibility());
		EditorProperties.getInstance().setSnapToGrid(settingPanel.getSnapToGrid());
		EditorProperties.getInstance().store();
	}

	public static void showDialog(Window parent) throws Exception {
		WGPropertySettingDialog dialog = new WGPropertySettingDialog(parent);
		dialog.setUpGUI();
	}

	public static void main(String[] args) throws Exception {
		WGPropertySettingDialog.showDialog(null);
	}
}
