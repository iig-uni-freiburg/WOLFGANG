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
	private JButton zoomInButton;

	// Tooltips
	private String zoomInTooltip = "zoom in";

	private String zoomOutTooltip = "zoom out";
	


	// further variables

	private ZoomOutAction zoomOutAction;

	private JButton zoomOutButton;


	public ZoomToolBar(final PNEditorComponent pnEditor, int orientation) throws ParameterException, PropertyException, IOException {
		super(orientation);
		Validate.notNull(pnEditor);


			zoomInAction = new ZoomInAction(pnEditor);	
			zoomOutAction = new ZoomOutAction(pnEditor);

		setFloatable(false);

		zoomInButton = add(zoomInAction);
		setButtonSettings(zoomInButton);	
		zoomOutButton = add(zoomOutAction);
		setButtonSettings(zoomOutButton);
	
		zoomInButton.setToolTipText(zoomInTooltip);
		zoomOutButton.setToolTipText(zoomOutTooltip);
		
		
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
