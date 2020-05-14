package simulator.view;


	import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

	
	

	public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{ // preguntar ana maria como poner a la izquierda
		
		private String[] label = {"Time", "Desc."};
		private static final long serialVersionUID = 1L;
		private List<Event> events;
		
		public EventsTableModel(Controller control) {
			
			control.addObserver(this);
			
			events =new ArrayList<Event>();
		}
		
		@Override
		public String getColumnName(int c) {
			return label[c];
		}

		@Override
		public int getRowCount() {
			
			return events.size();
		}

		@Override
		public int getColumnCount() {
			
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			int time=0;
			String desc ="";  
			
			Event e = events.get(rowIndex);
			
			switch(columnIndex) {
			case 0:
				time = e.getTime();
				return time;
			case 1:
				desc = e.toString();
				return desc;
			}
			return null;
			
		}

		@Override
		public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
			this.events=events;
			fireTableDataChanged();
			
		}

		@Override
		public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
			this.events=events;
			fireTableDataChanged();
			
		}

		@Override
		public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
			this.events=events;
			fireTableDataChanged();
			
		}

		@Override
		public void onReset(RoadMap map, List<Event> events, int time) {
			fireTableDataChanged();
			
		}

		@Override
		public void onRegister(RoadMap map, List<Event> events, int time) {
			
			
		}

		@Override
		public void onError(String err) {
			
			
		}

	}

