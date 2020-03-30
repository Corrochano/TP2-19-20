package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event> {

	public NewCityRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int length = data.getInt("length");
		int co2limit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");
		String w = data.getString("weather");
		Weather weather = Weather.valueOf(w);
		
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}

}

// (int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather)