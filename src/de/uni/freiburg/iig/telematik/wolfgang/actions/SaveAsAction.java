package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.wolfgang.editor.Wolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class SaveAsAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	public SaveAsAction(Wolfgang wolfgang) throws PropertyException, IOException {
		super(wolfgang, "Save", IconFactory.getIcon("save"));
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
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
		fc.setDialogTitle("Save PNML");
		int returnVal = fc.showDialog(wolfgang.getEditorComponent().getGraphComponent(), "save PNML");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			if (!filename.toLowerCase().endsWith(".pnml"))
				filename += ".pnml";
			String netName = fc.getName(fc.getSelectedFile());
			if (netName != null)
				wolfgang.getEditorComponent().getNetContainer().getPetriNet().setName(netName);
			PNSerialization.serialize(wolfgang.getEditorComponent().getNetContainer(), PNSerializationFormat.PNML, filename);
			wolfgang.getEditorComponent().setModified(false);
		}
	}

}
