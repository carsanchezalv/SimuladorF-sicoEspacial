package simulator.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;

public class Viewer extends JPanel implements SimulatorObserver {
/**
	 * 
	 */
	private static final long serialVersionUID = 7176703575939873077L;
	// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;

	public Viewer(Controller ctrl) {
		super();
		initGUI();
		ctrl.addObserver(this);
		
	}

	private void initGUI()
	{
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
															"View", TitledBorder.LEFT, TitledBorder.TOP));
		
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		addKeyListener(new KeyListener() {
			// ...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+':
					_scale = Math.max(1.0, _scale / 1.1);
					break;
				case '=':
					autoScale();
					break;
				case 'h':
					_showHelp = !_showHelp;
					break;
				default:
				}
				repaint();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		addMouseListener(new MouseListener() {
			// ...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});    	
    	
    	this.add(new JScrollPane());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
				
		gr.setColor(Color.red);
		
		gr.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		gr.drawString("+", _centerX - 10, _centerY + 15);
		gr.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		gr.setColor(Color.blue);
		
		if (this._showHelp)
		{
			long x = Math.round(this.getBounds().getMinX());
			long y = Math.round(this.getBounds().getMinY());
			
			
			gr.setColor(Color.RED);
			gr.drawString("h: toggle help, +: zoom-in, -: zoom-out, =: fit", x + 8, y / 7);
			gr.drawString("Scaling ratio: " + this._scale, x + 8, y / 5);
		}

		
		for(Body b : _bodies)
		{
			int equis = (int) Math.round(_centerX + (b.getPosition().coordinate(0) / this._scale) - 4);
			int igriega = (int) Math.round(_centerY + (b.getPosition().coordinate(1) / this._scale) - 4);
			
			gr.setColor(Color.BLUE);
			gr.fillOval(equis, igriega, 10, 10);
			gr.setColor(Color.BLACK);
			gr.drawString(b.getId(), equis, igriega - 10);			
		}
	}

	
	private void autoScale() 
	{
		double max = 1.0;
		for (Body b : _bodies)
		{
			Vector p = b.getPosition();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max, Math.abs(b.getPosition().coordinate(i)));
		}
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {				
				_bodies = bodies;
				autoScale();
				repaint();			
			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				//_bodies.clear();
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body body) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
			
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				repaint();
			}
		});
	}

	@Override
	public void onDeltaTimeChanged(double dt) {	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {	}
}