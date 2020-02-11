package simulator.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.SimulatorObserver;

public class Controller {
	
	public PhysicsSimulator sim;
	Factory<Body> bodiesFactory;
	

	private Factory<GravityLaws> laws;
	
	public Controller(PhysicsSimulator sim, Factory<Body>bodiesFactory,  Factory<GravityLaws> laws)
	{
		this.sim = sim;
		this.bodiesFactory = bodiesFactory;
		this.laws = laws;
	}
	
	public void loadBodies(InputStream in)
	{
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		
		for (int i = 0; i < bodies.length(); i++)
		{	
			sim.addBody(bodiesFactory.createInstance(bodies.getJSONObject(i)));
		}
	}
	
	public void run(int steps, OutputStream out)
	{
		if(out == null)
			run(steps);
		
		StringBuilder aux1 = new StringBuilder();
		aux1.append("{\n\"states\": [\n");
		
		StringBuilder aux3 = new StringBuilder();
		
		aux3.append(sim.toString());
		aux3.append(",\n");
		
		for(int i = 0; i < steps - 1; i++)
		{
			sim.advance();
			aux3.append(sim.toString());
			aux3.append(",\n");
		}
		
		sim.advance();
		aux3.append(sim.toString());
		aux3.append("\n]\n}");
		aux1.append(aux3);
		
		try {
			
			Writer escritor = new BufferedWriter(new OutputStreamWriter(out));
			escritor.write(aux1.toString());
		   
		   	escritor.close();
			 
		} catch(IOException e)
		{
			System.out.println("Error outputstream");
		}
	}
	
	public void run(int steps) {
		for(int i = 0; i < steps; ++i)
			sim.advance();
	}

	public void addObserver(SimulatorObserver obs) {
		// TODO Auto-generated method stub
		sim.addObserver(obs);
	}

	public Factory<GravityLaws> getGravityLawsFactory() {
		return laws;
	}

	public void setGravityLaws(JSONObject law) {
		GravityLaws leyes = laws.createInstance(law);
		sim.setGravityLaws(leyes);
	}

	public void setDeltaTime(double parseDouble) {
		this.sim.setDeltaTime(parseDouble);
	}

	public void reset() {
		this.sim.reset();
	}
	public Factory<Body> getBodiesFactory()
	{
		return this.bodiesFactory;
	}
}