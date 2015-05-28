package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import java.awt.Color;

import javax.swing.JLabel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;

public class PropertyCheckResultLabel extends JLabel {

	private static final long serialVersionUID = -3387692188070593237L;

	public PropertyCheckResultLabel(String string, PropertyCheckingResult result) {
		super(string);
		updatePropertyCheckingResult(result);
	}

	public void updatePropertyCheckingResult(PropertyCheckingResult result) {
		setToolTipText(result + "");
		switch (result) {
		case FALSE:
			setForeground(Color.RED);
			break;
		case TRUE:
			setForeground(Color.GREEN);
			break;
		case UNKNOWN:
			setForeground(Color.GRAY);
			break;
		default:
			break;
		}
		repaint();
	}

}
