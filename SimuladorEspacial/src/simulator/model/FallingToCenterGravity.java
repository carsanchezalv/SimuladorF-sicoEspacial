package simulator.model;

import java.util.List;

public class FallingToCenterGravity implements GravityLaws
{
	
	public static final String nombre = "Falling to center gravity law";
	
	private double gravity = -9.81;
	
	public void apply(List<Body> bodies)
	{
		 for(Body one : bodies)
		 {
			 one.setAcceleration(one.getPosition().direction().scale(gravity));
		 }
	}
	
	public String toString() {
		return nombre;
	}
}