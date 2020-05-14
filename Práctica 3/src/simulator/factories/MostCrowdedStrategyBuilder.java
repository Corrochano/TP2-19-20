package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		
		//JSONObject aux;
		int timeSlot;
		
		//aux = data.getJSONObject("data");
		timeSlot = data.optInt("timeslot");
		
		if(timeSlot == 0) {
			timeSlot = 1;
		}
		
		return new MostCrowdedStrategy(timeSlot);
	}
	
//	ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
//	lsbs.add( new RoundRobinStrategyBuilder() );
//	lsbs.add( new MostCrowdedStrategyBuilder() );
//	Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);

}
