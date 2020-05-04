package simulator.view;


import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private String[] label = {"Id", "Length", "Weather", "Max. Speed", 
			"Speed Limit", "Total CO2", "CO2 Limit"};
	private static final long serialVersionUID = 1L;
	private List<Road> roads;
	
	public RoadsTableModel(Controller control) {
		control.addObserver(this);
		roads =new ArrayList<Road>();
	}
	
	@Override
	public String getColumnName(int c) {
		return label[c];
	}
	
	@Override
	public int getRowCount() {
		
		return roads.size();
	}

	@Override
	public int getColumnCount() {
		
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String auxstr="";
		Weather weatherAux;
		int auxint=0;
		
		Road r = roads.get(rowIndex);
		switch(columnIndex) {
		case 0:
			auxstr=r.getId();
			return auxstr;
		case 1:
			auxint=r.getLength();
			return auxint;
		case 2:
			weatherAux=r.getWeatherConditions();
			return weatherAux;
		case 3:
			auxint=r.getMaximumSpeed();
			return auxint;
		case 4:
			auxint=r.getCurrentSpeedLimit();
			return auxint;
		case 5:
			auxint=r.getTotalContamination();
			return auxint;
		case 6:
			auxint=r.getContaminationAlarmLimit();
			return auxint;
		}
		
		return null;
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.roads=map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.roads=map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.roads=map.getRoads();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	public List<Road> getRoads() {
		return roads;
	}

	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}

}
