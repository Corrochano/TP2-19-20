package simulator.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String,Junction> junctionMap;
	private Map<String,Road> roadMap;
	private Map<String,Vehicle> vehicleMap;
	
	protected RoadMap() {
		this.junctionList = new ArrayList<Junction>();
		this.roadList = new ArrayList<Road>();
		this.vehicleList = new ArrayList<Vehicle>();
		this.junctionMap = new HashMap<String,Junction>();
		this.roadMap = new HashMap<String,Road>();
		this.vehicleMap = new HashMap<String,Vehicle>();
	}
	
	protected void addJunction(Junction j) {
		if(this.junctionMap.containsValue(j)) {
			throw new InvalidParameterException("The junction is already in the junction map.");
		}
		
		this.junctionList.add(j);
		this.junctionMap.put(j.getId(), j);
	}
	
	protected void addRoad(Road r) {
		if(this.roadMap.containsValue(r)) {
			throw new InvalidParameterException("The road is already in the road map.");
		}
		
		if(!this.junctionMap.containsValue(r.getSourceJunction())
				||  !this.junctionMap.containsValue(r.getDestinationJunction())) {
			throw new InvalidParameterException("The source junction and the destination junction must exits in the road map.");
		}
		
		this.roadList.add(r);
		this.roadMap.put(r.getId(), r);
	}
	
	protected void addVehicle(Vehicle v) {
		if(this.vehicleMap.containsValue(v)) {
			throw new InvalidParameterException("The vehicle is already in the map.");
		}
		
		List<Junction> aux = v.getItinerary();
		
		for(int i = 0; i < aux.size() - 1; i++) {
			if(aux.get(i).roadTo(aux.get(i + 1)) == null) {
				throw new InvalidParameterException("Invalid vehicle itinerary.");
			}
		}
		
		this.vehicleList.add(v);
		this.vehicleMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id){
		return this.junctionMap.get(id);
	}
	
	public Road getRoad(String id){
		return this.roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return this.vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(new ArrayList<>(this.junctionList));
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(new ArrayList<>(this.roadList));
	}
	
	public List<Vehicle> getVehicles(){
		return  Collections.unmodifiableList(new ArrayList<>(this.vehicleList));
	}
	
	protected void reset() {
		this.junctionList.clear();
		this.junctionMap.clear();
		this.roadList.clear();
		this.roadMap.clear();
		this.vehicleList.clear();
		this.vehicleMap.clear();
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		
		JSONArray ja = new JSONArray();
		 
		 json.put("junctions", ja);
		 
		 for(Junction j : this.junctionList) {
			 ja.put(j.report());
		 }
		 
		 JSONArray ra = new JSONArray();
		 json.put("roads", ra);
		 
		 for(Road r : this.roadList) {
			 ra.put(r.report());
		 }
		 
		 JSONArray va = new JSONArray();
		 json.put("vehicles", va);
		 
		 for(Vehicle v : this.vehicleList) {
			 va.put(v.report());
		 }
		 
		 return json;
	}
	
}
