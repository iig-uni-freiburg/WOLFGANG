package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractWFCheckLabel<P> extends AbstractPNPropertyCheckLabel<P> {

	private static final long serialVersionUID = -8561240983245503666L;
	
	protected Set<WFCheckLabelListener<P>> wfCheckListeners = new HashSet<WFCheckLabelListener<P>>();

	public AbstractWFCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		super(editorComponent, propertyName);
	}

	public AbstractWFCheckLabel(PNEditorComponent editorComponent) {
		super(editorComponent);
	}
	
	public void addWFCheckListener(WFCheckLabelListener<P> listener){
		wfCheckListeners.add(listener);
	}
	
	@Override
	public void executorFinished(P result) {
		super.executorFinished(result);
		for(WFCheckLabelListener<P> listener: wfCheckListeners)
			listener.wfCheckFinished(AbstractWFCheckLabel.this, result);
	}

	@Override
	public void executorStopped() {
		super.executorStopped();
		for(WFCheckLabelListener<P> listener: wfCheckListeners)			
			listener.wfCheckStopped(AbstractWFCheckLabel.this, null);
	}
	
}
