package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws> {

	public static final String type = "ftcg";
	public static final String description = "Falling to center gravity law";
	
	public FallingToCenterGravityBuilder()
	{
		typeTag = type;
		desc = description;
	}	
	protected GravityLaws createTheInstance(JSONObject json)
	{
		try {
			return new FallingToCenterGravity();
			
		} catch (JSONException e) {
			throw new IllegalArgumentException("Unable");
		}
	}
}