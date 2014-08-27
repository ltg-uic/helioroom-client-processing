import controlP5.*;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.util.*;
import java.text.DecimalFormat;
import java.net.InetAddress;
import java.net.SocketException;

// Model
HelioRoomModel hr = null;
JSONObject config;
// Sketch
PImage background_image = null;
PFont labelsFont;
PFont cp5Font;
float planet_diameter;
double deg_to_px_ratio;
long timeOffset = 0;
ControlP5 cp5;
RadioButton r;
Textlabel l;
Map<String, PImage> images = new HashMap<String, PImage>();
// FPS indicator
boolean FPS = false;
long prev_ts;
int fps_size;
double fps_total = 0d;
int fps_index = 0;
double fps_samples[];


////////////////////////
// Processing methods //
////////////////////////

public void setup() {
  // Model
  hr = new HelioRoomModel();
  config = loadJSONObject("helioroom_resources/config.json");
  hr.init(config);
  // Sketch
  size(displayWidth, displayHeight);
  // Create fonts, load resources
  labelsFont = createFont("Helvetica", 96, true);
  cp5Font = createFont("BitFontStandard58", 20, true);
  loadImages();
  // More setup
  setupGUI();        
  updateTime();
  // Initialize FPS counters
  fps_size = 30;
  fps_samples = new double[fps_size];
  for (int i = 0; i < fps_size; i++) fps_samples[i] = 0d;
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
  long cur_ts = new Date().getTime();
  double dt = cur_ts + timeOffset - hr.getStartTime()*1000;
  // Draw planets and labels
  drawPlanets(dt);
  // Print framerate
  if (FPS)
    printFramerate(prev_ts, cur_ts);
  prev_ts = cur_ts;
}


/////////////////////
// Drawing methods //
/////////////////////

private void drawTiledStarsBackground() {
  imageMode(CORNER);
  int o_rep = width/background_image.width;
  int v_rep = height/background_image.height;
  for (int j=0;j<=v_rep;j++)
    for (int i=0;i<=o_rep;i++)
      image(background_image, i*background_image.width, j*background_image.height, background_image.width, background_image.height);
}

private void drawPlanets(double dt) {
  // Remember: dt is in milliseconds since the beginning of the simulation, not the launch of the app
  // Calculate diameter
  planet_diameter = .6f*height;
  // Calculate degree to pixels ratio based on the current view angle
  deg_to_px_ratio = ((double) width) / hr.getViewAngle();
  // For each planet...
  for (Planet p: hr.getPlanets())
    drawPlanet(dt, p);
}

private void drawPlanet(double dt, Planet p) {
  // Get planet color
  int pc = unhex(p.getColor().substring(2));
  fill(pc);
  // Calculate planet x and y coordinates
  float x = calculatePlanetPosition(p.getStartPosition(), p.getClassOrbitalTime(), dt, p);
  float y = height/2;
  // If planet is outside of screen we don't need to draw it
  if (x < -planet_diameter/2 - 10 || x > width + planet_diameter/2 + 10)
    return;
  // Choose planet representation
  imageMode(CENTER);
  PImage i= null;
  if (p.getRepresentation().equals(HelioRoomModel.REP_SPHERE))
    i = images.get(p.getColorName());  // Colored sphere image
  else if (p.getRepresentation().equals(HelioRoomModel.REP_IMAGE))
    i = images.get(p.getName());  // Planet image 
  else {
    System.err.println("ERROR: no such a representation for the planet. Choose: sphere or image");
    return;
  }
  // Calculate planet size
  float p_h = planet_diameter;
  if (i==null)
    return;
  float p_w = p_h*(i.width/i.height);
  // Draw planet
  image(i, x, y, p_w, p_h);
  // Draw label
  if (p.getLabelType().equals(HelioRoomModel.LABEL_NONE))
    return;
  float l_x = x;
  textFont(labelsFont);
  fill(0);
  textAlign(CENTER, CENTER);
  if (p.getLabelType().equals(HelioRoomModel.LABEL_NAME))
    text(p.getName().toUpperCase(), l_x, height/2);
  if (p.getLabelType().equals(HelioRoomModel.LABEL_COLOR))
    text(p.getColorName().toUpperCase(), l_x, height/2);
}

private float calculatePlanetPosition(double deg_0, double classOrbitalTime, double dt, Planet p) {
  // Position in degrees
  double deg = (deg_0 + .006d * dt / classOrbitalTime) % 360;
  // Displacement (in deg) from viewAngleEnd (left side of the screen)
  double deg_displ = hr.getViewAngleEnd() - deg;
  if (deg_displ < -180)
    deg_displ = 360 + deg_displ;
  return (float) (deg_displ * deg_to_px_ratio);
}

