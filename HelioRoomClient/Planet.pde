public class Planet {  
  private String name = null;
  private String colorValue = null;
  private String colorName = null;
  private double classOrbitalTime = -1;
  private double startPosition = 0;
  private String representation = null;
  private String labelType = null;
  
  public Planet(String name, String colorValue, String colorName,
      double classOrbitalTime, double startPosition, String representation, String labelType) {
    this.name = name;
    this.colorValue = colorValue;
    this.colorName = colorName;
    this.classOrbitalTime = classOrbitalTime;
    this.startPosition = startPosition;
    this.representation = representation;
    this.labelType = labelType;
  }
  
  public Planet (JSONObject planet) {
    name = planet.getString("name");
    colorValue = planet.getString("color");
    colorName = planet.getString("colorName");
    classOrbitalTime = planet.getInt("classOrbitalTime");
    startPosition = planet.getInt("startPosition");
    representation = parseRepresentation(planet.getString("representation"));
    labelType = parseLabelType(planet.getString("labelType"));
  }
  
  private String parseRepresentation(String rep) {
    if (rep.equals(HelioRoomModel.REP_IMAGE))
      return HelioRoomModel.REP_IMAGE;
    if (rep.equals(HelioRoomModel.REP_SPHERE))
      return HelioRoomModel.REP_SPHERE;
    throw new RuntimeException("Problems parsing planet representation");
  }
  
  private String parseLabelType(String label) {
    if (label.equals(HelioRoomModel.LABEL_NONE))
      return HelioRoomModel.LABEL_NONE;
    if (label.equals(HelioRoomModel.LABEL_COLOR))
      return HelioRoomModel.LABEL_COLOR;
    if (label.equals(HelioRoomModel.LABEL_NAME))
      return HelioRoomModel.LABEL_NAME;
    throw new RuntimeException("Problems parsing planet label types");
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return colorValue;
  }

  public String getColorName() {
    return colorName;
  }

  public double getClassOrbitalTime() {
    return classOrbitalTime;
  }

  public double getStartPosition() {
    return startPosition;
  }
  
  public String getRepresentation() {
    return representation;
  }
  
  public String getLabelType() {
    return labelType;
  }
  
  
  public void adjustOrbitalTime(int multiplier) {
    this.classOrbitalTime *= multiplier;
  }

}
