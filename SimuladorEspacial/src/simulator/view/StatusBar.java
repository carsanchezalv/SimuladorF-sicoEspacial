package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;

public class StatusBar extends JPanel implements SimulatorObserver {


	private static final long serialVersionUID = -7672898771810657361L;
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies

	StatusBar(Controller ctrl)
	{
		super();
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		this._currTime = new JLabel("Time: 0.0 \t");
		this._currLaws = new JLabel("Laws: Undefined \t");
		this._numOfBodies = new JLabel("Bodies: 0 \t");
		
		// Para que se ajuste el tiempo al tama�o del status bar
		JPanel tiempo = new JPanel();
		tiempo.setLayout(new BorderLayout());
		tiempo.setPreferredSize(new Dimension(100, 20));
		tiempo.add(_currTime);
		
		this.add(tiempo);
		this.add(createSeparator());
		
		this.add(_numOfBodies);
		this.add(createSeparator());
		
		this.add(_currLaws);
		
		this.setPreferredSize(new Dimension(700, 30));
	}

	private JSeparator createSeparator()
	{
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		Dimension d = separator.getPreferredSize();
		d.height = _currTime.getPreferredSize().height; // Obtengo la altura de uno de los Label (todas sus alturas son iguales)

		separator.setPreferredSize(d);
		
		return separator;
	}
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				
				_currTime.setText("Time: " + Double.toString(time) + "\t");
				_numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()) + "\t");
				_currLaws.setText("Laws: " + gLawsDesc + "\t");
				
			}
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				_numOfBodies.setText("Bodies: " + bodies.size() + "\t");
				_currTime.setText("Time: " + time + "\t");
				_currLaws.setText("Laws: " + gLawsDesc + "\t");
				
			}
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body body)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				_numOfBodies.setText("Bodies: " + bodies.size() + "\t");
				
			}
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time)
	{
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				_currTime.setText("Time: " + time + "\t");
				
			}
		});
		
	}

	@Override
	public void onDeltaTimeChanged(double dt)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc)
	{
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				_currLaws.setText("Laws: " + gLawsDesc + "\t");
				repaint();
				
			}
		});
	
	}
}