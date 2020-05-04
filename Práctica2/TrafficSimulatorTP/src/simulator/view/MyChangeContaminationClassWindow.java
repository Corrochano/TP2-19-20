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

import simulator.model.Vehicle;

public class MyChangeContaminationClassWindow extends JDialog{

	private static final long serialVersionUID = 1L;

	private static int _ticks = 1;
	private int _status;
	private JComboBox<Vehicle> _vehicles;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel;
	
	private JComboBox<Integer> _co2Class;
	private DefaultComboBoxModel<Integer> _co2ClassModel;
	
	private JSpinner _ticksSpinner;
	
	private JLabel vehicle;
	private JLabel co2Class;
	private JLabel ticks;

	public MyChangeContaminationClassWindow(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		String s = "<html><p>Schedule an event to change the CO2 class of a vehicle after a given"
				+ "number of simulation ticks from now.</p></html>";
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

		vehicle = new JLabel(" Vehicle: ");
		vehicle.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(vehicle);
		
		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
		_vehicles.setAlignmentX(LEFT_ALIGNMENT);
		
		viewsPanel.add(_vehicles);
		
		co2Class = new JLabel(" CO2 Class: ");
		co2Class.setAlignmentX(CENTER_ALIGNMENT);
		
		viewsPanel.add(co2Class);
		
		_co2ClassModel = new DefaultComboBoxModel<>();
		_co2Class = new JComboBox<>(_co2ClassModel);
		_co2Class.setAlignmentX(CENTER_ALIGNMENT);
		
		viewsPanel.add(_co2Class);
		
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
				MyChangeContaminationClassWindow.this.setVisible(false);
			}
		});
		
		buttonsPanel.add(cancelButton);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO
				if (_vehiclesModel.getSelectedItem() != null && _co2ClassModel.getSelectedItem() != null) {
					_status = 1;
					MyChangeContaminationClassWindow.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open(List<Vehicle> vList) {

		_vehiclesModel.removeAllElements();
		for (Vehicle v : vList)
			_vehiclesModel.addElement(v);
		
		_co2ClassModel.removeAllElements();
		
		for(int i = 0; i <= 10; i++) {
			_co2ClassModel.addElement(i);
		}

		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);
		return _status;
	}

	Vehicle getVehicle() {
		return (Vehicle) _vehiclesModel.getSelectedItem();
	}
	
	int getTicks() {
		return (int) _ticksSpinner.getValue();
	}
	
	int getCo2Class() {
		return (int) _co2ClassModel.getSelectedItem();
	}

}
