package de.uni.freiburg.iig.telematik.wolfgang.properties.tree;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import de.uni.freiburg.iig.telematik.wolfgang.properties.PropertiesView.PropertiesField;

public class EditorForPropertiesFieldColumn implements TableCellEditor {

	JTextField textField;
	JScrollPane scrollPane;

	public EditorForPropertiesFieldColumn() {
		textField = new JTextField();
		scrollPane = new JScrollPane(textField);
	}

	public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, int column) {
		if (value instanceof JTextField)
			textField = (PropertiesField) value;
		else
			textField.setText((String) value);
		// table.clearSelection();
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				table.removeRowSelectionInterval(row, row);

			}

			@Override
			public void focusGained(FocusEvent e) {
				table.setRowSelectionInterval(row, row);
			}
		});
		return textField;
	}

	public void addCellEditorListener(CellEditorListener l) {
	}

	public void cancelCellEditing() {
	}

	public Object getCellEditorValue() {
		return textField.getText();
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


