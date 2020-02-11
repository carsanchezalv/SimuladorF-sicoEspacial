package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {

	private double lossFactor; // Entre 0 y 1
	private double lossFrequency; // Intervalo de tiempo
	private double c;
	
	public MassLossingBody(String id, Vector velocidad, Vector aceleracion, Vector posicion, double masa, double frecuencia, double factor)
	{
		super(id, velocidad, aceleracion, posicion, masa);
		this.lossFactor = factor;
		this.lossFrequency = frecuencia;
		c = 0.0;
	}
	
	public void move(double t)
	{
		super.move(t);
		
		c += t;
		if(c >= lossFrequency)
		{
			c = 0.0;
			masa *= (1 - lossFactor);
		}
		
		
	}
}