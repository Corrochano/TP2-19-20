package simulator.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject { // Añadir excepciones

	private List<Junction> itinerary;
	private int maximumSpeed;
	private int currentSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contaminationClass;
	private int totalContamination;
	private int totalTravelledDistance;
	private int lastJunction;
	
	
	protected Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		
		if(maxSpeed < 0) {
			throw new InvalidParameterException("Maximum speed must be positive.");
		}
		
		if(contClass < 0 || contClass > 10) {
			throw new InvalidParameterException("The contamination class must be between 0 and 10.");
		}
		
		if(itinerary.size() < 2) {
			throw new InvalidParameterException("The itinerary size must be minimum two.");
		}
		
		this.maximumSpeed = maxSpeed;
		
		this.setContaminationClass(contClass);
		
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary)); 
		
		this.lastJunction = 0;
		
		this.status = VehicleStatus.PENDING;
		this.location = 0;
		this.totalContamination = 0;
		this.totalTravelledDistance = 0;
		this.currentSpeed = 0;
	} 

	@Override
	void advance(int time) {
		if(this.status == VehicleStatus.TRAVELING) {
			
			// A
			
			int i = this.location + this.currentSpeed, ii = this.road.getLength(), 
					lastLocation = this.location;
			if(i < ii) {
				this.location = i;
			}
			else {
				this.location = ii;
			}
			
			// B
			
			int l = this.location - lastLocation;
			this.totalTravelledDistance += l; // OJO
			int c = l * this.contaminationClass; // f = contaminationClass¿?
			this.totalContamination += c;
			this.road.addContamination(c);
			
			// C
			
			if(this.location >= this.road.getLength()) {
				this.currentSpeed = 0;
				this.status = VehicleStatus.WAITING;
				this.itinerary.get(this.itinerary.indexOf(this.road.getDestinationJunction())).enter(this); // Echar un ojo
			}
		
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		 
		 json.put("id", this._id);
		 json.put("speed", this.currentSpeed);
		 json.put("distance", this.totalTravelledDistance);
		 json.put("co2", this.totalContamination);
		 json.put("class", this.contaminationClass);
		 json.put("status", this.status);
		 if(this.getStatus() != VehicleStatus.ARRIVED) {
			 json.put("road", this.road.getId());
		 	json.put("location", this.location);
		 }
		 
		 return json;
	}
	
	protected void setSpeed(int s) { // OJO
		if(s > this.maximumSpeed) {
			this.currentSpeed = this.maximumSpeed;
		}
		else {
			this.currentSpeed = s;
		}
	}
	
	protected void setContaminationClass(int c) {
		this.contaminationClass = c;
	}

	void moveToNextRoad() {
		if(this.status != VehicleStatus.PENDING && this.status != VehicleStatus.WAITING) {
			//throw new InvalidParameterException("The status must be pending or waiting.");
			return;
		}
		
		this.currentSpeed = 0;
		this.location = 0;
		
		if(this.status == VehicleStatus.PENDING) {
			this.road = this.itinerary.get(0).roadTo(this.itinerary.get(1));
			this.road.enter(this);
			this.status = VehicleStatus.TRAVELING;
		}
		else {
			lastJunction++;
			if(this.lastJunction >= this.itinerary.size() - 1) { // OJO
				this.road.exit(this);
				this.road = null;
				this.status = VehicleStatus.ARRIVED;
			}
			else {
				//this.location = 0;
				this.road.exit(this);
				this.road = this.itinerary.get(lastJunction).roadTo(this.itinerary.get(lastJunction + 1)); // OJO EL +1
				this.road.enter(this);
				this.status = VehicleStatus.TRAVELING;
				//this.lastJunction++;
			}
		}
	}
	
	public int getCurrentSpeed() {
		return this.currentSpeed;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	public int getContaminationClass() {
		return this.contaminationClass;
	}
	
	public Road getRoad() {
		return this.road;
	}
	
	public List<Junction> getItinerary(){
		return this.itinerary;
	}

	public VehicleStatus getStatus() {
		return this.status;
	}

	public int getMaxiumSpeed() {
		return this.maximumSpeed;
	}

	public int getTotalContamination() {
		return this.totalContamination;
	}

	public int getTotalTravelledDistance() {
		return this.totalTravelledDistance;
	}
	
}
