package simulator.model;

import java.util.List;

public class NoGravity implements GravityLaws {
	
	public static final String nombre = "No Gravity law";
	
	
	
	@Override
	public void apply(List<Body> bodies) { }
	
	
	public String toString() {
		return nombre;
	}
}