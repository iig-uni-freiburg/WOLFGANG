package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.actions.export.ExportPDFAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.export.ExportPNGAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class ExportToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	// Actions

	private ExportPDFAction pdfAction;

	// Buttons
	private JButton pdfButton;

	// Tooltips
	private String pdfButtonTooltip = "Export to PDF";

	private ExportPNGAction pngAction;

	private JButton pngButton;

	private String pngButtonTooltip = "Export to PNG";

	public ExportToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);

		pdfAction = new ExportPDFAction(pnEditor);
		pngAction = new ExportPNGAction(pnEditor);

		setFloatable(false);

		pdfButton = add(pdfAction);
		setButtonSettings(pdfButton);
		pdfButton.setToolTipText(pdfButtonTooltip);

		pngButton = add(pngAction);
		setButtonSettings(pngButton);
		pngButton.setToolTipText(pngButtonTooltip);

	}

	private void setButtonSettings(final JButton button) {
		button.setBorderPainted(false);
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBorderPainted(false);
				super.mouseReleased(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				button.setBorderPainted(true);
				super.mousePressed(e);
			}

		});
	}

}
