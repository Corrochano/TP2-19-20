package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator trafficSimulator;
	private Factory<Event> eFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		
		if(sim == null || eventsFactory == null) {
			throw new NullPointerException("Traffic Simulator and Event Factory can't be null.");
		}
		
		this.trafficSimulator = sim;
		this.eFactory = eventsFactory;
	}

	public void loadEvents(InputStream in){
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray eventList = jo.getJSONArray("events");
		
		if(eventList == null) {
			throw new NullPointerException("Invalid event list format.");
		}
		
		for(int i = 0; i < eventList.length(); i++) {
			this.trafficSimulator.addEvent(this.eFactory.createInstance(eventList.getJSONObject(i))); // OJO CUIDAO 
		}
	}
	
	public void run(int n, OutputStream out){
		
		PrintStream p = new PrintStream(out);
		p.print("{\n  \"states\": [\n"); 
		for(int i = 0; i < n; i++) {
			this.trafficSimulator.advance();
			p.print(this.trafficSimulator.report());
			if(i != n - 1) {
				p.print(", \n");
			}
		}
		
		p.print("\n] \n}");
		
	}
	
	public void run(int n) {
		for(int i = 0; i < n; i++) {
			this.trafficSimulator.advance();
		}
	}
	
	public void reset() {
		this.trafficSimulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		this.trafficSimulator.addObserver(o);
	}
	
	protected void removeObserver(TrafficSimObserver o){
		this.trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e){
		this.trafficSimulator.addEvent(e);
	}
	
}
