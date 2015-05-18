package de.uni.freiburg.iig.telematik.wolfgang.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetCheckingProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.WFNetProperties;

public class PropertyCheckView extends JPanel {

	private NetCheckingProperties propertyCheckProperties;
	private PropertyCheckLabelHeadline headline;

	private Separator separator;
	private Separator separator0;
	private Separator separator1;
	private Separator separator2;
	private Separator separator3;
	private Separator separator4;

	private PropertyCheckPropertiesLabel labelCWNStructure;
	private PropertyCheckPropertiesLabel labelValidInOutPlaces;
	private PropertyCheckPropertiesLabel labelStrongConnectedness;
	private PropertyCheckPropertiesLabel labelValidInitialMarking;
	private PropertyCheckPropertiesLabel labelControlFlowDependency;
	private PropertyCheckPropertiesLabel labelIsBounded;
	private PropertyCheckPropertiesLabel labelOptionToComplete;
	private PropertyCheckPropertiesLabel labelProperCompletion;
	private PropertyCheckPropertiesLabel labelNoDeadTransitions;
	private JButton errorButton;
	
	private int lineHeight = 16;

	public PropertyCheckView(NetCheckingProperties propertyCheckProperties) {
		super(new BorderLayout());
		if(propertyCheckProperties instanceof CWNProperties)
			setUpCWNGui((CWNProperties) propertyCheckProperties);
		if(propertyCheckProperties instanceof WFNetProperties)
			setUpPTGui((WFNetProperties) propertyCheckProperties);

	}
	
	public void setUpPTGui(WFNetProperties propertyCheckProperties) {
		final JPanel upperPTPanel = new JPanel(new SpringLayout());
		setPropertyCheckProperties(propertyCheckProperties);
		boolean isExpanded = true;
		String headline = "WF Net Check";
		setHeadline(new PropertyCheckLabelHeadline(headline, isExpanded));

		upperPTPanel.add(getHeadline());

		separator = new JPopupMenu.Separator();
		upperPTPanel.add(separator);

		errorButton = new JButton("Show Details");
		addErrorButtonListener();
		upperPTPanel.add(errorButton);
		separator0 = new JPopupMenu.Separator();
		upperPTPanel.add(separator0);

		labelCWNStructure = new PropertyCheckPropertiesLabel(" \u2022 WF Net Structure", ((WFNetProperties)getPropertyCheckProperties()).hasWFNetStructure);
		upperPTPanel.add(labelCWNStructure);
		labelValidInOutPlaces = new PropertyCheckPropertiesLabel("    \u2022 Valid InOut Places", ((WFNetProperties)getPropertyCheckProperties()).validInOutPlaces);
		upperPTPanel.add(labelValidInOutPlaces);
		labelStrongConnectedness = new PropertyCheckPropertiesLabel("    \u2022 Strong Connectedness", ((WFNetProperties)getPropertyCheckProperties()).strongConnectedness);
		upperPTPanel.add(labelStrongConnectedness);

		separator1 = new JPopupMenu.Separator();
		upperPTPanel.add(separator1);

		labelIsBounded = new PropertyCheckPropertiesLabel(" \u2022 Is Bounded", getPropertyCheckProperties().isBounded);

		upperPTPanel.add(labelIsBounded);

		int numberOfPanels = 9;
		SpringUtilities.makeCompactGrid(upperPTPanel, numberOfPanels, 1, 0, 0, 0, 0);
		add(upperPTPanel, BorderLayout.PAGE_START);
		setMinimumSize(new Dimension((int)upperPTPanel.getSize().getWidth(), (int) (lineHeight*numberOfPanels)));
		addHeadLineListener(upperPTPanel, numberOfPanels);
	}

