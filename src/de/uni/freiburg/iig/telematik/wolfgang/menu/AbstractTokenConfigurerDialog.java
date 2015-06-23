package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.graphic.component.RestrictedTextField;
import de.invation.code.toval.graphic.component.event.RestrictedTextFieldListener;
import de.invation.code.toval.graphic.dialog.AbstractDialog;
import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.wolfgang.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public abstract class AbstractTokenConfigurerDialog extends AbstractDialog {
	protected static final int MAX_CAPACITY = 99;
	private static final int SPINNER_DEFAULT_WIDTH = 63;

	private JButton addButton;
	protected PNGraph graph;
	protected Map<String, Color> colors;
	protected String cellName;
	private Multiset<String> multiSet;

	public AbstractTokenConfigurerDialog(Window window, String name, PNGraph cpnGraph) {
		super(window, name);
		mainPanel().setLayout(new SpringLayout());
		setIncludeCancelButton(false);
		setOKButtonText("finished");
		cellName = name;
		graph = cpnGraph;

	}

	public void updateTokenConfigurerView() {

		AbstractCPNGraphics cpnGraphics = (AbstractCPNGraphics) getNetContainer().getPetriNetGraphics();
		colors = cpnGraphics.getColors();

		mainPanel().removeAll();

		createAddBtn();

		mainPanel().add(new JLabel(getCellSpecificHeadline()));

		// Nötig?
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());

		if (getCellSpecific2ndHeadline() != null)
			mainPanel().add(new JLabel(getCellSpecific2ndHeadline()));
		else
			mainPanel().add(Box.createGlue());

		mainPanel().add(Box.createGlue());

		if (getTokenColors().contains("black"))
			colors.put("black", Color.BLACK);

		int size = 0;
		for (String color : colors.keySet()) {
			if (shouldAddRow(color)) {
				addRow(color);
				size++;
			}
		}

		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());

		createCellSpecificComponents();
		mainPanel().add(Box.createGlue());

		SpringUtilities.makeCompactGrid(mainPanel(), size + 2, 6, 6, 6, 6, 6);

		pack();

	}

	protected abstract boolean shouldAddRow(String color);

	private void createAddBtn() {
		try {
			addButton = new JButton(IconFactory.getIcon("maximize"));
			creaeAddBtnListener();
			mainPanel().add(addButton);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Buttons could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
	}

	protected abstract void createCellSpecificComponents();

	protected abstract String getCellSpecificHeadline();

	protected abstract String getCellSpecific2ndHeadline();

	private void creaeAddBtnListener() {
		addButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JPopupMenu popup = new JPopupMenu();
				for (Entry<String, Color> c : colors.entrySet()) {
					final String color = c.getKey();
					if (!getMultiSet().contains(color)) {
						JMenuItem item = new JMenuItem(color);
						item.setName(color);

						item.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								((mxGraphModel) graph.getModel()).beginUpdate();
								createCellSpecificAddBtnAction(color);
								((mxGraphModel) graph.getModel()).endUpdate();

								if (getMultiSet().contains(colors.keySet())) {
									addButton.setEnabled(false);
								}

								updateTokenConfigurerView();
								pack();
							}

						});
						popup.add(item);
					}

				}

				popup.show(addButton, addButton.getWidth() * 4 / 5, addButton.getHeight() * 4 / 5);
			}
		});
	}

	protected abstract void createCellSpecificAddBtnAction(final String color);

	protected void addRow(String tokenLabel) {

		mainPanel().add(getTokenCircle(tokenLabel));
		mainPanel().add(getTokenSpinner(tokenLabel));
		mainPanel().add(new JLabel(tokenLabel));

		mainPanel().add(Box.createGlue());

		if (get2ndSpinner(tokenLabel) != null) {
			mainPanel().add(get2ndSpinner(tokenLabel));
		} else {
			mainPanel().add(Box.createGlue());
		}

		
		mainPanel().add(getRemoveButton(tokenLabel));
	}

	protected abstract boolean isControlFlowRemoveable();

	protected abstract JSpinner get2ndSpinner(final String tokenLabel);

	private TokenSpinner getTokenSpinner(final String tokenLabel) {
		int size = getMultiSet().multiplicity(tokenLabel);
		int cap = getSpinnerCapacity(tokenLabel);
		int min = 0;
		int step = 1;
		SpinnerModel model = new SpinnerNumberModel(size, min, cap, step);
		TokenSpinner spinner = new TokenSpinner(model, tokenLabel, cap);
		int w = spinner.getWidth();
		int h = spinner.getHeight();
		Dimension d = new Dimension(SPINNER_DEFAULT_WIDTH, h);
		spinner.setPreferredSize(d);
		spinner.setMinimumSize(d);

		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("change");
				JSpinner spinner = (TokenSpinner) e.getSource();
				Integer currentValue = (Integer) spinner.getValue();
				System.out.println("current" + currentValue);
				if (currentValue > MAX_CAPACITY)
					spinner.setValue(MAX_CAPACITY);

				Multiset<String> newMarking = getMultiSet();
				if (newMarking == null)
					newMarking = new Multiset<String>();
				newMarking.setMultiplicity(tokenLabel, currentValue);
				((mxGraphModel) graph.getModel()).execute(createCellSpecificChange((PNGraph) graph, cellName, newMarking));
			}

		});
		return spinner;
	}

	protected abstract mxAtomicGraphModelChange createCellSpecificChange(PNGraph graph, String paName, Multiset<String> newMarking);

	protected abstract int getSpinnerCapacity(String tokenLabel);

	private JButton getRemoveButton(final String tokenName) {
		JButton remove = null;
		try {
			remove = new JButton(IconFactory.getIcon("minimize"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				((mxGraphModel) graph.getModel()).beginUpdate();
				if (isControlFlowRemoveable())
				createCellSpecificRemoveBtnAction(tokenName);
				((mxGraphModel) graph.getModel()).endUpdate();
				addButton.setEnabled(true);
				pack();
				updateTokenConfigurerView();
			}

		});	
		return remove;
	}

	protected abstract void createCellSpecificRemoveBtnAction(String tokenName);

	private CirclePanel getTokenCircle(String tokenLabel) {
		Color tokenColor = colors.get(tokenLabel);
		CirclePanel circle = null;
		try {
			circle = new CirclePanel(tokenColor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Circle-Panel could not be generated. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		return circle;
	}

	protected Multiset<String> getMultiSet() {
		if (multiSet == null) {
			multiSet = (getCellSpecificMultiSet() == null) ? new Multiset<String>() : getCellSpecificMultiSet();
		}
		return multiSet;
	};

	protected abstract Multiset<String> getCellSpecificMultiSet();

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		AbstractGraphicalCPN graphicalNet = (AbstractGraphicalCPN) graph.getNetContainer();
		return graphicalNet;
	}

	protected Set<String> getTokenColors() {
		AbstractCPN net = (AbstractCPN) graph.getNetContainer().getPetriNet();
		return net.getTokenColors();
	}

	private class TokenSpinner extends JSpinner implements RestrictedTextFieldListener {

		private String tokenName;
		private int capacity;

		public TokenSpinner(SpinnerModel model, String tokenName, int cap) {
			super(model);
			this.tokenName = tokenName;
			this.capacity = cap;
		}

		@Override
		public void valueChanged(String oldValue, String newValue) {

			Integer currentValue = new Integer(newValue);
			if (currentValue <= capacity) {
				this.setValue(currentValue);
				Multiset<String> newMarking = getMultiSet();
				if (newMarking == null)
					newMarking = new Multiset<String>();
				newMarking.setMultiplicity(tokenName, currentValue);
				((mxGraphModel) graph.getModel()).execute(createCellSpecificChange((PNGraph) graph, cellName, newMarking));
			} else
				((RestrictedTextField) getEditor()).setText("" + capacity);
		}

	}

	@Override
	protected void addComponents() throws Exception {
		updateTokenConfigurerView();
	}
	
}
