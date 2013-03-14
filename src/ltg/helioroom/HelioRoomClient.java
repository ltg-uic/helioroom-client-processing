package ltg.helioroom;

import java.util.Date;

import ltg.commons.PhenomenaEvent;
import ltg.commons.PhenomenaEventHandler;
import ltg.commons.PhenomenaEventListener;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class HelioRoomClient extends PApplet {
	private static final long serialVersionUID = 1L;

	// Event Handler
	private PhenomenaEventHandler peh = null;
	// Model
	private HelioRoomModel hr = new HelioRoomModel();
	// Sketch
	private PImage background = null;
	private PFont labelsFont;
	private int planet_diameter;


	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "HelioRoomClient" });
	}



	////////////////////////
	// Processing methods //
	////////////////////////


	public void setup() {
		// Sketch
		frameRate(30);
		size(displayWidth/2, displayHeight/2);
		planet_diameter = (int) (.6*height);
		background = loadImage("../resources/stars2.jpeg");
		labelsFont = createFont("Helvetica",16,true);
		// Logic
		peh = new PhenomenaEventHandler("hr_dev_w1@54.243.60.48", "hr_dev_w1");
		peh.registerHandler("helioroom", new PhenomenaEventListener() {
			@Override
			public void processEvent(PhenomenaEvent e) {
				processInitEvent(e);
			}
		});
		peh.runAsynchronously();
	}


	public void draw() {
		// Return if no data in model
		if (!hr.isInitialized()) {
			background(0);
			return;
		}
		drawTiledStarsBackground();
		// Draw planets
		double dt = new Date().getTime() - hr.getStartTime()*1000;
		drawPlanets(dt);
	}



	/////////////////////
	// Drawing methods //
	/////////////////////

	private void drawTiledStarsBackground() {
		int o_rep = width/background.width;
		int v_rep = height/background.height;
		for (int j=0;j<=v_rep;j++)
			for (int i=0;i<=o_rep;i++)
				image(background, i*background.width, j*background.height, background.width, background.height);
	}


	private void drawPlanets(double dt) {
		// remember... dt is in milliseconds since the beginning of the simulation!
		for (Planet p: hr.getPlanets()) {
			int pc = unhex(p.getColor().substring(2));
			fill(pc);
			ellipse(calculatePlanetPosition(p.getStartPosition(), p.getClassOrbitalTime(), dt, p), height/2, planet_diameter, planet_diameter);
		}
	}
	
	
	private float calculatePlanetPosition(double deg_0, double classOrbitalTime, double dt, Planet p) {
		// Position in degrees
		double deg = (deg_0 + .006*dt/classOrbitalTime) % 360;
		// Degrees to pixels ratio
		double deg_to_px_ratio = ((double) width) / hr.getViewAngle();
		// Displacement (in deg) from viewAngleEnd
		double deg_displ = hr.getViewAngleEnd() - deg;
		if (deg_displ < -180)
			deg_displ = 360 + deg_displ ;
		if (p.getName().equals("Mercury")) 
			System.out.format("P(deg) %+.2f Disp(deg) %+.2f%n", deg, deg_displ);
		return (float) (deg_displ*deg_to_px_ratio);	
	}




	////////////////////////////
	// Event handling methods //
	////////////////////////////

	private void processInitEvent(PhenomenaEvent e) {
		hr.init(e.getXML());
	}



}
