package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ExceptionDialog;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillGradientRotationDiagonal;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillGradientRotationHorizontal;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillGradientRotationVertical;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillNoFillAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineCurveAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineDashAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineDotAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineLineAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineNoFillAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineShapeAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineSolidAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineStyleAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphListener;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;

public class GraphicsToolBar extends JToolBar implements PNGraphListener {

	private static final long serialVersionUID = -6491749112943066366L;

	// Actions

	private Action strokeColorAction;
	private Action gradientVerticalAction;
	private Action gradientHorizontalAction;
	private Action gradientDiagonalAction;

	private FillNoFillAction noFillAction;
	private LineNoFillAction noLineAction;

	private LineStyleAction lineStyleAction;
	private FillColorSelectionAction colorSelectionAction;
	private LineShapeAction lineShapeAction;
	private FillBackgroundColorAction backgroundColorAction;
	private FillGradientColorAction gradientColorAction;

	// Buttons
	private JButton backgroundColorButton;
	private JButton gradientColorButton;
	private JButton colorSelectionButton;
	private JToggleButton gradientDiagonalButton;
	private JToggleButton gradientHorizontalButton;
	private JToggleButton gradientVerticalButton;
	private JButton noFillButton;
	private ButtonGroup fillGroup;
	private ButtonGroup gradientDirectionGroup;
	private JComboBox strokeWeightBox;

	// Tooltips

	private String backgroundColorTooltip = "fill color";
	private String gradientColorTooltip = "gradient color";
	private String noFillTooltip = "no fill";
	private String noLineTooltip = "no line";
	private String colorSelectionTooltip = " select color";
	private String gradientDiagonalTooltip = "diagonal rotation";
	private String gradientHorizontalTooltip = "hirizontal rotation";
	private String gradientVerticalTooltip = "vertical rotation";

	private String strokeWeightTooltip = "set strokeweight";
	private String strokeColorTooltip = "set stroke color";
	private String lineStyleTooltip = "switch solid/ dash/ dot";
	private String lineShapeTooltip = "switch line/curve";
	private String curveTooltip = "curve";
	private String lineTooltip = "line";
	private String lineSolidTooltip = "solid";
	private String lineDashTooltip = "dash";
	private String lineDotTooltip = "dot";

	// further variables
	private PNEditorComponent pnEditor = null;
	private PNGraphCell selectedCell = null;
	private FillStyle fillStyle = FillStyle.SOLID;
	private LineStyle lineStyle = LineStyle.NORMAL;
	private Color currentFillColor;

	private JLabel fillLabel;

	private String fontLabelText = "Font:";
	private String fillLabelText = "Fill:";
	private String lineLabelText = "Line:";

	private JLabel lineLabel;

	private JLabel fontLabel;
	private JToggleButton lineSolidButton;
	private JToggleButton lineDashButton;
	private JToggleButton lineDotButton;
	private JToggleButton lineButton;
	private JToggleButton curveButton;
	private JToggleButton noLineButton;
	private LineLineAction lineAction;

	private LineDashAction lineDashAction;
	private LineSolidAction lineSolidAction;
	private LineDotAction lineDotAction;
	private LineColorSelectionAction lineColorSelectionAction;
	private LineCurveAction curveAction;
	private ButtonGroup lineGroup;
	private ButtonGroup lineStyleGroup;
	private JToggleButton lineColorSelectionButton;
	private int strokeWeight = (int) Line.DEFAULT_WIDTH;

