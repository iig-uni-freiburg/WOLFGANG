package de.uni.freiburg.iig.telematik.wolfgang.properties.view.tree;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

public class JTableRenderer implements TableCellRenderer {

	JScrollPane scp;
	JTextField txt;

	public JTableRenderer() {
		setUpGui();
	}

	private void setUpGui() {
		txt = new JTextField();
		scp = new JScrollPane(txt);	
	}

	@Override
	public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (value instanceof JTextField){
			txt = (JTextField) value;
			}
		else{
			txt.setText((String) value);}
		
		return txt;
	}

}
