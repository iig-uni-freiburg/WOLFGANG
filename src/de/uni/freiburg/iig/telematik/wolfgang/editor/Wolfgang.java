package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class Wolfgang extends JFrame {

	private static final long serialVersionUID = 6384805641357595809L;

	public static final Dimension PREFERRED_SIZE_WORKBENCH = new Dimension(1024, 768);
	private static final Dimension PREFERRED_SIZE_PROPERTIES_PANEL = new Dimension(200, 768);
	private static final Dimension MINIMUM_SIZE_EDITOR_PANEL = new Dimension(824, 768);

	private File fileReference = null;

	protected PNEditorComponent editorComponent = null;
	private WGMenuBar menuBar = null;
	protected JPanel content = null;
	protected JSplitPane centerPanel = null;

	private String netName;

	public Wolfgang() throws Exception {
		setUpGUI();
	}

	public Wolfgang(AbstractGraphicalPN net) throws Exception {
		this(net, null);
	}

	public Wolfgang(AbstractGraphicalPN net, LayoutOption layoutOption) throws Exception {
		setNet(net, layoutOption);
	}

	public Wolfgang(AbstractGraphicalPN net, boolean askForLayout) throws Exception {
		setNet(net, askForLayout);
	}

	public void setNet(AbstractGraphicalPN net, LayoutOption layoutOption) {
		prepareNetInsertion(net);
		if (net.getPetriNet().getNetType().equals(NetType.PTNet)) {
			this.editorComponent = new PTNetEditorComponent((GraphicalPTNet) net, layoutOption);
		} else {
			this.editorComponent = new CPNEditorComponent((GraphicalCPN) net, layoutOption);
		}
	}

	public void setNet(AbstractGraphicalPN net, boolean askForLayout) {
		prepareNetInsertion(net);
		if (net.getPetriNet().getNetType().equals(NetType.PTNet)) {
			this.editorComponent = new PTNetEditorComponent((GraphicalPTNet) net, askForLayout);
		} else {
			this.editorComponent = new CPNEditorComponent((GraphicalCPN) net, askForLayout);
		}
	}

	private void prepareNetInsertion(AbstractGraphicalPN net) {
		Validate.notNull(net);
		NetType netType = net.getPetriNet().getNetType();
		if (!netType.equals(NetType.PTNet) && !netType.equals(NetType.CPN))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unsupported net type: " + net.getPetriNet().getNetType());

		if (editorComponent != null) {
			centerPanel.remove(editorComponent);
			centerPanel.remove(editorComponent.getPropertiesView());
		}
	}

	public PNEditorComponent getEditorComponent() {
		return editorComponent;
	}

	public File getFileReference() {
		return fileReference;
	}

	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}

	public void setUpGUI() throws PropertyException, IOException, Exception {
		setLookAndFeel();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE_WORKBENCH);
		setResizable(true);
		if (editorComponent != null)
			setContentPane(getContent());
		else
			setChooseNetPanel();
		setJMenuBar(getWGMenuBar());
		pack();
		setVisible(true);
	}

	@Override
	public String getTitle() {
		if (this.getEditorComponent() != null)
			return "WOLFGANG" + " - " + this.getEditorComponent().getNetContainer().getPetriNet().getNetType() + getNetName();
		return "WOLFGANG";
	}

	private String getNetName() {
		if (netName == null)
			return "";
		else
			return " - " + netName + ".pnml";
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	/** Changes Look and Feel if running on Linux **/
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
				setLocationByPlatform(true);
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {
			}
		} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}
		}
	}

	private JComponent getContent() throws Exception {
		if (content == null) {
			content = new JPanel(new BorderLayout());
			content.add(getCenterComponent(), BorderLayout.CENTER);
			setEditorPanels();
			JComponent bottomComponent = getBottomComponent();
			if (bottomComponent != null)
				content.add(bottomComponent, BorderLayout.PAGE_END);
		}
		return content;
	}

	private void setChooseNetPanel() {
		final JButton ptButton = getPTButton();
		final JButton cpnButton = getCPNButton();
		ptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setNet(new GraphicalPTNet(), false);
				packWolfgang();

			}
		});
		cpnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setNet(new GraphicalCPN(), false);
				packWolfgang();
			}

		});
		JPanel chooseNetPanel = new JPanel();
		chooseNetPanel.add(ptButton);
		chooseNetPanel.add(cpnButton);
		add(chooseNetPanel);

	}

	private static JButton getPTButton() {
		String path = "choosePT.png";
		URL imageURL = IconFactory.class.getResource(path);
		ImageIcon icon = new ImageIcon(imageURL);
		JButton buttonPT = new JButton(icon);
		buttonPT.setFocusPainted(false);
		return buttonPT;
	}

	private static JButton getCPNButton() {
		String path = "chooseCPN.png";
		URL imageURL = IconFactory.class.getResource(path);
		ImageIcon icon = new ImageIcon(imageURL);
		JButton buttonCPN = new JButton(icon);
		buttonCPN.setFocusPainted(false);
		return buttonCPN;
	}

	protected void packWolfgang() {
		try {
			setUpGUI();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pack();

	}

	private void removeButtonComponents(final JButton ptButton, final JButton cpnButton) {
		centerPanel.remove(ptButton);
		centerPanel.remove(cpnButton);
	}

	protected JComponent getCenterComponent() throws Exception {
		if (centerPanel == null) {
			centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			centerPanel.setDividerLocation(0.8);
		}
		return centerPanel;
	}

	protected JComponent getBottomComponent() throws Exception {
		return null;
	}

	protected void setEditorPanels() throws Exception {
		content.add(editorComponent.getEditorToolbar(), BorderLayout.NORTH);
		centerPanel.add(getEditorPanel());
		centerPanel.add(editorComponent.getPropertiesView());
	}

	protected JComponent getEditorPanel() {
		JScrollPane scrollPane = new JScrollPane(editorComponent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(MINIMUM_SIZE_EDITOR_PANEL);
		return scrollPane;
	}

	public WGMenuBar getWGMenuBar() throws PropertyException, IOException {
		if (menuBar == null) {
			menuBar = new WGMenuBar(this);
		}
		return menuBar;
	}

	public static void main(String[] args) throws Exception {
		// Wolfgang wolfgang = new Wolfgang(new GraphicalPTNet());
		// wolfgang.setUpGUI();
		// Wolfgang wolfgang = new Wolfgang(new GraphicalCPN());
		// wolfgang.setUpGUI();
		Wolfgang wolfgang = new Wolfgang();
	}

}
