package simulator.view;

public class HiloNuestro extends Thread {

	private ControlPanel panel;
	private int delay;
	private int n;

	public HiloNuestro (ControlPanel panel, int delay, int n) {
		this.panel = panel;
		this.delay = delay;
		this.n = n;
	}
	public void run() {
		panel.run_sim(n, delay);
	}
}