package de.uni.freiburg.iig.telematik.wolfgang.properties;

import javax.swing.JLabel;

public class PropertyCheckLabelHeadline extends JLabel {

	private boolean isExpanded;
	private String headline;

	public PropertyCheckLabelHeadline(String headline, boolean expanded) {
		super(headline);
		this.headline = headline;
		setExpanded(expanded);
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		if(isExpanded)
			setText("▼ " + headline);
		else
			setText("► " + headline);
	}

}
