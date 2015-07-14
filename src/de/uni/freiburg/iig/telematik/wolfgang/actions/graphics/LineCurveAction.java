package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.GraphicsToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;

public class LineCurveAction extends AbstractPNEditorGraphicsAction {

    /**
     *
     */
    private static final long serialVersionUID = -5817890089224292792L;

    public LineCurveAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
        super(editor, "Curve Color", IconFactory.getIcon("round"));
        setButtonScale(3, 3);
        setIconImage(getIcon().getImage());
    }

    @Override
    protected void performLabelAction() {
    }

    @Override
    protected void performNoLabelAction() {
    }

    @Override
    protected void doMoreFancyStuff(ActionEvent e) throws Exception {
                getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "true");
                getGraph().setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		getEditor().getEditorToolbar().getGraphicsToolbar().setLineStyle(GraphicsToolBar.LineStyle.CURVE);
    }
}
