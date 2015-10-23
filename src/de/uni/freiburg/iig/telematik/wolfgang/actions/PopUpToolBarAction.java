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
	
	private JDialog dlgPopup;
	private JToggleButton tglButton;
	private JComponent cmpToolbarContent;
	private JButton btnNewDialog;
	private PopupToolBar popupToolBar;
	protected JDialog dlg;

	public PopUpToolBarAction(PNEditorComponent editor, String name, ImageIcon icon, JComponent cmpToolbar) throws ParameterException, PropertyException, IOException {
		super(editor, name, icon);
		popupToolBar = new PopupToolBar();
		cmpToolbarContent = cmpToolbar;
		btnNewDialog = new JButton(IconFactory.getIcon("maximize"));
		btnNewDialog.setBorderPainted(false);
		newWindowButton(popupToolBar, cmpToolbarContent, btnNewDialog);
	}

	protected JDialog getPopupFrame() {
		return dlgPopup;
	}

	public void setPopupFrame(JDialog dlg) {
		this.dlgPopup = dlg;
	}

	protected JToggleButton getButton() {
		// TODO Auto-generated method stub
		return tglButton;
	}

	public void setButton(JToggleButton tglAdded) {
		this.tglButton = tglAdded;
	}

	private void newWindowButton(final PopupToolBar popupToolBar, final JComponent cmpToolbarContent2, final JButton btnNewDialog) {
		btnNewDialog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupToolBar.setButton(getButton(), true);
				Window window = SwingUtilities.getWindowAncestor(getButton());
				JDialog dlg = new ToolBarDialog(window, cmpToolbarContent2.getName(), false);
				if (btnNewDialog.getName() != null)
					dlg.setTitle(btnNewDialog.getName());
				dlg.setLocationRelativeTo(window);
				dlg.add(cmpToolbarContent2);
				dlg.setModal(false);
				dlg.setResizable(false);
				getButton().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						getButton().setSelected(true);
						super.mouseClicked(e);
					}
				});

				dlg.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						getButton().setSelected(false);
						setPopupFrame(null);

					}
				});
				setPopupFrame(dlg);
				dlg.pack();
				dlg.setVisible(true);
				setDialog(dlg);
			}

		});
	}

	protected void setDialog(JDialog dlg) {
		this.dlg = dlg;
	}

	public JDialog getDialog() {
		return dlg;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (getPopupFrame() == null) {
			popupToolBar.setButton(getButton(), false);

			popupToolBar.add(cmpToolbarContent);
			popupToolBar.add(btnNewDialog);

			int size = 0;
			size = EditorProperties.getInstance().getIconSize().getSize();
			popupToolBar.show(getButton(), 0, size + size / 2);
		}
	}

}
