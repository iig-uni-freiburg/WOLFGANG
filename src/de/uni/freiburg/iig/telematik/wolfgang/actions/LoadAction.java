package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.wolfgang.editor.Wolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class LoadAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	public LoadAction(Wolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "Net PT Net", IconFactory.getIcon("save"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (wolfgang.getEditorComponent() == null)
			return;
		success = true;
		JFileChooser fc;

		fc = new JFileChooser(System.getProperty("user.home"));

		fc.addChoosableFileFilter(new FileFilter() {
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
		fc.setDialogTitle("Load PNML");
		int returnVal = fc.showDialog(wolfgang.getEditorComponent().getGraphComponent(), "load PNML");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			if (!filename.toLowerCase().endsWith(".pnml"))
				filename += ".pnml";
			AbstractGraphicalPN net = new PNMLParser().parse(filename, WolfgangProperties.getInstance().getRequestNetType(), WolfgangProperties.getInstance().getPNValidation());

			wolfgang = new Wolfgang(net);
			wolfgang.setNetName(net.getPetriNet().getName());
			wolfgang.setUpGUI();
		}

	}
}
