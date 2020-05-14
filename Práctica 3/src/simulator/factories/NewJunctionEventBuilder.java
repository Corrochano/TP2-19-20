package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{

	 Factory<LightSwitchingStrategy> lssFactory;
	 Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(String type, Factory<LightSwitchingStrategy> lssFactory,
			Factory<DequeuingStrategy> dqsFactory) {
		super(type);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		JSONObject ls_Strategy = data.getJSONObject("ls_strategy");
		JSONObject dq_Strategy = data.getJSONObject("dq_strategy");
		
//		ArrayList<Builder<LightSwitchingStrategy>> lsbs= new ArrayList<>();
//		lsbs.add( new RoundRobinStrategyBuilder(ls_Strategy.getString("type")));
//		lsbs.add( new MostCrowdedStrategyBuilder(ls_Strategy.getString("type")));
//		Factory<LightSwitchingStrategy> lssFactory= new BuilderBasedFactory<>(lsbs);
//		
//		ArrayList<Builder<DequeuingStrategy>> dqbs= new ArrayList<>();
//		dqbs.add( new MoveFirstStrategyBuilder(dq_Strategy.getString("type")));
//		dqbs.add( new MoveAllStrategyBuilder(dq_Strategy.getString("type")));
//		Factory<DequeuingStrategy> dqsFactory= new BuilderBasedFactory<>( dqbs);

		return new NewJunctionEvent(time, id, this.lssFactory.createInstance(ls_Strategy),
				this.dqsFactory.createInstance(dq_Strategy), coor.getInt(0), coor.getInt(1));
	}
	
}
