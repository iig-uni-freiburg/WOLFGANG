package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import javax.swing.SpringLayout;

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
        lblStructure = new PropertyCheckResultLabel("\u2022 WF Net Structure", PropertyCheckingResult.UNKNOWN);
        panel.add(lblStructure);
        JPanel structureSubPanel = new JPanel(new SpringLayout());
        panel.add(structureSubPanel);
        lblInOutPlaces = new PropertyCheckResultLabel("\u2022 Valid InOut Places", PropertyCheckingResult.UNKNOWN);
        structureSubPanel.add(lblInOutPlaces);
        lblConnectedness = new PropertyCheckResultLabel("\u2022 Strong Connectedness", PropertyCheckingResult.UNKNOWN);
        structureSubPanel.add(lblConnectedness);
        SpringUtilities.makeCompactGrid(structureSubPanel, structureSubPanel.getComponentCount(), 1, 15,0,0,0);
    }

    @Override
    public void resetFieldContent() {
                System.out.println("WFreset");

        lblStructure.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
        lblInOutPlaces.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
        lblConnectedness.updatePropertyCheckingResult(PropertyCheckingResult.UNKNOWN);
    }

    @Override
    public void updateFieldContent(WFNetProperties checkResult, Exception exception) {
        System.out.println("WFupdate");
        super.updateFieldContent(checkResult, exception);
        lblStructure.updatePropertyCheckingResult(checkResult.hasWFNetStructure);
        lblInOutPlaces.updatePropertyCheckingResult(checkResult.validInOutPlaces);
        lblConnectedness.updatePropertyCheckingResult(checkResult.strongConnectedness);
    }

    public static void main(String[] args) {
        WFNetPropertyCheckView view = new WFNetPropertyCheckView();
        view.setUpGui();
        view.updateFieldContent(new WFNetProperties(), null);
        new DisplayFrame(view, true);
    }

}
