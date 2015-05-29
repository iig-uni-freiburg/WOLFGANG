package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckBoundednessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckCWNSoundnessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckCWNStructureAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.properties.CheckValidityAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.PropertyCheckToolbar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.TokenToolBar;

public class CPNToolBar extends AbstractToolBar {

	// further variables
	private PNEditorComponent pnEditor = null;
	private boolean ignoreZoomChange = false;
	private Mode mode = Mode.EDIT;

	private enum Mode {
		EDIT, PLAY
	}

	private TokenToolBar tokenToolbar;
	// private TokenlabelToolBar tokenlabelToolbar;
	private PopUpToolBarAction tokenAction;
	private PopUpToolBarAction editTokenlabelAction;
	// private CheckValidityAction checkValidityAction;
	private JToggleButton tokenButton;
	// private JToggleButton checkValidityButton;
	// private JToggleButton checkSoundnessButton;
	private CheckValidityAction checkValidityAction;
	
	protected PropertyCheckToolbar propertyCheckToolbar;


	public CheckValidityAction getCheckValidityAction() {
		return checkValidityAction;
	}

	public void setCheckValidityAction(CheckValidityAction checkValidityAction) {
		this.checkValidityAction = checkValidityAction;
	}

	private JButton validityButton;
	private CheckCWNStructureAction checkCWNStructureAction;
	private JButton cwfStructureButton;

	// currently soudness is same as validity in cpn
	// private CheckSoundnessAction checkSoundnessAction;
	// private JButton soudnessButton;

	private CheckBoundednessAction checkBoundednessAction;

	public CheckBoundednessAction getCheckBoundednessAction() {
		return checkBoundednessAction;
	}

	public void setCheckBoundednessAction(CheckBoundednessAction checkBoundednessAction) {
		this.checkBoundednessAction = checkBoundednessAction;
	}

	private JButton boundednessButton;
	private CheckCWNSoundnessAction checkCWNSoundnessAction;
	protected JButton cwfSoundnessButton;

	public CPNToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificToolbarButtons() {
		tokenButton = (JToggleButton) add(tokenAction, true);

		tokenAction.setButton(tokenButton);

		validityButton = add(checkValidityAction);
		validityButton.setBorderPainted(false);

		// soudnessButton = add(checkSoundnessAction);
		// soudnessButton.setBorderPainted(false);

		boundednessButton = add(checkBoundednessAction);
		boundednessButton.setBorderPainted(false);

		cwfStructureButton = add(getCheckCWNStructureAction());
		cwfStructureButton.setBorderPainted(false);

		cwfSoundnessButton = add(getCheckCWNSoundnessAction());
		cwfSoundnessButton.setBorderPainted(false);

		// checkValidityButton = (JToggleButton) add(checkValidityAction, true);
		// checkSoundnessButton = (JToggleButton) add(checkSoundnessAction,
		// true);

	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);

			tokenAction = new PopUpToolBarAction(pnEditor, "Token", IconFactory.getIcon("marking"), tokenToolbar);

			checkValidityAction = new CheckValidityAction(pnEditor);
			// checkSoundnessAction= new CheckSoundnessAction(pnEditor);
			checkBoundednessAction = new CheckBoundednessAction(pnEditor);
			setCheckCWNStructureAction(new CheckCWNStructureAction(pnEditor));
			setCheckCWNSoundnessAction(new CheckCWNSoundnessAction(pnEditor));
		}

	}

	public void updateGlobalTokenConfigurer() {
		tokenToolbar.updateView();
	}

	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {
		tokenButton.setVisible(b);
		if (tokenAction.getDialog() != null && tokenButton.isSelected())
			tokenAction.getDialog().setVisible(b);
	}

	public CheckCWNSoundnessAction getCheckCWNSoundnessAction() {
		return checkCWNSoundnessAction;
	}

	public void setCheckCWNSoundnessAction(CheckCWNSoundnessAction checkCWNSoundnessAction) {
		this.checkCWNSoundnessAction = checkCWNSoundnessAction;
	}

	public CheckCWNStructureAction getCheckCWNStructureAction() {
		return checkCWNStructureAction;
	}

	public void setCheckCWNStructureAction(CheckCWNStructureAction checkCWNStructureAction) {
		this.checkCWNStructureAction = checkCWNStructureAction;
	}

	@Override
	protected JToolBar getPropertyCheckToolbar() throws ParameterException, PropertyException, IOException {
		if (propertyCheckToolbar == null) {
			propertyCheckToolbar = new PropertyCheckToolbar(pnEditor, JToolBar.HORIZONTAL);
		}
		return propertyCheckToolbar;
	}

}
