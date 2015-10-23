package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.component.ColorChooserPanel.ColorMode;
import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class LinePanel extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9066508120886312900L;
	private GraphicsFillColorChooserPanel lineColor;
	private ButtonGroup btnShapeGroup;
	private JButton btnLine;
	private JButton btnRound;
	private JButton btnLineSolid;
	private JButton btnLineDash;
	private JButton btnLineDot;
	private ButtonGroup btnStyleGroup;
	private JComboBox cmbStrokeWeight;
	private Set<LineListener> listeners = new HashSet<>();

	public boolean addLinePanelListener(LineListener listener) {
		return listeners.add(listener);
	}

	public boolean removeLinePanelListener(LineListener listener) {
		return listeners.remove(listener);
	}

	public LinePanel() {
		super();
		try {
			setUpGUI();
		} catch (Exception e) {
		}
	}

	private void setUpGUI() throws PropertyException, IOException {
		setFloatable(false);
		
		lineColor = new GraphicsFillColorChooserPanel(ColorMode.HEX) {
			@Override
			public void labelColorChanged(Color color) {
				for (LineListener listener : listeners) {
					listener.lineColorChanged(color);
				}
			}
		};
	
		add(new JLabel("Line: "));
		add(lineColor);
		add(getcmbStrokeWeight());
		add(getLineModesPanel());
	}

	private JPanel getLineModesPanel() throws PropertyException, IOException {
		JPanel pnlChooseColorMode = new JPanel(new SpringLayout());

			btnLine = new JButton(IconFactory.getIcon("line"));
			btnLine.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (LineListener listener : listeners) {
						listener.lineShapeChanged(Line.Shape.LINE);
						updateLineShape(Line.Shape.LINE);
					}
				}
			});
			btnRound = new JButton(IconFactory.getIcon("round"));
			btnRound.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (LineListener listener : listeners) {
						listener.lineShapeChanged(Line.Shape.CURVE);
						updateLineShape(Line.Shape.CURVE);
					}
				}
			});
			btnLineSolid = new JButton(IconFactory.getIcon("solid"));
			btnLineSolid.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (LineListener listener : listeners) {
						listener.lineStyleChanged(Line.Style.SOLID);
						updateLineStyle(Line.Style.SOLID);
					}
				}
			});
			btnLineDash = new JButton(IconFactory.getIcon("dash"));
			btnLineDash.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (LineListener listener : listeners) {
						listener.lineStyleChanged(Line.Style.DASH);
						updateLineStyle(Line.Style.DASH);
					}
				}
			});
			btnLineDot = new JButton(IconFactory.getIcon("dot"));
			btnLineDot.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (LineListener listener : listeners) {
						listener.lineStyleChanged(Line.Style.DOT);
						updateLineStyle(Line.Style.DOT);
					}
				}
			});
			
			btnLine.setFocusable(false);
			btnRound.setFocusable(false);
			btnLineSolid.setFocusable(false);
			btnLineDash.setFocusable(false);	
			btnLineDot.setFocusable(false);

			btnShapeGroup = new ButtonGroup();
			btnShapeGroup.add(btnLine);
			btnShapeGroup.add(btnRound);
			
			btnStyleGroup = new ButtonGroup();
			btnStyleGroup.add(btnLineSolid);
			btnStyleGroup.add(btnLineDash);
			btnStyleGroup.add(btnLineDot);

			pnlChooseColorMode.add(btnLine);
			pnlChooseColorMode.add(btnRound);
			pnlChooseColorMode.add(btnLineSolid);
			pnlChooseColorMode.add(btnLineDash);
			pnlChooseColorMode.add(btnLineDot);

		SpringUtilities.makeCompactGrid(pnlChooseColorMode, 1, 5, 0, 0, 0, 0);
		
		return pnlChooseColorMode;
	}

	private JComboBox getcmbStrokeWeight() {
		if (cmbStrokeWeight == null) {
			cmbStrokeWeight = new JComboBox(new Object[] { "-", "0px", "1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px", "11px", "12px" });
			cmbStrokeWeight.setSelectedIndex(1);

			cmbStrokeWeight.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof JComboBox) {
						String strokeWeight;
						if (cmbStrokeWeight.getSelectedItem().toString().contains("px")) {
							strokeWeight = cmbStrokeWeight.getSelectedItem().toString().replace("px", "");
						} else {
							cmbStrokeWeight.setSelectedIndex(1);
							strokeWeight = 0 + "";
						}
						for (LineListener listener : listeners) {
							listener.lineWeightChanged(strokeWeight);
						}

					}
				}
			});
		}
		cmbStrokeWeight.setFocusable(false);
		return cmbStrokeWeight;
	}

	public void updatePanel(Color color, boolean differentLineColor, Shape line, Style style, int weight) {
		if(!differentLineColor)
		lineColor.getLblColor().updateColor(color);
		else 
			lineColor.getLblColor().differentColors();
		updateLineShape(line);
		updateLineStyle(style);
		updateLineStroke(weight);
	}

	private void updateLineStroke(int weight) {
		if (weight >= 0) {
			getcmbStrokeWeight().setSelectedItem(weight + "px");
		} else {
			getcmbStrokeWeight().setSelectedItem("-");

		}
	}

	private void updateLineShape(Shape line) {
		btnShapeGroup.clearSelection();
		if (line != null)
			switch (line) {
			case CURVE:
				btnShapeGroup.setSelected(btnRound.getModel(), true);
				break;
			case LINE:
				btnShapeGroup.setSelected(btnLine.getModel(), true);
				break;
			default:
				break;

			}
	}

	private void updateLineStyle(Style style) {
		btnStyleGroup.clearSelection();
		if (style != null)
			switch (style) {
			case DASH:
				btnStyleGroup.setSelected(btnLineDash.getModel(), true);
				break;
			case DOT:
				btnStyleGroup.setSelected(btnLineDot.getModel(), true);
				break;
			case SOLID:
				btnStyleGroup.setSelected(btnLineSolid.getModel(), true);
				break;
			default:
				break;

			}
	}


	public static void main(String[] args) {
		LinePanel fp = new LinePanel();
		fp.updatePanel(Color.BLACK,false, Line.Shape.LINE, Line.Style.SOLID, 5);
		JPanel pnl = new JPanel();
		pnl.add(fp);
		new DisplayFrame(pnl, true);

	}

}
