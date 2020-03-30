package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray info = data.getJSONArray("info");
		List<Pair<String,Integer>> cs = new ArrayList<Pair<String,Integer>>();
		
		for(int i = 0; i < info.length(); i++) {
			cs.add(new Pair<String,Integer>(info.getJSONObject(i).getString("vehicle"), 
					info.getJSONObject(i).getInt("class")));
		}
		
		return new NewSetContClassEvent(time, cs);
	}

}

// int time, List<Pair<String,Integer>> cs