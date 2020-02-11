 package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLossingBodyBuilder extends Builder <Body> {
	
	public static final String type = "mlb";
	public static final String description = "Mass losing body";
	
	public MassLossingBodyBuilder()
	{
		typeTag = type;
		desc = description;
	}
	
	protected Body createTheInstance(JSONObject json) 
	{
		try {
			String type = json.getString("type");

			JSONObject data = json.getJSONObject("data");
			
			String id = data.getString("id");
			
			
			double[] posContent = jsonArrayToDoubleArray(data.getJSONArray("pos"));
			
			Vector posicion = new Vector(posContent);
			
			double[] velContent = this.jsonArrayToDoubleArray(data.getJSONArray("vel"));
			
			Vector velocidad = new Vector(velContent);
			double masa = data.getDouble("mass");
			double frecuencia = data.getDouble("freq");
			double factor = data.getDouble("factor");
			double[] aceleracion = {0,0};
			Vector vAceleracion = new Vector(aceleracion);
			
			
			if(type.equalsIgnoreCase(typeTag))
				return new MassLossingBody(id,velocidad,vAceleracion, posicion, masa, frecuencia, factor);
			else
				return null;
			
		} catch(JSONException e) {
			throw new IllegalArgumentException("Datos incorrectos");
		}
	}
	
	protected JSONObject createData()
	{
		JSONObject obj = new JSONObject();
		
		obj.put("id", "El identificador");
		obj.put("pos","La posicion del vector");
		obj.put("vel", "La velocidad");
		obj.put("mass", "La masa");
		obj.put("freq", "La frecuencia de perdida");
		obj.put("factor", "El factor de perdida");
		
		return obj;
	}
}