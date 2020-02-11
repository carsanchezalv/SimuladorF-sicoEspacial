package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder <GravityLaws> {

	public static final String type = "ng";
	public static final String description = "No gravity law";
	
	public NoGravityBuilder()
	{
		typeTag = type;
		desc = description;
	}
	protected GravityLaws createTheInstance(JSONObject json)
	{
		try {
			return new NoGravity();
			
		} catch (JSONException e) {
			throw new IllegalArgumentException("No se ha podido");
		}
	}
}
