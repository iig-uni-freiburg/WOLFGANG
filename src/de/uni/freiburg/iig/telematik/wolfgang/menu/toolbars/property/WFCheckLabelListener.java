package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

public interface WFCheckLabelListener<Z> {

	public void wfCheckFinished(Object sender, Z result);
	
	public void wfCheckException(Object sender, Exception exception);
	
	public void wfCheckStopped(Object sender, Z result);
}
