package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import javax.swing.JLabel;

public class PropertyCheckHeadlineLabel extends JLabel {

	private static final long serialVersionUID = 4058709892403753995L;
	
	private boolean isExpanded;
	private String headline;

	public PropertyCheckHeadlineLabel(String headline, boolean expanded) {
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
			setText("\u25bc " + headline);
		else
			setText("\u25ba " + headline);
	}

}
