package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.invation.code.toval.graphic.dialog.ExceptionDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.MarkingChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.PNMarkingListener;
import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListener;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.RelationChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckResult;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class AbstractPNPropertyCheckToolbar extends JToolBar implements PNStructureListener, PNMarkingListener, PNPropertyCheckLabelListener {

	private static final long serialVersionUID = 6881881369803746536L;
	
	protected BoundednessCheckLabel boundednessCheckLabel;
	private AbstractMarkingGraph<?, ?, ?, ?> markingGraph = null;
	protected AbstractValidityCheckLabel validityCheckLabel;
	protected PNEditorComponent pnEditor = null;
	private static final Color DEFAULT_BG_COLOR = UIManager.getColor("Panel.background");
	

	public AbstractPNPropertyCheckToolbar(final PNEditorComponent pnEditor, int orientation) throws PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);
		setFloatable(false);
		this.pnEditor = pnEditor;
		pnEditor.getNetContainer().getPetriNet().addStructureListener(this);
		pnEditor.getNetContainer().getPetriNet().addMarkingListener(this);
		setUpGui();
		
	}

	private void setUpGui() {
		validityCheckLabel = createValidityCheckLabel();
		validityCheckLabel.addListener(this);
		add(validityCheckLabel);
		boundednessCheckLabel = new BoundednessCheckLabel(pnEditor, "Bound-\nedness");
		boundednessCheckLabel.addListener(this);
		boundednessCheckLabel.setEnabled(false);
		add(boundednessCheckLabel);
		addNetSpecificCheckLabels(pnEditor);
		setBackground(DEFAULT_BG_COLOR);
		
	}

	protected abstract void addNetSpecificCheckLabels(PNEditorComponent pnEditor);
	
	protected abstract AbstractValidityCheckLabel createValidityCheckLabel();

	// Listeners
	@Override
	public void markingChanged(MarkingChangeEvent markingEvent) {
		netChanged();
	}

	@Override
	public void initialMarkingChanged(MarkingChangeEvent markingEvent) {
		netChanged();
	}

	@Override
	public void structureChanged() {
		netChanged();
	}
	
	private void netChanged(){
		try{
			reactOnNetChange();
		} catch (Exception e) {
			ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "Internal Exception", e, true);
			e.printStackTrace();
		}
	}
	
	protected void reactOnNetChange() throws Exception {
		validityCheckLabel.reset();
		boundednessCheckLabel.reset();
		boundednessCheckLabel.setEnabled(false);
		setMarkingGraph(null);
	}

	@Override
	public void placeAdded(PlaceChangeEvent event) {}

	@Override
	public void placeRemoved(PlaceChangeEvent event) {}

	@Override
	public void transitionAdded(TransitionChangeEvent event) {}

	@Override
	public void transitionRemoved(TransitionChangeEvent event) {}

	@Override
	public void relationAdded(RelationChangeEvent event) {}

	@Override
	public void relationRemoved(RelationChangeEvent event) {}
	
	@Override
	public final void labelCalculationFinished(Object sender, Object result) {
		if (sender == validityCheckLabel) {
			validityCheckFinished((Boolean) result);
		} else if(sender == boundednessCheckLabel) {
			boundednessCheckFinished((BoundednessCheckResult) result);
		}
	}
	
	protected void validityCheckFinished(Boolean result){
		boundednessCheckLabel.setEnabled(result != null ? result : false);
	}
	
	protected void boundednessCheckFinished(BoundednessCheckResult result){
		setMarkingGraph(result.getMarkingGraph());
	}

	@Override
	public final void labelCalculationStopped(Object sender, Object result) {
		if (sender == validityCheckLabel) {
			validityCheckStopped((Boolean) result);
		} else if(sender == boundednessCheckLabel) {
			boundednessCheckStopped((BoundednessCheckResult) result);
		}
	}
	
	protected void validityCheckStopped(Boolean result){
//		validityCheckLabel.reset();
		boundednessCheckLabel.setEnabled(result != null ? result : false);
	}
	
	protected void boundednessCheckStopped(BoundednessCheckResult result){
//		boundednessCheckLabel.reset();
		setMarkingGraph(null);
	}

	@Override
	public final void labelCalculationException(Object sender, Exception exception) {
		if (sender == validityCheckLabel) {
			validityCheckException(exception);
		} else if(sender == boundednessCheckLabel){
			boundednessCheckException(exception);
		}
	}
	
	protected void validityCheckException(Exception exception){
		ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "Validity Check Exception", exception, true);
//		validityCheckLabel.reset();
//		boundednessCheckLabel.setEnabled(false);
	}
	
	protected void boundednessCheckException(Exception exception){
		ExceptionDialog.showException(SwingUtilities.getWindowAncestor(this), "Boundedness Check Exception", exception, true);
		setMarkingGraph(null);
//		boundednessCheckLabel.reset();
	}
	
	protected void setMarkingGraph(AbstractMarkingGraph<?,?,?,?> markingGraph){
		this.markingGraph = markingGraph;
	}
	
	protected AbstractMarkingGraph<?,?,?,?> getMarkingGraph(){
		return markingGraph;
	}

}
