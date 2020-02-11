package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory <T> implements Factory<T> {

	List <Builder<T>> builderList;
	List <JSONObject> factoryElements;
	
	public BuilderBasedFactory(List <Builder<T>> builderList)
	{
		this.builderList = new ArrayList<Builder<T>>(builderList);
		this.factoryElements = new ArrayList<JSONObject>();
		
	}
	
	public T createInstance(JSONObject info)
	{
		for(Builder<T> b : builderList)
		{
			T build = b.createInstance(info);
			if(build != null)
				return build;
		}
		throw new IllegalArgumentException("Argumentos invalidos.");
	}
	
	public List <JSONObject> getInfo()
	{
		factoryElements.clear();
		for (Builder<T> b : builderList)
		{
			factoryElements.add(b.getBuilderInfo());	
		}
		
		return factoryElements;
	}
}