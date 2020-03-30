package simulator.model;

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int tc = this.getTotalContamination();
		if(this.getWeatherConditions() == Weather.WINDY || this.getWeatherConditions() == Weather.STORM) {
			if(tc - 10 < 0) {
				tc += 10;
				//throw new InvalidParameterException("The new value for total contaminition can't be negative.");
			}
			this.setTotalContamination(tc - 10);
		}
		else {
			if(tc - 2 < 0) {
				//throw new InvalidParameterException("The new value for total contaminition can't be negative.");
				tc += 2;
			}
			this.setTotalContamination(tc - 2);
		}
	}

	@Override
	void updateSpeedLimit() {
		// this.setCurrentSpeedLimit(this.getMaximumSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int s, f;
		
		s = this.getCurrentSpeedLimit();
		
		f = v.getContaminationClass();
		
		return (int)(((11.0- f )/11.0)* s);
	}

}
