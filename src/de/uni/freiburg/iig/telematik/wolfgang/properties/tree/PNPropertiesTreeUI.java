package de.uni.freiburg.iig.telematik.wolfgang.properties.tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;

public class PNPropertiesTreeUI extends BasicTreeUI {

	public PNPropertiesTreeUI() {
		super();
	}

	@Override
	/**
	 * Creates an instance of NodeDimensions that is able to determine
	 * the size of a given node in the tree.
	 */
	protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
		return new PNNodeDimensionsHandler();
	}

	/**
	 * Class responsible for getting size of node, method is forwarded to
	 * BasicTreeUI method. X location does not include insets, that is handled
	 * in getPathBounds.
	 */
	// This returns locations that don't include any Insets.
	public class PNNodeDimensionsHandler extends AbstractLayoutCache.NodeDimensions {
		/**
		 * Responsible for getting the size of a particular node.
		 */
		public Rectangle getNodeDimensions(Object value, int row, int depth, boolean expanded, Rectangle size) {
			// Return size of editing component, if editing and asking
			// for editing row.
			if (editingComponent != null && editingRow == row) {
				Dimension prefSize = editingComponent.getPreferredSize();
				int rh = getRowHeight();

				if (rh > 0 && rh != prefSize.height)
					prefSize.height = rh;
				if (size != null) {
					size.x = getRowX(row, depth);
					size.width = prefSize.width;
					size.height = prefSize.height;
				} else {
					size = new Rectangle(getRowX(row, depth), 0, prefSize.width, prefSize.height);
				}
				return size;
			}
			// Not editing, use renderer.
			if (currentCellRenderer != null) {
				Component aComponent;

				aComponent = currentCellRenderer.getTreeCellRendererComponent(tree, value, tree.isRowSelected(row), expanded, treeModel.isLeaf(value), row, false);
				if (tree != null) {
					// Only ever removed when UI changes, this is OK!
					rendererPane.add(aComponent);
					aComponent.validate();
				}
				Dimension prefSize = aComponent.getPreferredSize();
				if (value instanceof PNTreeNode) {
					PNTreeNode pnTreeNode = (PNTreeNode)value;
					switch(pnTreeNode.getFieldType()){
					case ARC:
						break;
					case ARCS:
						break;
					case LEAF:
						depth = 1;
						break;
					case PLACE:
						break;
					case PLACES:
						break;
					case ROOT:
						break;
					case TRANSITION:
						break;
					case TRANSITIONS:
						break;
					default:
						break;	
					}
				}
				if (size != null) {
					size.x = getRowX(row, depth);
					size.width = prefSize.width;
					size.height = prefSize.height;
				} else {
					size = new Rectangle(getRowX(row, depth), 0, prefSize.width, prefSize.height);
				}
				return size;
			}
			return null;
		}

		/**
		 * @return amount to indent the given row.
		 */
		protected int getRowX(int row, int depth) {
			return PNPropertiesTreeUI.this.getRowX(row, depth);
		}

	} // End of class BasicTreeUI.NodeDimensionsHandler
}
