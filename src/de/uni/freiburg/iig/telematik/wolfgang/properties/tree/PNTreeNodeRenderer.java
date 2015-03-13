package de.uni.freiburg.iig.telematik.wolfgang.properties.tree;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory.IconSize;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PNProperties.PNComponent;

public class PNTreeNodeRenderer extends DefaultTreeCellRenderer {

	public PNTreeNodeRenderer() throws PropertyException, IOException {
		super();
		this.placeImage = createNodeImage(PNComponent.PLACE);
		this.transitionImage = createNodeImage(PNComponent.TRANSITION);
		this.arcIcon = createNodeImage(PNComponent.ARC);
	}

	private static final long serialVersionUID = -7829208008630231526L;
	private static String nodeColor = "#333333";
	static int size = IconSize.MEDIUM.getSize();
	private static Color defaultFillColor = Utils.parseColor(nodeColor);
	private Image placeImage;
	private Image transitionImage;
	private Image arcIcon;

	public static Image createNodeImage(PNComponent type) throws PropertyException, IOException {
		Image image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(defaultFillColor);

		switch (type) {
		case ARC:
			createArcGraphics2D(g2);
			break;
		case PLACE:
			drawPlaceGraphics2D(g2);
			break;
		case TRANSITION:
			createTransistionG2D(g2);
			break;
		}
		g2.dispose();
		return image;
	}

	private static void drawPlaceGraphics2D(Graphics2D g2) {
		int placeSize = (size - 1) / 2;
		g2.setColor(defaultFillColor);
		g2.fillOval(0, placeSize / 3, placeSize + placeSize / 2, placeSize + placeSize / 2);
	}

	private static void createTransistionG2D(Graphics2D g2) {
		int transitionSize = (size - 1) / 2;
		g2.fillRect(0, (transitionSize / 3), transitionSize + (transitionSize / 2), transitionSize + (transitionSize / 2));
	}

	private static void createArcGraphics2D(Graphics2D g2) {
		int transitionSize = (size - 1) / 2;
		g2.setStroke(new BasicStroke(3));
		int rightX = transitionSize + (transitionSize / 3);
		int rightY = transitionSize / 2 + 3;
		g2.drawLine(0, transitionSize * 2 - 5, rightX - 2, rightY + 2);
		g2.setStroke(new BasicStroke(2));
		int xPoints[] = { rightX - 11, rightX - 5, rightX };
		int yPoints[] = { rightY, rightY + 12, rightY };
		int npoints = 3;
		g2.fillPolygon(xPoints, yPoints, npoints);
	}

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		final PNTreeNode node = (PNTreeNode) value;
		Component container = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		container.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				tree.stopEditing();
			}

		});

		Component result = this;
		setOpaque(true);

		switch (node.getFieldType()) {

		case ARCS:
			setBackground(null);
			setIcon(new ImageIcon(arcIcon.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			setText(getText() + " (" + node.getChildCount() + ")");
			break;
		case PLACES:
			setBackground(null);
			setIcon(new ImageIcon(placeImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			setText(getText() + " (" + node.getChildCount() + ")");
			break;
		case TRANSITIONS:
			setBackground(null);
			setIcon(new ImageIcon(transitionImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			setText(getText() + " (" + node.getChildCount() + ")");
			break;
		default:
			break;
		case ROOT:
			break;
		case PLACE:
			setIcon(new ImageIcon(placeImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case TRANSITION:
			setIcon(new ImageIcon(transitionImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case ARC:
			setIcon(new ImageIcon(arcIcon.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
			break;
		case LEAF:
			JPanel tablePanel = new JPanel();
			tablePanel.setLayout(new BorderLayout());
			tablePanel.add(node.getTable(), BorderLayout.NORTH);
			tablePanel.add(new JPopupMenu.Separator(), BorderLayout.SOUTH);
			result = tablePanel;
			break;

		}

		return result;
	}

	// �berdenken wann genau Node highlighted sein soll
	/**
	 * @param tree
	 * @param node
	 */
	public void keepSelectionWhileEditing(JTree tree, PNTreeNode node) {
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (child != null && child.isLeaf() && child.getParent() == node) {
			selected = true;
		}
//		if (child != null && child.isLeaf() && child.getParent() != node) {
//			selected = false;
//		}
//		if (child != null && !child.isLeaf() && child == node) {
//			selected = true;
//		}
//		if (child != null && !child.isLeaf() && child != node) {
//			selected = false;
//		}
	}

}