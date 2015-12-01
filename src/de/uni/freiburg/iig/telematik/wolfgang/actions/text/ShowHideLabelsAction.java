package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.FontToolBar;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class ShowHideLabelsAction extends AbstractPNEditorAction {

    private Image visible;
    private Image invisible;
    private FontToolBar fontTB;

    public ShowHideLabelsAction(PNEditorComponent editor) throws PropertyException, IOException {
        super(editor, "visible", IconFactory.getIcon("visible"));
        setUpGui();
    }

    private void setUpGui() throws PropertyException, IOException {
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
        Object[] selectionCells = getEditor().getGraphComponent().getGraph().getSelectionCells();
        ArrayList<Object> list = new ArrayList<Object>();
        Collections.addAll(list, selectionCells);
        for (Object c : selectionCells) {
            if (c instanceof PNGraphCell) {
                PNGraphCell pnCell = ((PNGraphCell) c);
                switch (pnCell.getType()) {
                    case TRANSITION:
                        if (getEditor().getNetContainer().getPetriNet().getTransition(pnCell.getId()).isSilent()) {
                            list.remove(pnCell);
                            if(list.isEmpty())
                                return;
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (getIcon().getImage() == visible) {
            getGraph().setCellStyles("noLabel", "1", list.toArray());
            fontTB.setFontEnabled(false);

        } else if (getIcon().getImage() == invisible) {
            getGraph().setCellStyles("noLabel", "0", list.toArray());
            fontTB.setFontEnabled(true);

        }
    }

}
