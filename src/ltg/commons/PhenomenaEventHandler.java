package ltg.commons;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jivesoftware.smack.packet.Message;

public class PhenomenaEventHandler {
	
	private SimpleXMPPClient sc = null;
	private Map<String, PhenomenaEventListener> listeners = new HashMap<String, PhenomenaEventListener>();


	public PhenomenaEventHandler(String fullJid, String password) {
		sc = new SimpleXMPPClient(fullJid, password);
	}


	public PhenomenaEventHandler(String fullJid, String password, String chatRoom)  {
		sc = new SimpleXMPPClient(fullJid, password, chatRoom);
	}


	public void registerHandler(String eventType, PhenomenaEventListener listener) {
		listeners.put(eventType, listener);
	}


	public void runSynchronously() {
		printRegisteredListeners();
		// We are now connected and in the group chat room. If we don't do something
		// the main thread will terminate. Let's go ahead and 
		// wait for a message to arrive...
		while (!Thread.currentThread().isInterrupted()) {
			// ... and process it ...
			processEvent(sc.nextMessage());
		}
		// ... and finally disconnect
		sc.disconnect();
	}


	public void runAsynchronously() {
		printRegisteredListeners();
		sc.registerEventListener(new MessageListener() {
			public void processMessage(Message m) {
				processEvent(m);
			}
		});
	}


	public void close() {
		sc.disconnect();
	}


	public void generatePrivateEvent(String destination, PhenomenaEvent e) {
		sc.sendMessage(destination, serializeEvent(e));
	}

	
	public void generatePrivateEvent(String destination, String event, Element payload) {
		generatePrivateEvent(destination, new PhenomenaEvent(event, payload));
	}

	
	private void processEvent(Message m) {
		PhenomenaEvent event = null;
		try {
			event = deserializeEvent(m.getBody());
		} catch (Exception e) {
			// Not XML or wrong format, ignore and return
			return;
		}
		// Process event
		PhenomenaEventListener el;
		if (event!=null) {
			el = listeners.get(event.getType());
			if (el!=null)
				el.processEvent(event);
		}
	}


	public static String serializeEvent(PhenomenaEvent pe) {
		Document event = DocumentHelper.createDocument(pe.getXML());
		// remove XML declaration
		StringWriter w = new StringWriter();
		OutputFormat f =  OutputFormat.createPrettyPrint();
		f.setSuppressDeclaration(true);
		XMLWriter xw = new XMLWriter(w, f);
		try {
			xw.write(event);
		} catch (IOException e) {
			System.err.println("Unable to print to a string? Really dom4j?");
		}
		return w.toString();
	}

	
	public static PhenomenaEvent deserializeEvent(String xml) throws DocumentException {
		// Parse XML
		Document doc = DocumentHelper.parseText(xml);
		String event = doc.getRootElement().getName();
		return new PhenomenaEvent(event, doc.getRootElement());
	}
	
	
	private void printRegisteredListeners() {
		String registeredListeners = " ";
		for (String s: listeners.keySet())
			registeredListeners = registeredListeners + s + ", ";
		if (registeredListeners.length()>3) {
		System.out.print("Listening for events of type [");
		System.out.print(registeredListeners.substring(0, registeredListeners.length()-2)+" ]\n");
		} else {
			System.out.print("Listening for events of type [ ]");
		}
	}

}