package simulator.model;

import java.util.List;

public interface GravityLaws {
	
	
	public void apply(List<Body>bodies);
	final double G = -6.67E-11;
	
}