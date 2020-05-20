package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0); // Rojo Verde Azul
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _ROAD_COLOR = Color.BLACK;

	private RoadMap _map;

	private Image _car; // En el nuestro, vehículo contaminación y weather
	private Image _contamination;
	private Image _weather;
	
	
	public MapByRoadComponent(Controller _ctrl) {
		setPreferredSize(new Dimension(300, 200));
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void paintComponent(Graphics graphics) { // invocar en repaint en trafficsimobserver
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize(); creo que no
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
	}
	
	private void drawRoads(Graphics g) {
		
		int x1 = 50, x2 = getWidth() - 100, y, i = 0;
		
		for(Road r : _map.getRoads()) {
			
			// Update y
			y = (i + 1) * 50;
			
			// Draw road
			g.setColor(_ROAD_COLOR);
			g.drawLine(x1, y, x2, y);
			
			// Draw source junction
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSourceJunction().getId(), x1, y - 5);
			
			// Draw destination junction
			int idx = r.getDestinationJunction().getIndSem();
			if (idx != -1 && r.equals(r.getDestinationJunction().getEntRoads().get(idx))) {
				g.setColor(_GREEN_LIGHT_COLOR);
			}
			else {
				g.setColor(_RED_LIGHT_COLOR);
			}
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getDestinationJunction().getId(), x2, y - 5);
			
			// Draw vehicles
			
			for(Vehicle v : _map.getVehicles()) {
				if(v.getRoad() != null) {
					if(v.getRoad().getId() == r.getId()) {
						int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContaminationClass()));
						g.setColor(new Color(0, vLabelColor, 0));
						int xCar = x1 + ( int ) ((x2 - x1) * (( double ) v.getLocation() / ( double ) r.getLength())); 
						g.fillOval(xCar, y - 10, 16, 16);   
						g.drawImage(_car, xCar, y - 10, 16, 16, this);
						
						g.drawString(v.getId(), xCar, y - 10);
					}
				}
			}
			
			// Draw road id
			g.setColor(_ROAD_COLOR);
			g.drawString(r.getId(), x1 / 2, y); //¿?
			
			// Draw Weather Conditions Image
			switch(r.getWeatherConditions()) {
				case CLOUDY:
					_weather = loadImage("cloud.png");
					break;
				case RAINY:
					_weather = loadImage("rain.png");
					break;
				case STORM:
					_weather = loadImage("storm.png");
					break;
				case SUNNY:
					_weather = loadImage("sun.png");
					break;
				case WINDY:
					_weather = loadImage("wind.png");
					break;
			}
			
			g.drawImage(_weather, x2 + 10, y - 20, 32, 32, this);
			
			// Draw CO2 contamination level
			int c = (int) Math.floor(Math.min((double) r.getTotalContamination() / (1.0 + (double) r.getContaminationAlarmLimit()),1.0) / 0.19);
			
			_contamination = loadImage("cont_" + c + ".png");
			g.drawImage(_contamination, x2 + 50, y - 20, 32, 32, this);
			
			// Increment iterator i
			i++;
		}
		
	}

	@SuppressWarnings("unused")
	private void updatePrefferedSize() { // El nuestro no hay que centrarlo   Spinner number modelpara los spinner paso del step 1
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}
	
	// Array de imágenes
	// Imágenes de condición atmosférica puede ir en el enum Weather
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				update(map);
			}
	
		});
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				update(map);
			}
	
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				update(map);
			}
	
		});
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater( new Runnable() {
			
			@Override
			public void run() {
				update(map);
			}
	
		});
	}

	@Override
	public void onError(String err) {
	}

	
	
}

// En main añadir opción -m que puede ser GUI o consola (p2 o p1)