package ltg.helioroom;

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
		if (!hr.isInitialized()) {
			background(0);
			return;
		}
		drawTiledStarsBackground();
		drawPlanets();
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


	private void drawPlanets() {
		int i = 0;
		for (Planet p: hr.getPlanets()) {
			i=i+100;
			int pc = unhex(p.getColor().substring(2));
			fill(pc);
			ellipse(i, height/2, planet_diameter, planet_diameter);
		}
	}




	////////////////////////////
	// Event handling methods //
	////////////////////////////

	private void processInitEvent(PhenomenaEvent e) {
		hr.init(e.getXML());
	}



}
