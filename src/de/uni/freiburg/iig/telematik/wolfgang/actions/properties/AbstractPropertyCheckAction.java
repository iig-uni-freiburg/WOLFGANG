package de.uni.freiburg.iig.telematik.wolfgang.actions.properties;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public abstract class AbstractPropertyCheckAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = -7823581979779506827L;
	public static Color PropertyUnknownColor = Color.GRAY;
	public static Color PropertyHolds = Color.GREEN;
	public static Color PropertyDoesntHold = Color.RED;
	private SwingWorker worker;
	private int iconSize;
	private String propertyString;

	public SwingWorker getWorker() {
		return worker;
	}

	public void setWorker(SwingWorker worker) {
		this.worker = worker;
	}

	public AbstractPropertyCheckAction(PNEditorComponent editor) throws ParameterException, PropertyException, IOException {
		super(editor, "PropertyCheck", new ImageIcon());
		iconSize = WolfgangProperties.getInstance().getIconSize().getSize();
		setInitialFill();
		createNewWorker();
	}

	protected abstract void setInitialFill();

	protected abstract void createNewWorker();

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		switch (worker.getState()) {
		case DONE:
			worker.execute();
			break;
		case PENDING:
			worker.execute();
			break;
		case STARTED:
			// Abort?
			// setFillColor(PropertyUnknownColor);
			// worker.cancel(true);
			break;
		default:
			break;
		}
	}

	static Image getLoadingDots() {
		String path = "loading-dots.gif";
		URL imageURL = IconFactory.class.getResource(path);
		ImageIcon icon = new ImageIcon(imageURL);
		return icon.getImage();
	}

	public void setIconImage(Image image) {
		getIcon().setImage(image);
		if (getEditor().getEditorToolbar() != null)
			getEditor().getEditorToolbar().updateUI();
	}

	public String getPropertyString() {
		return propertyString;
	}

	public void setPropertyString(String propertyString) {
		this.propertyString = propertyString;
	}

	public void setFillColor(Color fillColor) {

		BufferedImage image = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.setPaint(Color.BLACK);
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 8));

		String s = getPropertyString();
		FontMetrics fm = g2d.getFontMetrics();
		int y = -fm.getHeight() + 4;

		g2d.setColor(Color.BLACK);
		y = drawString(g2d, s, 1, y);
		y += 2;
		g2d.setColor(fillColor);
		g2d.fillRect(0, y, image.getHeight() - 1, image.getWidth() - 1);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawRect(0, y, image.getHeight() - 1, image.getWidth() - 1);
		g2d.drawRect(0, 0, image.getHeight() - 1, image.getWidth() - 1);
		g2d.dispose();
		setIconImage(img);
	}

	private int drawString(Graphics2D g, String text, int x, int y) {
		for (String line : text.split("\n"))
			y = drawCenteredString(line, iconSize, y += g.getFontMetrics().getHeight() + 8, g);
		return y;
	}

	public int drawCenteredString(String s, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
		return y;
	}

}