	public GraphicsToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);
		this.pnEditor = pnEditor;
		this.pnEditor.getGraphComponent().getGraph().addPNGraphListener(this);
		strokeColorAction = new LineStrokeColorAction(pnEditor);
		backgroundColorAction = new FillBackgroundColorAction(pnEditor);
		gradientColorAction = new FillGradientColorAction(pnEditor);
		lineStyleAction = new LineStyleAction(pnEditor);

		lineShapeAction = new LineShapeAction(pnEditor);

		gradientHorizontalAction = new FillGradientRotationHorizontal(pnEditor);
		gradientVerticalAction = new FillGradientRotationVertical(pnEditor);
		gradientDiagonalAction = new FillGradientRotationDiagonal(pnEditor);
		colorSelectionAction = new FillColorSelectionAction(pnEditor);

		lineAction = new LineLineAction(pnEditor);
		curveAction = new LineCurveAction(pnEditor);
		noLineAction = new LineNoFillAction(pnEditor);
		lineDashAction = new LineDashAction(pnEditor);
		lineSolidAction = new LineSolidAction(pnEditor);
		lineDotAction = new LineDotAction(pnEditor);
		lineColorSelectionAction = new LineColorSelectionAction(pnEditor);

		noFillAction = new FillNoFillAction(pnEditor);

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
		setFloatable(false);

		fillLabel = new JLabel(fillLabelText);
		add(fillLabel);
		backgroundColorButton = (JButton) add(backgroundColorAction);
		gradientColorButton = (JButton) add(gradientColorAction);
		noFillButton = (JButton) add(noFillAction);

		fillGroup = new ButtonGroup();
		fillGroup.add(backgroundColorButton);
		fillGroup.add(gradientColorButton);
		fillGroup.add(noFillButton);

		gradientVerticalButton = (JToggleButton) nestedAdd(gradientVerticalAction);
		gradientHorizontalButton = (JToggleButton) nestedAdd(gradientHorizontalAction);
		gradientDiagonalButton = (JToggleButton) nestedAdd(gradientDiagonalAction);

		gradientDirectionGroup = new ButtonGroup();
		gradientDirectionGroup.add(gradientVerticalButton);
		gradientDirectionGroup.add(gradientHorizontalButton);
		gradientDirectionGroup.add(gradientDiagonalButton);

		colorSelectionButton = (JButton) add(colorSelectionAction);

		setUpFillPanel();
		addSeparator();

		lineLabel = new JLabel(lineLabelText);
		add(lineLabel);
		lineButton = (JToggleButton) nestedAdd(lineAction);
		curveButton = (JToggleButton) nestedAdd(curveAction);
		noLineButton = (JToggleButton) nestedAdd(noLineAction);

		lineGroup = new ButtonGroup();
		lineGroup.add(lineButton);
		lineGroup.add(curveButton);

		lineSolidButton = (JToggleButton) nestedAdd(lineSolidAction);
		lineDashButton = (JToggleButton) nestedAdd(lineDashAction);
		lineDotButton = (JToggleButton) nestedAdd(lineDotAction);

		lineStyleGroup = new ButtonGroup();
		lineStyleGroup.add(lineSolidButton);
		lineStyleGroup.add(lineDashButton);
		lineStyleGroup.add(lineDotButton);

		lineColorSelectionButton = (JToggleButton) nestedAdd(lineColorSelectionAction);

		setUpLinePanel();

		strokeWeightBox = getStrokeWeightBox();
		add(strokeWeightBox);

		addSeparator();

		deactivate();

		backgroundColorButton.setToolTipText(backgroundColorTooltip);
		gradientColorButton.setToolTipText(gradientColorTooltip);
		noFillButton.setToolTipText(noFillTooltip);
		gradientVerticalButton.setToolTipText(gradientVerticalTooltip);
		gradientHorizontalButton.setToolTipText(gradientHorizontalTooltip);
		gradientDiagonalButton.setToolTipText(gradientDiagonalTooltip);
		colorSelectionButton.setToolTipText(colorSelectionTooltip);
		noLineButton.setToolTipText(noLineTooltip);
		strokeWeightBox.setToolTipText(strokeWeightTooltip);
		lineButton.setToolTipText(lineTooltip);
		curveButton.setToolTipText(curveTooltip);
		noLineButton.setToolTipText(noLineTooltip);
		lineSolidButton.setToolTipText(lineSolidTooltip);
		lineDashButton.setToolTipText(lineDashTooltip);
		lineDotButton.setToolTipText(lineDotTooltip);
		lineColorSelectionButton.setToolTipText(strokeColorTooltip);
	}

	private void setUpFillPanel() {
		int iconSize = 0;
		try {
			iconSize = EditorProperties.getInstance().getIconSize().getSize();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		pane.setMinimumSize(new Dimension(iconSize * 2, iconSize));
		pane.setPreferredSize(new Dimension(iconSize * 2, iconSize));
		pane.setMaximumSize(new Dimension(iconSize * 2, iconSize));

		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 0.5;
		c.weighty = 0.3;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(backgroundColorButton, c);

		c.weighty = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(gradientColorButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(noFillButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 0;
		pane.add(gradientVerticalButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		pane.add(gradientHorizontalButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		pane.add(gradientDiagonalButton, c);

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 0; //reset to default
		c.weighty = 1.0; // request any extra vertical space
		// c.anchor = GridBagConstraints.LAST_LINE_START; //bottom of space
		// c.insets = new Insets(10,0,0,0); //top padding
		c.gridx = 1;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.gridy = 0;
		pane.add(colorSelectionButton, c);

		add(pane);
	}

	private void setUpLinePanel() {
		int iconSize = 0;
		try {
			iconSize = EditorProperties.getInstance().getIconSize().getSize();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		pane.setMinimumSize(new Dimension(iconSize * 2, iconSize));
		pane.setPreferredSize(new Dimension(iconSize * 2, iconSize));
		pane.setMaximumSize(new Dimension(iconSize * 2, iconSize));

		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 0.5;
		c.weighty = 0.3;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(lineButton, c);

		c.weighty = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(curveButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(noLineButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 0;
		pane.add(lineSolidButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		pane.add(lineDashButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		pane.add(lineDotButton, c);

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 0; //reset to default
		c.weighty = 1.0; // request any extra vertical space
		// c.anchor = GridBagConstraints.LAST_LINE_START; //bottom of space
		// c.insets = new Insets(10,0,0,0); //top padding
		c.gridx = 1;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.gridy = 0;
		pane.add(lineColorSelectionButton, c);

		add(pane);
	}

	private JComponent nestedAdd(Action action) {
		JToggleButton b = createToggleActionComponent(action);
		b.setAction(action);
		return b;
	}

	protected JToggleButton createToggleActionComponent(Action a) {
		JToggleButton b = new JToggleButton() {
			private static final long serialVersionUID = -3143341784881719155L;

			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
				return super.createActionPropertyChangeListener(a);
			}
		};
		if (a != null && (a.getValue(Action.SMALL_ICON) != null || a.getValue(Action.LARGE_ICON_KEY) != null)) {
			b.setHideActionText(true);
		}
		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		b.setBorderPainted(false);
		return b;
	}

	private JComboBox getStrokeWeightBox() {
		if (strokeWeightBox == null) {
			strokeWeightBox = new JComboBox(new Object[] { "0px", "1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px", "11px", "12px" });
			strokeWeightBox.setSelectedIndex(1);
			strokeWeightBox.setMinimumSize(new Dimension(100, 24));
			strokeWeightBox.setPreferredSize(new Dimension(100, 24));
			strokeWeightBox.setMaximumSize(new Dimension(100, 24));
			add(strokeWeightBox);

			strokeWeightBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof JComboBox) {
						if (selectedCell != null) {
							String strokeWeight = strokeWeightBox.getSelectedItem().toString().replace("px", "");
							PNGraph graph = GraphicsToolBar.this.pnEditor.getGraphComponent().getGraph();
							graph.setStrokeWeightOfSelectedCell(strokeWeight);
						}
					}
				}
			});
		}
		return strokeWeightBox;
	}

	public void deactivate() {
		setLineEnabled(false);
		setFillEnabled(false);
	}

	private void setLineEnabled(boolean b) {
		lineLabel.setEnabled(b);
		strokeColorAction.setEnabled(b);
		noLineAction.setEnabled(b);
		getStrokeWeightBox().setEnabled(b);
		lineStyleAction.setEnabled(b);
		lineShapeAction.setEnabled(b);
		lineDotAction.setEnabled(b);
		lineDashAction.setEnabled(b);
		lineShapeAction.setEnabled(b);
		lineAction.setEnabled(b);
		curveAction.setEnabled(b);
		lineColorSelectionAction.setEnabled(b);
		noLineAction.setEnabled(b);
	}

	private void setFillEnabled(boolean b) {
		fillLabel.setEnabled(b);
		backgroundColorAction.setEnabled(b);
		backgroundColorAction.setEnabled(b);
		gradientColorAction.setEnabled(b);
		noFillAction.setEnabled(b);
		gradientDiagonalAction.setEnabled(b);
		gradientHorizontalAction.setEnabled(b);
		gradientVerticalAction.setEnabled(b);
		colorSelectionAction.setEnabled(b);
	}


	// private void setFillToolbar(Fill fill, boolean isLabel) throws
	// PropertyException, IOException {
	// boolean isFillSolid = false;
	// boolean isFillGradient = false;
	// boolean isFillEmpty = false;
	// boolean isGradientDiagonal = false;
	// boolean isGradientVertical = false;
	// boolean isGradientHorizontal = false;
	// boolean containsFillColor = false;
	// boolean containsGradientColor = false;
	// boolean containsGradientRotation = false;
	//
	// if (fill != null) {
	// String colorString = fill.getColor();
	// String gradientString = fill.getGradientColor();
	// GradientRotation gradientRotation = fill.getGradientRotation();
	//
	//
	//
	// Color fillColor = null;
	// if (colorString != null) {
	// if (!colorString.equals("transparent") && (colorString != null)) {
	// fillColor = Utils.parseColor(colorString);
	// containsFillColor = true;
	// } else {
	// fillColor = EditorProperties.getInstance().getDefaultPlaceColor();
	// }
	// }
	//
	//
	// Color gradientColor;
	// if (!gradientString.equals("transparent") && (gradientString != null)) {
	// gradientColor = Utils.parseColor(gradientString);
	// containsGradientColor = true;
	// }
	// else {
	// gradientColor =
	// EditorProperties.getInstance().getDefaultGradientColor();
	// if(gradientColor == null)
	// gradientColor = fillColor;
	// }
	//
	// if (gradientRotation != null) {
	// containsGradientRotation = true;
	// } else{
	// gradientRotation =
	// EditorProperties.getInstance().getDefaultGradientDirection();
	// if(gradientRotation == null)
	// gradientRotation = GradientRotation.VERTICAL;
	// }
	//
	// if (!containsFillColor) {
	// // setFillStyle(FillStyle.NOFILL, null, null, null);
	// isFillEmpty = true;
	//
	// } else if (containsFillColor && containsGradientColor &&
	// containsGradientRotation && !isLabel) {
	// // setFillStyle(FillStyle.GRADIENT, fillColor, gradientColor,
	// gradientRotation);
	// isFillGradient = true;
	// } else {
	// // setFillStyle(FillStyle.SOLID, fillColor, fillColor, gradientRotation);
	// isFillSolid = true;
	// }
	// // currentFillColor = fillColor;
	//
	//
	// if (containsGradientRotation) {
	// switch (gradientRotation) {
	// case DIAGONAL:
	// isGradientDiagonal = true;
	// break;
	// case HORIZONTAL:
	// isGradientHorizontal = true;
	// break;
	// case VERTICAL:
	// isGradientVertical = true;
	// break;
	// default:
	// break;
	//
	// }
	// }

	// fillGroup.clearSelection();
	// backgroundColorButton.setSelected(isFillSolid);
	// gradientColorButton.setSelected(true);
	// noFillButton.setSelected(isFillEmpty);
	//
	// gradientDirectionGroup.clearSelection();
	// gradientDiagonalButton.setSelected(isGradientDiagonal);
	// gradientHorizontalButton.setSelected(isGradientHorizontal);
	// gradientVerticalButton.setSelected(isGradientVertical);
	//
	// gradientColorButton.repaint();
	// backgroundColorButton.repaint();
	// colorSelectionButton.repaint();
	// backgroundColorButton.repaint();
	// }
	//
	// }

	private void setFillToolbar(Fill fill, boolean isLabel) throws PropertyException, IOException {
		Validate.notNull(fill);
		Color fillcolor = Utils.parseColor(fill.getColor());

		if (fill.getGradientColor() != null && fill.getGradientRotation() != null) {
			Color gradientColor = Utils.parseColor(fill.getGradientColor());
			colorSelectionAction.setFillColor(fillcolor, gradientColor, fill.getGradientRotation());
		} else {
			colorSelectionAction.setFillColor(fillcolor);
			gradientColorAction.setFillColor(fillcolor, gradientColorAction.getButtonGradientColor(), fill.getGradientRotation());
		}
		backgroundColorAction.setFillColor(fillcolor);
		colorSelectionButton.repaint();
		gradientColorButton.repaint();
		backgroundColorButton.repaint();
	}

	private void setLineStyle(LineStyle nofill, Color fillColor, Style linestyle, boolean isLineCurve) throws ParameterException, PropertyException, IOException {
		switch (nofill) {
		case NOFILL:
			lineColorSelectionAction.setNoFill();
			break;
		case NORMAL:
			if (fillColor != null)
				lineColorSelectionAction.setLineColor(fillColor, 1.0, linestyle, isLineCurve);
			break;
		default:
			break;
		}

		// colorSelectionButton.repaint();
	}

	private void setLineToolbar(Line line) throws ParameterException, PropertyException, IOException {
		boolean isLineEmpty = false;
		boolean isLineLine = false;
		boolean isLineCurve = false;
		boolean isLineSolid = false;
		boolean isLineDashed = false;
		boolean isLineDotted = false;
		Color fillColor = null;
		if (line != null) {
			String lineColorString = line.getColor();
			Style lineStyle = line.getStyle();
			Shape lineShape = line.getShape();

			boolean containsLineColor = false;
			boolean containsStyle = false;

			switch (lineShape) {
			case CURVE:
				isLineCurve = true;
				break;
			case LINE:
				isLineLine = true;
				break;
			}
			if (lineColorString != null) {
				if (!lineColorString.equals("transparent")) {
					fillColor = Utils.parseColor(lineColorString);
					containsLineColor = true;
				} else {
					containsLineColor = false;
				}
			}
			if (fillColor == null)
				containsLineColor = false;

			if (lineStyle != null) {
				containsStyle = true;
			} else
				lineStyle = Style.SOLID;

			if (!containsLineColor) {

				setLineStyle(LineStyle.NOFILL, null, lineStyle, isLineCurve);
				isLineEmpty = true;

			} else {
				setLineStyle(LineStyle.NORMAL, fillColor, lineStyle, isLineCurve);

			}
			currentFillColor = fillColor;
			if (containsStyle) {
				switch (lineStyle) {
				case DASH:
					isLineDashed = true;
					break;
				case DOT:
					isLineDotted = true;
					break;
				case SOLID:
					isLineSolid = true;
					break;

				}
			}

			curveButton.repaint();
			lineButton.repaint();
			lineColorSelectionButton.repaint();

			lineGroup.clearSelection();
			lineButton.setSelected(isLineLine);
			curveButton.setSelected(isLineCurve);
			noLineButton.setSelected(isLineEmpty);

			lineStyleGroup.clearSelection();
			lineDotButton.setSelected(isLineDotted);
			lineDashButton.setSelected(isLineDashed);
			lineSolidButton.setSelected(isLineSolid);
			lineButton.repaint();

			strokeWeight = (int) line.getWidth();
			getStrokeWeightBox().setSelectedItem(strokeWeight + "px");

		}
	}

	public FillStyle getFillStyle() {
		return fillStyle;
	}

	// public void setFillStyle(FillStyle fillStyle, Color fillColor, Color
	// gradientColor, GradientRotation rotation) throws PropertyException,
	// IOException {
	// this.fillStyle = fillStyle;
	// switch (fillStyle) {
	//
	// case SOLID:
	// if (fillColor != null)
	// backgroundColorButton.setSelected(true);
	// colorSelectionAction.setFillColor(fillColor, fillColor, rotation);
	//
	// break;
	// case GRADIENT:
	// if (gradientColor != null) {
	// colorSelectionAction.setFillColor(fillColor, gradientColor, rotation);
	// // gradientColorAction.setFillColor(gradientColor);
	// } else {
	// colorSelectionAction.setFillColor(fillColor,
	// EditorProperties.getInstance().getDefaultGradientColor(),
	// GradientRotation.VERTICAL);
	// }
	// // gradientColorButton.setSelected(true);
	// break;
	// case NOFILL:
	// noFillButton.setSelected(true);
	// colorSelectionAction.setNoFill();
	// break;
	// default:
	// break;
	//
	// }
	// if (fillColor != null)
	// backgroundColorAction.setFillColor(fillColor);
	// if (fillColor == gradientColor)
	// gradientColor =
	// EditorProperties.getInstance().getDefaultGradientColor();
	// if (fillColor != null && gradientColor != null)
	// gradientColorAction.setFillColor(fillColor, gradientColor);
	//
	// colorSelectionButton.repaint();
	//
	// }

	public enum FillStyle {
		SOLID, GRADIENT, NOFILL
	}

	public void setFillStyle(FillStyle style) {
		this.fillStyle = style;

	}

	public enum LineStyle {
		NORMAL, NOFILL
	}

	public void setLineStyle(LineStyle nofill) {
		this.lineStyle = nofill;

	}

	@Override
	public void placeAdded(AbstractPlace place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transitionAdded(AbstractTransition transition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void relationAdded(AbstractFlowRelation relation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeRemoved(AbstractPlace place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transitionRemoved(AbstractTransition transition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void relationRemoved(AbstractFlowRelation relation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeCapacityChanged(String placeName, String color, int newCapacity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void constraintChanged(String flowRelation, Multiset constraint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentsSelected(Set<PNGraphCell> selectedComponents) {
		if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
			if (selectedComponents == null || selectedComponents.isEmpty()) {
				deactivate();
				this.selectedCell = null;
				return;
			}
			if (!selectedComponents.isEmpty()) {

				if (selectedComponents.size() >= 1) {
					this.selectedCell = selectedComponents.iterator().next();
					boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
					boolean isTransitionSilent = false;
					if (isTransitionCell) {
						if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().containsTransition(selectedCell.getId()))
							isTransitionSilent = pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getTransition(selectedCell.getId()).isSilent();
					}
					boolean isArcCell = selectedCell.getType() == PNComponent.ARC;
					boolean labelSelected = pnEditor.getGraphComponent().getGraph().isLabelSelected();

					NodeGraphics nodeGraphics = null;
					AnnotationGraphics annotationGraphics = null;
					ArcGraphics arcGraphics = null;
					if (!isTransitionSilent) {
						setLineEnabled(true);
						setFillEnabled(true);
						switch (selectedCell.getType()) {
						case PLACE:
							nodeGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(selectedCell.getId());
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(selectedCell.getId());
							break;
						case TRANSITION:
							nodeGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(selectedCell.getId());
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(selectedCell.getId());
							break;
						case ARC:
							arcGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcGraphics().get(selectedCell.getId());
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(selectedCell.getId());
							break;
						}
						try {
							if (nodeGraphics != null && !labelSelected) {

								Fill fill = nodeGraphics.getFill();
								setFillToolbar(fill, false);
								Line line = nodeGraphics.getLine();
								setLineToolbar(line);

							}

							if (arcGraphics != null && isArcCell) {
								Line line = arcGraphics.getLine();
								setLineToolbar(line);
							}

							if (annotationGraphics != null && labelSelected) {
								Fill fill = annotationGraphics.getFill();
								setFillToolbar(fill, true);
								gradientColorAction.setEnabled(false);
								gradientDiagonalAction.setEnabled(false);
								gradientHorizontalAction.setEnabled(false);
								gradientVerticalAction.setEnabled(false);
								backgroundColorButton.repaint();

								Line line = annotationGraphics.getLine();

								setLineToolbar(line);
								curveAction.setEnabled(false);
							}
						} catch (Exception e) {
							ExceptionDialog.showException(SwingUtilities.getWindowAncestor(pnEditor), "Exception Updating Graphics Toolbar", e);
						}

					}

				} else {
					this.selectedCell = null;
				}
			}
		}

	}

}
