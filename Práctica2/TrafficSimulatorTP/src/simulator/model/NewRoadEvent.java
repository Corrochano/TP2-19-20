package simulator.model;

public abstract class NewRoadEvent extends Event{
	protected String id;
	protected String srcJunc;
	protected String destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;
	
	public NewRoadEvent(int time) {
		super(time);
	}
	
}
