package de.uni.freiburg.iig.telematik.wolfgang.properties;

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

	public PropertyCheckView(NetCheckingProperties propertyCheckProperties) {
		super(new SpringLayout());
		if(propertyCheckProperties instanceof CWNProperties)
			setUpCWNGui((CWNProperties) propertyCheckProperties);
		if(propertyCheckProperties instanceof WFNetProperties)
			setUpPTGui((WFNetProperties) propertyCheckProperties);
	

	}
	
	public void setUpPTGui(WFNetProperties propertyCheckProperties) {
		setPropertyCheckProperties(propertyCheckProperties);
		int initialX = 0;
		int initialY = 0;
		int xPad = 0;
		int yPad = 0;
		boolean isExpanded = true;
		String headline = "WF Net Check";
		setHeadline(new PropertyCheckLabelHeadline(headline, isExpanded));

		add(getHeadline());

		separator = new JPopupMenu.Separator();
		add(separator);

		errorButton = new JButton("Show Details");
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
		add(errorButton);
		separator0 = new JPopupMenu.Separator();
		add(separator0);

		labelCWNStructure = new PropertyCheckPropertiesLabel(" ● WF Net Structure", ((WFNetProperties)getPropertyCheckProperties()).hasWFNetStructure);
		add(labelCWNStructure);
		labelValidInOutPlaces = new PropertyCheckPropertiesLabel("    ● Valid InOut Places", ((WFNetProperties)getPropertyCheckProperties()).validInOutPlaces);
		add(labelValidInOutPlaces);
		labelStrongConnectedness = new PropertyCheckPropertiesLabel("    ● Strong Connectedness", ((WFNetProperties)getPropertyCheckProperties()).strongConnectedness);
		add(labelStrongConnectedness);
//		labelValidInitialMarking = new PropertyCheckPropertiesLabel("    ● Valid Initial Marking", getPropertyCheckProperties().validInitialMarking);
//		add(labelValidInitialMarking);
//		labelControlFlowDependency = new PropertyCheckPropertiesLabel("    ● Control Flow Dependency", getPropertyCheckProperties().controlFlowDependency);
//		add(labelControlFlowDependency);
//
		separator1 = new JPopupMenu.Separator();
		add(separator1);

		labelIsBounded = new PropertyCheckPropertiesLabel(" ● Is Bounded", getPropertyCheckProperties().isBounded);
		add(labelIsBounded);
//		separator2 = new JPopupMenu.Separator();
//		add(separator2);
//
//		labelOptionToComplete = new PropertyCheckPropertiesLabel(" ● Option To Complete", getPropertyCheckProperties().optionToCompleteAndProperCompletion);
//		add(labelOptionToComplete);
//		separator3 = new JPopupMenu.Separator();
//		add(separator3);
//		labelProperCompletion = new PropertyCheckPropertiesLabel(" ● Proper Completion", getPropertyCheckProperties().optionToCompleteAndProperCompletion);
//		add(labelProperCompletion);
//		separator4 = new JPopupMenu.Separator();
//		add(separator4);
//		labelNoDeadTransitions = new PropertyCheckPropertiesLabel(" ● No Dead Transitions", getPropertyCheckProperties().noDeadTransitions);
//		add(labelNoDeadTransitions);
		SpringUtilities.makeCompactGrid(getPropertyCheckView(), 9, 1, initialX, initialY, xPad, yPad);

		getHeadline().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!getHeadline().isExpanded()) {
						SpringUtilities.makeCompactGrid(getPropertyCheckView(), 9, 1, 0, 0, 0, 0);
						showCheckedProperties(true);
						getHeadline().setExpanded(true);

					} else {
						SpringUtilities.makeCompactGrid(getPropertyCheckView(), 1, 1, 0, 0, 0, 0);
						showCheckedProperties(false);
						getHeadline().setExpanded(false);

					}
				}
			}
		});
	}

	public void setUpCWNGui(CWNProperties propertyCheckProperties) {
		setPropertyCheckProperties(propertyCheckProperties);
		int initialX = 0;
		int initialY = 0;
		int xPad = 0;
		int yPad = 0;
		boolean isExpanded = true;
		String headline = "Colored WF Net Check";
		setHeadline(new PropertyCheckLabelHeadline(headline, isExpanded));

		add(getHeadline());

		separator = new JPopupMenu.Separator();
		add(separator);

		errorButton = new JButton("Show Details");
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
		add(errorButton);
		separator0 = new JPopupMenu.Separator();
		add(separator0);

		labelCWNStructure = new PropertyCheckPropertiesLabel(" ● CWN Structure", ((CWNProperties)getPropertyCheckProperties()).hasCWNStructure);
		add(labelCWNStructure);
		labelValidInOutPlaces = new PropertyCheckPropertiesLabel("    ● Valid InOut Places", ((CWNProperties)getPropertyCheckProperties()).validInOutPlaces);
		add(labelValidInOutPlaces);
		labelStrongConnectedness = new PropertyCheckPropertiesLabel("    ● Strong Connectedness", ((CWNProperties)getPropertyCheckProperties()).strongConnectedness);
		add(labelStrongConnectedness);
		labelValidInitialMarking = new PropertyCheckPropertiesLabel("    ● Valid Initial Marking", ((CWNProperties)getPropertyCheckProperties()).validInitialMarking);
		add(labelValidInitialMarking);
		labelControlFlowDependency = new PropertyCheckPropertiesLabel("    ● Control Flow Dependency", ((CWNProperties)getPropertyCheckProperties()).controlFlowDependency);
		add(labelControlFlowDependency);

		separator1 = new JPopupMenu.Separator();
		add(separator1);

		labelIsBounded = new PropertyCheckPropertiesLabel(" ● Is Bounded", getPropertyCheckProperties().isBounded);
		add(labelIsBounded);
		separator2 = new JPopupMenu.Separator();
		add(separator2);

		labelOptionToComplete = new PropertyCheckPropertiesLabel(" ● Option To Complete", ((CWNProperties)getPropertyCheckProperties()).optionToCompleteAndProperCompletion);
		add(labelOptionToComplete);
		separator3 = new JPopupMenu.Separator();
		add(separator3);
		labelProperCompletion = new PropertyCheckPropertiesLabel(" ● Proper Completion", ((CWNProperties)getPropertyCheckProperties()).optionToCompleteAndProperCompletion);
		add(labelProperCompletion);
		separator4 = new JPopupMenu.Separator();
		add(separator4);
		labelNoDeadTransitions = new PropertyCheckPropertiesLabel(" ● No Dead Transitions", ((CWNProperties)getPropertyCheckProperties()).noDeadTransitions);
		add(labelNoDeadTransitions);
		SpringUtilities.makeCompactGrid(getPropertyCheckView(), 17, 1, initialX, initialY, xPad, yPad);

		getHeadline().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!getHeadline().isExpanded()) {
						SpringUtilities.makeCompactGrid(getPropertyCheckView(), 17, 1, 0, 0, 0, 0);
						showCheckedProperties(true);
						getHeadline().setExpanded(true);

					} else {
						SpringUtilities.makeCompactGrid(getPropertyCheckView(), 1, 1, 0, 0, 0, 0);
						showCheckedProperties(false);
						getHeadline().setExpanded(false);

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
//		if(getPropertyCheckProperties().isBounded.equals(PropertyCheckingResult.TRUE))
//			cwnProperties.isBounded = getPropertyCheckProperties().isBounded;
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
//		if(getPropertyCheckProperties().isBounded.equals(PropertyCheckingResult.TRUE))
//			cwnProperties.isBounded = getPropertyCheckProperties().isBounded;
		setPropertyCheckProperties(wfNetProperties);
		labelCWNStructure.updatePropertyCheckingResult(wfNetProperties.hasWFNetStructure);
		labelValidInOutPlaces.updatePropertyCheckingResult(wfNetProperties.validInOutPlaces);
		labelStrongConnectedness.updatePropertyCheckingResult(wfNetProperties.strongConnectedness);
//		labelValidInitialMarking.updatePropertyCheckingResult(cwnProperties.validInitialMarking);
//		labelControlFlowDependency.updatePropertyCheckingResult(cwnProperties.controlFlowDependency);
		labelIsBounded.updatePropertyCheckingResult(wfNetProperties.isBounded);
//		labelOptionToComplete.updatePropertyCheckingResult(cwnProperties.optionToCompleteAndProperCompletion);
//		labelProperCompletion.updatePropertyCheckingResult(cwnProperties.optionToCompleteAndProperCompletion);
//		labelNoDeadTransitions.updatePropertyCheckingResult(cwnProperties.noDeadTransitions);

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