	private void addErrorButtonListener() {
		errorButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (getPropertyCheckProperties().exception != null) {
					JOptionPane.showMessageDialog(getPropertyCheckView().getParent().getParent(), getPropertyCheckProperties().exception.getMessage(), "Property Check Details:",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(getPropertyCheckView().getParent().getParent(), "no error", "Property Check Details:", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});
	}

	public void setUpCWNGui(CWNProperties propertyCheckProperties) {
		final JPanel upperCWNPanel = new JPanel(new SpringLayout());
		setPropertyCheckProperties(propertyCheckProperties);
		boolean isExpanded = true;
		String headline = "Colored WF Net Check";
		setHeadline(new PropertyCheckLabelHeadline(headline, isExpanded));

		upperCWNPanel.add(getHeadline());

		separator = new JPopupMenu.Separator();
		upperCWNPanel.add(separator);

		errorButton = new JButton("Show Details");
		addErrorButtonListener();
		upperCWNPanel.add(errorButton);
		
		separator0 = new JPopupMenu.Separator();
		upperCWNPanel.add(separator0);

		labelCWNStructure = new PropertyCheckPropertiesLabel(" \u2022 CWN Structure", ((CWNProperties)getPropertyCheckProperties()).hasCWNStructure);
		upperCWNPanel.add(labelCWNStructure);
		labelValidInOutPlaces = new PropertyCheckPropertiesLabel("    \u2022 Valid InOut Places", ((CWNProperties)getPropertyCheckProperties()).validInOutPlaces);
		upperCWNPanel.add(labelValidInOutPlaces);
		labelStrongConnectedness = new PropertyCheckPropertiesLabel("    \u2022 Strong Connectedness", ((CWNProperties)getPropertyCheckProperties()).strongConnectedness);
		upperCWNPanel.add(labelStrongConnectedness);
		labelValidInitialMarking = new PropertyCheckPropertiesLabel("    \u2022 Valid Initial Marking", ((CWNProperties)getPropertyCheckProperties()).validInitialMarking);
		upperCWNPanel.add(labelValidInitialMarking);
		labelControlFlowDependency = new PropertyCheckPropertiesLabel("    \u2022 Control Flow Dependency", ((CWNProperties)getPropertyCheckProperties()).controlFlowDependency);
		upperCWNPanel.add(labelControlFlowDependency);

		separator1 = new JPopupMenu.Separator();
		upperCWNPanel.add(separator1);

		labelIsBounded = new PropertyCheckPropertiesLabel(" \u2022 Is Bounded", getPropertyCheckProperties().isBounded);
		upperCWNPanel.add(labelIsBounded);
		separator2 = new JPopupMenu.Separator();
		upperCWNPanel.add(separator2);

		labelOptionToComplete = new PropertyCheckPropertiesLabel(" \u2022 Option To Complete", ((CWNProperties)getPropertyCheckProperties()).optionToCompleteAndProperCompletion);
		upperCWNPanel.add(labelOptionToComplete);
		separator3 = new JPopupMenu.Separator();
		upperCWNPanel.add(separator3);
		labelProperCompletion = new PropertyCheckPropertiesLabel(" \u2022 Proper Completion", ((CWNProperties)getPropertyCheckProperties()).optionToCompleteAndProperCompletion);
		upperCWNPanel.add(labelProperCompletion);
		separator4 = new JPopupMenu.Separator();
		upperCWNPanel.add(separator4);
		labelNoDeadTransitions = new PropertyCheckPropertiesLabel(" \u2022 No Dead Transitions", ((CWNProperties)getPropertyCheckProperties()).noDeadTransitions);
		upperCWNPanel.add(labelNoDeadTransitions);
		final int numberOfPanels = 17;
		SpringUtilities.makeCompactGrid(upperCWNPanel, numberOfPanels, 1, 0, 0, 0, 0);
		add(upperCWNPanel, BorderLayout.PAGE_START);
		setMinimumSize(new Dimension((int)upperCWNPanel.getSize().getWidth(), (int) (lineHeight*numberOfPanels)));
		addHeadLineListener(upperCWNPanel, numberOfPanels);
	}

	private void addHeadLineListener(final JPanel upperPanel, final int numberOfPanels) {
		getHeadline().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!getHeadline().isExpanded()) {
						SpringUtilities.makeCompactGrid(upperPanel, numberOfPanels, 1, 0, 0, 0, 0);
						showCheckedProperties(true);
						getHeadline().setExpanded(true);
						setMinimumSize(new Dimension((int)upperPanel.getSize().getWidth(), (int) (lineHeight*numberOfPanels)));

					} else {
						SpringUtilities.makeCompactGrid(upperPanel, 1, 1, 0, 0, 0, 0);
						showCheckedProperties(false);
						getHeadline().setExpanded(false);
						setMinimumSize(new Dimension((int)upperPanel.getSize().getWidth(), (int) (lineHeight*1)));

					}
				}
			}
		});
	}

	private void setHeadline(PropertyCheckLabelHeadline propertyCheckLabelHeadline) {
		this.headline = propertyCheckLabelHeadline;

	}

	public PropertyCheckLabelHeadline getHeadline() {
		return this.headline;
	}

	public void setPropertyCheckProperties(NetCheckingProperties propertyCheckProperties) {
		this.propertyCheckProperties = propertyCheckProperties;
	}

	public NetCheckingProperties getPropertyCheckProperties() {
		return propertyCheckProperties;
	}

	PropertyCheckView getPropertyCheckView() {
		return this;
	}

	private void showCheckedProperties(boolean b) {
		if(errorButton != null)
		errorButton.setVisible(b);
		if(separator != null)
		separator.setVisible(b);
		if(separator0 != null)
		separator0.setVisible(b);
		if(separator1 != null)
		separator1.setVisible(b);
		if(separator2 != null)
		separator2.setVisible(b);
		if(separator3 != null)
		separator3.setVisible(b);
		if(separator4 != null)
		separator4.setVisible(b);

		if(labelCWNStructure != null)
		labelCWNStructure.setVisible(b);
		if(labelValidInOutPlaces != null)
		labelValidInOutPlaces.setVisible(b);
		if(labelStrongConnectedness != null)
		labelStrongConnectedness.setVisible(b);
		if(labelValidInitialMarking != null)
		labelValidInitialMarking.setVisible(b);
		if(labelControlFlowDependency != null)
		labelControlFlowDependency.setVisible(b);
		if(labelIsBounded != null)
		labelIsBounded.setVisible(b);
		if(labelOptionToComplete != null)
		labelOptionToComplete.setVisible(b);
		if(labelProperCompletion != null)
		labelProperCompletion.setVisible(b);
		if(labelNoDeadTransitions != null)
		labelNoDeadTransitions.setVisible(b);
	}

	public void updateCWNProperties(CWNProperties cwnProperties) {
		setPropertyCheckProperties(cwnProperties);
		labelCWNStructure.updatePropertyCheckingResult(cwnProperties.hasCWNStructure);
		labelValidInOutPlaces.updatePropertyCheckingResult(cwnProperties.validInOutPlaces);
		labelStrongConnectedness.updatePropertyCheckingResult(cwnProperties.strongConnectedness);
		labelValidInitialMarking.updatePropertyCheckingResult(cwnProperties.validInitialMarking);
		labelControlFlowDependency.updatePropertyCheckingResult(cwnProperties.controlFlowDependency);
		labelIsBounded.updatePropertyCheckingResult(cwnProperties.isBounded);
		labelOptionToComplete.updatePropertyCheckingResult(cwnProperties.optionToCompleteAndProperCompletion);
		labelProperCompletion.updatePropertyCheckingResult(cwnProperties.optionToCompleteAndProperCompletion);
		labelNoDeadTransitions.updatePropertyCheckingResult(cwnProperties.noDeadTransitions);

	}
	
	public void updateWFNetProperties(WFNetProperties wfNetProperties) {
		setPropertyCheckProperties(wfNetProperties);
		labelCWNStructure.updatePropertyCheckingResult(wfNetProperties.hasWFNetStructure);
		labelValidInOutPlaces.updatePropertyCheckingResult(wfNetProperties.validInOutPlaces);
		labelStrongConnectedness.updatePropertyCheckingResult(wfNetProperties.strongConnectedness);
		labelIsBounded.updatePropertyCheckingResult(wfNetProperties.isBounded);
	}


	public static void main(String[] args) {
		new DisplayFrame(new PropertyCheckView(new CWNProperties()), true);
	}

	public void updateBoundedness(PropertyCheckingResult isBounded, Exception exception) {
		getPropertyCheckProperties().isBounded = isBounded;
		labelIsBounded.updatePropertyCheckingResult(getPropertyCheckProperties().isBounded);
		if (exception != null)
		getPropertyCheckProperties().exception = exception;
	}

	public void updateCWNStructuredness(PropertyCheckingResult hasCWNStructure, PropertyCheckingResult validInOutPlaces, PropertyCheckingResult strongConnectedness,
			PropertyCheckingResult validInitialMarking, PropertyCheckingResult controlFlowDependency, Exception exception) {
		((CWNProperties)getPropertyCheckProperties()).hasCWNStructure = hasCWNStructure;
		labelCWNStructure.updatePropertyCheckingResult(((CWNProperties)getPropertyCheckProperties()).hasCWNStructure);
		((CWNProperties)getPropertyCheckProperties()).validInOutPlaces = validInOutPlaces;
		labelValidInOutPlaces.updatePropertyCheckingResult(((CWNProperties)getPropertyCheckProperties()).validInOutPlaces);
		((CWNProperties)getPropertyCheckProperties()).strongConnectedness = strongConnectedness;
		labelStrongConnectedness.updatePropertyCheckingResult(((CWNProperties)getPropertyCheckProperties()).strongConnectedness);
		((CWNProperties)getPropertyCheckProperties()).validInitialMarking = validInitialMarking;
		labelValidInitialMarking.updatePropertyCheckingResult(((CWNProperties)getPropertyCheckProperties()).validInitialMarking);
		((CWNProperties)getPropertyCheckProperties()).controlFlowDependency = controlFlowDependency;
		labelControlFlowDependency.updatePropertyCheckingResult(((CWNProperties)getPropertyCheckProperties()).controlFlowDependency);
		if (exception != null)
		getPropertyCheckProperties().exception = exception;

	}

	public void updateWFNetStructuredness(PropertyCheckingResult hasWFNetStructure, PropertyCheckingResult validInOutPlaces, PropertyCheckingResult strongConnectedness, Exception exception) {
		((WFNetProperties)getPropertyCheckProperties()).hasWFNetStructure = hasWFNetStructure;
		labelCWNStructure.updatePropertyCheckingResult(((WFNetProperties)getPropertyCheckProperties()).hasWFNetStructure);
		((WFNetProperties)getPropertyCheckProperties()).validInOutPlaces = validInOutPlaces;
		labelValidInOutPlaces.updatePropertyCheckingResult(((WFNetProperties)getPropertyCheckProperties()).validInOutPlaces);
		((WFNetProperties)getPropertyCheckProperties()).strongConnectedness = strongConnectedness;
		labelStrongConnectedness.updatePropertyCheckingResult(((WFNetProperties)getPropertyCheckProperties()).strongConnectedness);
		if (exception != null)
		getPropertyCheckProperties().exception = exception;
		
	}

}
