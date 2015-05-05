package de.uni.freiburg.iig.telematik.wolfgang.editor;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;

public class WolfgangPT extends AbstractWolfgang<PTPlace, 
													PTTransition, 
													PTFlowRelation, 
													PTMarking, 
													Integer, 
													PTMarkingGraphState, 
													PTMarkingGraphRelation, 
													PTNet, 
													PTGraphics, 
													GraphicalPTNet>{

	private static final long serialVersionUID = -2551408144149421967L;

	public WolfgangPT() throws Exception {
		super();
	}

	public WolfgangPT(GraphicalPTNet net, boolean askForLayout) throws Exception {
		super(net, askForLayout);
	}

	public WolfgangPT(GraphicalPTNet net, LayoutOption layoutOption) throws Exception {
		super(net, layoutOption);
	}

	public WolfgangPT(GraphicalPTNet net) throws Exception {
		super(net);
	}

	@Override
	protected PNEditorComponent newEditorComponent(GraphicalPTNet net, LayoutOption layoutOption) {
		return new PTNetEditorComponent(net, layoutOption);
	}

	@Override
	protected PNEditorComponent newEditorComponent(GraphicalPTNet net, boolean askForLayout) {
		return new PTNetEditorComponent(net, askForLayout);
	}

	@Override
	protected NetType getAcceptedNetType() {
		return NetType.PTNet;
	}

	@Override
	protected GraphicalPTNet newNet() {
		return new GraphicalPTNet();
	}


}
