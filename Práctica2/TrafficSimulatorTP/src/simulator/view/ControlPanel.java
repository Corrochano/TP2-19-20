package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JToolBar toolbar;
	private Boolean _stopped;
	
	private RoadMap _rmap;
	
	private int ticks = 10;
	private int time = 0;
	
	private JButton loadButton;
	private JButton changeVehicleContClassButton;
	private JButton changeRoadWeatherButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton exitButton;
	
	private JLabel ticksLabel;
	
	private JSpinner ticksSpinner;
	
	public ControlPanel(Controller ctrl) throws IOException{
		this._ctrl = ctrl;
		_stopped = false;
		initGUI();
		this._ctrl.addObserver(this);
	}
	
	private void initGUI() throws IOException {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.PAGE_START);
		
		// Load Button
		
		initLoadButton();
		toolbar.addSeparator();
		
		// Change Vehicle Cont. Class Button
		
		initVehicleContClassButton();
		toolbar.addSeparator();
		
		// Change Road Weather Button
		
		initChangeRoadWeatherButton();
		toolbar.addSeparator();
		
		// Start Button
		
		initStartButton();
		
		// Stop Button
		
		initStopButton();
		
		// Ticks Button
		
		ticksLabel = new JLabel();
		ticksLabel.setText(" Ticks: ");
		toolbar.add(ticksLabel);
		initTicksSpinner();
		
		toolbar.addSeparator();
		
		// Exit Button
		
		initExitButton();
		
	}
	
	

	private void initStartButton() throws IOException {
		startButton = new JButton();
		startButton.setToolTipText("Run ticks value cycles in the traffic simulator.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/run.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		startButton.setIcon(new ImageIcon(i));
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
			
		});
		
		toolbar.add(startButton);
	}
	
	private void start() {
		_stopped = false;
		enableToolBar(false); // creo
		run_sim((Integer)ticksSpinner.getValue());
	}

	private void initTicksSpinner() {
		SpinnerNumberModel model = new SpinnerNumberModel(1, 0, 1000, 1);
		ticksSpinner = new JSpinner(model);
		ticksSpinner.setValue(ticks);
		ticksSpinner.setMinimumSize(new Dimension(80, 30));
		ticksSpinner.setMaximumSize(new Dimension(200, 30));
		ticksSpinner.setPreferredSize(new Dimension(80, 30));
		
		ticksSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				
				ticks = Integer.valueOf(ticksSpinner.getValue().toString()); // = o += ¿?
				
			}
			
		});		
		
		toolbar.add(ticksSpinner);
		
	}
	
	
	private void initStopButton() throws IOException {
		stopButton = new JButton();
		stopButton.setToolTipText("Stops Traffic Simulator.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/stop.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		stopButton.setIcon(new ImageIcon(i));
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
			
		});
		
		toolbar.add(stopButton);
	}
	
	
	
	private void initExitButton() throws IOException {
		exitButton = new JButton();
		exitButton.setAlignmentX(RIGHT_ALIGNMENT);
		exitButton.setToolTipText("Exits the traffic simulator.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/exit.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		exitButton.setIcon(new ImageIcon(i)); // Usar loadImage ¿?

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(null, "Are sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
				
				if (ret == JOptionPane.YES_OPTION) System.exit(0);
			}
			
		});
		
		toolbar.add(Box.createHorizontalGlue()); // ¿Horizontal?
		toolbar.add(exitButton);
	}

	private void run_sim( int n ) {
		
		if ( n > 0 && ! _stopped ) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Run sim error.");
				_stopped = true ;
				return ;
			}
			
		SwingUtilities.invokeLater( new Runnable() {
		
			@Override
			public void run() {
				run_sim(n - 1);
			}
		
			});
		
		} else {
			enableToolBar(true);
			_stopped = true ;
		}
	}

	private void stop() {
		_stopped = true ;
	}

	private void initChangeRoadWeatherButton() throws IOException {
		changeRoadWeatherButton = new JButton();
		changeRoadWeatherButton.setToolTipText("Change the weather of a road.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/weather.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		changeRoadWeatherButton.setIcon(new ImageIcon(i));
		
		changeRoadWeatherButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectWeather();
			}
			
		});
		
		toolbar.add(changeRoadWeatherButton);
	}

	protected void selectWeather() {
		JFrame f = new JFrame();
		MyChangeWeatherWindow dialog = new MyChangeWeatherWindow(f);

		List<Road> roads = new ArrayList<Road>();
		
		for (Road r : _rmap.getRoads()) {
			roads.add(r);
		}
		
		int status = dialog.open(roads);
		
		if (status == 1) {
			List<Pair<String, Weather>> ps = new ArrayList<Pair<String, Weather>>();
			
			Pair<String,Weather> p = new Pair<String, Weather>(dialog.getRoad().getId(), dialog.getWeather());
			
			ps.add(p);
			
			SetWeatherEvent e = new SetWeatherEvent(this.time + dialog.getTicks(), ps);
			
			_ctrl.addEvent(e);
			
			JOptionPane.showMessageDialog(null, "Event added to queue!");	
			
		}
		
		
	}

	private void initVehicleContClassButton() throws IOException {
		changeVehicleContClassButton = new JButton();
		changeVehicleContClassButton.setToolTipText("Change the contamination class of a vehicle.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/co2class.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		changeVehicleContClassButton.setIcon(new ImageIcon(i));
		
		changeVehicleContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectCO2();
			}
			
		});
		
		toolbar.add(changeVehicleContClassButton);
	}
	
	protected void selectCO2() {
		JFrame f = new JFrame();
		MyChangeContaminationClassWindow dialog = new MyChangeContaminationClassWindow(f);

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		for (Vehicle v : _rmap.getVehicles()) {
			vehicles.add(v);
		}
		
		int status = dialog.open(vehicles);
		
		if (status == 1) {
			//JOptionPane.showMessageDialog(null, "You must select all the options.");	
			List<Pair<String, Integer>> ps = new ArrayList<Pair<String, Integer>>();
			
			Pair<String, Integer> p = new Pair<String, Integer>(dialog.getVehicle().getId(), dialog.getCo2Class());
			
			ps.add(p);
			
			NewSetContClassEvent e = new NewSetContClassEvent(time + dialog.getTicks(), ps);
			
			_ctrl.addEvent(e);
			
			JOptionPane.showMessageDialog(null, "Event added to queue!");
		}
		
	}

	private void initLoadButton() throws IOException { // Hacer bien
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File", "json"); // Funciona?
		fc.setFileFilter(filter);
		fc.setDialogTitle("Choose a Event File to load into the simulator.");
		
		loadButton = new JButton();
		loadButton.setToolTipText("Loads a JSON file with the events.");
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/open.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Load image error.");
			throw new IOException("Load image error.");
		}
		loadButton.setIcon(new ImageIcon(i));
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int result = fc.showOpenDialog(fc);
				
				if(result == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					InputStream is;
					
					try {
						is = new FileInputStream(file);
						_ctrl.reset();
						_ctrl.loadEvents(is);
						JOptionPane.showMessageDialog(fc, "You choose " + fc.getSelectedFile());
					} catch(FileNotFoundException x) {
						JOptionPane.showMessageDialog(null, "File not found.");
					}	
				} else {
					JOptionPane.showMessageDialog(fc, "Canceled.");
				}
			}	
		});
		
		toolbar.add(loadButton);
	}
	
	private void enableToolBar(boolean b) {
		
		loadButton.setEnabled(b);
		changeVehicleContClassButton.setEnabled(b);
		changeRoadWeatherButton.setEnabled(b);
		startButton.setEnabled(b);
	//	stopButton.setEnabled(b);
		exitButton.setEnabled(b);
		
		ticksSpinner.setEnabled(b);
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_rmap = map;
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_rmap = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		ticks = 10;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_rmap = map;
		this.time = time;
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
	
//	this.add(jToolBar b);
	
}


// Para separar los botones: 

// .add(Box.createGlue())
// JToolbar.addSeparator
