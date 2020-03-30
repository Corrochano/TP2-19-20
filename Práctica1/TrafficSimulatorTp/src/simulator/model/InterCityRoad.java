package simulator.model;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int tc = this.getTotalContamination(), x;
		
		switch(this.getWeatherConditions()) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		default:
			x = 0; // ¿Except?
		}
		// (int)((100.0 - x)/100.0)*tc
		int aux1 = 100 - x;
		double aux2 = aux1 / 100.0;
		int aux3 = (int)(aux2 * tc);
		
		this.setTotalContamination(aux3);
	}

	@Override
	void updateSpeedLimit() {
		if(this.getTotalContamination() > this.getContaminationAlarmLimit()) {
			this.setCurrentSpeedLimit((int)(this.getMaximumSpeed()*0.5));
		}
		else {
			this.setCurrentSpeedLimit(this.getMaximumSpeed());
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(this.getWeatherConditions() == Weather.STORM) {
			return (int)(this.getCurrentSpeedLimit()*0.8);
			// v.setCurrentSpeed((int)(this.getCurrentSpeedLimit()*0.8)); // ¿?
		}
		else {
			return this.getCurrentSpeedLimit();
			//v.setCurrentSpeed(this.getCurrentSpeedLimit()); // ¿?
		}
	}

}
