package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontRotationAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.text.ShowHideTokensOnArcsAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphListener;

public class FontToolBar extends JToolBar implements PNGraphListener {

    private static final long serialVersionUID = -6491749112943066366L;
    Color bgcolor = UIManager.getColor("Panel.background");

    // Actions
    private ShowHideLabelsAction showHideLabelsAction;
    private ShowHideTokensOnArcsAction showHideTokensOnArcsAction;

    private FontBoldStyleAction boldFontAction;
    private FontItalicStyleAction italicFontAction;
    private FontRotationAction textRotationAction;
    private FontUnderlineStyleAction underlineFontAction;
    private FontLineThroughStyleAction lineThroughFontaction;
    private FontAlignLeftAction alignLeftAction;
    private FontAlignCenterAction alignCenterAction;
    private FontAlignRightAction alignRightAction;

    // Buttons
    private JButton btnShowHideTokensOnArcs;
    private JButton btnShowHideLabels;
    private JButton btnTextRotation;
    private JToggleButton tglBoldFont = null;
    private JToggleButton tglAlignCenter = null;
    private JToggleButton tglAlignRight = null;
    private JToggleButton tglItalicFont = null;
    private JToggleButton tglUnderlineFont = null;
    private JToggleButton tglAlignLeft = null;
    private JComboBox cmbFont = null;
    private JComboBox cmbFontSize = null;

    // Tooltips
    private final String showHideTokensOnArcsTooltip = "show/ hide constraints as tokens on arcs";
    private final String showHideLabelsTooltip = "show/ hide labels";
    private final String boldFontTooltip = "bold";
    private final String italicFontTooltip = "italic";
    private final String underlineFontTooltip = "underline";
    private final String alignRightTooltip = "right";
    private final String alingCenterTooltip = "center";
    private final String alignLeftTooltip = "left";
    private final String textRotationTooltip = "rotate text 90-Degrees";

    private final String fontTooltip = "choose fontfamily";
    private final String fontSizeTooltip = "fontsize";

    // further variables
    private PNEditorComponent pnEditor = null;
    private PNGraphCell selectedCell = null;

    private final String lblFontText = "Font:";

    private JLabel lblFont;

    public FontToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
        super(orientation);
        Validate.notNull(pnEditor);
        this.pnEditor = pnEditor;
        this.pnEditor.getGraphComponent().getGraph().addPNGraphListener(this);
        setFloatable(false);

