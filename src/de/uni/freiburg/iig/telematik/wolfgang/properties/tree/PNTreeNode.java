package de.uni.freiburg.iig.telematik.wolfgang.properties.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperty;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PropertiesView.PropertiesField;

public class PNTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517382720076787324L;

	private PropertiesField textfield;

	private PNTreeNodeType fieldType;

	private Map<PNProperty, PropertiesField> fieldMap;

	private JTable table;
    
    public PNProperty getPropertyType() {
		return textfield.getPNProperty();
	}

	public PNTreeNode(String title, PNTreeNodeType type) {
        super(title);
        this.fieldType = type;
    }
    
    public PNTreeNode(String title, PNTreeNodeType type, PropertiesField field) {
    	super(title);
    	this.fieldType = type;
        this.textfield  = field;
    }

    public PNTreeNode(HashMap<PNProperty, PropertiesField> fieldMap, PNTreeNodeType type) {
    	super("leaf");
    	this.setFieldMap(fieldMap);
    	this.fieldType = type;
	}

	public PNTreeNode(JTable table, PNTreeNodeType type) {
    	super("leaf");
    	this.table = table;
    	this.fieldType = type;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTextField getTextfield() {
		return textfield;
	}
	
	public PNTreeNodeType getFieldType() {
		return fieldType;
	}

	public void setTextField(PropertiesField field) {
		this.textfield = field;
		
	}

	public Map<PNProperty, PropertiesField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(HashMap<PNProperty, PropertiesField> fieldMap2) {
		this.fieldMap = fieldMap2;
	}
   
}