package de.uni.freiburg.iig.telematik.wolfgang.actions;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;

public class NewPTNetAction extends AbstractNewNetAction<GraphicalPTNet> {

	private static final long serialVersionUID = -5898716538739837861L;

	protected NewPTNetAction(AbstractWolfgang wolfgang) {
		super(wolfgang);
	}

	@Override
	protected GraphicalPTNet createNewGraphicalPN() {
		return new GraphicalPTNet();
	}

}
