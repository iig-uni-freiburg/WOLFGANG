package de.uni.freiburg.iig.telematik.wolfgang.actions.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class TokenColorSelectionAction extends AbstractPNEditorGraphicsAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6459666528398022696L;
	private String tokenLabel;
	private JPanel parent;

	public TokenColorSelectionAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "TokenColor", IconFactory.getIcon("fill"));
		setFillColor(Color.BLACK);

	}

	public TokenColorSelectionAction(PNEditorComponent editor, String tokenLabel) throws ParameterException, PropertyException, IOException {
		this(editor);
		this.tokenLabel = tokenLabel;
	}

	public void setParent(JPanel parent) {
		this.parent = parent;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		Color backgroundColor = JColorChooser.showDialog(parent, "Token Color", null);
		if (backgroundColor != null) {
			setFillColor(backgroundColor);
			if (tokenLabel != null) {
				AbstractCPNGraphics cpnGraphics = (AbstractCPNGraphics) editor.getGraphComponent().getGraph().getNetContainer().getPetriNetGraphics();
				((mxGraphModel) getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, getButtonFillColor()));
			}
		}
	}

	@Override
	protected void performLabelAction() throws PropertyException, IOException {}

	@Override
	protected void performNoLabelAction() throws PropertyException, IOException {}

	@Override
	protected void doMoreFancyStuff(ActionEvent e) throws Exception {}

}
