package simulator.model;

import simulator.misc.Vector;

public class Body {
	
	protected String id;
	protected Vector velocidad;
	protected Vector aceleracion;
	protected Vector posicion;
	protected double masa;
	
	public Body(String id, Vector velocidad, Vector aceleracion, Vector posicion, double masa)
	{
		this.id = id;
		this.velocidad = velocidad;
		this.aceleracion = aceleracion;
		this.posicion = posicion;
		this.masa = masa;
	}
	
	public void move(double t)
	{
		double aux = t * t;
		double aux2 = aux * 0.5;
		posicion = posicion.plus(velocidad.scale(t)).plus(aceleracion.scale(aux2));
		velocidad = velocidad.plus(aceleracion.scale(t));
	}
	
	public String toString() 
	{
		StringBuilder constructor = new StringBuilder();
		constructor.append("{  \"id\": ");
		constructor.append("\"");
		constructor.append(this.id);
		constructor.append("\"");
		constructor.append(", \"mass\": ");
		constructor.append(this.masa);
		constructor.append(", \"pos\": ");
		constructor.append(this.posicion);
		constructor.append(", \"vel\": ");
		constructor.append(this.velocidad);
		constructor.append(", \"acc\": ");
		constructor.append(this.aceleracion);
		constructor.append(" }");
		
		return constructor.toString();
	}
	
	public Vector getVelocity()
	{
		return new Vector(velocidad);
	}
	
	public void setVelocity(Vector v)
	{
		Vector nuevaVel = new Vector(v);
		this.velocidad = nuevaVel;
	}
	
	public Vector getAcceleration()
	{
		return new Vector(aceleracion);
	}
	
	public void setAcceleration(Vector a)
	{
		Vector nuevaAcc = new Vector(a);
		this.aceleracion = nuevaAcc;
	}
	
	public Vector getPosition()
	{
		return new Vector(posicion); // Asi con todos, sirve para coger una copia y no el objeto en sí, para no poder modificarlo
	}
	
	public void setPosition(Vector p)
	{
		Vector nuevaPos = new Vector(p);
		this.posicion = nuevaPos;
	}
	
	public String getId()
	{
		return id;
	}
	
	public double getMass()
	{
		return masa;
	}
	
	public boolean equals(Body body)
	{
		if(this.id == body.id)
			return true;
		else
			return false;
	}
}