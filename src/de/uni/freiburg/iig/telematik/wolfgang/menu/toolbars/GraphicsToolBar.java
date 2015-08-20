package de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillListener;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.FillPanel;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LineListener;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphics.LinePanel;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphListener;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.MXConstants;
import de.uni.freiburg.iig.telematik.wolfgang.graph.util.Utils;

public class GraphicsToolBar extends JToolBar implements PNGraphListener, LineListener, FillListener {

	private static final long serialVersionUID = -6491749112943066366L;

	private PNEditorComponent pnEditor;

	private LinePanel lp;
	private FillPanel fp;

	public GraphicsToolBar(PNEditorComponent pnEditor, int horizontal) {
		setOrientation(horizontal);
		this.pnEditor = pnEditor;
		this.pnEditor.getGraphComponent().getGraph().addPNGraphListener(this);
		setUpGUI();
	}

	private void setUpGUI() {
		setFloatable(false);
		fp = new FillPanel();
		lp = new LinePanel();
		
		fp.addFillPanelListener(this);
		lp.addLinePanelListener(this);
		
		add(fp);
		add(lp);
	}

	@Override
	public void placeAdded(AbstractPlace place) {
	}

	@Override
	public void transitionAdded(AbstractTransition transition) {
	}

	@Override
	public void relationAdded(AbstractFlowRelation relation) {
	}

	@Override
	public void placeRemoved(AbstractPlace place) {
	}

	@Override
	public void transitionRemoved(AbstractTransition transition) {
	}

	@Override
	public void relationRemoved(AbstractFlowRelation relation) {
	}

	@Override
	public void markingForPlaceChanged(String placeName, Multiset placeMarking) {
	}

	@Override
	public void placeCapacityChanged(String placeName, String color, int newCapacity) {
	}

	@Override
	public void constraintChanged(String flowRelation, Multiset constraint) {
	}

