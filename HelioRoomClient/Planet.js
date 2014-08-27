function Planet (json) {
	this.name = json.name;
	this.colorValue = json.color;
	this.colorName = json.colorName;
	this.classOrbitalTime = json.classOrbitalTime;
	this.startPosition = json.startPosition;
	this.representation = json.representation;
	this.labelType = json.labelType;
}

Planet.prototype.adjustOrbitalTime = function (multiplier) {
	this.classOrbitalTime *= multiplier;
}