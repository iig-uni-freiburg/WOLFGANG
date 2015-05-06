package de.uni.freiburg.iig.telematik.wolfgang;

import de.invation.code.toval.graphic.misc.AbstractStartup;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.NetTypeChooserDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;

public class WolfgangStartup extends AbstractStartup {

	private static final String TOOL_NAME = "Wolfgang";

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	@Override
	protected void startApplication() throws Exception {
		NetType chosenNetType = NetTypeChooserDialog.showDialog();
		if(chosenNetType == null)
			return;
		switch (chosenNetType) {
		case CPN:
			new WolfgangCPN().setUpGUI();
			break;
		case PTNet:
			new WolfgangPT().setUpGUI();
			break;
		default:
			return;
		}
	}

	public static void main(String[] args) {
		new WolfgangStartup();
	}
}
