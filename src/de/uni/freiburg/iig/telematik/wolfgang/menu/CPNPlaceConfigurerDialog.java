package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.CapacityChange;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class CPNPlaceConfigurerDialog extends AbstractTokenConfigurerDialog {

	public CPNPlaceConfigurerDialog(Window window, String name, PNGraph cpnGraph) {
		super(window, name, cpnGraph);
	}

	private AbstractCPNPlace getPlace() {
		return (AbstractCPNPlace) graph.getNetContainer().getPetriNet().getPlace(cellName);
	}

	@Override
	protected String getCellSpecificHeadline() {
		return "Token";
	}

	@Override
	protected int getSpinnerCapacity(String tokenName) {
		return !(((AbstractCPNPlace) graph.getNetContainer().getPetriNet().getPlace(cellName)).getColorCapacity(tokenName) < 0) ? ((AbstractCPNPlace) graph.getNetContainer().getPetriNet()
				.getPlace(cellName)).getColorCapacity(tokenName) : MAX_CAPACITY;
	}

	@Override
	protected String getCellSpecific2ndHeadline() {
		return "Capacity";
	}

	@Override
	protected JSpinner get2ndSpinner(final String tokenLabel) {
		JSpinner capacitySpinner;
		if (!getPlace().isBounded()) {
			String[] string = { "\u221e" };
			SpinnerModel capacityModel = new SpinnerListModel(string);
			capacitySpinner = new JSpinner(capacityModel);
		} else {
			int capacitiy = getPlace().getColorCapacity(tokenLabel);
			SpinnerModel capacityModel = new SpinnerNumberModel(capacitiy, getMultiSet().multiplicity(tokenLabel), MAX_CAPACITY, 1);
			capacitySpinner = new JSpinner(capacityModel);
		}

		capacitySpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner capacitySpinner = (JSpinner) e.getSource();
				Integer currentValue = (Integer) capacitySpinner.getValue();

				((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, getPlace().getName(), tokenLabel, currentValue));
				updateTokenConfigurerView();
			}
		});
		return capacitySpinner;
	}

	@Override
	protected void createCellSpecificAddBtnAction(String color) {
		if (((AbstractCPNPlace) graph.getNetContainer().getPetriNet().getPlace(cellName)).getColorCapacity(color) == 0)
			((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, cellName, color, 1));
		getMultiSet().setMultiplicity(color, 1);
		((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, cellName, getMultiSet()));

	}

	@Override
	protected void createCellSpecificRemoveBtnAction(String tokenName) {
		if (getMultiSet() != null) {
			getMultiSet().setMultiplicity(tokenName, 0);
			((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, cellName, getMultiSet()));
		}
		((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, cellName, tokenName, 0));

	}

	@Override
	protected mxAtomicGraphModelChange createCellSpecificChange(PNGraph graph, String paName, Multiset<String> newMarking) {
		return new TokenChange((PNGraph) graph, paName, newMarking);
	}

	@Override
	protected Multiset<String> getCellSpecificMultiSet() {
		return (Multiset<String>) graph.getNetContainer().getPetriNet().getInitialMarking().get(cellName);
	}

	@Override
	protected boolean shouldAddRow(String color) {
		return getPlace().getColorCapacity(color) > 0 || getMultiSet().contains(color);

	}

	@Override
	protected void createCellSpecificComponents() {
		try {
			Dimension dim2 = new Dimension(35, 35);
			JToggleButton infiniteButton = getInfiniteButton(dim2);
			JToggleButton boundButton = getBoundButon(dim2);

			JPanel boundOrInfinite = new JPanel(new SpringLayout());
			boundOrInfinite.add(infiniteButton);
			boundOrInfinite.add(boundButton);
			if (getPlace().isBounded()) {
				boundButton.setEnabled(false);
				infiniteButton.setEnabled(true);
			}
			if (!getPlace().isBounded()) {
				boundButton.setEnabled(true);
				infiniteButton.setEnabled(false);
			}
			SpringUtilities.makeCompactGrid(boundOrInfinite, 1, 2, 1, 1, 1, 1);
			mainPanel().add(boundOrInfinite);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Buttons could not be added. \nReason: " + e.getMessage(), "" + e.getClass(), JOptionPane.ERROR);
		}
	}

	private JToggleButton getInfiniteButton(Dimension dim2) throws PropertyException, IOException {
		JToggleButton infiniteButton = new JToggleButton(IconFactory.getIcon("infinite"));
		infiniteButton.setBorder(BorderFactory.createEmptyBorder());

		infiniteButton.setPreferredSize(dim2);
		infiniteButton.setMinimumSize(dim2);
		infiniteButton.setMaximumSize(dim2);
		infiniteButton.setSize(dim2);
		infiniteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Set<String> tokencolors = getTokenColors();
				int newCapacity = -1;
				((mxGraphModel) graph.getModel()).beginUpdate();
				for (String color : tokencolors)
					((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, cellName, color, newCapacity));
				((mxGraphModel) graph.getModel()).endUpdate();
				updateTokenConfigurerView();
			}

		});
		return infiniteButton;
	}

	private JToggleButton getBoundButon(Dimension dim2) throws PropertyException, IOException {
		JToggleButton boundButton = new JToggleButton(IconFactory.getIcon("bounded"));
		boundButton.setBorder(BorderFactory.createEmptyBorder());
		boundButton.setPreferredSize(dim2);
		boundButton.setMinimumSize(dim2);
		boundButton.setMaximumSize(dim2);
		boundButton.setSize(dim2);
		boundButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Set<String> tokencolors = getTokenColors();
				((mxGraphModel) graph.getModel()).beginUpdate();
				for (String color : tokencolors) {
					int newCapacity = getMultiSet().multiplicity(color);
					if(newCapacity==0) newCapacity = 1;
					((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, cellName, color, newCapacity));
				}

				((mxGraphModel) graph.getModel()).endUpdate();
				updateTokenConfigurerView();

			}
		});
		return boundButton;
	}

	@Override
	protected void setTitle() {
		// TODO Auto-generated method stub

	}




	@Override
	protected int getMinimumCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isRemoveBtnEnabled() {
		return true;
	}

	
	public static CPNPlaceConfigurerDialog showDialog(Window window, String string, PNGraph cpnGraph) {
		CPNPlaceConfigurerDialog dialog = null;
		try {
			dialog  = new CPNPlaceConfigurerDialog(window, string, cpnGraph);
			dialog.setUpGUI();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window, "Cannot launch ExceptionDialog.\nReason: " + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
		return dialog;
	}




}
