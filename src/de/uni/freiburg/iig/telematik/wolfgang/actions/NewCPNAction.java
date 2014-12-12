package de.uni.freiburg.iig.telematik.wolfgang.actions;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.Wolfgang;

public class NewCPNAction extends AbstractNewNetAction<GraphicalCPN>{

	private static final long serialVersionUID = 8830243374604859523L;

	protected NewCPNAction(Wolfgang wolfgang) {
		super(wolfgang);
	}

	@Override
	protected GraphicalCPN createNewGraphicalPN() {
		return new GraphicalCPN();
	}

}
