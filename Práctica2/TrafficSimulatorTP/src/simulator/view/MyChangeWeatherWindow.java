package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.Weather;

public class MyChangeWeatherWindow extends JDialog{

	private static final long serialVersionUID = 1L;

	private static int _ticks = 1;
	private int _status;
	private JComboBox<Road> _roads;
	private DefaultComboBoxModel<Road> _roadsModel;
	
	private JComboBox<Weather> _weathers;
	private DefaultComboBoxModel<Weather> _weathersModel;
	
	private JSpinner _ticksSpinner;
	
	private JLabel road;
	private JLabel weather;
	private JLabel ticks;

	public MyChangeWeatherWindow(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		String s = "<html><p>Schedule an event to change the weather of a road after a given number "
				+ "of simulation ticks from now.</p></html>";
		JLabel helpMsg = new JLabel(s);
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		//buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		road = new JLabel(" Road: ");
		road.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(road);
		
		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		_roads.setAlignmentX(LEFT_ALIGNMENT);
		
		viewsPanel.add(_roads);
		
		weather = new JLabel(" Weather: ");
		weather.setAlignmentX(CENTER_ALIGNMENT);
		
		viewsPanel.add(weather);
		
		_weathersModel = new DefaultComboBoxModel<>();
		_weathers = new JComboBox<>(_weathersModel);
		_weathers.setAlignmentX(CENTER_ALIGNMENT);
		
		viewsPanel.add(_weathers);
		
		ticks = new JLabel(" Ticks: ");
		ticks.setAlignmentX(RIGHT_ALIGNMENT);
		
		viewsPanel.add(ticks);
		
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 1000, 1);
		_ticksSpinner = new JSpinner(model);
		_ticksSpinner.setValue(_ticks);
		_ticksSpinner.setMinimumSize(new Dimension(80, 30));
		_ticksSpinner.setMaximumSize(new Dimension(200, 30));
		_ticksSpinner.setPreferredSize(new Dimension(80, 30));
		_ticksSpinner.setAlignmentX(RIGHT_ALIGNMENT);
		
		viewsPanel.add(_ticksSpinner);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = -1;
				MyChangeWeatherWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//TODO 
				
				if (_roadsModel.getSelectedItem() != null && _weathersModel.getSelectedItem() != null) {
					_status = 1;
					MyChangeWeatherWindow.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open(List<Road> rList) {

		_roadsModel.removeAllElements();
		for (Road r : rList)
			_roadsModel.addElement(r);
		
		_weathersModel.removeAllElements();
		
		for(Weather w : Weather.values())
			_weathersModel.addElement(w);

		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);
		return _status;
	}

	Road getRoad() {
		return (Road) _roadsModel.getSelectedItem();
	}

	int getTicks() {
		return (int) _ticksSpinner.getValue();
	}
	
	Weather getWeather() {
		return (Weather) _weathersModel.getSelectedItem();
	}
	
}
