package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.Body;

public class ControlPanel extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 5393346653218188113L;

	private Controller _ctrl;
	private JFileChooser fileChooser;
	private JSpinner steps;
	private JSpinner delay;

	private JTextField dTime; // delta

	// Botoncines
	private JButton run; // play
	private JButton stop; // stop
	private JButton load; // load
	private JButton gravityLaws; // change law
	private JButton exit; // salir

	private volatile Thread hilo;

	ControlPanel(Controller ctrl) {
		super();
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
		hilo = null;
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		this.add(createSupIzda(), BorderLayout.WEST);
		this.add(createSupDcha(), BorderLayout.EAST);
	}

	private JPanel createSupDcha() {

		JPanel panelDcho = new JPanel();
		panelDcho.setLayout(new FlowLayout());

		exit = new JButton();
		exit.setToolTipText("Salir"); // Texto al poner el ratón encima
		exit.setIcon(new ImageIcon("resources/icons/exit.png")); // Colocar el icono
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int confirmacion = JOptionPane.showConfirmDialog(null, "Seguro que quieres salir?", "Salir",
						JOptionPane.YES_NO_OPTION);

				if (confirmacion == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		panelDcho.add(exit);

		return panelDcho;
	}

	private JToolBar createSupIzda() {

		JToolBar toolbar = new JToolBar();

		load = new JButton();
		load.setIcon(new ImageIcon("resources/icons/open.png")); // Colocar el icono
		load.setToolTipText("Cargar."); // Texto al poner el ratón encima

		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "txt");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources"));
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(filter);
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					_ctrl.reset();
					try {
						_ctrl.loadBodies(new FileInputStream(fileChooser.getSelectedFile()));
					} catch (FileNotFoundException | JSONException e) {
					//	throw new IllegalArgumentException("Invalid file.");
						JOptionPane.showMessageDialog(null, "Error de fichero", "Error", JOptionPane.ERROR_MESSAGE); 
					}
				}
			}
		});

		gravityLaws = new JButton();
		gravityLaws.setToolTipText("Leyes."); // Texto al poner el ratón encima
		gravityLaws.setIcon(new ImageIcon("resources/icons/physics.png")); // Colocar el icono
		gravityLaws.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				List<JSONObject> infoLeyes = _ctrl.getGravityLawsFactory().getInfo();

				boolean encontrado = false;
				int j = 0;
				String[] leyes = new String[infoLeyes.size()];
				ArrayList<String> leyes2 = new ArrayList<String>();

				for (int i = 0; i < leyes.length; i++)
					if (!leyes2.contains(infoLeyes.get(i).getString("desc")))
						leyes2.add(infoLeyes.get(i).getString("desc"));

				String descripcion = (String) JOptionPane.showInputDialog(null, "Seleccionar ley", "Gravity Laws",
						JOptionPane.DEFAULT_OPTION, null, leyes2.toArray(), leyes2.toArray()[0]);

				while (j < leyes2.toArray().length && !encontrado) {
					if (((String) leyes2.toArray()[j]).equalsIgnoreCase(descripcion)) {
						_ctrl.setGravityLaws(infoLeyes.get(j));
						encontrado = true;
					}
					j++;
				}
			}

		});

		run = new JButton();
		run.setToolTipText("Ejecutar simulador."); // Texto al poner el ratón encima
		run.setIcon(new ImageIcon("resources/icons/run.png")); // Colocar el icono
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_ctrl.setDeltaTime(Double.parseDouble(ControlPanel.this.dTime.getText()));
				botones(false);
				crearHilo();
			}
		});

		stop = new JButton();
		stop.setToolTipText("Parar simulador."); // Texto al poner el ratón encima
		stop.setIcon(new ImageIcon("resources/icons/stop.png")); // Colocar el icono
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (hilo != null) {

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							botones(true);
							if (hilo != null)
								hilo.interrupt();
							hilo = null;
						}
					});
				}
			}
		});

		steps = new JSpinner();
		steps.setValue(10000);
		
		// Aumentar el tama�o del JSpinner:
		Component mySpinnerEditor = steps.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(5);

		dTime = new JTextField("10000", 10);

		delay = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		
		
		toolbar.add(load);
		toolbar.addSeparator(new Dimension(10, 10));
		toolbar.add(gravityLaws);
		toolbar.addSeparator(new Dimension(10, 10));
		toolbar.add(run);
		toolbar.add(stop);
		toolbar.addSeparator(new Dimension(10, 10));
		toolbar.add(new JLabel("Delay: "));
		toolbar.add(delay);
		toolbar.addSeparator(new Dimension(10, 10));
		toolbar.add(new JLabel("Steps: "));
		toolbar.add(steps);
		toolbar.addSeparator(new Dimension(10, 10));
		toolbar.add(new JLabel("Delta Time: "));
		toolbar.add(dTime);
		

		load.setAlignmentX(Component.LEFT_ALIGNMENT);

		return toolbar;
	}

	protected void run_sim(int n, int retraso) {

		while (n > 0 && !Thread.interrupted()) 
		{	try {
				_ctrl.run(1);
				if(retraso > 0)
					Thread.sleep(retraso);
			} catch (InterruptedException e) {
				botones(true);
				return;
			}
			catch (Exception e2) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null, "Error de ejecucion", "Error", JOptionPane.OK_OPTION);
						botones(true);
						return;
					}
				});
			}
			--n;
		}
		botones(true);
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dTime.setText(Double.toString(dt));
			}
		});

	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dTime.setText(Double.toString(dt));
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body body) {
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dTime.setText("" + dt);
			}
		});
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
	}

	public void crearHilo() {
		hilo = new HiloNuestro(this, Integer.parseInt(delay.getValue().toString()),
				Integer.parseInt(steps.getValue().toString()));
		botones(false);
		hilo.start();
	}

	private void botones(boolean habilitado) {

		load.setEnabled(habilitado);
		run.setEnabled(habilitado);
		gravityLaws.setEnabled(habilitado);
		exit.setEnabled(habilitado);
		steps.setEnabled(habilitado);
		dTime.setEnabled(habilitado);
		delay.setEnabled(habilitado);
	}
}