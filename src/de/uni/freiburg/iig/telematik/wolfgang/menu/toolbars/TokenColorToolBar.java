package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.component.RestrictedTextField;
import de.invation.code.toval.graphic.component.event.RestrictedTextFieldListener;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.CirclePanel;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.TokenColorChooserPanel.ColorMode;

public class TokenColorToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;
	// Buttons

	// further variables
	private Map<String, Color> mapColorsForToolBar;
	private PNEditorComponent editor;
	private JPanel pnlTokenColors;
	private JButton addButton;
	private Image propertiesImage = IconFactory.getIconImageFixSize("edit_properties");
	private PopUpToolBarAction tokenAction;
	private static final String InitialPlaceHolderTokenColorName = "-type name-";

	public TokenColorToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		this.editor = pnEditor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pnlTokenColors = new JPanel(new SpringLayout());
		setFloatable(false);
		setUpGui();

	}

	private void createTokenColorMap() {
		AbstractCPNGraphics graphics = (AbstractCPNGraphics) this.editor.getNetContainer().getPetriNetGraphics();
		mapColorsForToolBar = graphics.getColors();
		mapColorsForToolBar.put("black", Color.BLACK);

	}

	public void setUpGui() {

		pnlTokenColors.removeAll();
		createAddBtn();

		pnlTokenColors.add(new JLabel("Color"));
		pnlTokenColors.add(Box.createGlue());
		pnlTokenColors.add(Box.createGlue());
		pnlTokenColors.add(Box.createGlue());
		pnlTokenColors.add(Box.createGlue());
		addRow("black");
		int size = 0;
		for (String color : mapColorsForToolBar.keySet()) {
			if (!color.equals("black"))
				addRow(color);
			size++;
		}
		SpringUtilities.makeCompactGrid(pnlTokenColors, size + 1, 6, 6, 6, 6, 6);
		add(pnlTokenColors);
	}

	private void createAddBtn() {
		try {
			addButton = new JButton(IconFactory.getIcon("maximize"));
			creaeAddBtnListener();
			pnlTokenColors.add(addButton);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Buttons could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
	}

	private void creaeAddBtnListener() throws PropertyException, IOException {
		final CirclePanel circle = new CirclePanel(Color.BLACK);
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);

				TokenColorChooserPanel newColorField = new TokenColorChooserPanel(ColorMode.HEX, null, circle);
				for (MouseListener ml : newColorField.getLblColor().getMouseListeners()) {
					ml.mouseClicked(e);
				}
				ChangeTokenColorNameField newName = new ChangeTokenColorNameField(InitialPlaceHolderTokenColorName, newColorField);

				addNewRow(circle, newColorField, newName);
			}
		});
	}

	protected void addNewRow(CirclePanel circle, TokenColorChooserPanel newField, ChangeTokenColorNameField newName) {
		pnlTokenColors.add(circle);
		pnlTokenColors.add(newField);
		pnlTokenColors.add(newName);
		Component box1 = pnlTokenColors.add(Box.createGlue());
		Component box2 = pnlTokenColors.add(Box.createGlue());
		pnlTokenColors.add(getRemoveButton(newName.getText(), circle, newField, newName, box1, box2));
		SpringUtilities.makeCompactGrid(pnlTokenColors, pnlTokenColors.getComponentCount() / 6, 6, 6, 6, 6, 6);
		this.tokenAction.actionPerformed(null);
		newName.requestFocus();
	}

	protected Set<String> getTokenColors() {
		AbstractCPN net = (AbstractCPN) this.editor.getNetContainer().getPetriNet();
		return net.getTokenColors();
	}

	protected void addRow(String tokenLabel) {
		CirclePanel circle = getTokenCircle(tokenLabel);
		pnlTokenColors.add(circle);
		if (!tokenLabel.equals("black")) {
			pnlTokenColors.add(new TokenColorChooserPanel(ColorMode.HEX, mapColorsForToolBar.get(tokenLabel), circle));
			pnlTokenColors.add(new ChangeTokenColorNameField(tokenLabel, null));
		} else {
			JLabel blackLabel = new JLabel();
			blackLabel.setPreferredSize(new Dimension(TokenColorChooserPanel.PREFERRED_HEIGHT * 3, 30));
			blackLabel.setOpaque(true);
			blackLabel.setBackground(Color.black);
			blackLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			pnlTokenColors.add(blackLabel);
			pnlTokenColors.add(new JLabel(tokenLabel));
		}

		pnlTokenColors.add(Box.createGlue());
		pnlTokenColors.add(Box.createGlue());
		Component rmv = getRemoveButton(tokenLabel);
		pnlTokenColors.add(rmv);
		if (tokenLabel.equals("black"))
			rmv.setEnabled(false);
	}

	private CirclePanel getTokenCircle(String tokenLabel) {
		Color tokenColor = mapColorsForToolBar.get(tokenLabel);
		CirclePanel circle = null;
		try {
			circle = new CirclePanel(tokenColor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Circle-Panel could not be generated. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		return circle;
	}

	private JButton getRemoveButton(final String tokenName, final CirclePanel circle, final TokenColorChooserPanel newField, final ChangeTokenColorNameField newName, final Component box1,
			final Component box2) {
		try {
			final JButton remove = new JButton(IconFactory.getIcon("minimize"));
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tokenLabel = (newName == null) ? tokenName : newName.getText();
					if (!tokenLabel.equals(InitialPlaceHolderTokenColorName)) {
						((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).beginUpdate();
						PNGraph graph = editor.getGraphComponent().getGraph();
						mxGraphModel model = ((mxGraphModel) graph.getModel());
						CPN pn = (CPN) graph.getNetContainer().getPetriNet();
							for (CPNFlowRelation flowrelation : pn.getFlowRelations()) {
								Multiset<String> constraint = flowrelation.getConstraint();
								if (constraint != null) {
									if (constraint.contains(tokenLabel)) {
										constraint.setMultiplicity(tokenLabel, 0);
										model.execute(new ConstraintChange((PNGraph) graph, flowrelation.getName(), constraint));
									}
								}
							}
							for (CPNPlace place : pn.getPlaces()) {
								Multiset<String> multiSet = (Multiset<String>) pn.getInitialMarking().get(place.getName());
								if (multiSet != null) {
									if (multiSet.contains(tokenLabel)) {
										multiSet.remove(tokenLabel);
										model.execute(new TokenChange((PNGraph) graph, place.getName(), multiSet));
									}
								}
							}
						
						((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));

						((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).endUpdate();
					} else {
						pnlTokenColors.remove(circle);
						pnlTokenColors.remove(newField);
						pnlTokenColors.remove(newName);
						pnlTokenColors.remove(remove);
						pnlTokenColors.remove(box1);
						pnlTokenColors.remove(box2);
						SpringUtilities.makeCompactGrid(pnlTokenColors, pnlTokenColors.getComponentCount() / 6, 6, 6, 6, 6, 6);

					}

					tokenAction.actionPerformed(null);

				}

			});
			return remove;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		return null;
	}

	private Component getRemoveButton(String tokenLabel) {
		return getRemoveButton(tokenLabel, null, null, null, null, null);
	}


	public class ChangeTokenColorNameField extends RestrictedTextField implements RestrictedTextFieldListener {

		private static final long serialVersionUID = -2791152505686200734L;

		private TokenColorChooserPanel colorField;

		public ChangeTokenColorNameField(String name, TokenColorChooserPanel newField) {
			super(RestrictedTextField.Restriction.NOT_EMPTY, name);
			this.colorField = newField;
			addListener(this);
			Color bgcolor = UIManager.getColor("Panel.background");
			Dimension dim = new Dimension(102, 30);
			setMinimumSize(dim);
			setMaximumSize(dim);
			setPreferredSize(dim);
			this.setBackground(bgcolor);
		}

		@Override
		public void setValidateOnTyping(boolean validateOnTyping) {
			super.setValidateOnTyping(false);
		}

		@Override
		public void valueChanged(String oldValue, String newValue) {
			if (oldValue.equals(InitialPlaceHolderTokenColorName)) {
				((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newValue, colorField.getChosenColor()));
			} else {

				String newTokenName = getText();
				String tokenLabel = oldValue;
				PNGraph graph = editor.getGraphComponent().getGraph();
				mxGraphModel model = ((mxGraphModel) graph.getModel());
				CPN pn = (CPN) graph.getNetContainer().getPetriNet();
				AbstractCPNGraphics pnGraphics = (AbstractCPNGraphics) graph.getNetContainer().getPetriNetGraphics();
				CPNMarking am = pn.getInitialMarking();
				Map<String, Color> colorsMap = pnGraphics.getColors();
				Color color = colorsMap.get(tokenLabel);
				if (newTokenName.length() <= 15 && !oldValue.equals(newTokenName) && !colorsMap.keySet().contains(newValue)) {
					model.beginUpdate();

					((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, newTokenName, color));
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
					// Remove Old Color From Colorset
					((mxGraphModel) editor.getGraphComponent().getGraph().getModel()).execute(new TokenColorChange(editor, tokenLabel, null));
					model.endUpdate();
				} else {
					setText(oldValue);
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor.getGraphComponent()), "Tokenname \"" + getText() + "\" already exists or is too long (>15 Characters).",
							"Problem", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (getParent() != null)
				getParent().requestFocus();
			tokenAction.actionPerformed(null);
		}

		@Override
		public void setBorder(Border border) {
			// Remove Border from Textfield
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(propertiesImage, 89, 10, null);
		}

	}

	public static void main(String[] args) {

		JPanel panel = new JPanel();
		panel.add(new TokenColorToolBar(new CPNEditorComponent(), JToolBar.HORIZONTAL));
		new DisplayFrame(panel, true);
	}

	public void setPopUpToolBarAction(PopUpToolBarAction tokenAction) {
		this.tokenAction = tokenAction;
	}
}
