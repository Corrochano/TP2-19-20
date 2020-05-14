package simulator.model;

import java.security.InvalidParameterException;
import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	
	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs.isEmpty()) {
			throw new InvalidParameterException("The pair list is empty.");
		}
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String,Integer> c : this.cs) {
			if(map.getVehicle(c.getFirst()) == null) {
				throw new NullPointerException("Vehicle isn't in the road map.");
			}
			map.getVehicle(c.getFirst()).setContaminationClass(c.getSecond());
		}
	}
	
	@Override
	public String toString() {
		String s;
		s = "Change CO2 Class";
		
		s += "[";
		for(Pair<String, Integer> p : this.cs) {
			s += "(";
			s += p.getFirst().toString();
			s += ", ";
			s += p.getSecond().toString();
			s += ")";
			if(cs.indexOf(p) != cs.size() - 1) {
				s += ", ";
			}
		}
		
		s += "]";
		return s;
	}
	
}
