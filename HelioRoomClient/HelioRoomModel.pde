
public class HelioRoomModel {

  // Planets representation constants
  public final static String REP_IMAGE     = "image";
  public final static String REP_SPHERE     = "sphere";

  // Planets names constants
  public final static String LABEL_NONE     = "none";
  public final static String LABEL_NAME     = "name";
  public final static String LABEL_COLOR     = "color";
  
  // State constants
  public final static String STATE_RUNNING  = "running";
  public final static String STATE_PAUSED   = "paused";

  // Simulation data
  private long startTime = -1;
  private String state = null;
  private int viewAngleBegin = -1;
  private int viewAngleEnd = -1;
  private List<Planet> planets = null;
  
  // Other data
  private boolean initialized = false;
  
  
  public synchronized double getStartTime() {
    return startTime;
  }

  public synchronized String getState() {
    return state;
  }

  public synchronized double getViewAngleBegin() {
    return viewAngleBegin;
  }

  public synchronized double getViewAngleEnd() {
    return viewAngleEnd;
  }

  public synchronized List<Planet> getPlanets() {
    return planets;
  }


  public synchronized void init(JSONObject json) {
    startTime = json.getInt("startTime");
    state = parseState(json.getString("state"));
    planets = parsePlanets(json.getJSONArray("planets"));
  }
  
  
  public synchronized void setViewAngleBegin(int angle) {
    viewAngleBegin = angle;
  }

  public synchronized void setViewAngleEnd(int angle) {
    viewAngleEnd = angle;
  }
  
  
  public synchronized boolean isInitialized() {
    return initialized;
  }
  
  public synchronized void setInitialized(boolean flag) {
    initialized = flag;
  }
  
  
  public synchronized double getViewAngle() {
    int w = viewAngleEnd - viewAngleBegin;
    if (w > 0)
      return w;
    else
      return (360 + w);
  }


  private String parseState(String state) {
    if (state.equals(STATE_RUNNING))
      return STATE_RUNNING;
    if (state.equals(STATE_PAUSED))
      return STATE_PAUSED;
    throw new RuntimeException("Problems parsing the state");
  }

  private List<Planet> parsePlanets(JSONArray planets) {
    List<Planet> plans = new ArrayList<Planet>();
    for (int i=0; i < planets.size(); i++) {
      plans.add(new Planet(planets.getJSONObject(i)));
    }
    Collections.reverse(plans);
    return plans;
  }
  

}

