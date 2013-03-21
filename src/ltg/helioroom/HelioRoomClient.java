package ltg.helioroom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

import ltg.commons.PhenomenaEvent;
import ltg.commons.PhenomenaEventHandler;
import ltg.commons.PhenomenaEventListener;
import ltg.helioroom.model.HelioRoomModel;
import ltg.helioroom.model.Planet;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import controlP5.ControlP5;
import controlP5.RadioButton;

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
	private long timeOffset = 0;
	private ControlP5 cp5;
	private RadioButton r;


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
		background = loadImage("../resources/stars2.jpeg");
		labelsFont = createFont("Helvetica",32,true);
		cp5 = new ControlP5(this);
		r = cp5.addRadioButton("radioButton")
				.setPosition(10,10)
				.setSize(20,20)
				.setItemsPerRow(2)
				.setSpacingColumn(100)
				.addItem("hr_julia_w1",1)
				.addItem("hr_ben_w1",5)
				.addItem("hr_julia_w2",2)
				.addItem("hr_ben_w2",6)
				.addItem("hr_julia_w3",3)
				.addItem("hr_ben_w3",7)
				.addItem("hr_julia_w4",4)
				.addItem("hr_ben_w4",8);				
		updateTime();
	}


	public void draw() {
		// If there is no data in the model draw a black background and return
		if (!hr.isInitialized()) {
			background(0);
			return;
		}
		// Draw stars
		drawTiledStarsBackground();
		// Calculate dt (in ms)
		double dt = new Date().getTime() + timeOffset - hr.getStartTime()*1000;
		// Draw planets and labels
		drawPlanets(dt);
	}


	public void radioButton(int a) {
		// Sketch
		r.deactivateAll();
		r.setVisible(false);
		String username = null;
		switch (a) {
		case 1:
			username = "hr_julia_w1";
			break;
		case 2:
			username = "hr_julia_w2";
			break;
		case 3:
			username = "hr_julia_w3";
			break;
		case 4:
			username = "hr_julia_w4";
			break;
		case 5:
			username = "hr_ben_w1";
			break;
		case 6:
			username = "hr_ben_w2";
			break;
		case 7:
			username = "hr_ben_w3";
			break;
		case 8:
			username = "hr_ben_w4";
			break;
		}		
		// Logic
		peh = new PhenomenaEventHandler(username+"@54.243.60.48", username);
		peh.registerHandler("helioroom", new PhenomenaEventListener() {
			@Override
			public void processEvent(PhenomenaEvent e) {
				processInitEvent(e);
			}
		});
		peh.runAsynchronously();
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
		// Remember: dt is in milliseconds since the beginning of the simulation!
		// Calculate diameter
		planet_diameter = (int) (.6*height);
		// For each planet...
		for (Planet p: hr.getPlanets()) {
			// Get planet color
			int pc = unhex(p.getColor().substring(2));
			fill(pc);
			// Draw planet
			ellipse(calculatePlanetPosition(p.getStartPosition(), p.getClassOrbitalTime(), dt, p), height/2, planet_diameter, planet_diameter);
			// Draw label
			if (p.getLabelType().equals(HelioRoomModel.LABEL_NONE))
				continue;
			float l_x = calculatePlanetPosition(p.getStartPosition(), p.getClassOrbitalTime(), dt, p);
			textFont(labelsFont);
			fill(255);
			textAlign(CENTER, CENTER);
			if (p.getLabelType().equals(HelioRoomModel.LABEL_NAME))
				text(p.getName().toUpperCase(), l_x, height/2);
			if (p.getLabelType().equals(HelioRoomModel.LABEL_COLOR))
				text(p.getColorName().toUpperCase(), l_x, height/2);
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
			deg_displ = 360 + deg_displ;
		return (float) (deg_displ*deg_to_px_ratio);	
	}




	////////////////////////////
	// Event handling methods //
	////////////////////////////

	private void processInitEvent(PhenomenaEvent e) {
		hr.init(e.getXML());
	}



	///////////////////
	// Other methods //
	///////////////////

	private void updateTime() {
		NTPUDPClient client = new NTPUDPClient();
		// We want to timeout if a response takes longer than 10 seconds
		client.setDefaultTimeout(10000);
		try {
			client.open();
			try {
				InetAddress hostAddr = InetAddress.getByName("us.pool.ntp.org");
				TimeInfo t = client.getTime(hostAddr);
				t.computeDetails();
				timeOffset = t.getOffset();
			} catch (IOException ioe) {
				return;
			}
		} catch (SocketException e) {
			return;
		}
		client.close();
	}



}
