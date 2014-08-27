function HelioRoomSketch(processing) {
	
	// Sketch
	// PImage background_image = null;
// 	PFont labelsFont;
// 	PFont cp5Font;
// 	float planet_diameter;
// 	double deg_to_px_ratio;
// 	long timeOffset = 0;
// 	ControlP5 cp5;
// 	RadioButton r;
// 	Textlabel l;
// 	Map<String, PImage> images = new HashMap<String, PImage>();
	// FPS indicator
	// boolean FPS = false;
	// long prev_ts;
	// int fps_size;
	// double fps_total = 0d;
	// int fps_index = 0;
	// double fps_samples[];
	
	var i = 0;
	
	processing.setup = function() {
		this.hr = new HelioRoomModel(HelioRoomSketch.getConfigJSON("helioroom_resources/config.json"));
	};
	
	processing.draw = function() {
		if (i < 5)
			console.log("60 times sec");
		i++;
	};
	
}


HelioRoomSketch.getConfigJSON = function(url) {
	request = new XMLHttpRequest();
	request.open('GET', url, false);
	request.send();
  if (request.status !== 200)
		console.err("We reached our target server, but it returned an error");
	return JSON.parse(request.responseText);
}

