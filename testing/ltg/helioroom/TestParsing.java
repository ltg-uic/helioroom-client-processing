package ltg.helioroom;
import static org.junit.Assert.*;

import org.junit.Test;


public class TestParsing {
	
	private static String xml = "<helioroom> <viewAngleBegin>45</viewAngleBegin> <viewAngleEnd>0</viewAngleEnd> <planetRepresentation>spheres</planetRepresentation> <planetNames>color</planetNames> <state>running</state> <startTime>1306244459</startTime> <planets> <planet> <name>Mercury</name> <color>0xFF8B4513</color> <colorName>Brown</colorName> <classOrbitalTime>2</classOrbitalTime> <startPosition>1.0</startPosition> </planet> <planet> <name>Venus</name> <color>0xFFFFB6C1</color> <colorName>Pink</colorName> <classOrbitalTime>5</classOrbitalTime> <startPosition>41.0</startPosition> </planet> <planet> <name>Earth</name> <color>0xFFCC0000</color> <colorName>Red</colorName> <classOrbitalTime>8</classOrbitalTime> <startPosition>81.0</startPosition> </planet> <planet> <name>Mars</name> <color>0xFF3D3D3D</color> <colorName>Gray</colorName> <classOrbitalTime>16</classOrbitalTime> <startPosition>121.0</startPosition> </planet> <planet> <name>Jupiter</name> <color>0xFF00EE00</color> <colorName>Green</colorName> <classOrbitalTime>98</classOrbitalTime> <startPosition>161.0</startPosition> </planet> <planet> <name>Saturn</name> <color>0xFFFFFF00</color> <colorName>Yellow</colorName> <classOrbitalTime>244</classOrbitalTime> <startPosition>201.0</startPosition> </planet> <planet> <name>Uranus</name> <color>0xFFFF6600</color> <colorName>Orange</colorName> <classOrbitalTime>695</classOrbitalTime> <startPosition>241.0</startPosition> </planet> <planet> <name>Neptune</name> <color>0xFF0000EE</color> <colorName>Blue</colorName> <classOrbitalTime>1359</classOrbitalTime> <startPosition>281.0</startPosition> </planet> <planet> <name>Pluto</name> <color>0xFF9900CC</color> <colorName>Purple</colorName> <classOrbitalTime>2059</classOrbitalTime> <startPosition>321.0</startPosition> </planet> </planets> </helioroom>";

	@Test
	public void testParsing() {
		fail("Not yet implemented");
	}
	
	
}
