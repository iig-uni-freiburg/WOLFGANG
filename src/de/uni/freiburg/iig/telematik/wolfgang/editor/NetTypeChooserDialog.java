package de.uni.freiburg.iig.telematik.wolfgang.editor;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.wolfgang.icons.IconFactory;

public class NetTypeChooserDialog extends JDialog {

	private static final long serialVersionUID = -48838377733567237L;
	
	private JButton btnPTNet;
	private JButton btnCPN;
	private JLabel lblWolfgang;
	
	private NetType chosenNetType = null;

	public NetTypeChooserDialog(Window owner){
		super(owner);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getLabelWolfgang(), BorderLayout.CENTER);
		
		JPanel chooseNetPanel = new JPanel();
		chooseNetPanel.add(getButtonPTNet());
		chooseNetPanel.add(getButtonCPN());
		getContentPane().add(chooseNetPanel, BorderLayout.PAGE_END);
		
		pack();
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}
	
	public NetType getChosenNetType(){
		return chosenNetType;
	}
	
	private JButton getButtonPTNet() {
		if(btnPTNet == null){
			URL imageURL = IconFactory.class.getResource("choosePT.png");
			ImageIcon icon = new ImageIcon(imageURL);
			btnPTNet = new JButton(icon);
			btnPTNet.setFocusPainted(false);
			btnPTNet.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					chosenNetType = NetType.PTNet;
					dispose();
				}
			});
		}
		return btnPTNet;
	}

	private JButton getButtonCPN() {
		if(btnCPN == null){
			URL imageURL = IconFactory.class.getResource("chooseCPN.png");
			ImageIcon icon = new ImageIcon(imageURL);
			btnCPN = new JButton(icon);
			btnCPN.setFocusPainted(false);
			btnCPN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					chosenNetType = NetType.CPN;
					dispose();
				}
			});
		}
		return btnCPN;
	}

	private JLabel getLabelWolfgang() {
		if(lblWolfgang == null){
			URL imageURL = IconFactory.class.getResource("wolfgang.png");
			ImageIcon icon = new ImageIcon(imageURL);
			lblWolfgang = new JLabel(icon);
		}
		return lblWolfgang;
	}
	
	public static NetType showDialog(){
		NetTypeChooserDialog dialog = new NetTypeChooserDialog(null);
		return dialog.getChosenNetType();
	}
	
	public static void main(String[] args) {
		System.out.println(NetTypeChooserDialog.showDialog());
	}

}
