package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 1L;
	
	private String[] label = {"Id", "Green", "Queues"};
	private List<Junction> junctions;
	public JunctionsTableModel(Controller control) {
		
		junctions = new ArrayList<Junction>();
		control.addObserver(this);
	}
	
	@Override
	public String getColumnName(int c) {
		return label[c];
	}
	
	@Override
	public int getRowCount() {
		
		return junctions.size();
	}

	@Override
	public int getColumnCount() {
	
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		String auxstr="";
		
		
		Junction j = junctions.get(rowIndex);
		switch(columnIndex) {
		case 0:
			auxstr=j.getId();
			return auxstr;
		case 1:
			int idx=j.getIndSem();
			if(idx==-1) {
				return "NONE";
			}
			return j.getEntRoads().get(idx).getId();
			
		case 2:
			String queue="";
			
			for(Road r: j.getEntRoads()) {
				queue+=r.getId();
				queue+= ":[";
				for(Vehicle v: r.getVehicles()) {
					if(v.getStatus() == VehicleStatus.WAITING) {
						queue+=v.getId();
					}
				}
				queue+="] ";
			}
		return queue;
		}
		return null;
		
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				junctions = map.getJunctions();
				fireTableDataChanged();
			}
	
		});
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				junctions = map.getJunctions();
				fireTableDataChanged();
			}
	
		});
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				junctions = map.getJunctions();
				fireTableDataChanged();
			}
	
		});
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				fireTableDataChanged();
			}
	
		});
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	
	}

	@Override
	public void onError(String err) {

	}

}
