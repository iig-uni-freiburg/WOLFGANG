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
	private JPanel propertyPanel;
	private JPanel specificFieldPanel;
	
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
	
	protected abstract void addSpecificFields(JPanel panel);
	
	public void setUpGui() {
		propertyPanel = new JPanel(new SpringLayout());
		propertyPanel.add(getLabelHeadline());

		propertyPanel.add(new JPopupMenu.Separator());
		propertyPanel.add(getButtonErrorDetails());
		propertyPanel.add(new JPopupMenu.Separator());
		
		propertyPanel.add(getPanelSpecificFields());
		SpringUtilities.makeCompactGrid(getPanelSpecificFields(), getPanelSpecificFields().getComponentCount(), 1, 0, 0, 0, 0);
		
		propertyPanel.add(new JPopupMenu.Separator());

		int componentCount = propertyPanel.getComponentCount();
		SpringUtilities.makeCompactGrid(propertyPanel, componentCount, 1, 0, 0, 0, 0);
		add(propertyPanel, BorderLayout.PAGE_START);
		setMinimumSize(new Dimension((int) propertyPanel.getSize().getWidth(), (int) (lineHeight*componentCount)));
	}
	
	private JPanel getPanelSpecificFields(){
		if(specificFieldPanel == null){
			specificFieldPanel = new JPanel(new SpringLayout());
			addSpecificFields(specificFieldPanel);
		}
		return specificFieldPanel;
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
		int c = !isExpanded ? propertyPanel.getComponentCount() : 1;
		SpringUtilities.makeCompactGrid(propertyPanel, c, 1, 0, 0, 0, 0);
		getPanelSpecificFields().setVisible(!isExpanded);
		getLabelHeadline().setExpanded(!isExpanded);
		setMinimumSize(new Dimension((int) propertyPanel.getSize().getWidth(), (int) (lineHeight*c)));
	}

}
