package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;

public class WFNetPropertyCheckView extends AbstractPropertyCheckView<WFNetProperties> {

	private static final long serialVersionUID = -950169446391727139L;
	
	private PropertyCheckResultLabel lblStructure;
	private PropertyCheckResultLabel lblInOutPlaces;
	private PropertyCheckResultLabel lblConnectedness;

	@Override
	protected String getHeadline() {
		return "WF-Net Check";
	}
	
	@Override
	protected void addSpecificFields(JPanel panel) {
		lblStructure = 		new PropertyCheckResultLabel(" \u2022 WF Net Structure", 			PropertyCheckingResult.UNKNOWN);
		panel.add(lblStructure);
		lblInOutPlaces = 	new PropertyCheckResultLabel("		\u2022 Valid InOut Places", 	PropertyCheckingResult.UNKNOWN);
		panel.add(lblInOutPlaces);
		lblConnectedness = 	new PropertyCheckResultLabel("		\u2022 Strong Connectedness", 	PropertyCheckingResult.UNKNOWN);
		panel.add(lblConnectedness);
	}
	
	@Override
	public void resetFieldContent() {
		lblStructure.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		lblInOutPlaces.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
		lblConnectedness.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
	}

	@Override
	public void updateFieldContent(WFNetProperties checkResult, Exception exception) {
		lblStructure.updatePropertyCheckingResult(checkResult.hasWFNetStructure);
		lblInOutPlaces.updatePropertyCheckingResult(checkResult.validInOutPlaces);
		lblConnectedness.updatePropertyCheckingResult(checkResult.strongConnectedness);
		this.exception = exception;
	}
	
	public static void main(String[] args) {
		WFNetPropertyCheckView view = new WFNetPropertyCheckView();
		view.setUpGui();
		view.updateFieldContent(new WFNetProperties(), null);
		new DisplayFrame(view, true);
	}

}
