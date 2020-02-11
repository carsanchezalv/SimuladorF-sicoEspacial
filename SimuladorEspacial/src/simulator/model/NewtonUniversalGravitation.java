package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws
{
	
	public static final String nombre = "Newton Universal Gravity Law";
	
	@Override
	public void apply(List<Body> bodies)
	{
		for(Body one : bodies)
		{
			Vector fuerza = new Vector(one.getPosition().dim());
			
			if(one.getMass() > 0.0)
			{	
				for(Body two : bodies)
				{
					if(!one.equals(two))
						fuerza = fuerza.plus(force(one, two));
					
				}
				Vector prod = fuerza.scale(1 / one.getMass());
				one.setAcceleration(prod);
			}
			else
			{
				// Se pone a 0
				one.setAcceleration(new Vector(one.getAcceleration().dim()));
				one.setVelocity(new Vector(one.getVelocity().dim()));
			}
		}
	}
	private Vector force(Body one, Body two)
	{
		Vector fuerza = null;
		Vector direccion = null;
		double distancia;
		double f;
		
		distancia = two.getPosition().distanceTo(one.getPosition());
		
		if(distancia < 0)
			distancia *= -1;
		
		f = G* ((one.getMass() * two.getMass()) / (distancia*distancia));

		direccion = (one.getPosition().minus(two.getPosition())).direction();

		fuerza = direccion.scale(f);
		
		return fuerza;
	}
	
	public String toString() {
		return nombre;
	}
}