        createFontActions(pnEditor);
        setUpGui();
    }

    private void setUpGui() {
    	btnShowHideTokensOnArcs = (JButton) add(showHideTokensOnArcsAction, false);
        setButtonSettings(btnShowHideTokensOnArcs);
        btnShowHideLabels = (JButton) add(showHideLabelsAction, false);
        setButtonSettings(btnShowHideLabels);
        lblFont = new JLabel(lblFontText);
        add(lblFont);
        add(getcmbFont());

        add(getcmbFontSize());

        addSeparator();
        tglBoldFont = (JToggleButton) add(boldFontAction, true);
        tglItalicFont = (JToggleButton) add(italicFontAction, true);

        tglUnderlineFont = (JToggleButton) add(underlineFontAction, true);

        addSeparator();

        tglAlignLeft = (JToggleButton) add(alignLeftAction, true);

        tglAlignCenter = (JToggleButton) add(alignCenterAction, true);

        tglAlignRight = (JToggleButton) add(alignRightAction, true);

        addSeparator();

        btnTextRotation = add(textRotationAction);

        addSeparator();
        setTooltips();
		
	}

    private void createFontActions(final PNEditorComponent pnEditor1) throws IOException, PropertyException {
        showHideTokensOnArcsAction = new ShowHideTokensOnArcsAction(pnEditor1);
        showHideTokensOnArcsAction.setFontToolbar(this);
        showHideLabelsAction = new ShowHideLabelsAction(pnEditor1);
        showHideLabelsAction.setFontToolbar(this);
        boldFontAction = new FontBoldStyleAction(pnEditor1);
        italicFontAction = new FontItalicStyleAction(pnEditor1);
        underlineFontAction = new FontUnderlineStyleAction(pnEditor1);
        lineThroughFontaction = new FontLineThroughStyleAction(pnEditor1);
        alignLeftAction = new FontAlignLeftAction(pnEditor1);
        alignCenterAction = new FontAlignCenterAction(pnEditor1);
        alignRightAction = new FontAlignRightAction(pnEditor1);
        textRotationAction = new FontRotationAction(pnEditor1);
    }

    private void setTooltips() {
        btnShowHideLabels.setToolTipText(showHideLabelsTooltip);
        cmbFont.setToolTipText(fontTooltip);
        cmbFontSize.setToolTipText(fontSizeTooltip);
        tglBoldFont.setToolTipText(boldFontTooltip);
        tglItalicFont.setToolTipText(italicFontTooltip);
        tglUnderlineFont.setToolTipText(underlineFontTooltip);
        tglAlignLeft.setToolTipText(alignLeftTooltip);
        tglAlignCenter.setToolTipText(alingCenterTooltip);
        tglAlignRight.setToolTipText(alignRightTooltip);
        btnTextRotation.setToolTipText(textRotationTooltip);
    }

    private void setButtonSelected(JToggleButton tgl) {
    	tgl.setOpaque(true);
    	tgl.setBackground(Color.LIGHT_GRAY);
    	tgl.setBorder(BorderFactory.createLineBorder(bgcolor));
    	tgl.setBorderPainted(true);
    }

    private void setButtonUnselected(JToggleButton tgl) {
    	tgl.setOpaque(true);
    	tgl.setBackground(bgcolor);
    	tgl.setBorder(BorderFactory.createLineBorder(bgcolor));
    	tgl.setBorderPainted(true);
    }

    private JComponent add(Action action, boolean asToggleButton) {
        if (!asToggleButton) {
            return super.add(action);
        }
        JToggleButton tgl = createToggleActionComponent(action);
        tgl.setAction(action);
        add(tgl);
        return tgl;
    }

    protected JToggleButton createToggleActionComponent(Action a) {
        JToggleButton tgl = new JToggleButton() {
            private static final long serialVersionUID = -3143341784881719155L;

            @Override
            protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                return super.createActionPropertyChangeListener(a);
            }
        };
        if (a != null && (a.getValue(Action.SMALL_ICON) != null || a.getValue(Action.LARGE_ICON_KEY) != null)) {
        	tgl.setHideActionText(true);
        }
        tgl.setHorizontalTextPosition(JButton.CENTER);
        tgl.setVerticalTextPosition(JButton.BOTTOM);
        tgl.setBorderPainted(false);
        return tgl;
    }

    private JComboBox getcmbFont() {
        if (cmbFont == null) {
            // Gets the list of available fonts from the local graphics
            // environment
            // and adds some frequently used fonts at the beginning of the list
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            List<String> fonts = new ArrayList<>();
            fonts.addAll(Arrays.asList(new String[]{"Helvetica", "Verdana", "Times New Roman", "Garamond", "Courier New", "-"}));
            fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));
            cmbFont = new JComboBox(fonts.toArray());
            cmbFont.setMinimumSize(new Dimension(200, 24));
            cmbFont.setPreferredSize(new Dimension(200, 24));
            cmbFont.setMaximumSize(new Dimension(200, 24));

            cmbFont.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
