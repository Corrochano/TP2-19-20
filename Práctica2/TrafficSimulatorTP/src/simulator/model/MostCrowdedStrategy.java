package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int ticks;
	
	public MostCrowdedStrategy(int timeSlot){
		this.ticks = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		if(roads.isEmpty()) {
			return -1;
		}
		
		
		List<Vehicle> auxList = new ArrayList<Vehicle>();
		
		if(currGreen == -1) {
			
			for(List<Vehicle> l : qs) {
				if(l.size() > auxList.size()) {
					auxList = l;
				}
			}
			
			return qs.indexOf(auxList); // OJO
			
		}
		
		
		if(currTime-lastSwitchingTime < this.ticks) {
			return currGreen;
		}
		
		else {
			
			auxList = new ArrayList<Vehicle>();
			
			int start = currGreen;
			int i = (currGreen + 1) % roads.size(); 
			
			do {
				
				if(qs.get(i).size() > auxList.size()) {
					auxList = qs.get(i);
				}
				
				i = (i + 1) % roads.size();
				
			} while(i != start);
			
			return qs.indexOf(auxList); // OJO
		}
	}

}
