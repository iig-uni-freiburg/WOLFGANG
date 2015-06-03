package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

public interface PNPropertyCheckLabelListener {

	public void labelCalculationFinished(Object sender, Object result);
	
	public void labelCalculationException(Object sender, Exception exception);
	
	public void labelCalculationStopped(Object sender, Object result);
}
