package de.uni.freiburg.iig.telematik.wolfgang.actions.text;

import java.awt.event.ActionEvent;
import java.io.IOException;

import com.mxgraph.util.mxConstants;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class FontRotationAction extends AbstractPNEditorAction {
	
	private static final long serialVersionUID = 7450908146578160638L;
	private String 	degree = "90";
	
	
	public FontRotationAction(PNEditorComponent editor) throws PropertyException, IOException {
		super(editor, "Rotation", IconFactory.getIcon("rotate"));		
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		getEditor().getGraphComponent().getGraph().setCellStyles(MXConstants.FONT_ROTATION_DEGREE, degree);
		if(degree.equals("0") || degree.equals("180") || degree.equals("360"))
		getEditor().getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_HORIZONTAL, "true");
		else
			getEditor().getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_HORIZONTAL, "false");
		degree = 	new Integer(degree)%360 + 90 + "";		
	}

}