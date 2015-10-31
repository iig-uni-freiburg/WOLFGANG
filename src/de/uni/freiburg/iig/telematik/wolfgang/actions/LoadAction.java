package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class LoadAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;
	
	private JFileChooser fch = null;

	@SuppressWarnings("rawtypes")
	public LoadAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "Open Net", IconFactory.getIcon("save"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (wolfgang.getEditorComponent() == null)
			return;
		success = true;
		setUpGui();

		int returnVal = fch.showDialog(wolfgang.getEditorComponent().getGraphComponent(), "load PNML");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fch.getSelectedFile().getAbsolutePath();

			if (filename.toLowerCase().endsWith(".pnml")) {
				AbstractGraphicalPN net = new PNMLParser().parse(filename, EditorProperties.getInstance().getRequestNetType(), EditorProperties.getInstance().getPNValidation());
				switch (net.getPetriNet().getNetType()) {
				case CPN:
					wolfgang = new WolfgangCPN((GraphicalCPN) net);
					break;
				case PTNet:
					wolfgang = new WolfgangPT((GraphicalPTNet) net);
					break;
				default:
					throw new Exception("Incompatible net type: " + net.getPetriNet().getNetType());
				}
				wolfgang.setUpGUI();
			} else
				throw new Exception("File is not in .pnml format");
		}

	}

	private void setUpGui() {
		fch = new JFileChooser(System.getProperty("user.home"));
		fch.removeChoosableFileFilter(fch.getFileFilter());
		fch.addChoosableFileFilter(new FileFilter() {
			public String getDescription() {
				return "PNML Documents (*.pnml)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(".pnml");
				}
			}
		});
		fch.setDialogTitle("Load PNML");
		
	}
}
