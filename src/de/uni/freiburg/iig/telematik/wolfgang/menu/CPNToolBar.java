package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.TokenColorToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property.cpn.CPNPropertyCheckToolbar;

public class CPNToolBar extends AbstractToolBar {

	private static final long serialVersionUID = -8554639019766448073L;
	
	private static final Color DEFAULT_BG_COLOR = UIManager.getColor("Panel.background");
	private TokenColorToolBar tokenToolbar;
	private PopUpToolBarAction tokenAction;
	private JToggleButton tokenButton;

	public CPNToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}

	@Override
	protected void addNetSpecificToolbarButtons() {
		tokenButton = (JToggleButton) add(tokenAction, true);
		tokenButton.setBackground(DEFAULT_BG_COLOR);
		tokenAction.setButton(tokenButton);
		tokenToolbar.setPopUpToolBarAction(tokenAction);
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			
			tokenToolbar = new TokenColorToolBar(pnEditor, JToolBar.HORIZONTAL);
			tokenAction = new PopUpToolBarAction(pnEditor, "Token", IconFactory.getIcon("marking"), tokenToolbar);
		}

	}

	public void updateGlobalTokenConfigurer() {
		tokenToolbar.setUpGui();
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
