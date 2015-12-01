package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Dimension;

import javax.swing.JComboBox;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ResourceContext;
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
	
	private JComboBox<ResourceContext> getResourceContextDropBox(){
		JComboBox<ResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}
	
	private JComboBox<ResourceContext> getTimeContextDropBox(){
		JComboBox<ResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}
	
	private JComboBox<ResourceContext> getAccessContextDropBox(){
		JComboBox<ResourceContext> cmbResult = new JComboBox<>();
		cmbResult.setSize(150, 30);
		cmbResult.setPreferredSize(new Dimension(130, 30));
		return cmbResult;
	}

	private static final long serialVersionUID = -7375088484674702613L;

}
