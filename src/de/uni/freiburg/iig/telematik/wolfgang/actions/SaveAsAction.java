package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class SaveAsAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	private JFileChooser fch = null;
	protected boolean success = false;
	protected String errorMessage = null;

	@SuppressWarnings("rawtypes")
	public SaveAsAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
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
		setUpGui();
		int returnVal = fch.showDialog(wolfgang.getEditorComponent().getGraphComponent(), "save PNML");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fch.getSelectedFile().getAbsolutePath();
			if (!filename.toLowerCase().endsWith(".pnml"))
				filename += ".pnml";
			String netName = fch.getName(fch.getSelectedFile());
			if (netName != null)
				wolfgang.getEditorComponent().getNetContainer().getPetriNet().setName(netName);
			PNSerialization.serialize(wolfgang.getEditorComponent().getNetContainer(), PNSerializationFormat.PNML, filename);
			wolfgang.setFileReference(fch.getSelectedFile());
			wolfgang.setTitle(wolfgang.getTitle());
			wolfgang.getEditorComponent().setModified(false);
		}
	}

	private void setUpGui() {
		fch = new JFileChooser(System.getProperty("user.home"));

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
		fch.setDialogTitle("Save PNML");
		
	}

}
