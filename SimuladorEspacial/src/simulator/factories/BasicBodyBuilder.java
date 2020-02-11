package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder <Body> {
	
	public static final String type = "basic";
	public static final String description = "Default body";
	public BasicBodyBuilder()
	{
		typeTag = type;
		desc = description;
	}
	@Override
	protected Body createTheInstance(JSONObject json)
	{
		try {
			JSONObject object = json.getJSONObject("data");
			
			String id = object.getString("id");
			
			double[] posContent = this.jsonArrayToDoubleArray(object.getJSONArray("pos"));
			
			Vector pos = new Vector(posContent);
			
			double[] velContent = this.jsonArrayToDoubleArray(object.getJSONArray("vel"));
			
			Vector vel = new Vector(velContent);
			
			double mass = object.getDouble("mass");
			
			double[] ceros = {0,0};
			Vector aceleracion = new Vector(ceros);
			
			return new Body(id,vel,aceleracion,pos,mass);
			
		} catch(JSONException e) {
			throw new IllegalArgumentException("Datos no validos.");
		}
	}
	protected JSONObject createData()
	{
		JSONObject data = new JSONObject();
		
		data.put("id", "El identificador");
		data.put("pos", "La posicion del vector");
		data.put("vel", "La velocidad del vector");
		data.put("mass", "La masa del cuerpo");
		
		return data;
	}
}