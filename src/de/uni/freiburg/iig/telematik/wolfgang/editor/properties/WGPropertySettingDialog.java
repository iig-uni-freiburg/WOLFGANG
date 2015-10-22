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
	private WGPropertySettingPanel pnlSetting = null;
	
	public WGPropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent, ButtonPanelLayout.CENTERED);
		setIncludeCancelButton(false);
		pnlSetting = new WGPropertySettingPanel();
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
		return pnlSetting;
	}

	@Override
	protected void setTitle() {
		setTitle("Edit Wolfgang Properties");
	}

	@Override
	protected void okProcedure() {
		try {
			transferProperties();
		} catch (ParameterException | PropertyException | IOException e) {
			JOptionPane.showMessageDialog(WGPropertySettingDialog.this, "Cannot store all properties:\nReason: " + e.getMessage(), "Exception during property change", JOptionPane.ERROR_MESSAGE);
			return;
		}
		super.okProcedure();
	}
	
	protected void transferProperties() throws ParameterException, PropertyException, IOException{
		EditorProperties.getInstance().setIconSize(pnlSetting.getIconSize());
		EditorProperties.getInstance().setDefaultPlaceSize(pnlSetting.getDefaultPlaceSize());
		EditorProperties.getInstance().setDefaultTransitionWidth(pnlSetting.getDefaultTransitionWidth());
		EditorProperties.getInstance().setDefaultTransitionHeight(pnlSetting.getDefaultTransitionHeight());
		EditorProperties.getInstance().setDefaultTokenSize(pnlSetting.getDefaultTokenSize());
		EditorProperties.getInstance().setDefaultTokenDistance(pnlSetting.getDefaultTokenDistance());
		EditorProperties.getInstance().setDefaultVerticalLabelOffset(pnlSetting.getDefaultVerticalLabelOffset());
		EditorProperties.getInstance().setDefaultHorizontalLabelOffset(pnlSetting.getDefaultHorizontalLabelOffset());
		EditorProperties.getInstance().setDefaultLabelBackgroundColor(pnlSetting.getDefaultLabelBackgroundColor());
		EditorProperties.getInstance().setDefaultLabelLineColor(pnlSetting.getDefaultLabelLineColor());
		EditorProperties.getInstance().setDefaultPlaceColor(pnlSetting.getDefaultPlaceColor());
		EditorProperties.getInstance().setDefaultTransitionColor(pnlSetting.getDefaultTransitionColor());
		EditorProperties.getInstance().setDefaultLineColor(pnlSetting.getDefaultLineColor());
		EditorProperties.getInstance().setDefaultGradientColor(pnlSetting.getDefaultGradientColor());
		EditorProperties.getInstance().setDefaultGradientDirection(pnlSetting.getDefaultGradientDirection());
		EditorProperties.getInstance().setDefaultFontFamily(pnlSetting.getDefaultFontFamily());
		EditorProperties.getInstance().setDefaultFontSize(pnlSetting.getDefaultFontSize());
		EditorProperties.getInstance().setDefaultZoomStep(pnlSetting.getDefaultZoomStep());
		EditorProperties.getInstance().setBackgroundColor(pnlSetting.getBackgroundColor());
		EditorProperties.getInstance().setGridColor(pnlSetting.getGridColor());
		EditorProperties.getInstance().setGridSize(pnlSetting.getGridSize());
		EditorProperties.getInstance().setGridVisibility(pnlSetting.getGridVisibility());
		EditorProperties.getInstance().setSnapToGrid(pnlSetting.getSnapToGrid());
		EditorProperties.getInstance().setShowUpdateNotification(pnlSetting.getShowUpdateNotification());
		EditorProperties.getInstance().setShowFileExtensionAssociation(pnlSetting.getShowFileExtensionAssociation());
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
