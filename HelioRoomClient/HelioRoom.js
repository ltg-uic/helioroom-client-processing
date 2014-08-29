function HelioRoomSketch(processing) {
	
	// Model
	var hr;
	// Sketch
	var prev_ts = 0;
	var images = {}
	var planet_diameter;
	var deg_to_px_ratio;
	var labelsFont;
	// FPS indicator
	FPS = false;
	var fps_total = 0;
	var fps_index = 0;
	var fps_size = 30;
	var fps_samples = new Array(fps_size);
	
	
	processing.setup = function() {
		hr = new HelioRoomModel(HelioRoomSketch.getConfigJSON("helioroom_resources/config.json"));
		// Sketch
		processing.size(screen.width, screen.height);
		labelsFont = processing.createFont("Helvetica", 96, true)
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
	  processing.drawPlanets(dt);
	  // Print framerate
	  if (FPS)
	    processing.printFramerate(cur_ts);
	  prev_ts = cur_ts;
	};
	
	
	/*
	 * Draws the background
	 */
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
	 * Draws all the planets
	 */
	processing.drawPlanets = function(dt) {
	  // Remember: dt is in milliseconds since the beginning of the simulation, NOT the launch of the app
	  // Calculate diameter
	  planet_diameter = .6 * processing.height;
	  // Calculate degree to pixels ratio based on the current view angle
	  deg_to_px_ratio = processing.width / hr.getViewAngle();
	  // For each planet...
		hr.planets.forEach(function(pl, i) {
			processing.drawPlanet(dt, pl);
		});
	}
	
	
	/*
	 * Draws a single planets
	 */
	processing.drawPlanet = function (dt, p) {
	  // Get planet color
	  var pc = processing.unhex(p.color.substring(2));
	  processing.fill(pc);
	  // Calculate planet x and y coordinates
	  var x = processing.calculatePlanetPosition(p.startPosition, p.classOrbitalTime, dt);
	  var y = processing.height / 2;
	  // If planet is outside of screen we don't need to draw it
	  if (x < -planet_diameter/2 - 10 || x > processing.width + planet_diameter/2 + 10)
	    return;
	  // Choose planet representation
	  processing.imageMode(processing.CENTER);
		var i;
	  if (p.representation=="sphere")
	    i = images[p.colorName];  // Colored sphere image
	  else if (p.representation=="image")
	    i = images[p.name];  // Planet image 
	  else {
	    console.error("No such a representation for the planet. Choose: sphere or image");
	    return;
	  }
	  // Calculate planet size
	  p_h = planet_diameter;
		
	  if (i===undefined)
	    return;
	  p_w = p_h*(i.width/i.height);
	  // Draw planet
	  processing.image(i, x, y, p_w, p_h);
	  // Draw label
	  if (p.labelType=="none")
	    return;
	  l_x = x;
	  processing.textFont(labelsFont);
	  processing.fill(0);
	  processing.textAlign(processing.CENTER, processing.CENTER);
	  if (p.labelType=="name")
			processing.text(p.name.toUpperCase(), l_x, processing.height/2);
		if (p.labelType=="color")
			processing.text(p.colorName.toUpperCase(), l_x, processing.height/2);
	}
	
	
	/*
	 * Calculates the position of a single planet
	 */
	processing.calculatePlanetPosition = function (deg_0, classOrbitalTime, dt) {
	  // Position in degrees
	  var deg = (deg_0 + .006 * dt / classOrbitalTime) % 360;
	  // Displacement (in deg) from viewAngleEnd (left side of the screen)
	  var deg_displ = hr.viewAngleEnd - deg;
	  if (deg_displ < -180)
	    deg_displ = 360 + deg_displ;
	  return deg_displ * deg_to_px_ratio;
	}
	
	
	/*
	 * Prints the current framerate 
	 */
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

/*
 * Loads json configuration from the server
 */
HelioRoomSketch.getConfigJSON = function(url) {
	request = new XMLHttpRequest();
	request.open('GET', url, false);
	request.send();
	if (request.status !== 200)
	console.err("We reached our target server, but it returned an error");
	return JSON.parse(request.responseText);
}
