package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

public interface WFNetCheckLabelListener<Z> {

	public void wfNetCheckFinished(Object sender, Z result);
	
	public void wfNetCheckException(Object sender, Exception exception);
	
	public void wfNetCheckStopped(Object sender, Z result);
}
