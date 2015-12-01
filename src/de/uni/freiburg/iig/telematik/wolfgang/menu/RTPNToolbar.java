package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Dimension;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;

public class RTPNToolbar extends PTNetToolBar {

	public RTPNToolbar(PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
		setUpGui();
	}
	
	private void setUpGui() {
		add(getResourceContextDropBox());
		addSeparator();
		add(getTimeContextDropBox());
		addSeparator();
		add(getAccessContextDropBox());
		revalidate();
		repaint();
	}
	
	private JComboBox<IResourceContext> getResourceContextDropBox(){
		JComboBox<IResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}
	
	private JComboBox<IResourceContext> getTimeContextDropBox(){
		JComboBox<IResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}
	
	private JComboBox<IResourceContext> getAccessContextDropBox(){
		JComboBox<IResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}

	private static final long serialVersionUID = -7375088484674702613L;

}
