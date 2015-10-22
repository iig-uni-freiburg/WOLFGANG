package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.component.ColorChooserPanel.ColorMode;
import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class FillPanel extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 33191267623680973L;
	private GraphicsFillColorChooserPanel fillColor;
	private GraphicsFillColorChooserPanel gradientColor;
	private ButtonGroup btnGroup;
	private JButton btnOnlyFillColor;
	private JButton btnGradientHorizontal;
	private JButton btnGradientVertical;
	private JButton btnGradientDiagonal;

	private Set<FillListener> listeners = new HashSet<>();
	
	public boolean addFillPanelListener(FillListener listener) {
		return listeners.add(listener);
	}

	public boolean removeFillPanelListener(FillListener listener) {
		return listeners.remove(listener);
	}

	public FillPanel() {
		try {
			setUpGUI();
		} catch (Exception e) {
		}
	}

	private void setUpGUI() throws PropertyException, IOException {

		setFloatable(false);

		fillColor = new GraphicsFillColorChooserPanel(ColorMode.HEX) {
			@Override
			public void labelColorChanged(Color color) {
				for (FillListener listener : listeners) {
					listener.fillColorChanged(color);
				}
			}
		};
		
		gradientColor = new GraphicsFillColorChooserPanel(ColorMode.HEX) {
			@Override
			public void labelColorChanged(Color color) {
				for (FillListener listener : listeners) {
					listener.gradientColorChanged(color);
				}
			}
		};

		add(new JLabel("Fill: "));
		add(fillColor);
		add(getColorModePanel());
		add(gradientColor);
	}

	private JPanel getColorModePanel() throws PropertyException, IOException {
		JPanel pnlChooseColorMode = new JPanel(new SpringLayout());

		btnOnlyFillColor = new JButton(IconFactory.getIcon("no_gradient"));
		btnOnlyFillColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (FillListener listener : listeners) {
					listener.gradientDirectionChanged(null);
				}
				updateGradientRotation(null, false);
			}
		});
		btnGradientHorizontal = new JButton(IconFactory.getIcon("gradient_horizontal"));
		btnGradientHorizontal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (FillListener listener : listeners) {
					listener.gradientDirectionChanged(GradientRotation.HORIZONTAL);
				}
				updateGradientRotation(GradientRotation.HORIZONTAL, false);

			}
		});
		btnGradientVertical = new JButton(IconFactory.getIcon("gradient_vertical"));
		btnGradientVertical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (FillListener listener : listeners) {
					listener.gradientDirectionChanged(GradientRotation.VERTICAL);
				}
				updateGradientRotation(GradientRotation.VERTICAL, false);

			}
		});
		btnGradientDiagonal = new JButton(IconFactory.getIcon("gradient-diagonal"));
		btnGradientDiagonal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (FillListener listener : listeners) {
					listener.gradientDirectionChanged(GradientRotation.DIAGONAL);
				}
				updateGradientRotation(GradientRotation.DIAGONAL, false);

			}
		});

		btnOnlyFillColor.setFocusable(false);
		btnGradientHorizontal.setFocusable(false);
		btnGradientVertical.setFocusable(false);
		btnGradientDiagonal.setFocusable(false);

		btnGroup = new ButtonGroup();
		btnGroup.add(btnOnlyFillColor);
		btnGroup.add(btnGradientHorizontal);
		btnGroup.add(btnGradientVertical);
		btnGroup.add(btnGradientDiagonal);

		pnlChooseColorMode.add(btnOnlyFillColor);
		pnlChooseColorMode.add(btnGradientHorizontal);
		pnlChooseColorMode.add(btnGradientVertical);
		pnlChooseColorMode.add(btnGradientDiagonal);
		SpringUtilities.makeCompactGrid(pnlChooseColorMode, 1, 4, 0, 0, 0, 0);
		
		return pnlChooseColorMode;
	}

	public void updatePanel(Color color, boolean differentFillColor, GradientRotation rotation, boolean clearSelection, Color gColor, boolean differentGradientFillColor) {
		if(!differentFillColor)
			fillColor.getLblColor().updateColor(color);
		else 
			fillColor.getLblColor().differentColors();
		
		setEnabledFillMode(true);

		if(!differentGradientFillColor)
		gradientColor.getLblColor().updateColor(gColor);
		else
			gradientColor.getLblColor().differentColors();
		updateGradientRotation(rotation, clearSelection);
	}
	
	public void setLabelFillMode(Color color, boolean differentFillColor) {
		if(!differentFillColor)
			fillColor.getLblColor().updateColor(color);
		else 
			fillColor.getLblColor().differentColors();
		gradientColor.getLblColor().updateColor(null);
		updateGradientRotation(null, false);
		setEnabledFillMode(false);
	}

	private void setEnabledFillMode(boolean b) {
		btnGradientHorizontal.setEnabled(b);
		btnGradientVertical.setEnabled(b);
		btnGradientDiagonal.setEnabled(b);
		gradientColor.setEnabled(b);
		gradientColor.getLblColor().setEnabled(b);
	}

	private void updateGradientRotation(GradientRotation rotation, boolean clearSelection) {
		btnGroup.clearSelection();
		if (!clearSelection) {
			if (rotation != null)
				switch (rotation) {
				case DIAGONAL:
					btnGroup.setSelected(btnGradientDiagonal.getModel(), true);
					break;
				case HORIZONTAL:
					btnGroup.setSelected(btnGradientHorizontal.getModel(), true);
					break;
				case VERTICAL:
					btnGroup.setSelected(btnGradientVertical.getModel(), true);
					break;
				default:
					break;

				}
			else {
				btnGroup.setSelected(btnOnlyFillColor.getModel(), true);
			}
		}
	}

	public static void main(String[] args) {
		FillPanel fp = new FillPanel();
		fp.updatePanel(Color.RED, false, GradientRotation.HORIZONTAL, false, Color.BLUE, false);
		JPanel pnl = new JPanel();
		pnl.add(fp);
		new DisplayFrame(pnl, true);

	}



}
