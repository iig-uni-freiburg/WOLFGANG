package de.uni.freiburg.iig.telematik.wolfgang.properties.view.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperty;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PropertiesView.PropertiesField;

public class PNTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517382720076787324L;

	private PropertiesField txt;

	private PNTreeNodeType fieldType;

	private Map<PNProperty, PropertiesField> fieldMap;

	private JTable tbl;
    
    public PNProperty getPropertyType() {
		return txt.getPNProperty();
	}

	public PNTreeNode(String title, PNTreeNodeType type) {
        super(title);
        this.fieldType = type;
    }
    
    public PNTreeNode(String title, PNTreeNodeType type, PropertiesField field) {
    	super(title);
    	this.fieldType = type;
        this.txt  = field;
    }

    public PNTreeNode(HashMap<PNProperty, PropertiesField> fieldMap, PNTreeNodeType type) {
    	super("leaf");
    	this.setFieldMap(fieldMap);
    	this.fieldType = type;
	}

	public PNTreeNode(JTable table, PNTreeNodeType type) {
    	super("leaf");
    	this.tbl = table;
    	this.fieldType = type;
	}

	public JTable getTable() {
		return tbl;
	}

	public void setTable(JTable tbl) {
		this.tbl = tbl;
	}

	public JTextField getTextfield() {
		return txt;
	}
	
	public PNTreeNodeType getFieldType() {
		return fieldType;
	}

	public void setTextField(PropertiesField field) {
		this.txt = field;
		
	}

	public Map<PNProperty, PropertiesField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(HashMap<PNProperty, PropertiesField> fieldMap2) {
		this.fieldMap = fieldMap2;
	}
   
}