package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxRectangle;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PNProperties.PNComponent;

public class NodePalettePanel extends JPanel {

	private static final long serialVersionUID = -1156941541375286369L;

	private final int PREFERRED_ICON_SIZE = 30;
	private final Dimension PREFERRED_PALETTE_COMPONENT_SIZE = new Dimension(70, 60);

	private JPanel transitionPanel = new JPanel() {

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color defaultColor;
			try {
				defaultColor = WolfgangProperties.getInstance().getDefaultTransitionColor();
				g2.setColor(defaultColor);
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			double w = PREFERRED_PALETTE_COMPONENT_SIZE.getWidth();
			double h = PREFERRED_PALETTE_COMPONENT_SIZE.getHeight();
			int x = ((int) w) - PREFERRED_ICON_SIZE;
			int y = ((int) h) - PREFERRED_ICON_SIZE;
			g2.fillRect(x / 2, y / 2 - 10, PREFERRED_ICON_SIZE, PREFERRED_ICON_SIZE);
		}
	};

	private JPanel placePanel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color defaultColor;
			try {
				defaultColor = WolfgangProperties.getInstance().getDefaultPlaceColor();
				g2.setColor(defaultColor);
			} catch (PropertyException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			double w = PREFERRED_PALETTE_COMPONENT_SIZE.getWidth();
			double h = PREFERRED_PALETTE_COMPONENT_SIZE.getHeight();
			int x = ((int) w) - PREFERRED_ICON_SIZE;
			int y = ((int) h) - PREFERRED_ICON_SIZE;
			g2.fillOval(x / 2, y / 2 - 10, PREFERRED_ICON_SIZE, PREFERRED_ICON_SIZE);
		}
	};

	protected JPanel selectedEntry = null;

	private JPanel panel;

	public NodePalettePanel() throws PropertyException, IOException {
		setLayout(new BorderLayout());
		panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		addHowToDescription();
		addPlaceTemplate();
		addTransitionTemplate();

		add(panel, BorderLayout.PAGE_START);

	}

	/**
	 * Drag new place to editor canvas.
	 * 
	 * @throws PropertyException
	 * @throws IOException
	 */
	public void addPlaceTemplate() throws PropertyException, IOException {
		int size = WolfgangProperties.getInstance().getDefaultPlaceSize();
		String style = MXConstants.getDefaultNodeStyle(PNComponent.PLACE);
		PNGraphCell cell = new PNGraphCell(null, new mxGeometry(0, 0, size, size), style, PNComponent.PLACE);
		cell.setVertex(true);
		addTemplate("Place", placePanel, cell);
	}

	/**
	 * Drag new transition to editor canvas.
	 * 
	 * @throws PropertyException
	 * @throws IOException
	 */
	public void addTransitionTemplate() throws PropertyException, IOException {
		int width = WolfgangProperties.getInstance().getDefaultTransitionWidth();
		int height = WolfgangProperties.getInstance().getDefaultTransitionHeight();
		String style = MXConstants.getDefaultNodeStyle(PNComponent.TRANSITION);
		PNGraphCell cell = new PNGraphCell(null, new mxGeometry(0, 0, width, height), style, PNComponent.TRANSITION);
		cell.setVertex(true);
		addTemplate("Transition", transitionPanel, cell);
	}

	private void addHowToDescription() {
		JLabel howtoLabel = new JLabel("Drag 'n Drop Nodes to Editor  ");
		JLabel howtoLabelArc = new JLabel("OR Arcs from Node Centers.");

		Font fontStyle = new Font(Font.DIALOG, Font.ITALIC, 10);
		howtoLabel.setFont(fontStyle);
		howtoLabelArc.setFont(fontStyle);
		add(howtoLabel, BorderLayout.CENTER);
		add(howtoLabelArc, BorderLayout.PAGE_END);

	}

	public void addTemplate(final String name, JPanel nodepanel, mxCell cell) {
		mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
		final mxGraphTransferable t = new mxGraphTransferable(new Object[] { cell }, bounds);

		JPanel paletteComponent = new JPanel(new BorderLayout());
		paletteComponent.setPreferredSize(PREFERRED_PALETTE_COMPONENT_SIZE);
		paletteComponent.setMaximumSize(PREFERRED_PALETTE_COMPONENT_SIZE);
		paletteComponent.setMinimumSize(PREFERRED_PALETTE_COMPONENT_SIZE);

		final JPanel iconPanel = new JPanel(new BorderLayout());
		iconPanel.setPreferredSize(new Dimension(PREFERRED_ICON_SIZE, PREFERRED_ICON_SIZE));
		iconPanel.setMaximumSize(new Dimension(PREFERRED_ICON_SIZE, PREFERRED_ICON_SIZE));
		iconPanel.setMinimumSize(new Dimension(PREFERRED_ICON_SIZE, PREFERRED_ICON_SIZE));
		nodepanel.setBackground(iconPanel.getBackground());

		paletteComponent.add(new JLabel(name, JLabel.CENTER), BorderLayout.PAGE_START);
		paletteComponent.add(nodepanel, BorderLayout.CENTER);

		DragGestureListener dragGestureListener = new DragGestureListener() {
			public void dragGestureRecognized(DragGestureEvent e) {
				if (!iconPanel.isEnabled()) {
					return;
				}
				e.startDrag(null, MXConstants.EMPTY_IMAGE, new Point(), t, null);
			}
		};

		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(nodepanel, DnDConstants.ACTION_COPY, dragGestureListener);

		panel.add(paletteComponent);
	}

}
