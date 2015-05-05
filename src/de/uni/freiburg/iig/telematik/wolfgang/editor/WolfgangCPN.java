package de.uni.freiburg.iig.telematik.wolfgang.editor;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.CPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.CPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent.LayoutOption;

public class WolfgangCPN extends AbstractWolfgang<  CPNPlace, 
													CPNTransition, 
													CPNFlowRelation, 
													CPNMarking, 
													Multiset<String>, 
													CPNMarkingGraphState, 
													CPNMarkingGraphRelation, 
													CPN, 
													CPNGraphics, 
													GraphicalCPN>{

	private static final long serialVersionUID = -506484681099261489L;

	public WolfgangCPN() throws Exception {
		super();
	}

	public WolfgangCPN(GraphicalCPN net, boolean askForLayout) throws Exception {
		super(net, askForLayout);
	}

	public WolfgangCPN(GraphicalCPN net, LayoutOption layoutOption) throws Exception {
		super(net, layoutOption);
	}

	public WolfgangCPN(GraphicalCPN net) throws Exception {
		super(net);
	}

	@Override
	protected PNEditorComponent newEditorComponent(GraphicalCPN net, LayoutOption layoutOption) {
		return new CPNEditorComponent(net, layoutOption);
	}

	@Override
	protected PNEditorComponent newEditorComponent(GraphicalCPN net, boolean askForLayout) {
		return new CPNEditorComponent(net, askForLayout);
	}

	@Override
	protected NetType getAcceptedNetType() {
		return NetType.CPN;
	}

	@Override
	protected GraphicalCPN newNet() {
		return new GraphicalCPN();
	}


}
