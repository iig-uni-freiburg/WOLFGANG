package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.FontToolBar;

@SuppressWarnings("serial")
public class ShowHideLabelsAction extends AbstractPNEditorAction {
	private Image visible;
	private Image invisible;
	private FontToolBar fontTB;

	public ShowHideLabelsAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "visible", IconFactory.getIcon("visible"));
		visible = getIcon().getImage();
		invisible = IconFactory.getIcon("invisible").getImage();

	}


	public void setHideIconImage() {
		getIcon().setImage(invisible);

	}

	public void setShowIconImage() {
		getIcon().setImage(visible);
	}

	public void setFontToolbar(FontToolBar fontToolBar) {
		fontTB = fontToolBar;
		
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {

		if (getIcon().getImage() == visible) {
			getGraph().setCellStyles("noLabel", "1");
			fontTB.setFontEnabled(false);
			
		}
		else if (getIcon().getImage() == invisible) {
			getGraph().setCellStyles("noLabel", "0");
			fontTB.setFontEnabled(true);

		}		
	}

}