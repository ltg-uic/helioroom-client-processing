package ltg.helioroom;

import ltg.commons.MessageListener;
import ltg.commons.SimpleXMPPClient;

import org.jivesoftware.smack.packet.Message;

import processing.core.PApplet;
import processing.core.PFont;

public class HelioRoomClient extends PApplet {
	private static final long serialVersionUID = 1L;

	// XMMP client
	private SimpleXMPPClient xmpp = null;
	// JSON parser
	//private JsonParser parser = new JsonParser();
	// Foraging game
	//private HelioRoom model = new HelioRoom();
	// Font
	private PFont labelsFont;


	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "HelioRoomClient" });
	}



	////////////////////////
	// Processing methods //
	////////////////////////


	public void setup() {
		// Sketch
		frameRate(1);
		size(displayWidth/2, displayHeight/2);
		labelsFont = createFont("Helvetica",16,true);
		// Logic
		xmpp = new SimpleXMPPClient("hr_dev_w1@54.243.60.48", "hr_dev_w1");
		println("Connected to XMPP server and listening");
		xmpp.registerEventListener(new MessageListener() {
			@Override
			public void processMessage(Message m) {
				processIncomingData(m.getBody());
			}
		});
	}


	public void draw() {
		
	}



	/////////////////////
	// Drawing methods //
	/////////////////////

	




	////////////////////////////
	// Event handling methods //
	////////////////////////////

	public void processIncomingData(String s) {
		
	}



}
