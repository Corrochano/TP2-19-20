package simulator.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	private Junction sourceJunction;
	private Junction destinationJunction;
	private int length;
	private int maximumSpeed;
	private int currentSpeedLimit;
	private int contaminationAlarmLimit;
	private Weather weatherConditions;
	private int totalContamination;
	private List<Vehicle> vehicles;
	private VehicleComparator comp;
	
	
	protected Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, 
			int contLimit, int length, Weather weather) {
			super(id);
			if(maxSpeed < 0 || contLimit < 0 || length < 0) {
				throw new InvalidParameterException("The maximum speed, contamination limit and length can't be null.");
			}
			if(srcJunc == null || destJunc == null || weather == null) {
				throw new NullPointerException("The source junction, destination junction and weather can't be null.");
			}
			this.setDestinationJunction(destJunc);
			this.setSourceJunction(srcJunc);
			this.setMaximumSpeed(maxSpeed);
			this.setContaminationAlarmLimit(contLimit);
			this.setLength(length);
			this.setWeather(weather);
			this.setTotalContamination(0); // ¿?
			this.currentSpeedLimit = 120;
			this.comp = new VehicleComparator();
			this.vehicles = new ArrayList<Vehicle>();
	}

	private void setDestinationJunction(Junction destJunc) {
		this.destinationJunction = destJunc;
		this.destinationJunction.addIncommingRoad(this);
	}

	@Override
	void advance(int time) {
		// 1
		this.reduceTotalContamination();
		// 2
		this.updateSpeedLimit();
		// 3
		for(Vehicle v: vehicles) {
			// A
			if(v.getStatus() != VehicleStatus.WAITING) {
				v.setSpeed(this.calculateVehicleSpeed(v));
			}
			// B
			v.advance(time);
		}
		
		this.vehicles.sort(comp); // ¿?
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		 
		 json.put("id", this._id);
		 json.put("speedlimit", this.getCurrentSpeedLimit());
		 json.put("weather", this.getWeatherConditions());
		 json.put("co2", this.totalContamination);
		 JSONArray ja = new JSONArray();
		 
		 for(Vehicle v : this.vehicles) {
			 ja.put(v.getId());
		 }
		 
		 json.put("vehicles", ja);
		 
		 return json;
	} 
	
	protected void enter(Vehicle v) {
		if(v.getCurrentSpeed() == 0 && v.getLocation() == 0){
			this.vehicles.add(v);
		}
		else {
			throw new InvalidParameterException("The vehicle speed and location must be 0.");
		}
	}
	
	protected void exit(Vehicle v) {
		if(!this.vehicles.contains(v)) {
			throw new InvalidParameterException("The vehicle isn't in the vehicle list.");
		}
		this.vehicles.remove(v); // ¿Excepción?
		
	}
	
	protected void setWeather(Weather w) {
		if(w == null) {
			throw new NullPointerException("The weather can't be null.");
		}
		
		this.weatherConditions = w;
	}
	
	protected void addContamination(int c) {
		if(c < 0) {
			throw new InvalidParameterException("The contamination can't be null.");
		}
		
		this.setTotalContamination(this.getTotalContamination() + c);
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);

	public int getLength() {
		return length;
	}

	private void setLength(int length) {
		this.length = length;
	}

	public int getTotalContamination() {
		return totalContamination;
	}

	protected void setTotalContamination(int totalContamination) {
		this.totalContamination = totalContamination;
	}

	public Weather getWeatherConditions() {
		return weatherConditions;
	}

	public int getContaminationAlarmLimit() {
		return contaminationAlarmLimit;
	}

	public void setContaminationAlarmLimit(int contaminationAlarmLimit) {
		this.contaminationAlarmLimit = contaminationAlarmLimit;
	}

	public int getCurrentSpeedLimit() {
		return currentSpeedLimit;
	}

	protected void setCurrentSpeedLimit(int currentSpeedLimit) {
		this.currentSpeedLimit = currentSpeedLimit;
	}

	public int getMaximumSpeed() {
		return maximumSpeed;
	}

	private void setMaximumSpeed(int maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}

	public Junction getDestinationJunction() {
		return destinationJunction;
	}

	public Junction getSourceJunction() {
		return sourceJunction;
	}

	private void setSourceJunction(Junction sourceJunction) {
		this.sourceJunction = sourceJunction;
		this.sourceJunction.addOutGoingRoad(this);
	}

	public List<Vehicle> getVehicles() {
		return this.vehicles;
	}

	
}
