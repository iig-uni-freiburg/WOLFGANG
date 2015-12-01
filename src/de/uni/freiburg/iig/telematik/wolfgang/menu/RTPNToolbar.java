package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Dimension;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.SubjectContext;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;

public class RTPNToolbar extends PTNetToolBar {

	public RTPNToolbar(PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
		add(getResourceContextDropBox());
		addSeparator();
		add(getTimeContextDropBox());
		addSeparator();
		add(getAccessContextDropBox());
		revalidate();
		repaint();
	}
	
	private JComboBox<SubjectContext> getResourceContextDropBox(){
		JComboBox<SubjectContext> result = new JComboBox<>();
		result.setSize(150, 30);
		result.setPreferredSize(new Dimension(130, 30));
		return result;
	}
	
	private JComboBox<SubjectContext> getTimeContextDropBox(){
		JComboBox<SubjectContext> result = new JComboBox<>();
		result.setSize(150, 30);
		result.setPreferredSize(new Dimension(130, 30));
		return result;
	}
	
	private JComboBox<SubjectContext> getAccessContextDropBox(){
		JComboBox<SubjectContext> result = new JComboBox<>();
		result.setSize(150, 30);
		result.setPreferredSize(new Dimension(130, 30));
		return result;
	}

	private static final long serialVersionUID = -7375088484674702613L;

}
