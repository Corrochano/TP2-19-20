package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public RoundRobinStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		
		int timeSlot;
		
		timeSlot = data.optInt("timeslot");
		
		if(timeSlot == 0) {
			timeSlot = 1;
		}
		
		return new RoundRobinStrategy(timeSlot);
	}

}
