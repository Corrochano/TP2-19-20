package simulator.model;

public class NewJunctionEvent extends Event{

	private String id;
	private LightSwitchingStrategy ls;
	private DequeuingStrategy dq;
	private int x;
	private int y;
//	private Junction junc;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.ls = lsStrategy;
		this.dq = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
		
	//	junc = new Junction(id, lsStrategy, dqStrategy, yCoor, yCoor);
	}

	@Override
	void execute(RoadMap map) {
		Junction junc = new Junction(this.id, this.ls, this.dq, this.x, this.y);
		map.addJunction(junc);
	}

	@Override
	public String toString() {
		return "New Junction '" + this.id + "'";
	}
	
}
