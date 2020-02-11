package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 5380981052872049157L;
	private List<Body> _bodies;
	private final int columnas = 5;

	public BodiesTableModel(Controller ctrl) {
		
		this._bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				//_bodies = new ArrayList<>(bodies);
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});

		
	}

	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		 
	}

	public void onBodyAdded(List<Body> bodies, Body b) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		 
	}

	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		 
	}

	@Override
	public int getColumnCount() {
		return this.columnas;
	}

	@Override
	public int getRowCount() {
		return this._bodies.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1)
	{
		switch (arg1) {

		case 0:
			return _bodies.get(arg0).getId();

		case 1:
			return _bodies.get(arg0).getMass();

		case 2:
			return _bodies.get(arg0).getPosition();

		case 3:
			return _bodies.get(arg0).getVelocity();

		case 4:
			return _bodies.get(arg0).getAcceleration();

		}

		return null;

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getColumnName(int column) {

		switch (column) {

		case 0:
			return "id";

		case 1:
			return "mass";

		case 2:
			return "position";

		case 3:
			return "velocity";

		case 4:
			return "Acceleration";

		}

		return null;
	}
}
