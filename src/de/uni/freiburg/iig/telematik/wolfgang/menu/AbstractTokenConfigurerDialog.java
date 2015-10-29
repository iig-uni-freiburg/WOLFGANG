package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public abstract class AbstractTokenConfigurerDialog extends AbstractDialog {
	protected static final int MAX_CAPACITY = 99;
	private static final int SPINNER_DEFAULT_WIDTH = 63;

	private JButton btnAdd;

	protected PNGraph graph;
	protected Map<String, Color> colors;
	protected String cellName;
	private Multiset<String> multiSet;

	public AbstractTokenConfigurerDialog(Window window, String name, PNGraph cpnGraph) {
		super(window, name);
		mainPanel().setLayout(new SpringLayout());
		setIncludeCancelButton(false);
		setOKButtonText("Finished");
		cellName = name;
		graph = cpnGraph;
		setButtonPanelLayout(ButtonPanelLayout.CENTERED);
		setMaximumSize(getPreferredSize());
		setResizable(false);

	}

	public void updateTokenConfigurerView() {
		if (getNetContainer() instanceof AbstractGraphicalCPN)
			colors = ((AbstractCPNGraphics) getNetContainer().getPetriNetGraphics()).getColors();
		if (getNetContainer() instanceof AbstractGraphicalPTNet) {
			colors = new HashMap<String, Color>();
			colors.put("black", Color.BLACK);
		}
		mainPanel().removeAll();

		if (shouldAddAddBtn())
			createAddBtn();
		else
			mainPanel().add(Box.createHorizontalStrut(25));

		mainPanel().add(new JLabel(getCellSpecificHeadline()));

		// Nötig?
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());

		if (getCellSpecific2ndHeadline() != null)
			mainPanel().add(new JLabel(getCellSpecific2ndHeadline()));
		else
			mainPanel().add(Box.createGlue());

		mainPanel().add(Box.createGlue());
		int size = 0;
		if (getTokenColors().contains("black")) {
			colors.put("black", Color.BLACK);
			if(shouldAddRow("black")){
			addRow("black");
			size++;}
		}

		TreeMap<String, Color> sortedColorMap = new TreeMap<String, Color>(colors);
		for (String color : sortedColorMap.keySet()) {
			if (shouldAddRow(color)) {
				if (!color.equals("black")) {
					addRow(color);
					size++;
				}
			}
		}

		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());

		createCellSpecificComponents();
		mainPanel().add(Box.createGlue());

		SpringUtilities.makeCompactGrid(mainPanel(), mainPanel().getComponentCount() / 6, 6, 6, 6, 6, 6);

		pack();

	}

	protected abstract boolean shouldAddAddBtn();

	protected abstract boolean shouldAddRow(String color);

	private void createAddBtn() {
		try {
			btnAdd = new JButton(IconFactory.getIcon("maximize"));
			btnAdd.setFocusable(false);
			creaeAddBtnListener();
			mainPanel().add(btnAdd);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Buttons could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
	}

	protected abstract void createCellSpecificComponents();

	protected abstract String getCellSpecificHeadline();

	protected abstract String getCellSpecific2ndHeadline();

	private void creaeAddBtnListener() {
		btnAdd.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				JPopupMenu pmn = new JPopupMenu();
				addColorItem(pmn, "black");
				TreeMap<String, Color> sortedColors = new TreeMap<String, Color>(colors);
				for (Entry<String, Color> c : sortedColors.entrySet()) {
					final String color = c.getKey();
					if (!color.equals("black"))
						addColorItem(pmn, color);
				}

				pmn.show(btnAdd, btnAdd.getWidth() * 4 / 5, btnAdd.getHeight() * 4 / 5);

			}

			private void addColorItem(JPopupMenu pmn, final String color) {
				if (!getMultiSet().contains(color)) {
					JMenuItem mniItem = new JMenuItem(color);
					mniItem.setName(color);

					mniItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							((mxGraphModel) graph.getModel()).beginUpdate();
							createCellSpecificAddBtnAction(color);
							((mxGraphModel) graph.getModel()).endUpdate();

							if (getMultiSet().contains(colors.keySet())) {
								if (btnAdd != null)
									btnAdd.setEnabled(false);
							}

							updateTokenConfigurerView();
							pack();
						}

					});
					pmn.add(mniItem);
				}
			}
		});
	}

	protected abstract void createCellSpecificAddBtnAction(final String color);

	protected void addRow(String lblToken) {

		mainPanel().add(getTokenCircle(lblToken));
		mainPanel().add(getTokenSpinner(lblToken));
		mainPanel().add(new JLabel(lblToken));

		mainPanel().add(Box.createGlue());

		if (get2ndSpinner(lblToken) != null) {
			mainPanel().add(get2ndSpinner(lblToken));
		} else {
			mainPanel().add(Box.createGlue());
		}

		JButton btnRmv = getRemoveButton(lblToken);
		mainPanel().add(btnRmv);
		btnRmv.setEnabled(isRemoveBtnEnabled());
	}

	protected abstract boolean isRemoveBtnEnabled();

	protected abstract JSpinner get2ndSpinner(final String lblToken);

	private TokenSpinner getTokenSpinner(final String lblToken) {
		int size = getMultiSet().multiplicity(lblToken);
		int cap = getSpinnerCapacity(lblToken);
		int min = getMinimumCapacity();
		int step = 1;
		SpinnerModel spnm = new SpinnerNumberModel(size, min, cap, step);
		TokenSpinner spn = new TokenSpinner(spnm, lblToken, cap);
		int w = spn.getWidth();
		int h = spn.getHeight();
		Dimension d = new Dimension(SPINNER_DEFAULT_WIDTH, h);
		spn.setPreferredSize(d);
		spn.setMinimumSize(d);

		spn.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spn = (TokenSpinner) e.getSource();
				Integer currentValue = (Integer) spn.getValue();
				if (currentValue > MAX_CAPACITY)
					spn.setValue(MAX_CAPACITY);

				Multiset<String> newMarking = getMultiSet();
				if (newMarking == null)
					newMarking = new Multiset<String>();
				newMarking.setMultiplicity(lblToken, currentValue);

				((mxGraphModel) graph.getModel()).execute(createCellSpecificChange((PNGraph) graph, cellName, newMarking));
			}

		});
		return spn;
	}

	protected abstract int getMinimumCapacity();

	protected abstract mxAtomicGraphModelChange createCellSpecificChange(PNGraph graph, String paName, Multiset<String> newMarking);

	protected abstract int getSpinnerCapacity(String lblToken);

	private JButton getRemoveButton(final String tokenName) {
		JButton btnRemove = null;
		try {
			btnRemove = new JButton(IconFactory.getIcon("minimize"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				((mxGraphModel) graph.getModel()).beginUpdate();
				createCellSpecificRemoveBtnAction(tokenName);
				((mxGraphModel) graph.getModel()).endUpdate();
				if (btnAdd != null)
					btnAdd.setEnabled(true);
				pack();
				updateTokenConfigurerView();
			}

		});
		return btnRemove;
	}

	protected abstract void createCellSpecificRemoveBtnAction(String tokenName);

	private CirclePanel getTokenCircle(String lblToken) {
		Color tokenColor = colors.get(lblToken);
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
		AbstractGraphicalPN graphicalNet = (AbstractGraphicalPN) graph.getNetContainer();
		return graphicalNet;
	}

	protected Set<String> getTokenColors() {
		if (graph.getNetContainer().getPetriNet() instanceof AbstractCPN)
			return ((AbstractCPN) graph.getNetContainer().getPetriNet()).getTokenColors();
		if (graph.getNetContainer().getPetriNet() instanceof AbstractPTNet) {
			HashSet<String> colorSet = new HashSet<String>();
			colorSet.add("black");
			return colorSet;
		}
		return null;
	}

	private class TokenSpinner extends JSpinner implements RestrictedTextFieldListener {

		private String tokenName;
		private int capacity;

		public TokenSpinner(SpinnerModel spnm, String tokenName, int cap) {
			super(spnm);
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

	public void resetMultiSet() {
		multiSet = null;
	}

}
