package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.wolfgang.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.change.ConstraintChange;

public class ConstraintConfigurerDialog extends AbstractTokenConfigurerDialog {

	public ConstraintConfigurerDialog(Window window, String name, PNGraph cpnGraph) {
		super(window, name, cpnGraph);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void okProcedure() {
		if (((CPNGraph) graph).getNetContainer().getPetriNet().getFlowRelation(cellName).getConstraint().isEmpty()) {
			int result = JOptionPane.showConfirmDialog(this, "There are currenlty no constraints for " + cellName + ". If you confirm, " + cellName
					+ " will be deleted. \n To add new constraints press \"no\".", "Warning", JOptionPane.YES_NO_OPTION);
			if (result != 1){
				super.okProcedure();
				PNGraphCell cell = graph.getNodeCell(cellName);
				graph.removeCells(new Object[]{cell});
			}
		} else
			super.okProcedure();

	}

	@Override
	protected String getCellSpecificHeadline() {
		return "Constraints";
	}

	@Override
	protected void createCellSpecificAddBtnAction(String color) {
		getMultiSet().setMultiplicity(color, 1);
		((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph) graph, cellName, getMultiSet()));
	}

	@Override
	protected void createCellSpecificRemoveBtnAction(String tokenName) {
		getMultiSet().setMultiplicity(tokenName, 0);
		((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph) graph, cellName, getMultiSet()));
	}

	@Override
	protected int getSpinnerCapacity(String tokenLabel) {
		return MAX_CAPACITY;
	}

	@Override
	protected Multiset<String> getCellSpecificMultiSet() {
		return (Multiset<String>) graph.getNetContainer().getPetriNet().getFlowRelation(cellName).getConstraint();
	}

	@Override
	protected boolean shouldAddRow(String color) {
		return getMultiSet().contains(color);
	}

	@Override
	protected mxAtomicGraphModelChange createCellSpecificChange(PNGraph graph, String paName, Multiset<String> newMarking) {
		return new ConstraintChange((PNGraph) graph, paName, newMarking);
	}

	@Override
	protected void createCellSpecificComponents() {
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());
		mainPanel().add(Box.createGlue());

	}

	// No 2nd col
	@Override
	protected String getCellSpecific2ndHeadline() {
		return null;
	}

	// No 2nd spinner
	@Override
	protected JSpinner get2ndSpinner(String tokenLabel) {
		return null;
	}

	@Override
	protected void setTitle() {
		// TODO Auto-generated method stub

	}

	public static ConstraintConfigurerDialog showDialog(Window window, String string, PNGraph cpnGraph) {
		ConstraintConfigurerDialog dialog = null;
		try {
			dialog = new ConstraintConfigurerDialog(window, string, cpnGraph);
			dialog.setUpGUI();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window, "Cannot launch ExceptionDialog.\nReason: " + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
		return dialog;
	}

	@Override
	protected boolean isControlFlowRemoveable() {
		// TODO Auto-generated method stub
		return false;
	}

}
