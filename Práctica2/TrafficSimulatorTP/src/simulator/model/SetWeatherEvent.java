package simulator.model;

import java.security.InvalidParameterException;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws.isEmpty()) {
			throw new InvalidParameterException("The pair can't be empty.");
		}
		
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		
		for(Pair<String,Weather> w : this.ws) {
			if(map.getRoad(w.getFirst()) != null) {
				map.getRoad(w.getFirst()).setWeather(w.getSecond());
			}
			else {
				throw new NullPointerException("Road isn't in the road map");
			}
		}
	}
	
	@Override
	public String toString() {
		String s;
		s = "Set Weather";
		
		for(Pair<String, Weather> p : this.ws) {
			s += " '";
			s += p.getFirst();
			s += " ";
			s += p.getSecond().toString();
			s += "'";
		}
		
		s += "'";
		return s;
	}

}
