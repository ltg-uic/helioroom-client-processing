package ltg.helioroom;

public class Planet {
	
	private String name = null;
	private String color = null;
	private String colorName = null;
	private double classOrbitalTime = -1;
	private double startPosition = 0;
	private String representation = null;
	private String labelType = null;
	
	public Planet(String name, String color, String colorName,
			double classOrbitalTime, double startPosition, String representation, String labelType) {
		this.name = name;
		this.color = color;
		this.colorName = colorName;
		this.classOrbitalTime = classOrbitalTime;
		this.startPosition = startPosition;
		this.representation = representation;
		this.labelType = labelType;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public void setClassOrbitalTime(double classOrbitalTime) {
		this.classOrbitalTime = classOrbitalTime;
	}

	public void setStartPosition(double startPosition) {
		this.startPosition = startPosition;
	}	
	
	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

}
