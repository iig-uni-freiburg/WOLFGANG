package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class FontBoldStyleAction extends AbstractPNEditorAction {

    private static final long serialVersionUID = 7450908146578160638L;

    public FontBoldStyleAction(PNEditorComponent editor) throws PropertyException, IOException {
        super(editor, "Bold", IconFactory.getIcon("bold"));
    }

    @Override
    protected void doFancyStuff(ActionEvent e) throws Exception {
        if (!isSelected) {
            getGraph().setCellStyles((String) MXConstants.FONT_WEIGHT, "bold");
        } else {
            getGraph().setCellStyles((String) MXConstants.FONT_WEIGHT, "normal");
        }

        getGraph().setSelectionCells(getEditor().getGraphComponent().getGraph().getSelectionCells());

    }

}
