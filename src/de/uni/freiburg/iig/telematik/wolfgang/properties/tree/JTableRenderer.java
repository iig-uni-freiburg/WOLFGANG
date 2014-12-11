package de.uni.freiburg.iig.telematik.wolfgang.properties.tree;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

public class JTableRenderer implements TableCellRenderer {

	JScrollPane scrollPane;
	JTextField textField;

	public JTableRenderer() {
		textField = new JTextField();
		scrollPane = new JScrollPane(textField);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (value instanceof JTextField)
			textField = (JTextField) value;
		else
			textField.setText((String) value);
		
		return textField;
	}

}
