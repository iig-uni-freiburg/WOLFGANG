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
import java.io.IOException;
import java.util.HashSet;
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
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.TokenColorSelectionAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.AccessModeChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.CapacityChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class TokenToolBar extends JToolBar {

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

	public TokenToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		this.editor = pnEditor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		AbstractPetriNet net = pnEditor.getNetContainer().getPetriNet();

		Set<String> colors = null;
		if (net instanceof AbstractCPN) {
			AbstractCPN cpn = (AbstractCPN) net;
			AbstractCPNGraphics graphics = (AbstractCPNGraphics) pnEditor.getNetContainer().getPetriNetGraphics();
			colors = cpn.getTokenColors();

			colorMap = graphics.getColors();
			colorMap.put("black", Color.BLACK);
		}
		if (net instanceof IFNet) {
			IFNet IFNet = (IFNet) net;
			IFNetGraphics graphics = (IFNetGraphics) pnEditor.getNetContainer().getPetriNetGraphics();
			colors = IFNet.getTokenColors();

			colorMap = graphics.getColors();
			colorMap.put("black", Color.BLACK);
		}
		setFloatable(false);

		loadTokenPanel(net, colorMap.keySet());

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
		} catch (ParameterException e1) {
		} catch (PropertyException e1) {
		} catch (IOException e1) {
		}

		addButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				AbstractCPN cpn = (AbstractCPN) editor.getGraphComponent().getGraph().getNetContainer().getPetriNet();

				addNewTokenColor();

				Set<String> colorsTemp = cpn.getTokenColors();
				tokenPanel.removeAll();
				for (String colorName : colorMap.keySet()) {
					addRow(cpn.getInitialMarking(), colorName);
				}
				editor.getGraphComponent().repaint();

			}
		});
	}

	protected void addNewTokenColor() {

		JTextField textField = new RestrictedTextField(Restriction.NOT_EMPTY, 5);

		TokenColorSelectionAction tokenColorAction = null;
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Select Color:"));

		try {
			tokenColorAction = new TokenColorSelectionAction(editor);
			tokenColorAction.setFillColor(Color.BLACK);
			JComponent tokenColorButton = nestedAdd(tokenColorAction);
			myPanel.add(tokenColorButton);
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}

		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Type Unique Name:"));
		myPanel.add(textField);
		tokenColorAction.setParent(myPanel);
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter Name and Color of the new Token", JOptionPane.OK_CANCEL_OPTION);

		String newTokenColor = textField.getText();

		if (colorMap.keySet().contains(newTokenColor))
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), "Token Name already exists", "Problem", JOptionPane.ERROR_MESSAGE);
		else if (newTokenColor.equals(""))
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), "Token Name is empty", "Problem", JOptionPane.ERROR_MESSAGE);
		else {
			colorMap.put(newTokenColor, tokenColorAction.getButtonFillColor());

			// UpdateBlock

			((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenColor, tokenColorAction.getButtonFillColor()));

			updateView();

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

		} catch (ParameterException e1) {
		} catch (PropertyException e1) {
		} catch (IOException e1) {
		}

	}

	private void addColorRemovalListener(final String tokenLabel, JButton remove) {
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				PNGraph graph = editor.getGraphComponent().getGraph();
				mxGraphModel model = ((mxGraphModel) graph.getModel());
				CPN cpn = null;
				if (editor.getNetContainer().getPetriNet() instanceof CPN)
					cpn = (CPN) editor.getNetContainer().getPetriNet();
				IFNet ifnet = null;
				if (editor.getNetContainer().getPetriNet() instanceof IFNet)
					ifnet = (IFNet) editor.getNetContainer().getPetriNet();
				AbstractCPNMarking im = (AbstractCPNMarking) editor.getNetContainer().getPetriNet().getInitialMarking();

				// UpdateBlock
				model.beginUpdate();
				if (editor.getNetContainer().getPetriNet() instanceof CPN) {
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
								multiSet.setMultiplicity(tokenLabel, 0);
								((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, place.getName(), multiSet));
							}
						}
						((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, place.getName(), tokenLabel, 0));

					}
				}

				if (editor.getNetContainer().getPetriNet() instanceof IFNet) {
					for (IFNetFlowRelation flowrelation : ifnet.getFlowRelations()) {

						Multiset<String> constraint = flowrelation.getConstraint();
						if (constraint != null) {
							if (constraint.contains(tokenLabel)) {
								constraint.setMultiplicity(tokenLabel, 0);
								model.execute(new ConstraintChange((PNGraph) graph, flowrelation.getName(), constraint));
							}
						}
					}
					for (IFNetPlace place : ifnet.getPlaces()) {
						Multiset<String> multiSet = (Multiset<String>) im.get(place.getName());
						if (multiSet != null) {
							if (multiSet.contains(tokenLabel)) {
								multiSet.setMultiplicity(tokenLabel, 0);
								((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, place.getName(), multiSet));
							}
						}
						((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, place.getName(), tokenLabel, 0));

					}

					for (AbstractIFNetTransition<IFNetFlowRelation> transition : ifnet.getTransitions()) {
						((mxGraphModel) graph.getModel()).execute(new AccessModeChange(graph, transition.getName(), tokenLabel, new HashSet<AccessMode>()));
						if (transition instanceof AbstractRegularIFNetTransition)
							((AbstractRegularIFNetTransition) transition).removeAccessModes(tokenLabel);
					}
				}
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));

				model.endUpdate();

				updateView();

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
				CPN pn = null;
				if (editor.getNetContainer().getPetriNet() instanceof CPN)
					pn = (CPN) graph.getNetContainer().getPetriNet();
				IFNet ifnet = null;
				if (editor.getNetContainer().getPetriNet() instanceof IFNet)
					ifnet = (IFNet) graph.getNetContainer().getPetriNet();
				AbstractCPNGraphics pnGraphics = (AbstractCPNGraphics) graph.getNetContainer().getPetriNetGraphics();
				CPNMarking am = pn.getInitialMarking();
				Map<String, Color> colorsMap = pnGraphics.getColors();
				Color color = colorsMap.get(tokenLabel);

				// UpdateBlock
				model.beginUpdate();
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, color));
				if (editor.getNetContainer().getPetriNet() instanceof CPN) {
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
				}
				if (editor.getNetContainer().getPetriNet() instanceof IFNet) {
					for (IFNetFlowRelation flowrelation : ifnet.getFlowRelations()) {
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

					for (IFNetPlace place : ifnet.getPlaces()) {
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
				}
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenName, color));
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));
				model.endUpdate();
				updateView();

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

	public void updateView() {
		tokenPanel.removeAll();
		AbstractCPN cpn = (AbstractCPN) editor.getGraphComponent().getGraph().getNetContainer().getPetriNet();
		Set<String> colorsTemp = cpn.getTokenColors();
		for (String colorName : colorsTemp) {
			addRow(cpn.getInitialMarking(), colorName);
		}

	}
}
