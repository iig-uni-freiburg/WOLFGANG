package de.uni.freiburg.iig.telematik.wolfgang;

import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.NetTypeChooserDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;

public class Startup {
	
	private static final String TOOL_NAME = "Wolfgang";
	
	public static void main(String[] args) {
		String osType = System.getProperty("os.name");
		if(osType.equals("Mac OS") || osType.equals("Mac OS X")){
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", TOOL_NAME);
			System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", TOOL_NAME);
		}
		
		try {
			NetType chosenNetType = NetTypeChooserDialog.showDialog();
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
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "Cannot launch Wolfgang.\nReason: " + e.getMessage(), "Internal Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
