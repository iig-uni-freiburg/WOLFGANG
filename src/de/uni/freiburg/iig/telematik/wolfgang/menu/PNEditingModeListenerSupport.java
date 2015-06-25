package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar.Mode;

public class PNEditingModeListenerSupport {
	
	
	protected List<PNEditingModeListener> editorListeners = new ArrayList<PNEditingModeListener>();

	public void addEditorListener(PNEditingModeListener listener){
		editorListeners.add(listener);
	}
	
	public void removeEditorListener(PNEditingModeListener listener){
		editorListeners.remove(listener);
	}
		
	public void notifyEditingModeChange(Mode mode){
		for(PNEditingModeListener listener: editorListeners){
			listener.editingModeChanged(mode);
		}
	}

}

