package ltg.helioroom.model;


import ltg.commons.phenomena_handler.PhenomenaXMLUtils;

import org.dom4j.DocumentException;
import org.dom4j.Element;

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
	
	public Planet (Element planet) throws DocumentException{
		name = PhenomenaXMLUtils.parseStringElement(planet, "name");
		color = PhenomenaXMLUtils.parseStringElement(planet, "color");
		colorName = PhenomenaXMLUtils.parseStringElement(planet, "colorName");
		classOrbitalTime = PhenomenaXMLUtils.parseDoubleElement(planet, "classOrbitalTime");
		startPosition = PhenomenaXMLUtils.parseDoubleElement(planet, "startPosition");
		representation = parseRepresentation(planet, "representation");
		labelType = parseLabelType(planet, "labelType");
		
	}
	
	private String parseRepresentation(Element planet, String element) throws DocumentException {
		String rep = PhenomenaXMLUtils.parseStringElement(planet, element);
		if (rep.equals(HelioRoomModel.REP_IMAGE))
			return HelioRoomModel.REP_IMAGE;
		if (rep.equals(HelioRoomModel.REP_SPHERE))
			return HelioRoomModel.REP_SPHERE;
		throw new DocumentException();
	}
	
	private String parseLabelType(Element planet, String element) throws DocumentException {
		String label = PhenomenaXMLUtils.parseStringElement(planet, element);
		if (label.equals(HelioRoomModel.LABEL_NONE))
			return HelioRoomModel.LABEL_NONE;
		if (label.equals(HelioRoomModel.LABEL_COLOR))
			return HelioRoomModel.LABEL_COLOR;
		if (label.equals(HelioRoomModel.LABEL_NAME))
			return HelioRoomModel.LABEL_NAME;
		throw new DocumentException();
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
