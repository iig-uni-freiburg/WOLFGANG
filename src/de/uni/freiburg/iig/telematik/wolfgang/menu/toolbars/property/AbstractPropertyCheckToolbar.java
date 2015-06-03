package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.io.IOException;

import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.MarkingChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.PNMarkingListener;
import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListener;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.RelationChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionChangeEvent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractPropertyCheckToolbar extends JToolBar implements PNStructureListener, PNMarkingListener, PNPropertyCheckLabelListener {

	private static final long serialVersionUID = 6881881369803746536L;
	
	private BoundednessCheckLabel boundednessCheckLabel;

	public AbstractPropertyCheckToolbar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);
		setFloatable(false);
		pnEditor.getNetContainer().getPetriNet().addStructureListener(this);
		pnEditor.getNetContainer().getPetriNet().addMarkingListener(this);

		boundednessCheckLabel = new BoundednessCheckLabel(pnEditor, "Bound-\nedness");
		add(boundednessCheckLabel);
		addNetSpecificCheckLabels(pnEditor);
	}

	protected abstract void addNetSpecificCheckLabels(PNEditorComponent pnEditor);

	// Listeners
	@Override
	public void markingChanged(MarkingChangeEvent markingEvent) {
		
	}

	@Override
	public void initialMarkingChanged(MarkingChangeEvent markingEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void structureChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeAdded(PlaceChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeRemoved(PlaceChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transitionAdded(TransitionChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transitionRemoved(TransitionChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void relationAdded(RelationChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void relationRemoved(RelationChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
