package de.uni.freiburg.iig.telematik.wolfgang.properties.view.tree;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PropertiesView.PropertiesField;

public class EditorForPropertiesFieldColumn implements TableCellEditor {

	JTextField txt;
	JScrollPane scp;

	public EditorForPropertiesFieldColumn() {
		setUpGui();
	}

	private void setUpGui() {
		txt = new JTextField();
		scp = new JScrollPane(txt);	
	}

	public Component getTableCellEditorComponent(final JTable tbl, Object value, boolean isSelected, final int row, int column) {
		if (value instanceof JTextField)
			txt = (PropertiesField) value;
		else
			txt.setText((String) value);
		// table.clearSelection();
		txt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				tbl.removeRowSelectionInterval(row, row);

			}

			@Override
			public void focusGained(FocusEvent e) {
				tbl.setRowSelectionInterval(row, row);
			}
		});
		return txt;
	}

	public void addCellEditorListener(CellEditorListener l) {
	}

	public void cancelCellEditing() {
	}

	public Object getCellEditorValue() {
		return txt.getText();
	}

	public boolean isCellEditable(EventObject anEvent) {

		return true;
	}

	public void removeCellEditorListener(CellEditorListener l) {
	}

	public boolean shouldSelectCell(EventObject anEvent) {

		return false;
	}

	public boolean stopCellEditing() {
		return true;
	}
	
	

}


