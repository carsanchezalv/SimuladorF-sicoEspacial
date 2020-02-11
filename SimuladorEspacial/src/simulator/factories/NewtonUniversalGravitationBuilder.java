package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws> {
	
	public static final String type = "nlug";
	public static final String description = "Newton universal gravitation";
	
	public NewtonUniversalGravitationBuilder()
	{
		typeTag = type;
		desc = description;
	}
	
	@Override
	protected GravityLaws createTheInstance(JSONObject json)
	{
		try {
			return new NewtonUniversalGravitation();
				
		} catch (JSONException e) {
			throw new IllegalArgumentException("Unable");
		}
	}	
}