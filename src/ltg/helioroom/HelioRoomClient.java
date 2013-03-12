package ltg.helioroom;

import ltg.commons.MessageListener;
import ltg.commons.PhenomenaEvent;
import ltg.commons.PhenomenaEventHandler;
import ltg.commons.PhenomenaEventListener;
import ltg.commons.SimpleXMPPClient;

import org.jivesoftware.smack.packet.Message;

import processing.core.PApplet;
import processing.core.PFont;

public class HelioRoomClient extends PApplet {
	private static final long serialVersionUID = 1L;

	// Event Handler
	private PhenomenaEventHandler peh = null;
	// Font
	//private PFont labelsFont;


	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "HelioRoomClient" });
	}



	////////////////////////
	// Processing methods //
	////////////////////////


	public void setup() {
		// Sketch
		size(displayWidth/2, displayHeight/2);
		//labelsFont = createFont("Helvetica",16,true);
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
		
	}



	/////////////////////
	// Drawing methods //
	/////////////////////

	




	////////////////////////////
	// Event handling methods //
	////////////////////////////

	private void processInitEvent(PhenomenaEvent e) {
		System.out.println("Received init");
	}



}
