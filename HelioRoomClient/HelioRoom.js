function HelioRoomSketch(p) {
	
	// Model
	var hr;
	// Sketch
	var prev_ts = 0;
	var images = {}
	var planet_diameter;
	var deg_to_px_ratio;
	var labelsFont;
	var wrongAspectRatio;
	// FPS indicator
	FPS = false;
	var fps_total = 0;
	var fps_index = 0;
	var fps_size = 30;
	var fps_samples = new Array(fps_size);
	
	
	p.setup = function() {
		p.initializeHRModel();
		p.setSketchSize();
		labelsFont = p.createFont("Helvetica", Math.floor(p.height/9.6+2.38), true);
		p.loadImages();
	  // Initialize FPS counters
		for (i=0; i<fps_size; i++) { 
		    fps_samples[i] = 0;
		}	
	};
	
	
	p.draw = function() {
	  // If there is no data in the model draw a black background and return
	  if (!hr.initialized) {
	    p.background(0);
	    return;
	  }
		if (wrongAspectRatio) {
			p.background(255);
		  p.textSize(18);
		  p.fill(255, 0, 0);	
			p.text("Helioroom cannot run in a canvas with an aspect ratio less than 4/3!", 10, 30);
			return;
		}
	  // Draw stars
	  p.drawTiledStarsBackground();
	  // Calculate dt (in ms)
	  cur_ts = new Date().getTime();
	  dt = cur_ts + hr.startTime*1000;
	  // Draw planets and labels
	  p.drawPlanets(dt);
	  // Print framerate
	  if (FPS)
	    p.printFramerate(cur_ts);
	  prev_ts = cur_ts;
	};
	
	
	/*
	 * Sets the sketch size
	 */
	p.initializeHRModel = function() {
		config_json = HelioRoomSketch.getConfigJSON("helioroom_resources/config.json")
		if (p.viewAngleBegin===undefined || p.viewAngleEnd===undefined) {
			console.warn("Assigning a defaul window of 45º from 0º to 45º! Pass '{viewAngleBegin: 0, viewAngleEnd: 45}' to the sketch to get rid of this warning.")
			config_json.viewAngleBegin = 0
			config_json.viewAngleEnd = 45
		} else {
			config_json.viewAngleBegin = p.viewAngleBegin
			config_json.viewAngleEnd = p.viewAngleEnd
		}
		console.log(config_json)
		hr = new HelioRoomModel(config_json);
	}
	
	
	
	/*
	 * Sets the sketch size
	 */
	p.setSketchSize = function() {
		if (p.fullscreen)
			p.size(screen.width, screen.height);
		else
			p.size(canvas.width, canvas.height);
		if (canvas.width/canvas.height >= 4/3) {
			wrongAspectRatio = false;
		} else {
			wrongAspectRatio = true;
			console.error("Helioroom cannot run in a canvas with an aspect ratio less than 4/3!");
		}
	}
	
	
	/*
	 * Draws the background
	 */
	p.drawTiledStarsBackground = function() {
	  p.imageMode(p.CORNER);
		o_rep = Math.floor(screen.width / images.background.width);
		v_rep = Math.floor(screen.height / images.background.height);
		if (o_rep == Number.POSITIVE_INFINITY || v_rep == Number.POSITIVE_INFINITY) return;
	  for (j=0; j<=v_rep; j++)
			for (i=0; i<=o_rep; i++)
				p.image(images.background, i*images.background.width, j*images.background.height, images.background.width, images.background.height);
	}
	
	
	/*
	 * Draws all the planets
	 */
	p.drawPlanets = function(dt) {
	  // Remember: dt is in milliseconds since the beginning of the simulation, NOT the launch of the app
	  // Calculate diameter
	  planet_diameter = .6 * p.height;
	  // Calculate degree to pixels ratio based on the current view angle
	  deg_to_px_ratio = p.width / hr.getViewAngle();
	  // For each planet...
		hr.planets.forEach(function(pl, i) {
			p.drawPlanet(dt, pl);
		});
	}
	
	
	/*
	 * Draws a single planets
	 */
	p.drawPlanet = function (dt, pl) {
	  // Get planet color
	  var pc = p.unhex(pl.color.substring(2));
	  p.fill(pc);
	  // Calculate planet x and y coordinates
	  var x = p.calculatePlanetPosition(pl.startPosition, pl.classOrbitalTime, dt);
	  var y = p.height / 2;
	  // If planet is outside of screen we don't need to draw it
	  if (x < -planet_diameter/2 - 10 || x > p.width + planet_diameter/2 + 10)
	    return;
	  // Choose planet representation
	  p.imageMode(p.CENTER);
		var i;
	  if (pl.representation=="sphere")
	    i = images[pl.colorName];  // Colored sphere image
	  else if (pl.representation=="image")
	    i = images[pl.name];  // Planet image 
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
	  p.image(i, x, y, p_w, p_h);
	  // Draw label
	  if (pl.labelType=="none")
	    return;
	  l_x = x;
	  p.textFont(labelsFont);
	  p.fill(0);
	  p.textAlign(p.CENTER, p.CENTER);
	  if (pl.labelType=="name")
			p.text(pl.name.toUpperCase(), l_x, p.height/2);
		if (pl.labelType=="color")
			p.text(pl.colorName.toUpperCase(), l_x, p.height/2);
	}
	
	
	/*
	 * Calculates the position of a single planet
	 */
	p.calculatePlanetPosition = function (deg_0, classOrbitalTime, dt) {
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
	p.printFramerate = function(cur_ts) {
		fps_total -= fps_samples[fps_index];
	  fps_samples[fps_index] = 1000/(cur_ts - prev_ts);
	  fps_total += 1000/(cur_ts - prev_ts);
	  if (++fps_index == fps_size) fps_index = 0; 
	  p.textSize(32);
	  p.fill(255, 0, 0);
		framerate = Math.floor(fps_total / fps_size);
	  p.text(framerate + " fps", screen.width - 200, 50);	
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
	p.loadImages = function() {
		// Background
		images = {};
		images.background = p.loadImage("helioroom_resources/stars2.jpeg");
		// Spheres
		images.Blue = p.loadImage("helioroom_resources/colors/blue.png");
		images.Brown = p.loadImage("helioroom_resources/colors/brown.png");
		images.Gray= p.loadImage("helioroom_resources/colors/gray.png");
		images.Green = p.loadImage("helioroom_resources/colors/green.png");
		images.Orange = p.loadImage("helioroom_resources/colors/orange.png");
		images.Pink = p.loadImage("helioroom_resources/colors/pink.png");
		images.Red = p.loadImage("helioroom_resources/colors/red.png");
		images.Yellow = p.loadImage("helioroom_resources/colors/yellow.png");
		// Planets
		images.Earth = p.loadImage("helioroom_resources/planets/earth.png");
		images.Jupiter = p.loadImage("helioroom_resources/planets/jupiter.png");
		images.Mars = p.loadImage("helioroom_resources/planets/mars.png");
		images.Mercury= p.loadImage("helioroom_resources/planets/mercury.png");
		images.Neptune = p.loadImage("helioroom_resources/planets/neptune.png");
		images.Saturn = p.loadImage("helioroom_resources/planets/saturn.png");
		images.Uranus = p.loadImage("helioroom_resources/planets/uranus.png");
		images.Venus = p.loadImage("helioroom_resources/planets/venus.png");
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
