package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.component.RestrictedTextField;
import de.invation.code.toval.graphic.component.RestrictedTextField.Restriction;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.TokenColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class TokenConfigurerToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private static final double TOKEN_ROW_WIDTH = 200;

	private static final double TOKEN_ROW_HEIGHT = 50;

	// Buttons
	private JButton addButton;

	// Actions
	private FillColorSelectionAction colorSelectionAction;

	// further variables
	private Map<String, Color> colorMap;

	private JPanel tokenPanel;

	private PNEditorComponent editor;

	public TokenConfigurerToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		this.editor = pnEditor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		AbstractPetriNet net = pnEditor.getNetContainer().getPetriNet();

		Set<String> colors = null;
		if (net instanceof CPN) {
			CPN cpn = (CPN) net;
			CPNGraphics graphics = (CPNGraphics) pnEditor.getNetContainer().getPetriNetGraphics();
			colors = cpn.getTokenColors();
			colorMap = graphics.getColors();
		}
		setFloatable(false);

		loadTokenPanel(net, colors);

		add(tokenPanel);
		add(addButton);

	}

	private void loadTokenPanel(AbstractPetriNet net, Set<String> colors) {
		tokenPanel = new JPanel();
		tokenPanel.setLayout(new BoxLayout(tokenPanel, BoxLayout.Y_AXIS));
		for (String colorName : colors) {
			addRow(net.getInitialMarking(), colorName);
		}


			try {
				addButton = new JButton(IconFactory.getIcon("maximize"));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Maximize-Button could not be added. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);
			}
	

		addButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				AbstractCPN cpn = (AbstractCPN) editor.getGraphComponent().getGraph().getNetContainer().getPetriNet();
				Object[] cells = editor.getGraphComponent().getGraph().getSelectionCells();

				if (!editor.getGraphComponent().getGraph().isSelectionEmpty())
					for (Object cell : cells) {
						if (cell instanceof PNGraphCell) {
							addNewTokenColor(((PNGraphCell) cell).getId(), cpn, (AbstractCPNGraphics) editor.getGraphComponent().getGraph().getNetContainer().getPetriNetGraphics());
						}
					}
				else {
					for (Object p : cpn.getSourcePlaces())
						addNewTokenColor(((AbstractPlace) p).getName(), cpn, (AbstractCPNGraphics) editor.getGraphComponent().getGraph().getNetContainer().getPetriNetGraphics());
				}
				Set<String> colorsTemp = cpn.getTokenColors();
				tokenPanel.removeAll();
				for (String colorName : colorsTemp) {
					addRow(cpn.getInitialMarking(), colorName);
				}
				editor.getGraphComponent().repaint();

			}
		});
	}

	protected void addNewTokenColor(String name, AbstractCPN cpn, AbstractCPNGraphics cpnGraphics) {

		JTextField textField = new RestrictedTextField(Restriction.NOT_EMPTY, 5);

		TokenColorSelectionAction tokenColorAction = null;
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Select Color:"));

		try {
			tokenColorAction = new TokenColorSelectionAction(editor);
			tokenColorAction.setFillColor(Color.BLACK);
			JComponent tokenColorButton = nestedAdd(tokenColorAction);
			myPanel.add(tokenColorButton);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Colour could not be added. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);
		}

		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Type Unique Name:"));
		myPanel.add(textField);
		tokenColorAction.setParent(myPanel);
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter Name and Color of the new Token", JOptionPane.OK_CANCEL_OPTION);

		String newTokenColor = textField.getText();
		AbstractMarking cpnMarking = cpn.getMarking();
		Multiset<String> placeMarking = (Multiset<String>) cpnMarking.get(name);
		if (placeMarking == null)
			placeMarking = new Multiset<String>();
		if (placeMarking.contains(newTokenColor))
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), "Token Name already exists", "Problem", JOptionPane.ERROR_MESSAGE);
		else if (newTokenColor.equals(""))
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), "Token Name is empty", "Problem", JOptionPane.ERROR_MESSAGE);
		else {
			placeMarking.add(newTokenColor);

			// UpdateBlock
			PNGraph graph = editor.getGraphComponent().getGraph();
			mxGraphModel model = ((mxGraphModel) graph.getModel());
			model.beginUpdate();
			((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenColor, tokenColorAction.getButtonFillColor()));
			((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenChange((PNGraph) editor.getGraphComponent().getGraph(), name, placeMarking));
			((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenColor, tokenColorAction.getButtonFillColor()));
			model.endUpdate();

		}

	}

	private void addRow(final AbstractMarking wholeMarking, final String tokenLabel) {
		final String name = tokenLabel;
		Color tokenColor = colorMap.get(tokenLabel);

		final JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		Dimension dim = new Dimension();
		double width = TOKEN_ROW_WIDTH;
		double height = TOKEN_ROW_HEIGHT;
		dim.setSize(width, height);
		row.setPreferredSize(dim);
		row.setMinimumSize(dim);
		row.setMaximumSize(dim);
		row.setSize(dim);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel firstElement = new JPanel();
		try {
			TokenColorSelectionAction tokenColorAction = new TokenColorSelectionAction(editor, tokenLabel);
			if (tokenColor != null)
				tokenColorAction.setFillColor(tokenColor);
			else
				tokenColorAction.setFillColor(Color.BLACK);
			JComponent tokenColorButton = nestedAdd(tokenColorAction);
			firstElement.add(tokenColorButton);

			RestrictedTextField textField = new RestrictedTextField(Restriction.NOT_EMPTY, name);

			addTokenRenamingListener(tokenLabel, textField);

			firstElement.add(textField);

			row.add(firstElement, BorderLayout.LINE_START);

			JButton remove = new JButton(IconFactory.getIcon("minimize"));
			addColorRemovalListener(tokenLabel, remove);

			row.add(remove, BorderLayout.LINE_END);
			tokenPanel.add(row);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Action could not be added. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);
		}

	}

	private void addColorRemovalListener(final String tokenLabel, JButton remove) {
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				PNGraph graph = editor.getGraphComponent().getGraph();
				mxGraphModel model = ((mxGraphModel) graph.getModel());
				CPN cpn = (CPN) editor.getNetContainer().getPetriNet();
				AbstractCPNMarking im = (AbstractCPNMarking) editor.getNetContainer().getPetriNet().getInitialMarking();

				// UpdateBlock
				model.beginUpdate();
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));

				for (CPNFlowRelation flowrelation : cpn.getFlowRelations()) {

					Multiset<String> constraint = flowrelation.getConstraint();
					if (constraint != null) {
						if (constraint.contains(tokenLabel)) {
							constraint.setMultiplicity(tokenLabel, 0);
							model.execute(new ConstraintChange((PNGraph) graph, flowrelation.getName(), constraint));
						}
					}
				}

				for (CPNPlace place : cpn.getPlaces()) {
					Multiset<String> multiSet = (Multiset<String>) im.get(place.getName());
					if (multiSet != null) {
						if (multiSet.contains(tokenLabel)) {
							multiSet.remove(tokenLabel);
							model.execute(new TokenChange((PNGraph) graph, place.getName(), multiSet));
						}
					}
				}

				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));

				model.endUpdate();
			}
		});
	}

	private void addTokenRenamingListener(final String tokenLabel, RestrictedTextField textField) {
		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RestrictedTextField tf = (RestrictedTextField) e.getSource();
				String newTokenName = tf.getText();

				PNGraph graph = editor.getGraphComponent().getGraph();
				mxGraphModel model = ((mxGraphModel) graph.getModel());

				CPN pn = (CPN) graph.getNetContainer().getPetriNet();
				CPNGraphics pnGraphics = (CPNGraphics) graph.getNetContainer().getPetriNetGraphics();
				CPNMarking am = pn.getInitialMarking();
				Map<String, Color> colorsMap = pnGraphics.getColors();
				Color color = colorsMap.get(tokenLabel);

				// UpdateBlock
				model.beginUpdate();
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, color));

				for (CPNFlowRelation flowrelation : pn.getFlowRelations()) {
					Multiset<String> constraint = flowrelation.getConstraint();
					if (constraint != null) {
						if (constraint.contains(tokenLabel)) {
							int constraintMultiplicity = constraint.multiplicity(tokenLabel);
							constraint.setMultiplicity(newTokenName, constraintMultiplicity);
							constraint.setMultiplicity(tokenLabel, 0);
							model.execute(new ConstraintChange((PNGraph) graph, flowrelation.getName(), constraint));
						}
					}
				}

				for (CPNPlace place : pn.getPlaces()) {
					Multiset<String> multiSet = (Multiset<String>) am.get(place.getName());
					if (multiSet != null) {
						if (multiSet.contains(tokenLabel)) {
							int multiplicity = multiSet.multiplicity(tokenLabel);
							multiSet.setMultiplicity(newTokenName, multiplicity);
							multiSet.remove(tokenLabel);
							model.execute(new TokenChange((PNGraph) graph, place.getName(), multiSet));
						}
					}

				}

				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenName, color));
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));
				model.endUpdate();
			}
		});
	}

	private JComponent nestedAdd(Action action) {
		JToggleButton b = createToggleActionComponent(action);
		b.setAction(action);
		return b;
	}

	protected JToggleButton createToggleActionComponent(Action a) {
		JToggleButton b = new JToggleButton() {
			private static final long serialVersionUID = -3143341784881719155L;

			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
				return super.createActionPropertyChangeListener(a);
			}
		};
		if (a != null && (a.getValue(Action.SMALL_ICON) != null || a.getValue(Action.LARGE_ICON_KEY) != null)) {
			b.setHideActionText(true);
		}
		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		b.setBorderPainted(false);
		return b;
	}

	public void updateTCTBView() {
		tokenPanel.removeAll();
		AbstractCPN cpn = (AbstractCPN) editor.getGraphComponent().getGraph().getNetContainer().getPetriNet();
		Set<String> colorsTemp = cpn.getTokenColors();
		for (String colorName : colorsTemp) {
			addRow(cpn.getInitialMarking(), colorName);
		}

	}
}
