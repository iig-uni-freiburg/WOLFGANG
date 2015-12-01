package de.uni.freiburg.iig.telematik.wolfgang.properties.check;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.validate.ExceptionDialog;

public abstract class AbstractPropertyCheckView<O> extends JPanel {

	private static final long serialVersionUID = -144286894277730018L;
	
	private static final Boolean DEFAULT_HEADLINE_LABEL_EXPANSION = true;

	private JButton btnErrorDetails;
	private PropertyCheckHeadlineLabel lblHeadline;
	private JPanel pnlProperty;
	private JPanel pnlSpecificField;
	
	private int lineHeight = 16;
	
	protected O checkResult = null;
	protected Exception exception;

	public AbstractPropertyCheckView() {
		super(new BorderLayout());
	}

	public abstract void resetFieldContent();
	
	public void updateFieldContent(O checkResult, Exception exception){
		this.exception = exception;
		getButtonErrorDetails().setEnabled(exception != null);
	}
	
	protected abstract String getHeadline();
	
	protected abstract void addSpecificFields(JPanel pnl);
	
	public void setUpGui() {
		pnlProperty = new JPanel(new SpringLayout());
		pnlProperty.add(getLabelHeadline());

		pnlProperty.add(new JPopupMenu.Separator());
		pnlProperty.add(getButtonErrorDetails());
		pnlProperty.add(new JPopupMenu.Separator());
		
		pnlProperty.add(getPanelSpecificFields());
		SpringUtilities.makeCompactGrid(getPanelSpecificFields(), getPanelSpecificFields().getComponentCount(), 1, 0, 0, 0, 0);
		
		pnlProperty.add(new JPopupMenu.Separator());

		int componentCount = pnlProperty.getComponentCount();
		SpringUtilities.makeCompactGrid(pnlProperty, componentCount, 1, 0, 0, 0, 0);
		add(pnlProperty, BorderLayout.PAGE_START);
		setMinimumSize(new Dimension((int) pnlProperty.getSize().getWidth(), (int) (lineHeight*componentCount)));
	}
	
	private JPanel getPanelSpecificFields(){
		if(pnlSpecificField == null){
			pnlSpecificField = new JPanel(new SpringLayout());
			addSpecificFields(pnlSpecificField);
		}
		return pnlSpecificField;
	}
	
	private JButton getButtonErrorDetails(){
		if(btnErrorDetails == null){
			btnErrorDetails = new JButton("Show Details");
			btnErrorDetails.setEnabled(exception != null);
			btnErrorDetails.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(exception == null)
						return;
					ExceptionDialog.showException(SwingUtilities.getWindowAncestor(AbstractPropertyCheckView.this), "PN Property Check Exception", exception, true);
				}
			});
		}
		return btnErrorDetails;
	}
	
	private PropertyCheckHeadlineLabel getLabelHeadline(){
		if(lblHeadline == null){
			lblHeadline = new PropertyCheckHeadlineLabel(getHeadline(), DEFAULT_HEADLINE_LABEL_EXPANSION);
			lblHeadline.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
						updatePanelView(lblHeadline.isExpanded());
				}
			});
		}
		return lblHeadline;
	}
	
	protected void updatePanelView(boolean isExpanded){
		int c = !isExpanded ? pnlProperty.getComponentCount() : 1;
		SpringUtilities.makeCompactGrid(pnlProperty, c, 1, 0, 0, 0, 0);
		getPanelSpecificFields().setVisible(!isExpanded);
		getLabelHeadline().setExpanded(!isExpanded);
		setMinimumSize(new Dimension((int) pnlProperty.getSize().getWidth(), (int) (lineHeight*c)));
	}

}
