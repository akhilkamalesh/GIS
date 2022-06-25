// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, including the Internet, either
// modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the grading code.
//
// Akhil Kamalesh
// akhilk24@vt.edu

/**
 * 
 */

/**
 * @author akhilkamalesh
 *
 */
public class GISRecord{

	private String featureName;
	private String featureClass;
	private String state;
	private String county;
	private String latitude;
	private String longitude;
//	private double latitudeDMS;
//	private long longitudeDMS;
	private int elevation;
	private Long offset;

	/*
	 * Constructor for gisRecord param: String gisRecord whenever it gets read in
	 * from the textfile
	 */
	public GISRecord(String gisRecord, Long os) {
		offset = os;
		String[] gisArr = gisRecord.split("\\|");
		featureName = gisArr[1];
		featureClass = gisArr[2];
		state = gisArr[3];
		county = gisArr[5];
		latitude = gisArr[7];
		longitude = gisArr[8];
//		latitudeDMS = Long.parseLong(gisArr[9]);
//		longitudeDMS = Long.parseLong(gisArr[10]);
		if (!gisArr[16].equals("")) {
			elevation = Integer.parseInt(gisArr[16]);
		}
 	}

	// Getter for featureName
	public String getFeatureName() {
		return featureName;
	}
	
	// Getter for featureClass
	public String getFeatureClass() {
		return featureClass;
	}
	
	// Getter for state
	public String getState() {
		return state;
	}
	
	// Getter for county
	public String getCounty() {
		return county;
	}

	// Getter for latitude
	public String getLatitude() {
		return latitude;
	}

	// Getter for longitude
	public String getLongitude() {
		return longitude;
	}
	
//	// Getter for latitudeDMS
//	public long getLatitudeDMS() {
//		return latitudeDMS;
//	}
//	
//	// Getter for longitudeDMS
//	public long getLongitudeDMS() {
//		return longitudeDMS;
//	}
	
	// Getter for elevation
	public int getElevation() {
		return elevation;
	}

	// Getter for offset
	public Long getOffset() {
		return offset;
	}
}