package de.uni.freiburg.iig.telematik.wolfgang.menu;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;

public class CirclePanel extends JPanel {

   private Color color;
   int size = 10;

//=========================================== constructor
   public CirclePanel(Color tokenColor) throws PropertyException, IOException {
	   color = tokenColor;
	  

		size = WolfgangProperties.getInstance().getIconSize().getSize()/3;

     
	setPreferredSize(new Dimension(size, size));
//       setBackground(Color.white);
   }//end constructor

   //=========================================== paintComponent
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
       g2.setColor(color);
       g2.fillOval(size/2+size/4,size, size, size);

   }//end paintComponent

   public Color getColor() {
	return color;
}

public void setColor(Color color) {
	this.color = color;
}

//========================================== drawCircle
   // Convenience method to draw from center with radius
   public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
       cg.drawOval(xCenter-r, yCenter-r, 2*r, 2*r);
   }//end drawCircle
} // end class CirclePanel