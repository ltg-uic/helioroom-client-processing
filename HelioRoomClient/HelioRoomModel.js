function HelioRoomModel(json) {
	this.initialized = true;
	this.startTime = json.startTime;
	this.state = json.state;
	this.planets = HelioRoomModel.parsePlanets(json.planets);	
	this.viewAngleBegin = json.viewAngleBegin ;
	this.viewAngleEnd = json.viewAngleEnd;
}
 
HelioRoomModel.prototype.getViewAngle = function() {
	var w = this.viewAngleEnd - this.viewAngleBegin;
	if (w > 0)
	return w;
	else
	return (360 + w);
}

HelioRoomModel.parsePlanets = function(jsonArray) {
	planets = new Array();
	jsonArray.forEach( function(item, i) {
		planets.push(new Planet(item));
	});
	planets.reverse();
	return planets;
}