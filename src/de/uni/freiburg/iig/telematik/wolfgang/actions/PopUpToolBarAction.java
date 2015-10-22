package de.uni.freiburg.iig.telematik.wolfgang.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.EditorProperties;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.menu.ToolBarDialog;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.PopupToolBar;

/**
 * 
 */
public class PopUpToolBarAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 3097560664208606500L;
	
	private JDialog popupDialog;
	private JToggleButton button;
	private JComponent toolbarContent;
	private JButton btnNewDialog;
	private PopupToolBar popupToolBar;
	protected JDialog dialog;

	public PopUpToolBarAction(PNEditorComponent editor, String name, ImageIcon icon, JComponent toolbar) throws ParameterException, PropertyException, IOException {
		super(editor, name, icon);
		popupToolBar = new PopupToolBar();
		toolbarContent = toolbar;
		btnNewDialog = new JButton(IconFactory.getIcon("maximize"));
		btnNewDialog.setBorderPainted(false);
		newWindowButton(popupToolBar, toolbarContent, btnNewDialog);
	}

	protected JDialog getPopupFrame() {
		return popupDialog;
	}

	public void setPopupFrame(JDialog dialog) {
		this.popupDialog = dialog;
	}

	protected JToggleButton getButton() {
		// TODO Auto-generated method stub
		return button;
	}

	public void setButton(JToggleButton addedButton) {
		this.button = addedButton;
	}

	private void newWindowButton(final PopupToolBar popupToolBar, final JComponent toolbarContent2, final JButton btnNewDialog) {
		btnNewDialog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupToolBar.setButton(getButton(), true);
				Window window = SwingUtilities.getWindowAncestor(getButton());
				JDialog dialog = new ToolBarDialog(window, toolbarContent2.getName(), false);
				if (btnNewDialog.getName() != null)
					dialog.setTitle(btnNewDialog.getName());
				dialog.setLocationRelativeTo(window);
				dialog.add(toolbarContent2);
				dialog.setModal(false);
				dialog.setResizable(false);
				getButton().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						getButton().setSelected(true);
						super.mouseClicked(e);
					}
				});

				dialog.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						getButton().setSelected(false);
						setPopupFrame(null);

					}
				});
				setPopupFrame(dialog);
				dialog.pack();
				dialog.setVisible(true);
				setDialog(dialog);
			}

		});
	}

	protected void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JDialog getDialog() {
		return dialog;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getPopupFrame() == null) {
			popupToolBar.setButton(getButton(), false);

			popupToolBar.add(toolbarContent);
			popupToolBar.add(btnNewDialog);

			int size = 0;
			size = EditorProperties.getInstance().getIconSize().getSize();
			popupToolBar.show(getButton(), 0, size + size / 2);
		}
	}

}
