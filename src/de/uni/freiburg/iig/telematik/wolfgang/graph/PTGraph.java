package de.uni.freiburg.iig.telematik.wolfgang.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.misc.CircularPointGroup;
import de.invation.code.toval.graphic.misc.PColor;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;
import de.uni.freiburg.iig.telematik.wolfgang.properties.view.PTProperties;

/**
 * @author julius
 *
 */
public class PTGraph extends PNGraph {

	public PTGraph(GraphicalPTNet netContainer, PTProperties properties) throws ParameterException {
		super(netContainer, properties);
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		if (((PTFlowRelation) relation).getWeight() == 1) 
			return "";
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}

	@Override
	public void updatePlaceState(String name, Multiset<String> state) throws ParameterException {
		int tokens =  state.multiplicity("black");
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(name, tokens);
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);	
		graphListenerSupport.notifyMarkingForPlaceChanged(name, state);
	}
	
	@Override
	public
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */ Multiset<String> getPlaceStateForCell(String name, CircularPointGroup circularPointGroup) {
		PTPlace place = (PTPlace) getNetContainer().getPetriNet().getPlace(name);
		if(place!= null){
			if(circularPointGroup != null)
                                circularPointGroup.addPoints(PColor.black, place.getState());
                        Multiset<String> multiSet = new Multiset<>();
                        multiSet.setMultiplicity("black", place.getState());
                        return multiSet;
                }
		return null;
	}
	
@Override
	/** Method for incrementing or decrementing the current #PTMarking of the given #PTPlace
	 * @param cell
	 * @param wheelRotation
	 * @return 
	 * @throws ParameterException
	 */
	public AbstractMarking inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		PTMarking oldValue = initialMarking;
		Integer current = initialMarking.get(cell.getId());
		int capacity = getNetContainer().getPetriNet().getPlace(cell.getId()).getCapacity();	
		if(current != null && (capacity<0 ^ capacity>=(current - wheelRotation))) // null means here that the value "zero" has been set before / capacity -1 means infinite
		initialMarking.set(cell.getId(),  current - wheelRotation);
		else if(current == null && wheelRotation < 0 ) // create new Marking
		initialMarking.set(cell.getId(),  1);
		
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
		return oldValue;
	}



	@Override
	protected String getPlaceToolTip(PNGraphCell cell) {
		PTPlace ptPlace = getNetContainer().getPetriNet().getPlace(cell.getId());

		return  "Cap:"+ ((ptPlace.getCapacity() == -1)?"\u221e":ptPlace.getCapacity());
	}

	
	@Override
	protected String getTransitionToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected String getArcToolTip(PNGraphCell cell) {
		return "";
	}



	@Override
	protected void setArcLabel(String arcName, String weight) {
		getPNProperties().setArcWeight(this, arcName, weight);
	}

	@Override
	public Color getTokenColorForName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTokenColor(String name, Color value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Multiset<String> getConstraintforArc(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateConstraint(String flowRelation, Multiset constraint) {
		graphListenerSupport.notifyConstraintChanged(flowRelation, constraint);	
		
	}

	@Override
	public void updateTokenConfigurer(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCapacityforPlace(String name, String color) {
		// TODO Auto-generated method stub
		return getNetContainer().getPetriNet().getPlace(name).getCapacity();
	}

	@Override
	public void updatePlaceCapacity(String name, String color, int newCapacity) {
		PTPlace place = getNetContainer().getPetriNet().getPlace(name);
		if (newCapacity <= 0)
			place.removeCapacity();
		else
			place.setCapacity(newCapacity);

		graphListenerSupport.notifyPlaceCapacityChanged(name,color,newCapacity);		
	}


	@Override
	protected void drawAdditionalTransitionGrahpics(mxGraphics2DCanvas canvas, mxCellState state) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void drawAdditionalArcGrahpics(mxGraphics2DCanvas canvas, mxCellState state) {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		String cellId = cell.getId();
		String hexColor = (String) state.getStyle().get(mxConstants.STYLE_STROKECOLOR);
		Color lineColor = Color.BLACK;;
		if(hexColor != null)
		lineColor  = Utils.parseColor(hexColor);
		PTFlowRelation flowRelation = getNetContainer().getPetriNet().getFlowRelation(cellId);
		
		int pointDiameter = (int) (getDefaultTokenSize() * getView().getScale());
		 int posX = (int) state.getCenterX();
			int posY = (int) state.getCenterY();
	
			Graphics g = canvas.getGraphics();
			int j = 0;
			int startY = posY - (1/2*10)- (pointDiameter/2);
			int spacingY = pointDiameter+2;
			int spacingX = pointDiameter+2;
			String c = "black";
				int constraint = flowRelation.getConstraint();
				if(constraint<5){
				for (int i = 0; i < constraint; i++) {
						g.setColor(Color.BLACK);
					g.fillOval(posX + (i * spacingX), startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(lineColor);
					g.drawOval(posX + (i * spacingX), startY + (j * spacingY), pointDiameter, pointDiameter);
				}
				}
				else {
					g.setColor(Color.BLACK);
					g.setFont(new Font("TimesRoman", Font.BOLD, 10)); 
					g.fillOval(posX , startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(lineColor);
					g.drawOval(posX, startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(Color.BLACK);
					 g.drawString(" : " +constraint +"\n", posX + pointDiameter , (startY+8) + (j * (spacingY)) );
				}
				if(constraint>0)
				j++;
			
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 
		 
		
	}

	@Override
	protected void drawAdditionalContextToTransition(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException {
	}

}
