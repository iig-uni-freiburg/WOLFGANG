package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.wolfgang.menu.ToolBarDialog;

public class PopupToolBar {

	private class DialogFocusListener implements WindowFocusListener {

		public void windowGainedFocus(WindowEvent e) {

		}

		public void windowLostFocus(WindowEvent e) {

			if (windows == null || windows.length == 0) {
				return;
			}
			
			if((e.getOppositeWindow() == null) && !(e.getOppositeWindow()instanceof ToolBarDialog)) // in case other application window is selected
			disposeAllWindows();

			if (!Arrays.asList(windows).contains(e.getOppositeWindow())) {
				//Reacting only if mouse is clicked outside opened toolbar OR if a ToolbarDialog is created
				if (e.getOppositeWindow() != null && e.getComponent().getParent() != null && e.getOppositeWindow().equals(e.getComponent().getParent()) || (e.getOppositeWindow()instanceof ToolBarDialog))
					disposeAllWindows();
				if (!dialogOpen)
					button.setSelected(false);
			}
		}
	}

	private JToolBar verticalToolBar;

	protected JToolBar horizontalToolBar;

	private Window[] windows;

	private DialogFocusListener focusListener;

	private JToggleButton button;

	private boolean dialogOpen;

	public PopupToolBar() {
	}

	protected JToolBar createToolBar(int alignment) {
		JToolBar toolBar = new JToolBar(alignment) {
			@Override
			protected JButton createActionComponent(Action a) {
				final JButton button = super.createActionComponent(a);
				button.setFocusable(false);
				// button.addMouseMotionListener(new MouseMotionAdapter() {
				// public void mouseMoved(MouseEvent e) {
				// }
				// });
				return button;
			}
		};
		toolBar.setFloatable(false);
		return toolBar;
	}

	// private Action encapsulateAction(final AbstractAction action) {
	// AbstractAction result = new AbstractAction() {
	//
	// public void actionPerformed(ActionEvent e) {
	// action.actionPerformed(e);
	// disposeAllWindows();
	// }
	// };
	// // result.putValue(Action.SHORT_DESCRIPTION,
	// // action.getValue(Action.SHORT_DESCRIPTION));
	// result.setEnabled(action.isEnabled());
	// PropertyChangeListener[] listeners = action.getPropertyChangeListeners();
	// for (int i = 0; listeners != null && i < listeners.length; i++) {
	// result.addPropertyChangeListener(listeners[i]);
	// }
	// Object[] keys = action.getKeys();
	// for (int i = 0; i < keys.length; i++) {
	// result.putValue((String) keys[i], action.getValue((String) keys[i]));
	// }
	// return result;
	// }

	/**
	 * Add an action to
	 * 
	 * @param action
	 *            the action to add
	 * @param alignment
	 *            the Alignment of the ToolBar either JToolBar.VERTICAL or
	 *            JToolBar.HORIZONTAL
	 * @return
	 */
	public JButton add(AbstractAction action, int alignment) {
		JButton button;
		switch (alignment) {
		case JToolBar.VERTICAL:
			if (verticalToolBar == null) {
				verticalToolBar = createToolBar(alignment);
			}
			button = verticalToolBar.add(action);
			break;
		case JToolBar.HORIZONTAL:
			if (horizontalToolBar == null) {
				horizontalToolBar = createToolBar(alignment);
			}
			button = horizontalToolBar.add(action);
			break;
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
		return button;
	}

	/**
	 * Retreive the tool bar
	 * 
	 * @param alignment
	 * @return
	 */
	private JToolBar getToolBar(int alignment) {
		switch (alignment) {
		case JToolBar.VERTICAL:
			return verticalToolBar;
		case JToolBar.HORIZONTAL:
			return horizontalToolBar;
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
	}

	/**
	 * Adjust the point depending on the alignment.
	 */
	private Point adujstPoint(Point initPoint, int popupWidth, int popupHeight, int alignment) {
		switch (alignment) {
		case JToolBar.VERTICAL:
			return new Point(initPoint.x - popupWidth, initPoint.y);
		case JToolBar.HORIZONTAL:
			return new Point(initPoint.x, initPoint.y - popupHeight);
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
	}

	private Point adujstPoint(Point initPoint) {

		return new Point(initPoint.x, initPoint.y);

	}

	/**
	 * Create the Window for the given alignment and
	 * 
	 * @param comp
	 *            father component
	 * @param alignment
	 *            algenment
	 * @param point
	 *            location point
	 * @return the created window
	 */
	protected Window createWindow(Component comp, int alignment, Point point) {
		JToolBar bar = getToolBar(alignment);
		if (bar == null) {
			return null;
		}
		final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(comp));
		dialog.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				if (!dialog.hasFocus())
					dialog.requestFocusInWindow();
			}

		});
		dialog.setUndecorated(true);
		dialog.setLayout(new BorderLayout());
		dialog.add(bar);
		dialog.pack();

		Point loc = adujstPoint(point);
		SwingUtilities.convertPointToScreen(loc, comp);
		dialog.setLocation(loc);
		return dialog;
	}

	/**
	 * Creates the windows with the according toolbars and display them
	 * 
	 * @param comp
	 * @param x
	 * @param y
	 */
	public void show(Component comp, int x, int y) {
		disposeAllWindows();
		Window horizontalWindow = createWindow(comp, JToolBar.HORIZONTAL, new Point(x, y));
		Window verticalWindow = createWindow(comp, JToolBar.VERTICAL, new Point(x, y));
		if (horizontalWindow == null && verticalWindow == null) {
			return;
		}
		ArrayList<Window> windowList = new ArrayList<Window>();
		if (horizontalWindow != null) {
			windowList.add(horizontalWindow);
		}
		if (verticalWindow != null) {
			windowList.add(verticalWindow);
		}
		windows = windowList.toArray(new Window[windowList.size()]);
		windows[0].requestFocusInWindow();
		setFocusListener(new DialogFocusListener());
		for (int i = 0; i < windows.length; i++) {
			windows[i].addWindowFocusListener(getFocusListener());
			windows[i].setVisible(true);
		}
	}

	public DialogFocusListener getFocusListener() {
		return focusListener;
	}

	private void setFocusListener(DialogFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	public void disposeAllWindows() {
		if (windows == null) {
			return;
		}
		for (int i = 0; i < windows.length; i++) {
			windows[i].dispose();
		}
	}

	public void add(JComponent action) {

		if (horizontalToolBar == null) {
			horizontalToolBar = createToolBar(JToolBar.HORIZONTAL);
		}
		horizontalToolBar.add(action);

	}

	public void setButton(JToggleButton jToggleButton, boolean dialogOpen) {
		this.button = jToggleButton;
		this.dialogOpen = dialogOpen;

	}

}