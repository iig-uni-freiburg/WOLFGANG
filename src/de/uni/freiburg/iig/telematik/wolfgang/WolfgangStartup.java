package de.uni.freiburg.iig.telematik.wolfgang;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import com.apple.eawt.AppEvent.OpenFilesEvent;

import de.invation.code.toval.graphic.misc.AbstractStartup;
import de.invation.code.toval.os.OSType;
import de.invation.code.toval.os.OSUtils;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.editor.NetTypeChooserDialog;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;

public class WolfgangStartup extends AbstractStartup {

	private static final String TOOL_NAME = "Wolfgang";
	private static String[] filePaths;
	private static WolfgangStartup wg;

	@Override
	protected String getToolName() {
		return TOOL_NAME;
	}

	@Override
	protected void startApplication() throws Exception {
		if (filePaths == null) {
			NetTypeChooserDialog dialog = new NetTypeChooserDialog(null);
			NetType chosenNetType = dialog.getChosenNetType();
			if (chosenNetType == null) {
				if (dialog.openNetOption()) {
					tryToOpenNet();
				}
			} else {
				setLookAndFeel();
				switch (chosenNetType) {
				case CPN:
					new WolfgangCPN().setUpGUI();
					break;
				case PTNet:
					new WolfgangPT().setUpGUI();
					break;
				default:
				}
			}
		} else {
			tryToOpenNet();
		}
	}

	private void tryToOpenNet() throws Exception {
		if (filePaths == null) {
			setLookAndFeel();
			JFileChooser fc;
			fc = new JFileChooser(OSUtils.getUserHomeDirectory());
			fc.removeChoosableFileFilter(fc.getFileFilter());
			fc.addChoosableFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "PNML Documents (*.pnml)";
				}

				@Override
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
				System.out.println(filename);
				openPNMLFile(filename);
			} else {
				startApplication();
			}
		} else {
			for (String path : filePaths)
				openPNMLFile(path);
		}

		// filePath = null;
	}

	private void openPNMLFile(String filename) throws Exception {
		if (!filename.toLowerCase().endsWith(".pnml")) {
			JOptionPane.showMessageDialog(null, "File is not in .pnml format", "Open Error", JOptionPane.ERROR_MESSAGE);
			startApplication();
		} else {
			@SuppressWarnings("rawtypes")
			AbstractGraphicalPN net = new PNMLParser().parse(filename, EditorProperties.getInstance().getRequestNetType(), EditorProperties.getInstance().getPNValidation());
			switch (net.getPetriNet().getNetType()) {
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

	private void setLookAndFeel() {
		if (OSUtils.getCurrentOS() == OSType.OS_LINUX) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			}
		} else if (OSUtils.getCurrentOS() == OSType.OS_WINDOWS) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			}
		}
	}

	public static void main(String[] args) {

		com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
		app.setOpenFileHandler(new com.apple.eawt.OpenFilesHandler() {

			@Override
			public void openFiles(OpenFilesEvent ofe) {
					if (args.length > 0) {
						filePaths = args;
					}
					String[] localArgs = new String[ofe.getFiles().size()];
					for (int i = 0; ofe.getFiles().size() > i; i++) {
						localArgs[i] = ofe.getFiles().get(i).getAbsolutePath();
					}

					filePaths = localArgs;
					if (wg != null) {
						try {
							wg.tryToOpenNet();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Failed to open file");
						}
					}
				}

		});

		if (args.length > 0) {
			filePaths = args;
		}
		wg = new WolfgangStartup();
	}

}
