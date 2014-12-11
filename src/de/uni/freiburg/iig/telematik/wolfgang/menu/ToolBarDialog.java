package de.uni.freiburg.iig.telematik.wolfgang.menu;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JRootPane;

public class ToolBarDialog extends JDialog {
    public ToolBarDialog(Window owner, String title, boolean modal) {
        super(owner, title, DEFAULT_MODALITY_TYPE);
    }

@Override
    // Override createRootPane() to automatically resize
    // the frame when contents change
    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane() {
            private boolean packing = false;

            public void validate() {
                super.validate();
                if (!packing) {
                    packing = true;
                    pack();
                    packing = false;
                }
            }
        };
        rootPane.setOpaque(true);
        return rootPane;
    }
}
