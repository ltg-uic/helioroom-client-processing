package ltg.helioroom;

import java.util.List;

public class HelioRoom {

	// Planets representation constants
	public final static String IMAGES 	= "images";
	public final static String SPHERES 	= "spheres";

	// Planets names constants
	public final static String NONE 	= "none";
	public final static String NAMES 	= "names";
	public final static String COLORS 	= "color";
	
	// State constants
	public final static String RUNNING 	= "running";
	public final static String PAUSED 	= "paused";

	// Simulation data
	private long startTime = -1;
	private String state = null;
	private int viewAngleBegin = -1;
	private int viewAngleEnd = -1;
	private List<Planet> planets = null;
	
	
	public synchronized void init() {
		
	}

}
