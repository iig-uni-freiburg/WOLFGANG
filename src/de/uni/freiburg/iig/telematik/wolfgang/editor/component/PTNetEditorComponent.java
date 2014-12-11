package de.uni.freiburg.iig.telematik.wolfgang.editor.component;

import java.io.IOException;

import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PTGraphComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.PTNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.EditorPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.menu.popup.TransitionPopupMenu;
import de.uni.freiburg.iig.telematik.wolfgang.properties.PTProperties;

public class PTNetEditorComponent extends PNEditorComponent {

	// public final static String PNML =
	// PNEditor.class.getResource("/samples/samplePTnet.pnml").getPath();
	// public final static String LABELING =
	// PNEditor.class.getResource("/samples/sampleIFnetLabeling01.xml").getPath();

	private static final long serialVersionUID = -5130690639223735136L;

	public PTNetEditorComponent() {
		super();
	}

	public PTNetEditorComponent(GraphicalPTNet netContainer) {
		super(netContainer);
	}

	@Override
	protected GraphicalPTNet createNetContainer() {
		return new GraphicalPTNet(new PTNet(), new PTGraphics());
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties createPNProperties() {
		return new PTProperties(getNetContainer());
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}

	@Override
	protected PNGraphComponent createGraphComponent() {
		return new PTGraphComponent(new PTGraph(getNetContainer(), getPNProperties()));

	}

	@Override
	public EditorPopupMenu getPopupMenu() {

		try {
			return new EditorPopupMenu(this);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

//	private static void testEmptyNet(JFrame frame) throws IOException, ParserException, ParameterException {
//		JPanel panel = createFrameEpmtyNet(frame);
//		PropertiesView pV = ((PNEditor) panel).getPropertiesView();
//		frame.setLayout(new BorderLayout());
//		frame.getContentPane().add(panel, BorderLayout.CENTER);
//		frame.getContentPane().add(pV, BorderLayout.LINE_END);
//
//	}

//	public static JPanel createFrameEpmtyNet(JFrame frame) throws IOException, ParserException, ParameterException {
//		String userHome = System.getProperty("user.home");
//		File file = new File(userHome + "test");
//		JPanel panel = new PTNetEditor(file);
//		frame.setTitle("PTNet Editor");
//		frame.setSize(800, 500);
//		panel.setBackground(Color.black);
//		return panel;
//	}

	@Override
	public TransitionPopupMenu getTransitionPopupMenu() {
		try {
			return new TransitionPopupMenu(this);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new PTNetToolBar(this, JToolBar.HORIZONTAL);
	}

}
