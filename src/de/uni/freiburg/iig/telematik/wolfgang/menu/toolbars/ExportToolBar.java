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
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class ExportToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	// Actions

	private ExportPDFAction pdfAction;

	// Buttons
	private JButton btnPdf;

	// Tooltips
	private String pdfButtonTooltip = "Export to PDF";

	public ExportToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);

		pdfAction = new ExportPDFAction(pnEditor);
		setFloatable(false);
		btnPdf = add(pdfAction);
		setButtonSettings(btnPdf);
		btnPdf.setToolTipText(pdfButtonTooltip);
	}

	private void setButtonSettings(final JButton btn) {
		btn.setBorderPainted(false);
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				btn.setBorderPainted(false);
				super.mouseReleased(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				btn.setBorderPainted(true);
				super.mousePressed(e);
			}

		});
	}

}
