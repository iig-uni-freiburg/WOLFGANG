package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

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
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PropertyCheckLabelHeadline;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PropertyCheckPropertiesLabel;

public class PTPropertyCheckView extends JPanel {

	private CWNProperties propertyCheckProperties;
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

	public PTPropertyCheckView(CWNProperties propertyCheckProperties) {
		super(new SpringLayout());
		setUpGui(propertyCheckProperties);

	}

	public void setUpGui(CWNProperties propertyCheckProperties) {
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

		labelCWNStructure = new PropertyCheckPropertiesLabel(" ● CWN Structure", getPropertyCheckProperties().hasCWNStructure);
		add(labelCWNStructure);
		labelValidInOutPlaces = new PropertyCheckPropertiesLabel("    ● Valid InOut Places", getPropertyCheckProperties().validInOutPlaces);
		add(labelValidInOutPlaces);
		labelStrongConnectedness = new PropertyCheckPropertiesLabel("    ● Strong Connectedness", getPropertyCheckProperties().strongConnectedness);
		add(labelStrongConnectedness);
		labelValidInitialMarking = new PropertyCheckPropertiesLabel("    ● Valid Initial Marking", getPropertyCheckProperties().validInitialMarking);
		add(labelValidInitialMarking);
		labelControlFlowDependency = new PropertyCheckPropertiesLabel("    ● Control Flow Dependency", getPropertyCheckProperties().controlFlowDependency);
		add(labelControlFlowDependency);

		separator1 = new JPopupMenu.Separator();
		add(separator1);

		labelIsBounded = new PropertyCheckPropertiesLabel(" ● Is Bounded", getPropertyCheckProperties().isBounded);
		add(labelIsBounded);
		separator2 = new JPopupMenu.Separator();
		add(separator2);

		labelOptionToComplete = new PropertyCheckPropertiesLabel(" ● Option To Complete", getPropertyCheckProperties().optionToCompleteAndProperCompletion);
		add(labelOptionToComplete);
		separator3 = new JPopupMenu.Separator();
		add(separator3);
		labelProperCompletion = new PropertyCheckPropertiesLabel(" ● Proper Completion", getPropertyCheckProperties().optionToCompleteAndProperCompletion);
		add(labelProperCompletion);
		separator4 = new JPopupMenu.Separator();
		add(separator4);
		labelNoDeadTransitions = new PropertyCheckPropertiesLabel(" ● No Dead Transitions", getPropertyCheckProperties().noDeadTransitions);
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

	public void setPropertyCheckProperties(CWNProperties propertyCheckProperties) {
		this.propertyCheckProperties = propertyCheckProperties;
	}

	public CWNProperties getPropertyCheckProperties() {
		return propertyCheckProperties;
	}

	PTPropertyCheckView getPropertyCheckView() {
		return this;
	}

	private void showCheckedProperties(boolean b) {
		errorButton.setVisible(b);

		separator.setVisible(b);
		separator0.setVisible(b);
		separator1.setVisible(b);
		separator2.setVisible(b);
		separator3.setVisible(b);
		separator4.setVisible(b);

		labelCWNStructure.setVisible(b);
		labelValidInOutPlaces.setVisible(b);
		labelStrongConnectedness.setVisible(b);
		labelValidInitialMarking.setVisible(b);
		labelControlFlowDependency.setVisible(b);
		labelIsBounded.setVisible(b);
		labelOptionToComplete.setVisible(b);
		labelProperCompletion.setVisible(b);
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

	public static void main(String[] args) {
		new DisplayFrame(new PTPropertyCheckView(new CWNProperties()), true);
	}

	public void updateBoundedness(PropertyCheckingResult isBounded, Exception exception) {
		getPropertyCheckProperties().isBounded = isBounded;
		labelIsBounded.updatePropertyCheckingResult(getPropertyCheckProperties().isBounded);
		if (exception != null)
		getPropertyCheckProperties().exception = exception;
	}

	public void updateCWNStructuredness(PropertyCheckingResult hasCWNStructure, PropertyCheckingResult validInOutPlaces, PropertyCheckingResult strongConnectedness,
			PropertyCheckingResult validInitialMarking, PropertyCheckingResult controlFlowDependency, Exception exception) {
		getPropertyCheckProperties().hasCWNStructure = hasCWNStructure;
		labelCWNStructure.updatePropertyCheckingResult(getPropertyCheckProperties().hasCWNStructure);
		getPropertyCheckProperties().validInOutPlaces = validInOutPlaces;
		labelValidInOutPlaces.updatePropertyCheckingResult(getPropertyCheckProperties().validInOutPlaces);
		getPropertyCheckProperties().strongConnectedness = strongConnectedness;
		labelStrongConnectedness.updatePropertyCheckingResult(getPropertyCheckProperties().strongConnectedness);
		getPropertyCheckProperties().validInitialMarking = validInitialMarking;
		labelValidInitialMarking.updatePropertyCheckingResult(getPropertyCheckProperties().validInitialMarking);
		getPropertyCheckProperties().controlFlowDependency = controlFlowDependency;
		labelControlFlowDependency.updatePropertyCheckingResult(getPropertyCheckProperties().controlFlowDependency);
		if (exception != null)
		getPropertyCheckProperties().exception = exception;

	}

	public void updateCWNStructuredness(PropertyCheckingResult hasWFNetStructure, PropertyCheckingResult validInOutPlaces, PropertyCheckingResult strongConnectedness, Exception exception) {
		// TODO Auto-generated method stub
		
	}

}
