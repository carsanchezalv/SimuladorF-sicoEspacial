package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 8509722595342779885L;
	ControlPanel controlPanel;
	Controller controlador;

	public MainWindow(Controller ctrl)
	{
		super("Physics simulator.");
		this.controlador = ctrl;
		initGUI();
	}
	private void initGUI()
	{
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout());
		
		this.setContentPane(panelPrincipal);
		
		controlPanel = new ControlPanel(controlador);
		panelPrincipal.add(controlPanel, BorderLayout.NORTH);
		panelPrincipal.add(centrito(), BorderLayout.CENTER);
		panelPrincipal.setBackground(Color.WHITE);
		panelPrincipal.add(new StatusBar(controlador), BorderLayout.SOUTH);
		
		this.setMinimumSize(new Dimension(900, 700));
		this.setVisible(true);
		this.setPreferredSize(new Dimension(900, 700));
		this.pack();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				int confirmacion = JOptionPane.showConfirmDialog(null, "Seguro que quieres salir?", "Salir",
						JOptionPane.YES_NO_OPTION);

				if (confirmacion == JOptionPane.YES_OPTION)
					System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}		
		});
		setLocationRelativeTo(null);
	}
	
	public JPanel centrito()
	{
		JPanel centro = new JPanel();
		centro.setLayout(new BorderLayout());
		
		centro.add(new BodiesTable(controlador), BorderLayout.NORTH);
		centro.add(new Viewer(controlador), BorderLayout.CENTER);
		centro.setBackground(Color.WHITE);
		
		return centro;
	}
}