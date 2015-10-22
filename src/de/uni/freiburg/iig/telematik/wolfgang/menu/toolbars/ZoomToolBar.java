package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.wolfgang.actions.zoom.ZoomInAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.zoom.ZoomOutAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class ZoomToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private boolean ignoreZoomChange = false;

	// Actions

	private ZoomInAction zoomInAction;
	
	// Buttons
	private JButton btnZoomIn;

	// Tooltips
	private String zoomInTooltip = "zoom in";

	private String zoomOutTooltip = "zoom out";
	


	// further variables

	private ZoomOutAction zoomOutAction;

	private JButton btnZoomOut;


	public ZoomToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);


			zoomInAction = new ZoomInAction(pnEditor);	
			zoomOutAction = new ZoomOutAction(pnEditor);

		setFloatable(false);

		btnZoomIn = add(zoomInAction);
		setButtonSettings(btnZoomIn);	
		btnZoomOut = add(zoomOutAction);
		setButtonSettings(btnZoomOut);
	
		btnZoomIn.setToolTipText(zoomInTooltip);
		btnZoomOut.setToolTipText(zoomOutTooltip);
		
		
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
