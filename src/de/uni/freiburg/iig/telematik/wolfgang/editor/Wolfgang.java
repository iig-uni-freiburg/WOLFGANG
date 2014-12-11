package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.io.File;

import javax.swing.JFrame;

import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class Wolfgang extends JFrame {

	private static final long serialVersionUID = 6384805641357595809L;
	
	private File fileReference = null;
	
	private PNEditorComponent editorComponent = null;
	

	public PNEditorComponent getEditorComponent() {
		return editorComponent;
	}

	public File getFileReference() {
		return fileReference;
	}

}
