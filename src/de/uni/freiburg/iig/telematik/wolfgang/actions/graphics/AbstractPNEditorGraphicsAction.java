package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractPNEditorGraphicsAction extends AbstractPNEditorAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8723535025613390455L;


	public AbstractPNEditorGraphicsAction(PNEditorComponent editor, String name, Icon icon) throws ParameterException {
		super(editor, name, icon);
		// TODO Auto-generated constructor stub
	}

	public AbstractPNEditorGraphicsAction(PNEditorComponent editor, String name) throws ParameterException {
		super(editor, name);
		// TODO Auto-generated constructor stub
	}

	public AbstractPNEditorGraphicsAction(PNEditorComponent editor) throws ParameterException {
		super(editor);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(getGraph().isLabelSelected())
		performLabelAction();
		else {
		performNoLabelAction();	
		}
		doMoreFancyStuff(e);
		updateViewWithSelectedCell();
	}

	protected abstract void performLabelAction();
	protected abstract void performNoLabelAction();	
	protected abstract void doMoreFancyStuff(ActionEvent e) throws Exception;




}
