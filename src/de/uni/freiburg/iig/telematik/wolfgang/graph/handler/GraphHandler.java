package de.uni.freiburg.iig.telematik.wolfgang.graph.handler;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

public class GraphHandler extends mxGraphHandler {
	private Integer staticX;
	private Integer staticY;
	private int x;
	private int y;

	@Override
	public void mouseDragged(MouseEvent e) {
		MouseEvent e2 = e;

		if (first != null && previewBounds != null) {
			setStaticX((int) (first.getX() - previewBounds.getX()));
			setStaticY((int) (first.getY() - previewBounds.getY()));

			x = e.getX();
			if (getStaticX() != null) {
				if (e.getX() < getStaticX())
					x = getStaticX();
			}
			y = e.getY();
			if (getStaticY() != null) {
				if (e.getY() < getStaticY())
					y = getStaticY();
			}

			e2 = new MouseEvent(e.getComponent(), e.getModifiers(), 0, 0, x, y, e.getClickCount(), true);
			super.mouseDragged(e2);
		}
		if (first == null && previewBounds != null) {

			e2 = new MouseEvent(e.getComponent(), e.getModifiers(), 0, 0, x + 50, y + 50, e.getClickCount(), true);
			super.mouseDragged(e2);
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent e) {
		super.dragEnter(e);
	}

	@Override
	public void dragOver(DropTargetDragEvent e) {
		if (canImport) {
			mouseDragged(createEvent(e));
			mxGraphTransferHandler handler = getGraphTransferHandler(e);

			if (handler != null) {
				mxGraph graph = graphComponent.getGraph();
				double scale = graph.getView().getScale();
				Point pt = SwingUtilities.convertPoint(graphComponent, e.getLocation(), graphComponent.getGraphControl());

				pt = graphComponent.snapScaledPoint(new mxPoint(pt)).getPoint();
				handler.setLocation(new Point(pt));

				int dx = 0;
				int dy = 0;

				// Centers the preview image
				if (centerPreview && transferBounds != null) {

					dx -= Math.round(transferBounds.getWidth() * scale / 2);
					dy -= Math.round(transferBounds.getHeight() * scale / 2);
				}

				// Sets the drop offset so that the location in the transfer
				// handler reflects the actual mouse position
				handler.setOffset(new Point((int) graph.snap(dx / scale), (int) graph.snap(dy / scale)));
				pt.translate(dx, dy);

				// Shifts the preview so that overlapping parts do not
				// affect the centering
				if (transferBounds != null && dragImage != null) {

					dx = (int) Math.round((dragImage.getIconWidth() - 2 - transferBounds.getWidth() * scale) / 2);
					dy = (int) Math.round((dragImage.getIconHeight() - 2 - transferBounds.getHeight() * scale) / 2);
					pt.translate(-dx, -dy);
				}

				if (!handler.isLocalDrag() && previewBounds != null) {
					// pt.setLocation(pt.x - previewBounds.width / 2, pt.y -
					// previewBounds.height / 2);
					// TODO: drag n drop generation has different reference
					// points than handling a already generated cell: this
					// results in not
					// being able to place a newly generated cell directly at
					// the
					// border...it snaps to the next best grid.

					pt.x = pt.x < 0 ? 0 : pt.x;
					pt.y = pt.y < 0 ? 0 : pt.y;

					setPreviewBounds(new Rectangle(pt, previewBounds.getSize()));
					handler.setLocation(new Point(pt.x + previewBounds.width, pt.y + previewBounds.height));

				}
			}
		} else {
			e.rejectDrag();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MouseEvent e2 = e;
		staticX = null;
		staticY = null;
		e2 = new MouseEvent(e.getComponent(), e.getModifiers(), 0, 0, x, y, e.getClickCount(), true);
		super.mouseReleased(e2);
	}

	public GraphHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
	}

	// Constructors and other variables and methods deleted for clarity

	public Integer getStaticX() {
		return staticX;
	}

	public void setStaticX(int staticX) {
		this.staticX = this.staticX == null ? staticX : this.staticX;
	}

	public Integer getStaticY() {
		return staticY;
	}

	public void setStaticY(int staticY) {
		this.staticY = this.staticY == null ? staticY : this.staticY;
	}

}
