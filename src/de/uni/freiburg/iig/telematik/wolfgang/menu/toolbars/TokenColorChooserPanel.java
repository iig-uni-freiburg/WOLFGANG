package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CirclePanel;

public class TokenColorChooserPanel extends JPanel {

	static final long serialVersionUID = 8557745082580816515L;
	
	static final int PREFERRED_HEIGHT = 20;
	
	private Set<ColorChooserListener> listeners = new HashSet<ColorChooserListener>();

	public void addListener(ColorChooserListener listener){
		this.listeners.add(listener);
	}
	
	public void removeListener(ColorChooserListener listener){
		this.listeners.remove(listener);
	}
	
	private ColorChooserLabel lblColor;
	public ColorChooserLabel getLblColor() {
		return lblColor;
	}

	public void setLblColor(ColorChooserLabel lblColor) {
		this.lblColor = lblColor;
	}

	private Color chosenColor = null;
	
	protected boolean isColorUnique = true;



	private CirclePanel circle;

	public TokenColorChooserPanel(ColorMode colorMode, Color color, CirclePanel circle){
		this(colorMode, color);
		this.circle = circle;
	}
	
	public TokenColorChooserPanel(ColorMode colorMode, Color initialColor){
		super(new BorderLayout());
		lblColor = new ColorChooserLabel(colorMode, initialColor);
		lblColor.setPreferredSize(new Dimension(PREFERRED_HEIGHT*3, PREFERRED_HEIGHT));
		add(lblColor, BorderLayout.CENTER);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	class ColorChooserLabel extends JLabel {

		private static final long serialVersionUID = -1319109332327149106L;
		
		private ColorMode colorMode = null;
		
		public ColorChooserLabel(ColorMode colorMode){
			Validate.notNull(colorMode);
			this.colorMode = colorMode;
			setHorizontalAlignment(JLabel.CENTER);
			setOpaque(true);
			updateColor(null);
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Color color = JColorChooser.showDialog(SwingUtilities.getWindowAncestor(TokenColorChooserPanel.this), "Choose Color", chosenColor);
					for(ColorChooserListener listener: listeners){
						listener.valueChanged(chosenColor, color);
					}
					if(color!=null && isColorUnique)
					ColorChooserLabel.this.updateColor(color);
					if(!isColorUnique)
						
						JOptionPane.showMessageDialog(ColorChooserLabel.this, "Please choose a different color","Color already exists", JOptionPane.WARNING_MESSAGE);

					
				}
			});
		}
		
		public ColorChooserLabel(ColorMode colorMode, Color color){
			this(colorMode);
			updateColor(color);
		}
		
		public void updateColor(Color color){
			chosenColor = color;
			setOpaque(color != null);
			if(chosenColor != null){
				if(colorMode == ColorMode.HEX){
					setText(hexString(chosenColor));
				} else {
					setText(String.format("(%s,%s,%s)", chosenColor.getRed(), chosenColor.getGreen(), chosenColor.getBlue()));
				}
				setBackground(chosenColor);
				if(circle != null){
				circle.setColor(chosenColor);
				circle.repaint();
				}
			} else {
				setText("---");
			}

	
		}
		
		private String hexString(Color color){
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			return String.format("#%02X%02X%02X", r, g, b);
		}
		
	}
	
	public Color getChosenColor(){
		return chosenColor;
	}
	
	public boolean isColorUnique() {
		return isColorUnique;
	}

	public void setColorUnique(boolean isColorUnique) {
		this.isColorUnique = isColorUnique;
	}
	
	public enum ColorMode {
		RGB, HEX;
	}

}
