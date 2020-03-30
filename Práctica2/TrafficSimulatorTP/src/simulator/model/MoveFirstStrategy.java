package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> ret = new ArrayList<Vehicle>();
		ret.add(q.get(0));
		return ret;
	}

}