//                    if (selectedCell != null) {
                    String font = cmbFont.getSelectedItem().toString();
                    PNGraph graph = FontToolBar.this.pnEditor.getGraphComponent().getGraph();
                    try {
                        graph.setFontOfSelectedCellLabel(font);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(FontToolBar.this.pnEditor, "Cannot set cell-font: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
                    }
//                    }
                }
            });
        }
        return cmbFont;
    }

    private JComboBox getcmbFontSize() {
        if (cmbFontSize == null) {
            cmbFontSize = new JComboBox(new Object[]{"-", "6pt", "8pt", "9pt", "10pt", "11pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt"});
            cmbFontSize.setMinimumSize(new Dimension(100, 24));
            cmbFontSize.setPreferredSize(new Dimension(100, 24));
            cmbFontSize.setMaximumSize(new Dimension(100, 24));

            cmbFontSize.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    if (selectedCell != null) {
                    String fontSize = cmbFontSize.getSelectedItem().toString().replace("pt", "");
                    PNGraph graph = FontToolBar.this.pnEditor.getGraphComponent().getGraph();
                    try {
                        graph.setFontSizeOfSelectedCellLabel(fontSize);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(FontToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
                    }
//                    }
                }
            });
        }
        return cmbFontSize;
    }

    public void setFontEnabled(boolean b) {

        lblFont.setEnabled(b);
        getcmbFont().setEnabled(b);
        getcmbFontSize().setEnabled(b);
        boldFontAction.setEnabled(b);
        italicFontAction.setEnabled(b);
        underlineFontAction.setEnabled(b);
        lineThroughFontaction.setEnabled(b);
        alignLeftAction.setEnabled(b);
        alignCenterAction.setEnabled(b);
        alignRightAction.setEnabled(b);
        textRotationAction.setEnabled(b);

        if (b) {
            showHideLabelsAction.setShowIconImage();
        } else {
            showHideLabelsAction.setHideIconImage();
        }
    }

    private void setButtonSettings(final JButton btnButton) {
    	btnButton.setBorderPainted(false);
    	btnButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
            	btnButton.setBorderPainted(false);
                super.mouseReleased(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            	btnButton.setBorderPainted(true);
                super.mousePressed(e);
            }

        });
    }

    public void setTokenOnArcEnabled(boolean b) {

        if (b) {
            showHideTokensOnArcsAction.setShowIconImage();
        } else {
            showHideTokensOnArcsAction.setHideIconImage();
        }

    }

    @Override
    public void placeAdded(AbstractPlace place) {
    }

    @Override
    public void transitionAdded(AbstractTransition transition) {
    }

    @Override
    public void relationAdded(AbstractFlowRelation relation) {
    }

    @Override
    public void placeRemoved(AbstractPlace place) {
    }

    @Override
    public void transitionRemoved(AbstractTransition transition) {
    }

    @Override
    public void relationRemoved(AbstractFlowRelation relation) {
    }

    @Override
    public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
    }

    @Override
    public void placeCapacityChanged(String placeName, String color, int newCapacity) {
    }

    @Override
    public void constraintChanged(String flowRelation, Multiset constraint) {
    }

    @Override
    public void componentsSelected(Set<PNGraphCell> selectedComponents) {

        if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
            if (selectedComponents == null || selectedComponents.isEmpty()) {
                setFontEnabled(false);
                setVisible(false);
                this.selectedCell = null;
                return;
            }
            if (!selectedComponents.isEmpty()) {
               setVisible(true);

                String initialFontFamily = null;
                int isSameFontCounter = 0;
                String initialFontSize = null;
                int isSameFontSizeCounter = 0;
                int fontDisabledCounter = 0;
                int isBoldCounter = 0;
                int isItalicCounter = 0;
                int isUnderlinedCounter = 0;

                Align initialAlign = null;
                int initialAlignCounter = 0;
                double initialRotation = 0.0;
                int initialRotationCounter = 0;

                for (PNGraphCell cell : selectedComponents) {
                    Map<String, Object> style = pnEditor.getGraphComponent().getGraph().getCellStyle(cell);
                    if (style.containsKey("noLabel")) {
                        if (style.get("noLabel").equals("1")) {
                            fontDisabledCounter++;
                        } else {
                            AnnotationGraphics labelGraphics = getLabelGraphics(cell);
                            if (labelGraphics != null) {
                                Font fontForCell = labelGraphics.getFont();
                                if (fontForCell != null) {

                               //Check similarities in cell selection for:
                                    //Font-Family
                                    if (fontForCell.getFamily() != null) {
                                        if (initialFontFamily == null) {
                                            initialFontFamily = fontForCell.getFamily();
                                            isSameFontCounter = 1;
                                        } else if (fontForCell.getFamily().equals(initialFontFamily)) {
                                            isSameFontCounter++;
                                        }

                                    }

                                    //Font-Size
                                    if (fontForCell.getSize() != null) {
                                        if (initialFontSize == null) {
                                            initialFontSize = fontForCell.getSize();
                                            isSameFontSizeCounter = 1;
                                        } else if (fontForCell.getSize().equals(initialFontSize)) {
                                            isSameFontSizeCounter++;
                                        }
                                    }

                                    //Font-Weight
                                    if (fontForCell.getWeight() != null) {
                                        if (fontForCell.getWeight().equals("bold")) {
                                            isBoldCounter++;
                                        }
                                    }

                                    //Font.Style
                                    if (fontForCell.getStyle() != null) {
                                        if (fontForCell.getStyle().equals("italic")) {
                                            isItalicCounter++;
                                        }
                                    }

                                    //Font-Undelining
                                    if (fontForCell.getDecoration() != null) {
                                        if (fontForCell.getDecoration().equals(Decoration.UNDERLINE)) {
                                            isUnderlinedCounter++;
                                        }
                                    }

                                    //Font-Align
                                    if (fontForCell.getAlign() != null) {
                                        if (initialAlign == null) {
                                            initialAlign = fontForCell.getAlign();
                                            initialAlignCounter = 1;
                                        } else if (fontForCell.getAlign().equals(initialAlign)) {
                                            initialAlignCounter++;
                                        }

                                    }

                                    //Font-Rotation
                                    if (initialRotation == 0.0) {

                                        initialRotation = fontForCell.getRotation();
                                        initialRotationCounter = 1;
                                    } else if (fontForCell.getRotation() == initialRotation) {
                                        initialRotationCounter++;
                                    }

                                }
                            }
                        }
                    }
                }

                if (selectedComponents.size() == fontDisabledCounter) //componentSize must be >0
                {
                    setFontEnabled(false);
                    showHideLabelsAction.setHideIconImage();
                    btnShowHideLabels.repaint();

                } else {
                    setFontEnabled(true);
                    showHideLabelsAction.setShowIconImage();
                    btnShowHideLabels.repaint();

                    if (selectedComponents.size() == isSameFontCounter) //componentSize must be >0
                    {
                        getcmbFont().setSelectedItem(initialFontFamily);
                    } else {
                        getcmbFont().setSelectedItem("-");
                    }

                    if (selectedComponents.size() == isSameFontSizeCounter) //componentSize must be >0
                    {
                        getcmbFontSize().setSelectedItem(initialFontSize + "pt");
                    } else {
                        getcmbFontSize().setSelectedItem("-");
                    }

                    setFontButtonSelection(selectedComponents, isBoldCounter, tglBoldFont, boldFontAction);
                    setFontButtonSelection(selectedComponents, isItalicCounter, tglItalicFont, italicFontAction);
                    setFontButtonSelection(selectedComponents, isUnderlinedCounter, tglUnderlineFont, underlineFontAction);

                    setAlignButtonSelection(selectedComponents, initialAlignCounter, initialAlign);
                    if (selectedComponents.size() == initialRotationCounter && initialRotation > 0.0) //componentSize must be >0
                    {
                        btnTextRotation.setOpaque(true);
                        btnTextRotation.setBackground(Color.LIGHT_GRAY);
                        btnTextRotation.setBorder(BorderFactory.createLineBorder(bgcolor));
                        btnTextRotation.setBorderPainted(true);
                        textRotationAction.setSelectionState(true);
                        textRotationAction.setDegree(initialRotation);
                    } else {
                        btnTextRotation.setOpaque(true);
                        btnTextRotation.setBackground(bgcolor);
                        btnTextRotation.setBorder(BorderFactory.createLineBorder(bgcolor));
                        btnTextRotation.setBorderPainted(true);
                        textRotationAction.setSelectionState(false);
                        textRotationAction.setDegree(0.0);

                    }
                }
            }
        }
    }

    private AnnotationGraphics getLabelGraphics(PNGraphCell cell) {
        switch (cell.getType()) {
            case PLACE:
                return pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics(cell.getId());
            case TRANSITION:
                return pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics(cell.getId());
            case ARC:
                return pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics(cell.getId());
            default:
                break;
        }
        return null;
    }

    private void setAlignButtonSelection(Set<PNGraphCell> selectedComponents, int isInitialAlignCounter, Align initialAlign) {
        if (selectedComponents.size() == isInitialAlignCounter) //componentSize must be >0
        {
            switch (initialAlign) {
                case LEFT:
                    setButtonSelected(tglAlignLeft);
                    alignLeftAction.setSelectionState(true);
                    setButtonUnselected(tglAlignCenter);
                    alignCenterAction.setSelectionState(false);
                    setButtonUnselected(tglAlignRight);
                    alignRightAction.setSelectionState(false);
                    break;
                case CENTER:
                    setButtonUnselected(tglAlignLeft);
                    alignLeftAction.setSelectionState(false);
                    setButtonSelected(tglAlignCenter);
                    alignCenterAction.setSelectionState(true);
                    setButtonUnselected(tglAlignRight);
                    alignRightAction.setSelectionState(false);
                    break;
                case RIGHT:
                    setButtonUnselected(tglAlignLeft);
                    alignLeftAction.setSelectionState(false);
                    setButtonUnselected(tglAlignCenter);
                    alignCenterAction.setSelectionState(false);
                    setButtonSelected(tglAlignRight);
                    alignRightAction.setSelectionState(true);
                    break;
                default:
                    break;
            }
        } else {
            setButtonUnselected(tglAlignLeft);
            alignLeftAction.setSelectionState(false);
            setButtonUnselected(tglAlignCenter);
            alignCenterAction.setSelectionState(false);
            setButtonUnselected(tglAlignRight);
            alignRightAction.setSelectionState(false);
        }
    }

    private void setFontButtonSelection(Set<PNGraphCell> selectedComponents, int counter, JToggleButton tgl, AbstractPNEditorAction action) {
        if (selectedComponents.size() == counter) //componentSize must be >0
        {
            setButtonSelected(tgl);
            action.setSelectionState(true);
        } else {
            setButtonUnselected(tgl);
            action.setSelectionState(false);
        }
    }
    
    public static void main(String[] args) {
        try {
			JPanel pnl = new JPanel();
			pnl.add(new FontToolBar(new PTNetEditorComponent(), JToolBar.HORIZONTAL));
			new DisplayFrame(pnl, true);
		} catch (ParameterException | PropertyException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
