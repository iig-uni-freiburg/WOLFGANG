package de.uni.freiburg.iig.telematik.wolfgang.event;

import java.util.ArrayList;
import java.util.List;

public class PNEditorListenerSupport {
	
	protected List<PNEditorListener> editorListeners = new ArrayList<PNEditorListener>();

	public void addEditorListener(PNEditorListener listener){
		editorListeners.add(listener);
	}
	
	public void removeEditorListener(PNEditorListener listener){
		editorListeners.remove(listener);
	}
	
	public void notifyModificationStateChange(boolean modified){
		for(PNEditorListener listener: editorListeners){
			listener.modificationStateChanged(modified);
		}
	}

}
