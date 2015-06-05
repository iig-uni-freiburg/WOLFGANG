package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.io.IOException;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.TokenToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.cpn.CPNPropertyCheckToolbar;

public class CPNToolBar extends AbstractToolBar {

	private static final long serialVersionUID = -8554639019766448073L;
	
	private TokenToolBar tokenToolbar;
	// private TokenlabelToolBar tokenlabelToolbar;
	private PopUpToolBarAction tokenAction;
	private PopUpToolBarAction editTokenlabelAction;
	private JToggleButton tokenButton;

	public CPNToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificToolbarButtons() {
		tokenButton = (JToggleButton) add(tokenAction, true);
		tokenAction.setButton(tokenButton);
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			
			tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);
			tokenAction = new PopUpToolBarAction(pnEditor, "Token", IconFactory.getIcon("marking"), tokenToolbar);
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

	@Override
	protected JToolBar createPropertyCheckToolbar() throws ParameterException, PropertyException, IOException {
		return new CPNPropertyCheckToolbar(pnEditor, JToolBar.HORIZONTAL);
	}

}
