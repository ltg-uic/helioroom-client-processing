function HelioRoomSketch(processing) {
	
	// Model
	var hr;
	// Sketch
	var prev_ts = 0;
	var images = {}
	// this.timeOffset = 0;
	// FPS indicator
	var FPS = false;
	var fps_total = 0;
	var fps_index = 0;
	var fps_size = 30;
	var fps_samples = new Array(fps_size);
	
	
	processing.setup = function() {
		hr = new HelioRoomModel(HelioRoomSketch.getConfigJSON("helioroom_resources/config.json"));
		// Sketch
		processing.size(screen.width, screen.height);
		processing.loadImages();
	  // Initialize FPS counters
		for (i=0; i<fps_size; i++) { 
		    fps_samples[i] = 0;
		}	
	};
	
	
	processing.draw = function() {
	  // If there is no data in the model draw a black background and return
	  if (!hr.initialized) {
	    processing.background(0);
	    return;
	  }
	  // Draw stars
	  processing.drawTiledStarsBackground();
	  // Calculate dt (in ms)
	  cur_ts = new Date().getTime();
	  dt = cur_ts + hr.startTime*1000;
	  // Draw planets and labels
	  // drawPlanets(dt);
	  // Print framerate
	  if (FPS)
	    processing.printFramerate(cur_ts);
	  prev_ts = cur_ts;
	};
	
	processing.printFramerate = function(cur_ts) {
		fps_total -= fps_samples[fps_index];
	  fps_samples[fps_index] = 1000/(cur_ts - prev_ts);
	  fps_total += 1000/(cur_ts - prev_ts);
	  if (++fps_index == fps_size) fps_index = 0; 
	  processing.textSize(32);
	  processing.fill(255, 0, 0);
		framerate = Math.floor(fps_total / fps_size);
	  processing.text(framerate + " fps", screen.width - 200, 50);	
	}
	
	processing.drawTiledStarsBackground = function() {
	  processing.imageMode(processing.CORNER);
		o_rep = Math.floor(screen.width / images.background.width);
		v_rep = Math.floor(screen.height / images.background.height);
		if (o_rep == Number.POSITIVE_INFINITY || v_rep == Number.POSITIVE_INFINITY) return;
	  for (j=0; j<=v_rep; j++)
			for (i=0; i<=o_rep; i++)
				processing.image(images.background, i*images.background.width, j*images.background.height, images.background.width, images.background.height);
	}
	
	/*
	  @pjs preload = 	
	"helioroom_resources/stars2.jpeg",
	"helioroom_resources/colors/blue.png",
	"helioroom_resources/colors/brown.png",
	"helioroom_resources/colors/gray.png",
	"helioroom_resources/colors/green.png",
	"helioroom_resources/colors/orange.png",
	"helioroom_resources/colors/pink.png",
	"helioroom_resources/colors/red.png",
	"helioroom_resources/colors/yellow.png",
	"helioroom_resources/planets/earth.png",
	"helioroom_resources/planets/jupiter.png",
	"helioroom_resources/planets/mars.png",
	"helioroom_resources/planets/mercury.png",
	"helioroom_resources/planets/neptune.png",
	"helioroom_resources/planets/saturn.png",
	"helioroom_resources/planets/uranus.png",
	"helioroom_resources/planets/venus.png";
	*/
	processing.loadImages = function() {
		// Background
		images = {};
		images.background = processing.loadImage("helioroom_resources/stars2.jpeg");
		// Spheres
		images.Blue = processing.loadImage("helioroom_resources/colors/blue.png");
		images.Brown = processing.loadImage("helioroom_resources/colors/brown.png");
		images.Gray= processing.loadImage("helioroom_resources/colors/gray.png");
		images.Green = processing.loadImage("helioroom_resources/colors/green.png");
		images.Orange = processing.loadImage("helioroom_resources/colors/orange.png");
		images.Pink = processing.loadImage("helioroom_resources/colors/pink.png");
		images.Red = processing.loadImage("helioroom_resources/colors/red.png");
		images.Yellow = processing.loadImage("helioroom_resources/colors/yellow.png");
		// Planets
		images.Earth = processing.loadImage("helioroom_resources/planets/earth.png");
		images.Jupiter = processing.loadImage("helioroom_resources/planets/jupiter.png");
		images.Mars = processing.loadImage("helioroom_resources/planets/mars.png");
		images.Mercury= processing.loadImage("helioroom_resources/planets/mercury.png");
		images.Neptune = processing.loadImage("helioroom_resources/planets/neptune.png");
		images.Saturn = processing.loadImage("helioroom_resources/planets/saturn.png");
		images.Uranus = processing.loadImage("helioroom_resources/planets/uranus.png");
		images.Venus = processing.loadImage("helioroom_resources/planets/venus.png");
	}
	
}


HelioRoomSketch.getConfigJSON = function(url) {
	request = new XMLHttpRequest();
	request.open('GET', url, false);
	request.send();
	if (request.status !== 200)
	console.err("We reached our target server, but it returned an error");
	return JSON.parse(request.responseText);
}
