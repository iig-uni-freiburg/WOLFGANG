package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.property;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.invation.code.toval.graphic.component.ExecutorLabel;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class PNPropertyCheckLabel<Z> extends ExecutorLabel<Z> {

	private static final long serialVersionUID = -2807606775313824173L;
	private static final Dimension DEFAULT_SIZE = new Dimension(40,40);


	public static Color COLOR_PROPERTY_UNKNOWN = Color.GRAY;
	public static Color COLOR_PROPERTY_TRUE = Color.GREEN;
	public static Color COLOR_PROPERTY_FALSE = Color.RED;

	private String propertyString = "";

	protected PNEditorComponent editorComponent;
	
	
	protected boolean propertyHolds = false;

	public PNPropertyCheckLabel(PNEditorComponent editorComponent) {
		super();
		setPreferredSize(DEFAULT_SIZE);
		setMinimumSize(DEFAULT_SIZE);
		setMaximumSize(DEFAULT_SIZE);
		setOpaque(true);
		this.editorComponent = editorComponent;
	}

	public PNPropertyCheckLabel(PNEditorComponent editorComponent, String propertyName) {
		this(editorComponent);
		this.propertyString = propertyName;
	}

	@Override
	protected void startExecutor() throws Exception {
		setExecutor(createNewExecutor());
		super.startExecutor();
	}

	protected abstract AbstractThreadedPNPropertyChecker<?,?,?,?,?,Z> createNewExecutor();

	public String getPropertyString() {
		return propertyString;
	}

	public void setPropertyString(String propertyString) {
		this.propertyString = propertyString;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawPropertyString(g);
	}

	private void drawPropertyString(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = DEFAULT_SIZE.width;
		int h = DEFAULT_SIZE.height;
		g2.setPaint(Color.BLACK);
		g2.setFont(new Font("Arial", Font.PLAIN, 9));
		String s = getPropertyString();
		FontMetrics fm = g2.getFontMetrics();
		int y = -fm.getHeight() + 4;
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, w - 1, h / 2);
		g2.setPaint(Color.BLACK);
		y = drawString(g2, s, w / 2, y);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(1));
		g2.drawRect(0, 0, h - 1, w - 1);
		g2.dispose();
	}

	private int drawString(Graphics2D g, String text, int x, int y) {
		for (String line : text.split("\n"))
			y = drawCenteredString(line, DEFAULT_SIZE.width, y += g.getFontMetrics().getHeight() + 8, g);
		return y;
	}

	public int drawCenteredString(String s, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
		return y;
	}
	
	@Override
	protected Color getColorInitial() {
		return COLOR_PROPERTY_UNKNOWN;
	}

	@Override
	protected Color getColorCancelled() {
		return COLOR_PROPERTY_UNKNOWN;
	}

	@Override
	protected Color getColorDone() {
		if(propertyHolds){
			return COLOR_PROPERTY_TRUE;
		} else {
			return COLOR_PROPERTY_FALSE;
		}
	}

	@Override
	public void executorFinished(Z result) {
		setPropertyHolds(result);
		super.executorFinished(result);
	}

	protected abstract void setPropertyHolds(Z calculationResult);

}
