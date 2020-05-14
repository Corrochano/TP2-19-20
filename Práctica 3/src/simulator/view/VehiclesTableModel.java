package simulator.view;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private String[] label = {"Id", "Location", "Itinerary", "CO2 Class", 
			"Max. Speed", "Speed", "Total CO2", "Distance"};
	private static final long serialVersionUID = 1L;
	private List<Vehicle> vehicles;
	
	public VehiclesTableModel(Controller control) {
		control.addObserver(this);
		vehicles = new ArrayList<Vehicle>();
	}
	
	@Override
	public String getColumnName(int c) {
		return label[c];
	}

	@Override
	public int getRowCount() {
	
		return vehicles.size();
	}

	@Override
	public int getColumnCount() {
	
		return 8;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String auxstr="";
		int auxint=0;
		List<Junction> auxitinerary;
		
		Vehicle v = vehicles.get(rowIndex);
		
		
		switch(columnIndex) {
		case 0:
			auxstr=v.getId().toString();
			return auxstr;
		case 1:
			auxint=v.getLocation();
			if(v.getStatus() == VehicleStatus.ARRIVED) return "Arrived";
			else return v.getRoad().getId() + ": " + auxint;
		case 2:
			auxitinerary=Collections.unmodifiableList(v.getItinerary());
			return auxitinerary;
		case 3:
			auxint=v.getContaminationClass();
			return auxint;
		case 4:
			auxint=v.getMaxiumSpeed();
			return auxint;
		case 5:
			auxint=v.getCurrentSpeed();
			return auxint;
		case 6:
			auxint=v.getTotalContamination();
			return auxint;
		case 7:
			auxint=v.getTotalTravelledDistance();
			return auxint;
		}
		return v;
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.vehicles=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.vehicles=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.vehicles=map.getVehicles();
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		//this.vehicles.clear();
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

	


}
