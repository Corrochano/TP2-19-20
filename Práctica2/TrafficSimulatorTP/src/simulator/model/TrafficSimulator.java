package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	private RoadMap rMap;
	private List<Event> eventList;
	private int time;
	private List<TrafficSimObserver> observerList; // ¿?
	
	public TrafficSimulator() {
		this.rMap = new RoadMap();
		this.eventList = new SortedArrayList<Event>();
		this.time = 0;
		this.observerList = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		try {
			eventList.add(e);
			this.notify_OnEventAdded(e);
		}
		catch(Exception x){
			this.notify_OnError(x.getMessage());
			// x.printStackTrace(); 
		}
	}
	
	public void advance() {
		
		try {
		
			// 1
			
			this.time++;
			
			this.notify_OnAdvanceStart();
			
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
			
			this.notify_OnAdvanceEnd();
		} 
		catch(Exception e) {
			this.notify_OnError(e.getMessage());
			// e.printStackTrace(); 
		}
		
	}
	
	public void reset() {
		this.rMap.reset();
		this.eventList.clear();
		this.time = 0;
		this.notify_OnReset();
	}
	
	public JSONObject report(){
		JSONObject json = new JSONObject();
		
		 json.put("time", this.time);
		 
		 JSONObject rmapJSON = this.rMap.report();
		 
		 json.put("state", rmapJSON);
		
		return json;
	}

	@Override
	public void addObserver(TrafficSimObserver o) { 
		try {
			this.observerList.add(o);
			this.notify_OnRegister();
		}
		catch(Exception e){
			this.notify_OnError("Impossible to add this observer.");
			// e.printStackTrace(); 
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
//		
		
		if(this.observerList.indexOf(o) == -1) {
			throw new NullPointerException("The observer isn't in the list.");
		}
		
		this.observerList.remove(o);
	}
	
	public void notify_OnAdvanceStart() {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onAdvanceStart(this.rMap, this.eventList, this.time);
		}
	}
	

	public void notify_OnAdvanceEnd() {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onAdvanceEnd(this.rMap, this.eventList, this.time);
		}
	}
	
	public void notify_OnEventAdded(Event e) {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onEventAdded(this.rMap, this.eventList, e, this.time);
		}
	}
	
	public void notify_OnReset() {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onReset(this.rMap, this.eventList, this.time);
		}
	}
	
	public void notify_OnRegister() {
//		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onRegister(this.rMap, this.eventList, this.time);
		}
	}
	
	public void notify_OnError(String msg) {
		
//		if(this.observerList.isEmpty()) {
//			throw new NullPointerException("The observerList is empty.");
//		}
		
		for(TrafficSimObserver o : this.observerList) {
			o.onError(msg);
		}
	}
	
}
