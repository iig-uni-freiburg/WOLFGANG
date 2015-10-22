package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.component.ColorChooserPanel.ColorMode;
import de.invation.code.toval.validate.Validate;


public class GraphicsFillColorChooserPanel extends JPanel {

	private static final int PREFERRED_HEIGHT = 20;

	private JLabel lblClear;
	private GraphicsColorChooserLabel lblColor;
	public GraphicsColorChooserLabel getLblColor() {
		return lblColor;
	}

	public void setLblColor(GraphicsColorChooserLabel lblColor) {
		this.lblColor = lblColor;
	}

	private Color chosenColor = null;
	
	public void labelColorChanged(Color color) {
		
	}

	public GraphicsFillColorChooserPanel(ColorMode colorMode) {
		this(colorMode, null);
	}

	public GraphicsFillColorChooserPanel(ColorMode colorMode, Color initialColor) {
		super(new BorderLayout());
		Validate.notNull(colorMode);
		lblClear = new JLabel("X", JLabel.CENTER);
		lblClear.setPreferredSize(new Dimension(PREFERRED_HEIGHT, PREFERRED_HEIGHT));
		lblClear.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (isEnabled()) {
					lblColor.updateColor(null);

					labelColorChanged(null);
				}
			}

		});
		add(lblClear, BorderLayout.LINE_START);
		lblColor = new GraphicsColorChooserLabel(colorMode, initialColor);
		lblColor.setPreferredSize(new Dimension(PREFERRED_HEIGHT * 4, PREFERRED_HEIGHT));
		add(lblColor, BorderLayout.CENTER);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}

	class GraphicsColorChooserLabel extends JLabel {

		private static final long serialVersionUID = -1319109332327149106L;

		private ColorMode colorMode = null;

		public GraphicsColorChooserLabel(ColorMode colorMode) {
			Validate.notNull(colorMode);
			this.colorMode = colorMode;
			setHorizontalAlignment(JLabel.CENTER);
			setOpaque(true);
			updateColor(null);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Color color = JColorChooser.showDialog(SwingUtilities.getWindowAncestor(GraphicsFillColorChooserPanel.this), "Choose Color", null);
					if (color != null)
					{
						GraphicsColorChooserLabel.this.updateColor(color);
						labelColorChanged(color);
					}
				}


			});
		}

		public GraphicsColorChooserLabel(ColorMode colorMode, Color color) {
			this(colorMode);
			updateColor(color);
		}

		public void updateColor(Color color) {
			chosenColor = color;
			setOpaque(color != null);
			if (chosenColor != null) {
				if (colorMode == ColorMode.HEX) {
					setText(hexString(chosenColor));
				} else {
					setText(String.format("(%s,%s,%s)", chosenColor.getRed(), chosenColor.getGreen(), chosenColor.getBlue()));
				}
				setBackground(chosenColor);
			} else {
				setText("---");
			}
		}
		
		public void differentColors(){
			updateColor(null);
			setText("various");
		}

		private String hexString(Color color) {
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			return String.format("#%02X%02X%02X", r, g, b);
		}

	}

	public Color getChosenColor() {
		return chosenColor;
	}
}
