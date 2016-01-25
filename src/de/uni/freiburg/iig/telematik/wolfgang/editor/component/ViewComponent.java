package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import javax.swing.JComponent;

/**
 * Interface providing MainComponent and the right-sided PropertiesView
 */
public interface ViewComponent {

	public JComponent getMainComponent();

	public JComponent getPropertiesView();

	public String getName();

}