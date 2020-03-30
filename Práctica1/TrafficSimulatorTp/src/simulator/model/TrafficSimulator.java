package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	private RoadMap rMap;
	private List<Event> eventList;
	private int time;
	
	public TrafficSimulator() {
		this.rMap = new RoadMap();
		this.eventList = new SortedArrayList<Event>();
		this.time = 0;
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
	}
	
	public void advance() {
		
		// 1
		
		this.time++;
		
		// 2
		
		while (this.eventList.size() > 0 && this.eventList.get(0).getTime() == this.time) {
			   this.eventList.remove(0).execute(this.rMap);
		}
		
		
//		for(Event e : this.eventList) {
//			if(e.getTime() == this.time) {
//				e.execute(this.rMap);
//				this.eventList.remove(e);
//			}
//		}
		
		// 3
		
		for(Junction j : this.rMap.getJunctions()) {
			j.advance(this.time);
		}
		
		// 4
		
		for(Road r : this.rMap.getRoads()) {
			r.advance(this.time);
		}
		
	}
	
	public void reset() {
		this.rMap.reset();
		this.eventList.clear();
		this.time = 0;
	}
	
	public JSONObject report(){
		JSONObject json = new JSONObject();
		
		 json.put("time", this.time);
		 
		 JSONObject rmapJSON = this.rMap.report();
		 
		 json.put("state", rmapJSON);
		
		return json;
	}
	
}
