package de.uni.freiburg.iig.telematik.wolfgang.actions.pn;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class ChecKSoundnessAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public ChecKSoundnessAction(PNEditorComponent pnEditor) throws PropertyException, IOException {
		super(pnEditor, "CheckSoundness", IconFactory.getIcon("soundcwn"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			AbstractPetriNet pn = editor.getNetContainer().getPetriNet();
			if (pn instanceof CPN) {
				CPN cpn = (CPN) pn;
				try {
					cpn.checkSoundness(true);
					JOptionPane.showMessageDialog(editor.getGraphComponent(), "Awesome! You have a SOUND Coloured Workflow Net.", "CWN is Sound - Awesome Job!", JOptionPane.INFORMATION_MESSAGE);

				} catch (PNValidationException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Validation Failed", JOptionPane.ERROR_MESSAGE);
				} catch (PNSoundnessException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Is Not Sound", JOptionPane.ERROR_MESSAGE);
				}

			}
		}
	}
}