private void printFramerate(double prev_ts, double cur_ts) {
  fps_total -= fps_samples[fps_index];
  fps_samples[fps_index] = 1000/(cur_ts - prev_ts);
  fps_total += 1000/(cur_ts - prev_ts);
  if (++fps_index == fps_size) fps_index = 0; 
  DecimalFormat formatter = new DecimalFormat("#0");     
  textSize(32);
  fill(255, 0, 0);
  text(formatter.format(fps_total / (double)fps_size) + " fps", width - 200, 50);
}


////////////////////
// Helper methods //
////////////////////

private void setupGUI() {
  cp5 = new ControlP5(this);
  cp5.setFont(cp5Font);
  r = cp5.addRadioButton("radioButton")
    .setPosition(50, 20)
      .setSize(50, 50)
        .setItemsPerRow(1)
            .addItem("A", 1)
                .addItem("B", 2)
                    .addItem("C", 3)
                        .addItem("D", 4);
                          
  if (background_image==null)
    l = cp5.addTextlabel("assets_warn")
           .setText("Can't seem to find folder: " + sketchPath+ "/helioroom_resources \n" + 
             "Please make sure the 'helioroom_resources' folder shipped with this demo is in said location.")
           .setPosition(50, 300)
           .setColorValue(0xffff0000)
           .setFont(createFont("BitFontStandard58", 20));
}


void radioButton(int a) {
  // Sketch
  r.deactivateAll();
  r.setVisible(false);
  String username = null;
  switch (a) {
  case 1:
    username = "hr-w1";
    hr.setViewAngleBegin(0);
    hr.setViewAngleEnd(10);
    break;
  case 2:
    username = "hr-w2";
    hr.setViewAngleBegin(90);
    hr.setViewAngleEnd(100);
    break;
  case 3:
    username = "hr-w3";
    hr.setViewAngleBegin(180);
    hr.setViewAngleEnd(190);
    break;
  case 4:
    username = "hr-w4";
    hr.setViewAngleBegin(270);
    hr.setViewAngleEnd(280);
    break;
  }    
  hr.setInitialized(true);
}


private void loadImages() {
  background_image = loadImage("helioroom_resources/stars2.jpeg");
  // Spheres
  images.put("Blue", loadImage("helioroom_resources/colors/blue.png"));
  images.put("Brown", loadImage("helioroom_resources/colors/brown.png"));
  images.put("Gray", loadImage("helioroom_resources/colors/gray.png"));
  images.put("Green", loadImage("helioroom_resources/colors/green.png"));
  images.put("Orange", loadImage("helioroom_resources/colors/orange.png"));
  images.put("Pink", loadImage("helioroom_resources/colors/pink.png"));
  images.put("Red", loadImage("helioroom_resources/colors/red.png"));
  images.put("Yellow", loadImage("helioroom_resources/colors/yellow.png"));
  // Planets
  images.put("Earth", loadImage("helioroom_resources/planets/earth.png"));
  images.put("Jupiter", loadImage("helioroom_resources/planets/jupiter.png"));
  images.put("Mars", loadImage("helioroom_resources/planets/mars.png"));
  images.put("Mercury", loadImage("helioroom_resources/planets/mercury.png"));
  images.put("Neptune", loadImage("helioroom_resources/planets/neptune.png"));
  images.put("Saturn", loadImage("helioroom_resources/planets/saturn.png"));
  images.put("Uranus", loadImage("helioroom_resources/planets/uranus.png"));
  images.put("Venus", loadImage("helioroom_resources/planets/venus.png"));
}


private void updateTime() {
  NTPUDPClient client = new NTPUDPClient();
  // We want to timeout if a response takes longer than 3 seconds
  client.setDefaultTimeout(3000);
  try {
    client.open();
    try {
      InetAddress hostAddr = InetAddress.getByName("us.pool.ntp.org");
      TimeInfo t = client.getTime(hostAddr);
      t.computeDetails();
      timeOffset = t.getOffset();
      if (timeOffset > 0) 
        println("The clock on this computer runs " + timeOffset + " ms late.");
      else
        println("The clock on this computer is " + -timeOffset + " ms ahead.");
    } 
    catch (IOException ioe) {
      return;
    }
  } 
  catch (SocketException e) {
    return;
  } finally {
    client.close();
  }
  
}

