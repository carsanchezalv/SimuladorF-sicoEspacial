package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.view.SimulatorObserver;

public class PhysicsSimulator {

	private GravityLaws laws;
	private double time;
	private double tiempoParcial;
	private List<Body> bodies;
	private List<SimulatorObserver> obs;

	public PhysicsSimulator(GravityLaws laws, double tiempoParcial) throws IllegalArgumentException {
		if (tiempoParcial <= 0)
			throw new IllegalArgumentException("Tiempo parcial no valido.");
		if (laws == null)
			throw new IllegalArgumentException("Leyes no validas.");

		this.laws = laws;
		this.time = 0.0;
		this.tiempoParcial = tiempoParcial;
		this.bodies = new ArrayList<Body>();
		this.obs = new ArrayList<>();
	}

	public void advance() {
		laws.apply(bodies);
		for (Body body : bodies) {
			body.move(tiempoParcial);
		}

		time += tiempoParcial;

		for (SimulatorObserver obs : this.obs) {
			obs.onAdvance(bodies, time);
		}
	}

	public void reset() {
		this.bodies.clear();
		this.time = 0;

		for (SimulatorObserver o : this.obs) {
			o.onReset(bodies, time, tiempoParcial, laws.toString());
		}
	}

	public void setDeltaTime(double dt) {
		if (tiempoParcial <= 0)
			throw new IllegalArgumentException("Wrong delta time.");

		tiempoParcial = dt;

		for (SimulatorObserver o : this.obs) {
			o.onDeltaTimeChanged(dt);
		}
	}

	public void setGravityLaws(GravityLaws gravityLaws) {
		if (gravityLaws == null)
			throw new IllegalArgumentException("Wrong law.");

		this.laws = gravityLaws;

		for (SimulatorObserver o : this.obs) {
			o.onGravityLawChanged(laws.toString());
		}
	}

	public void addBody(Body body) {
		if (bodies.contains(body))
			throw new IllegalArgumentException("Cuerpo repetido");

		bodies.add(body);
		
		for (SimulatorObserver o : this.obs)
			o.onBodyAdded(bodies, body);
	}

	public String toString() {
		StringBuilder primero = new StringBuilder();
		primero.append("{ \"time\": ");
		primero.append(this.time);
		primero.append(", \"bodies\": [ ");

		StringBuilder segundo = new StringBuilder();

		for (int i = 0; i < bodies.size() - 1; ++i) {
			segundo.append(bodies.get(i).toString());
			segundo.append(", ");
		}

		segundo.append(bodies.get(bodies.size() - 1).toString());
		segundo.append(" ] }");

		primero.append(segundo);

		return primero.toString();
	}

	public void addObserver(SimulatorObserver observer) {
		if (!obs.contains(observer)) {
			this.obs.add(observer);
			observer.onRegister(bodies, time, tiempoParcial, laws.toString());
		}

	}

}