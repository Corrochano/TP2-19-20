package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray info = data.getJSONArray("info");
		List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
		
		for(int i = 0; i < info.length(); i++) {
			ws.add(new Pair<String,Weather>(info.getJSONObject(i).getString("road"), 
					Weather.valueOf(info.getJSONObject(i).getString("weather"))));
		}
		
		return new SetWeatherEvent(time, ws);
	}

}

// int time, List<Pair<String,Weather>> ws