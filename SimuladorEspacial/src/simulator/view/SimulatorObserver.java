package simulator.view;

import java.util.List;

import simulator.model.Body;

public interface SimulatorObserver {
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc);
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc);
	public void onBodyAdded(List<Body> bodies, Body body);
	public void onAdvance(List<Body> bodies, double time);
	public void onDeltaTimeChanged(double dt);
	public void onGravityLawChanged(String gLawsDesc);
}