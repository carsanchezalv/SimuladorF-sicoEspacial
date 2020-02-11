package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder <T> {

	protected String typeTag;
	protected String desc;
	
	protected double[] jsonArrayToDoubleArray(JSONArray json)
	{
		double[] array = new double[json.length()];
		
		for(int i = 0; i < json.length(); ++i) // Bucle para la copia
		{
			array[i] = json.getDouble(i);
		}
		return array;
	}
	
	public T createInstance(JSONObject json)
	{
		T b = null;
		if (typeTag != null && typeTag.equals(json.getString("type")))
		{
			b = createTheInstance(json);
		}
		return b;
	}
	
	protected abstract T createTheInstance(JSONObject json);
	
	public JSONObject getBuilderInfo()
	{
		JSONObject obj = new JSONObject();
		
		obj.put("type", typeTag);
		obj.put("data", createData());
		obj.put("desc", desc);
		
		return obj;
	}
	
	protected JSONObject createData() // Lo tienen tambien los cuerpos builder, pero no las leyes. En los objetos lo sobreescribe
	{
		return new JSONObject(); // Vacío
	}
}