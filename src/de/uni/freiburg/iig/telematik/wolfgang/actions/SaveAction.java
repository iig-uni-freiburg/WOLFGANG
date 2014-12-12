package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.wolfgang.editor.Wolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class SaveAction extends AbstractWolfgangAction {

	private static final long serialVersionUID = 7716993627349722001L;

	protected boolean success = false;
	protected String errorMessage = null;

	
	public SaveAction(Wolfgang wolfgang) throws PropertyException, IOException {
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
		if(wolfgang.getEditorComponent() == null)
			return;
		success = true;
		PNSerialization.serialize(wolfgang.getEditorComponent().getNetContainer(), PNSerializationFormat.PNML, wolfgang.getFileReference().getAbsolutePath());
		wolfgang.getEditorComponent().setModified(false);
	}
}
