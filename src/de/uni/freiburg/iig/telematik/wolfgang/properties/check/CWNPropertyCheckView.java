package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class CWNPropertyCheckView extends AbstractPropertyCheckView<CWNProperties> {

	private static final long serialVersionUID = -950169446391727139L;

	private PropertyCheckResultLabel lblStructure;
	private PropertyCheckResultLabel lblInOutPlaces;
	private PropertyCheckResultLabel lblConnectedness;
	private PropertyCheckResultLabel lblValidMarking;
	private PropertyCheckResultLabel lblCFDependency;
	private PropertyCheckResultLabel lblNoDeadTransitions;
	private PropertyCheckResultLabel lblCompletion;
	private PropertyCheckResultLabel lblOptionComplete;
	private PropertyCheckResultLabel lblBounded;

	@Override
	protected String getHeadline() {
		return "Colored WF Net Check";
	}

	@Override
	protected void addSpecificFields(JPanel panel) {
		lblStructure = new PropertyCheckResultLabel(" \u2022 CWN Structure", PropertyCheckingResult.UNKNOWN);
		panel.add(lblStructure);
		lblInOutPlaces = new PropertyCheckResultLabel("		\u2022 Valid InOut Places", PropertyCheckingResult.UNKNOWN);
		panel.add(lblInOutPlaces);
		lblConnectedness = new PropertyCheckResultLabel(" 	\u2022 Strong Connectedness", PropertyCheckingResult.UNKNOWN);
		panel.add(lblConnectedness);
		lblValidMarking = new PropertyCheckResultLabel(" 	\u2022 Valid Initial Marking", PropertyCheckingResult.UNKNOWN);
		panel.add(lblValidMarking);
		lblCFDependency = new PropertyCheckResultLabel(" 	\u2022 Control Flow Dependency", PropertyCheckingResult.UNKNOWN);
		panel.add(lblCFDependency);
		panel.add(new JPopupMenu.Separator());
		lblBounded = new PropertyCheckResultLabel(" \u2022 Is Bounded", PropertyCheckingResult.UNKNOWN);
		panel.add(lblBounded);
		panel.add(new JPopupMenu.Separator());
		lblOptionComplete = new PropertyCheckResultLabel(" \u2022 Option To Complete", PropertyCheckingResult.UNKNOWN);
		panel.add(lblOptionComplete);
		panel.add(new JPopupMenu.Separator());
		lblCompletion = new PropertyCheckResultLabel(" \u2022 Proper Completion", PropertyCheckingResult.UNKNOWN);
		panel.add(lblCompletion);
		panel.add(new JPopupMenu.Separator());
		lblNoDeadTransitions = new PropertyCheckResultLabel(" \u2022 No Dead Transitions", PropertyCheckingResult.UNKNOWN);
		panel.add(lblNoDeadTransitions);
	}

	@Override
	public void resetFieldContent() {
		if (lblStructure != null)
			lblStructure.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblInOutPlaces != null)
			lblInOutPlaces.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblConnectedness != null)
			lblConnectedness.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblValidMarking != null)
			lblValidMarking.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblCFDependency != null)
			lblCFDependency.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblBounded != null)
			lblBounded.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblOptionComplete != null)
			lblOptionComplete.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblCompletion != null)
			lblCompletion.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		if (lblNoDeadTransitions != null)
			lblNoDeadTransitions.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);

	}

	@Override
	public void updateFieldContent(CWNProperties checkResult, Exception exception) {
		lblStructure.updatePropertyCheckingResult(checkResult.hasCWNStructure);
		lblInOutPlaces.updatePropertyCheckingResult(checkResult.validInOutPlaces);
		lblConnectedness.updatePropertyCheckingResult(checkResult.strongConnectedness);
		lblValidMarking.updatePropertyCheckingResult(checkResult.validInitialMarking);
		lblCFDependency.updatePropertyCheckingResult(checkResult.controlFlowDependency);
		lblBounded.updatePropertyCheckingResult(checkResult.isBounded);
		lblOptionComplete.updatePropertyCheckingResult(checkResult.optionToCompleteAndProperCompletion);
		lblCompletion.updatePropertyCheckingResult(checkResult.optionToCompleteAndProperCompletion);
		lblNoDeadTransitions.updatePropertyCheckingResult(checkResult.noDeadTransitions);
		this.exception = exception;
	}

	public static void main(String[] args) {
		CWNPropertyCheckView view = new CWNPropertyCheckView();
		view.setUpGui();
		view.updateFieldContent(new CWNProperties(), null);
		new DisplayFrame(view, true);
	}

}
