package simulator.view;


	import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

	
	

	public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{ // preguntar ana maria como poner a la izquierda
		
		private String[] label = {"Time", "Desc."};
		private static final long serialVersionUID = 1L;
		private List<Event> _events;
		
		public EventsTableModel(Controller control) {
			
			control.addObserver(this);
			
			_events =new ArrayList<Event>();
		}
		
		@Override
		public String getColumnName(int c) {
			return label[c];
		}

		@Override
		public int getRowCount() {
			
			return _events.size();
		}

		@Override
		public int getColumnCount() {
			
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			int time=0;
			String desc ="";  
			
			Event e = _events.get(rowIndex);
			
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
			
			SwingUtilities.invokeLater( new Runnable() {
				
				@Override
				public void run() {
					_events = events;
					fireTableDataChanged();
				}
		
			});
			
		}

		@Override
		public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
			
			SwingUtilities.invokeLater( new Runnable() {
				
				@Override
				public void run() {
					_events = events;
					fireTableDataChanged();
				}
		
			});
			
		}

		@Override
		public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
			
			SwingUtilities.invokeLater( new Runnable() {
				
				@Override
				public void run() {
					_events = events;
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