	@Override
	public void componentsSelected(Set<PNGraphCell> selectedComponents) {
		if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
			if (selectedComponents == null || selectedComponents.isEmpty()) {
				setVisible(false);
				return;
			}
			if (!selectedComponents.isEmpty()) {
				setVisible(true);

				String initialFillColor = null;
				int isSameFillColorCounter = 0;
				String initialGradientColor = null;
				int isSameGradientColorCounter = 0;


				GradientRotation initialGradientRotation = null;
				int initialGradientRotationCounter = 0;
				
				int containsNoFillCounter = 0;

				boolean clearSelection = false;
				
				boolean differentFillColor = false;
				
				boolean differentGradientFillColor = false;
				
				boolean differentLineColor = false;

				// LineShape
				Shape initialLineShape = null;
				int initialLineShapeCounter = 0;

				// LineColor
				String initialLineColor = null;
				int isSameLineColorCounter = 0;

				// LineStyle
				Style initialLineStyle = null;
				int initialLineStyleCounter = 0;

				// LineWeight
				double initialLineWeight = -1.0;
				int isSameLineWeightCounter = 0;

				for (PNGraphCell cell : selectedComponents) {
					Map<String, Object> style = pnEditor.getGraphComponent().getGraph().getCellStyle(cell);
					if (style.containsKey("noLabel")) {
						if (style.get("noLabel").equals("1")) {
//							fillDisabledCounter++;
						} else {
							Fill fillForCell = null;
							Line lineForCell = null;
							if (pnEditor.getGraphComponent().getGraph().isLabelSelected()) {
								AnnotationGraphics annotationGraphics = getCellAnnotationGraphics(cell);
								if (annotationGraphics != null) {
									fillForCell = annotationGraphics.getFill();
									lineForCell = annotationGraphics.getLine();

								}
							} else {
								AbstractObjectGraphics cellGraphics = getCellGraphics(cell);
								if (cellGraphics instanceof NodeGraphics) {
									fillForCell = ((NodeGraphics) cellGraphics).getFill();
									lineForCell = ((NodeGraphics) cellGraphics).getLine();
								}
								if (cellGraphics instanceof ArcGraphics) {
									lineForCell = ((ArcGraphics) cellGraphics).getLine();
								}
							}

							if (fillForCell != null) {
								// Check similarities in cell selection for:
								// Font-Family
								if (fillForCell.getColor() != null) {

									if (initialFillColor == null) {
										initialFillColor = fillForCell.getColor();
										isSameFillColorCounter = 1;
									} else if (fillForCell.getColor().equals(initialFillColor)) {
										isSameFillColorCounter++;

									}

								}

								// Font-Size
								if (fillForCell.getGradientColor() != null) {
									if (initialGradientColor == null) {
										initialGradientColor = fillForCell.getGradientColor();
										isSameGradientColorCounter = 1;
									} else if (fillForCell.getGradientColor().equals(initialGradientColor)) {
										isSameGradientColorCounter++;
									}
								}

								// Fill-GradientRotation
								if (fillForCell.getGradientRotation() != null) {
									if (initialGradientRotation == null) {
										initialGradientRotation = fillForCell.getGradientRotation();
										initialGradientRotationCounter = 1;
									} else if (fillForCell.getGradientRotation().equals(initialGradientRotation)) {
										initialGradientRotationCounter++;
									}

								}
								

							}
							else {
								containsNoFillCounter++;
							}

							if (lineForCell != null) {

								// Line-Shape
								if (lineForCell.getShape() != null) {
									if (initialLineShape == null) {
										initialLineShape = lineForCell.getShape();
										initialLineShapeCounter = 1;
									} else if (lineForCell.getShape().equals(initialLineShape)) {
										initialLineShapeCounter++;
									}

								}

								// Line-Color
								if (lineForCell.getColor() != null) {
									if (initialLineColor == null) {
										initialLineColor = lineForCell.getColor();
										isSameLineColorCounter = 1;
									} else if (lineForCell.getColor().equals(initialLineColor)) {
										isSameLineColorCounter++;
									}
								}

								// Line-Style
								if (lineForCell.getStyle() != null) {
									if (initialLineStyle == null) {
										initialLineStyle = lineForCell.getStyle();
										initialLineStyleCounter = 1;
									} else if (lineForCell.getStyle() == initialLineStyle) {
										initialLineStyleCounter++;
									}
								}

								// Line-Weight
								if (initialLineWeight == -1.0) {
									initialLineWeight = lineForCell.getWidth();
									isSameLineWeightCounter = 1;
								} else if (lineForCell.getWidth() == initialLineWeight) {
									isSameLineWeightCounter++;
								}

							}

						}
					}
				}


				Color fillColor = null;
				Color gradientColor = null;
				GradientRotation rotation = null;
				
				fp.setVisible(!(selectedComponents.size() == containsNoFillCounter));
					
				if (selectedComponents.size() == isSameFillColorCounter+containsNoFillCounter && initialFillColor != null) {
					if(!initialFillColor.equals("transparent"))
					fillColor = Utils.parseColor(initialFillColor);
				}
				else {
					differentFillColor = true;
				}

				if (selectedComponents.size() == isSameGradientColorCounter+containsNoFillCounter && initialGradientColor != null) {
					gradientColor = Utils.parseColor(initialGradientColor);
				}
				else {
					differentGradientFillColor = true;
				}
				
				if (selectedComponents.size() == initialGradientRotationCounter+containsNoFillCounter) {
					rotation = initialGradientRotation;
				} else {
					if (initialGradientRotation != null)
						clearSelection = true;
				}
				if (pnEditor.getGraphComponent().getGraph().isLabelSelected())
					fp.setLabelFillMode(fillColor, differentFillColor);
				else
				fp.updatePanel(fillColor, differentFillColor, rotation, clearSelection, gradientColor, differentGradientFillColor);

				
				

				lp.setEnabled(true);

				Color lineColor = null;
				Shape lineShape = null;
				Style lineStyle = null;
				int lineWeight = -1;

				if (selectedComponents.size() == isSameLineColorCounter && initialLineColor != null) {
					lineColor = Utils.parseColor(initialLineColor);
				}
				else{
					differentLineColor = true;
				}
				if (selectedComponents.size() == initialLineShapeCounter && initialLineShape != null) {
					lineShape = initialLineShape;
				}

				if (selectedComponents.size() == initialLineStyleCounter && initialLineStyle != null) {
					lineStyle = initialLineStyle;
				}

				if (selectedComponents.size() == isSameLineWeightCounter && initialLineWeight >= 0.0) {
					lineWeight = new Double(initialLineWeight).intValue();
				}

				lp.updatePanel(lineColor, differentLineColor, lineShape, lineStyle, lineWeight);

			}
		}
	}

	private AnnotationGraphics getCellAnnotationGraphics(PNGraphCell cell) {
		switch (cell.getType()) {
		case PLACE:
			return pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics(cell.getId());
		case TRANSITION:
			return pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics(cell.getId());
		case ARC:
			return pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics(cell.getId());
		default:
			break;
		}
		return null;

	}

	private AbstractObjectGraphics getCellGraphics(PNGraphCell cell) {
		switch (cell.getType()) {
		case PLACE:
			return pnEditor.getNetContainer().getPetriNetGraphics().getPlaceGraphics(cell.getId());
		case TRANSITION:
			return pnEditor.getNetContainer().getPetriNetGraphics().getTransitionGraphics(cell.getId());
		case ARC:
			return pnEditor.getNetContainer().getPetriNetGraphics().getArcGraphics(cell.getId());
		default:
			break;
		}
		return null;

	}

	// Line Listeners
	@Override
	public void lineShapeChanged(Shape line) {
		switch (line) {
		case CURVE:
			pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "true");
			pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
			break;
		case LINE:
			pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_ROUNDED, "false");
			pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_EDGE, "direct");
			break;
		default:
			break;
		}

	}

	@Override
	public void lineStyleChanged(Style solid) {
		if (pnEditor.getGraphComponent().getGraph().isLabelSelected()) {
			setLineStyle(solid, MXConstants.LABEL_LINE_STYLE);
		} else {
			setLineStyle(solid, MXConstants.LINE_STYLE);
		}

	}

	private void setLineStyle(Style solid, String key) {
		switch (solid) {
		case DASH:
			pnEditor.getGraphComponent().getGraph().setCellStyles(key, "dash");
			break;
		case DOT:
			pnEditor.getGraphComponent().getGraph().setCellStyles(key, "dot");
			break;
		case SOLID:
			pnEditor.getGraphComponent().getGraph().setCellStyles(key, "solid");
			break;
		default:
			break;
		}
	}

	@Override
	public void lineWeightChanged(String strokeWeight) {
		pnEditor.getGraphComponent().getGraph().setStrokeWeightOfSelectedCell(strokeWeight);
	}

	@Override
	public void lineColorChanged(Color color) {
		if (color == null) {
			if (pnEditor.getGraphComponent().getGraph().isLabelSelected())
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, "none");
			else
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "none");
		} else {
			if (pnEditor.getGraphComponent().getGraph().isLabelSelected()) {
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.hexString(color));
			} else
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(color));
		}
	}

	// Fill Listeners
	@Override
	public void fillColorChanged(Color color) {
		if (pnEditor.getGraphComponent().getGraph().isLabelSelected()) {
			if (color == null)
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "none");
			else
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.hexString(color));

		} else {
			if (color == null)
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "none");
			else
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(color));

		}

	}

	@Override
	public void gradientColorChanged(Color color) {
		if (pnEditor.getGraphComponent().getGraph().isLabelSelected()) {
			if (color == null)
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, "none");
			else
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.LABEL_GRADIENTCOLOR, mxUtils.hexString(color));

		} else {
			if (color == null)
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, "none");
			else
				pnEditor.getGraphComponent().getGraph().setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.hexString(color));

		}
	}

	@Override
	public void gradientDirectionChanged(GradientRotation rotation) {
		if (pnEditor.getGraphComponent().getGraph().isLabelSelected())
			if (rotation == null){
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, null);
			} else {
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.LABEL_GRADIENT_ROTATION, rotation.toString());

			}
		else {
			if (rotation == null) {
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, null);
			} else {
				pnEditor.getGraphComponent().getGraph().setCellStyles(MXConstants.GRADIENT_ROTATION, rotation.toString());

			}
		}

	}

	public static void main(String[] args) {

		JPanel pnl = new JPanel();
		pnl.add(new GraphicsToolBar(new PTNetEditorComponent(), JToolBar.HORIZONTAL));
		new DisplayFrame(pnl, true);

	}

}
