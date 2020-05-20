package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel time;
	private JLabel event;
	
	private JLabel _currTime;
	private JLabel _lastEvent;
	
	StatusBar(Controller ctrl) {
		
		initGUI();
		ctrl.addObserver(this);
		
	}
	
	private void initGUI() {
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		time = new JLabel(" Time: ");
		event = new JLabel(" Event added ");
		
		_currTime = new JLabel();
		_lastEvent = new JLabel();
		
		this.add(time);
		this.add(_currTime);
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(10, 20));
		this.add(sep);
		
		this.add(event);
		this.add(_lastEvent);
		//this.add(sep);
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_currTime.setText(String.valueOf(time));
		_lastEvent.setText("");
		event.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText(String.valueOf(time));
				event.setText(" Event added: ");
				_lastEvent.setText(/*events.get(events.size() - 1)*/e.toString()); // OJO
			}
	
		});
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText(String.valueOf(0));
				_lastEvent.setText("Welcome!");
			}
	
		});
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				_currTime.setText(String.valueOf(time));
				event.setText("Welcome!");
			}
	
		});
		
	}

	@Override
	public void onError(String err) {
	}

}
