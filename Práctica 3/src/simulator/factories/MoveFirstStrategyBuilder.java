package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends Builder<DequeuingStrategy>{

	public MoveFirstStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		return new MoveFirstStrategy();
	}

}

//ArrayList<Builder<DequeuingStrategy>> dqbs= newArrayList<>();
//dqbs.add( newMoveFirstStrategyBuilder() );
//dqbs.add( newMoveAllStrategyBuilder() );
//Factory<DequeuingStrategy> dqsFactory= newBuilderBasedFactory<>(dqbs);