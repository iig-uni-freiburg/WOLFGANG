package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;

public abstract class AbstractPNEditorAction extends AbstractAction {

	private static final long serialVersionUID = -308657344513249134L;

	protected PNEditorComponent editor = null;

	private ImageIcon icon;

	public AbstractPNEditorAction(PNEditorComponent editor) throws ParameterException {
		super();
		setEditor(editor);
	}

	public AbstractPNEditorAction(PNEditorComponent editor, String name) throws ParameterException {
		super(name);
		setEditor(editor);
	}

	public AbstractPNEditorAction(PNEditorComponent editor, String name, Icon icon) throws ParameterException {
		super(name, icon);
		setEditor(editor);
		setIcon(icon);
	}

	protected void setIcon(Icon icon) throws ParameterException {
		Validate.notNull(icon);
		this.icon = (ImageIcon) icon;
	}

	protected ImageIcon getIcon() {
		return icon;
	}

	private void setEditor(PNEditorComponent editor) throws ParameterException {
		Validate.notNull(editor);
		this.editor = editor;
	}

	protected PNEditorComponent getEditor() {
		return editor;
	}

	protected PNGraph getGraph() {
		return getEditor().getGraphComponent().getGraph();
	}

	protected PNGraphCell getGraphSelectionCell() {
		return (PNGraphCell) getGraph().getSelectionCell();
	}

	protected boolean isCellSelected() {
		return getGraphSelectionCell() != null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			doFancyStuff(e);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected abstract void doFancyStuff(ActionEvent e) throws Exception;

}
