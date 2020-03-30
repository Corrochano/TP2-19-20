package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc,
			int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJun;
		this.destJunc = destJunc;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
		this.length = length;
	}

	@Override
	void execute(RoadMap map) {
		Road r = new CityRoad(this.id, map.getJunction(this.srcJunc), map.getJunction(this.destJunc), this.maxSpeed, this.co2Limit, this.length, this.weather);
		map.addRoad(r);
	}
}
