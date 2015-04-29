package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.awt.Color;

import de.invation.code.toval.graphic.component.ExecutorLabel;
import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class PropertyCheckLabel extends ExecutorLabel {
	
	private static final long serialVersionUID = -2807606775313824173L;
	
	public static Color COLOR_PROPERTY_UNKNOWN = Color.GRAY;
	public static Color COLOR_PROPERTY_TRUE = Color.GREEN;
	public static Color COLOR_PROPERTY_FALSE = Color.RED;

	protected PNEditorComponent editorComponent;

	public PropertyCheckLabel(PNEditorComponent editorComponent) {
		super();
		this.editorComponent = editorComponent;
	}
	
	@Override
	protected void startExecutor() {
		setExecutor(createNewExecutor());
		super.startExecutor();
	}
	
	protected abstract SingleThreadExecutorService createNewExecutor();


}
