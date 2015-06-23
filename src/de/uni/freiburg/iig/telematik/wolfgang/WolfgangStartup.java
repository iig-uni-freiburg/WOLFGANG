package de.uni.freiburg.iig.telematik.wolfgang;

import java.io.File;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import de.invation.code.toval.graphic.misc.AbstractStartup;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.NetTypeChooserDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WolfgangProperties;

public class WolfgangStartup extends AbstractStartup {

	private static final String TOOL_NAME = "Wolfgang";

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	@Override
	protected void startApplication() throws Exception {
		NetTypeChooserDialog dialog = new NetTypeChooserDialog(null);
		NetType chosenNetType = dialog.getChosenNetType();
		if(chosenNetType == null){
			if(dialog.openNetOption()){
				tryToOpenNet();
			} else {
				return;
			}
		} else {
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
	}

	private void tryToOpenNet() throws Exception {
		setLookAndFeel();
		JFileChooser fc;
		fc = new JFileChooser(System.getProperty("user.home"));
		fc.removeChoosableFileFilter(fc.getFileFilter());
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
		int returnVal = fc.showDialog(null, "load PNML");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			if (!filename.toLowerCase().endsWith(".pnml")) {
				JOptionPane.showMessageDialog(null, "File is not in .pnml format", "Open Error", JOptionPane.ERROR_MESSAGE);
				startApplication();
			}		
			else {
				@SuppressWarnings("rawtypes")
				AbstractGraphicalPN net = new PNMLParser().parse(filename, WolfgangProperties.getInstance().getRequestNetType(), WolfgangProperties.getInstance().getPNValidation());
				switch(net.getPetriNet().getNetType()){
				case CPN:
					new WolfgangCPN((GraphicalCPN) net).setUpGUI();
					break;
				case PTNet:
					new WolfgangPT((GraphicalPTNet) net).setUpGUI();
					break;
				default: 
					throw new Exception("Incompatible net type: " + net.getPetriNet().getNetType());
				}
			}
		}
		else
			startApplication();
	}
	
	private void setLookAndFeel() {
		if (System.getProperty("os.name").toLowerCase().contains("nux")) {
			try {
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

	public static void main(String[] args) {
		if(args.length > 1)
			System.out.println(args[0]);
		new WolfgangStartup();
	}
}
