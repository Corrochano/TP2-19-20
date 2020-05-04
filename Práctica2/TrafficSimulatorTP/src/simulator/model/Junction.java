package simulator.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{

	private List<Road> entList; 
	private Map<Junction,Road> extList;
	private List<List<Vehicle>> colaList;
	private Map <Road, List<Vehicle>> roadList;
	private int indSem;
	private int lastSemChang;
	private LightSwitchingStrategy semEst;
	private DequeuingStrategy qEst;
	@SuppressWarnings("unused")
	private int x;
	@SuppressWarnings("unused")
	private int y;
	
	protected Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) {
			super(id);
			
			if(lsStrategy == null || dqStrategy == null) {
				throw new NullPointerException("The light switching strategy and dequeing strategy can't be null.");
			}
			
			if(x < 0 || y < 0) {
				throw new InvalidParameterException("x and y can't be negative numbers.");
			}
			
			this.semEst = lsStrategy;
			this.qEst = dqStrategy;
			this.x = xCoor;
			this.y = yCoor;
			this.entList = new ArrayList<Road>();
			this.extList = new HashMap<Junction,Road>();
			this.roadList = new HashMap<Road, List<Vehicle>>();
			this.indSem = -1;
			this.lastSemChang = 0;
			this.colaList = new ArrayList<List<Vehicle>>();
	}

	@Override
	void advance(int time) {
		// 1
		if(this.indSem != -1) {
			if(!this.colaList.get(this.indSem).isEmpty()) {
				List<Vehicle> vMove = this.qEst.dequeue(colaList.get(indSem)); // ??
				for(Vehicle v : vMove) {
					Road r = v.getRoad(); // OJO
					v.moveToNextRoad();
					if(v.getStatus() != VehicleStatus.ARRIVED) {
						if(v.getRoad().getId() != r.getId()) {
							this.roadList.get(r).remove(v);
						}
					}
					else {
						this.colaList.get(this.indSem).remove(v);
					}
				}
			}
		}
		// 2
		int newIndSem = this.semEst.chooseNextGreen(this.entList, this.colaList, this.indSem, this.lastSemChang, time);
		if(newIndSem != this.indSem) {
			this.indSem = newIndSem;
			this.lastSemChang = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		 
		 json.put("id", this._id);
		 if(this.indSem == -1) {
			 json.put("green", "none");
			}
		 else {
			 json.put("green", this.entList.get(this.indSem).getId());
		 }
		 
		 JSONArray qs = new JSONArray();
		 
		 json.put("queues", qs); // Bucle jsonarray relleno de jason object
		
		 for(Road r : this.entList) {
			 JSONObject jo = new JSONObject();
			 qs.put(jo);
			 
			 jo.put("road", r.getId());
			 
			 JSONArray jv = new JSONArray();
			 
			 jo.put("vehicles", jv);
			 
			 for(Vehicle v : this.roadList.get(r)) {
				jv.put(v.getId()); 
			 }
			 
		 }

		 return json;
	}

	protected void addIncommingRoad(Road r) {
		
		if(r.getDestinationJunction() != this) {
			throw new InvalidParameterException("The destination junction must be this.");
		}
		
		this.entList.add(r);
		
		List<Vehicle> e = new ArrayList<Vehicle>();
		this.colaList.add(e);
		this.roadList.put(r, e);
	}
	
	protected void addOutGoingRoad(Road r) {
		
		if(r.getSourceJunction() != this) {
			throw new InvalidParameterException("The source junction must be this.");
		}
		
		if(this.extList.get(this) != null) {
			throw new NullPointerException("The outcoming roads list is null");
		}
		
		if(this.extList.containsKey(r.getDestinationJunction())) {
			throw new InvalidParameterException("This junction is already in the oucoming roads list.");
		}
		
		this.extList.put(r.getDestinationJunction(), r);
	}
	
	protected void enter(Vehicle v) {
		
		this.roadList.get(v.getRoad()).add(v);
	
	}
	
	protected Road roadTo(Junction j) {
		
	return	this.extList.get(j); // �Error?
		
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public int getIndSem() {
		return this.indSem;
	}
	
	public List<Road> getEntRoads(){
		return this.entList;
	}
	
	public List<List<Vehicle>> getColaList() {
		return this.colaList;
	}
	
	public List<Vehicle> getQueue(){
		List<Vehicle> vMove = this.qEst.dequeue(colaList.get(indSem));
		return vMove;
	}
	
}
