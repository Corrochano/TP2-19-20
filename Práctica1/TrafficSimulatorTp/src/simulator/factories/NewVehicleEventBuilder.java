package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		JSONArray JSONItinerary = data.getJSONArray("itinerary");
		
		List<String> itinerary = new ArrayList<String>();
		
		for(int i = 0; i < JSONItinerary.length(); i++) {
			itinerary.add(JSONItinerary.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}

// int time, String id, int maxSpeed, int contClass, List<String> itinerary