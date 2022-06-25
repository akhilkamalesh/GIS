import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * CommandProcessor class processes all the commands and sends them to the GIS
 * class (main class)
 * 
 * @author akhilkamalesh
 *
 */
public class CommandProcessor {

	public FileWriter fw;
	public static CoordinateIndex coorInd;
	public static NameIndex nameInd;
	public static BufferIndex buffInd;
	static boolean firstImport = true;
	static int whatIsInCount;
	static int count = 0;

	/**
	 * CommandProcess constructs that takes in FileWriter, CoordinateIndex,
	 * NameIndex, BufferIndex
	 * 
	 * @param logFw
	 * @param cI    CoordinateIndex
	 * @param nI    NameIndex
	 * @param bI    BufferIndex
	 */
	public CommandProcessor(FileWriter logFw, CoordinateIndex cI, NameIndex nI, BufferIndex bI) {
		fw = logFw;
		coorInd = cI;
		nameInd = nI;
		buffInd = bI;
	}

	/**
	 * Creates the world for which the valid records
	 * 
	 * @param westLong int
	 * @param eastLong int
	 * @param southLat int
	 * @param northLat int
	 * @return prQuadTree
	 */
	public static prQuadTree world(int westLong, int eastLong, int southLat, int northLat) {
		// Create a tree that is the world
		prQuadTree tree = new prQuadTree(westLong, eastLong, southLat, northLat);
		return tree;
	}

	/**
	 * Method imports the valid records from the gis records database file into a
	 * personal database file
	 * 
	 * @param randAccFile RandomAccessFIl
	 * @param dbRaf       RandomAccessFIl
	 * @param logFw       FileWRiter
	 * @param coorInd     Coordinateindex
	 * @throws IOException
	 */
	public static void importFile(RandomAccessFile randAccFile, RandomAccessFile dbRaf, FileWriter logFw,
			CoordinateIndex coorInd) throws IOException {
		randAccFile.seek(dbRaf.length());
		String gisStr = randAccFile.readLine(); // Reads in the first line
		// System.out.println("|" + gisStr + "|");
		// System.out.println("|"+String.join(" ", gisStr.getBytes()+"|");
		// dbRaf.write(gisStr.getBytes()); // Write the first line to the database file
		// dbRaf.writeUTF("\n");

		if (!firstImport) {
			gisStr = randAccFile.readLine();
		} else {
			firstImport = false;
		}
		if (gisStr != null) {
			dbRaf.write(gisStr.getBytes());
			dbRaf.write("\n".getBytes());
		}
		// dbRaf.writeUTF("\n");
		int names = 0;

		/*
		 * Checking to see if the point is within the parameters; if so write to
		 * database file
		 */
		while (true) {
			long offset = randAccFile.getFilePointer(); // Grabs the file pointer from previous line
			gisStr = randAccFile.readLine(); // Reads the first line
			// System.out.println(gisStr);
			if (gisStr != null) {
				// create new GISRecord object
				GISRecord gisRec = new GISRecord(gisStr, offset);

				// Grab the longitude and latitude
				// System.out.println(gisRec.getLongitude());
				int longitude = convertLong(gisRec.getLongitude());
				int latitude = convertLat(gisRec.getLatitude());

				Point testPoint = new Point(longitude, latitude);

				// check to see if the point is inside the tree (which will be initialized in
				// the main method through world()
				if (testPoint.inBox(coorInd.getXMin(), coorInd.getXMax(), coorInd.getYMin(), coorInd.getYMax())) {
					names += gisRec.getFeatureName().length();
					dbRaf.write(gisStr.getBytes());
					dbRaf.write("\n".getBytes());
					count++;
				}

			} else {
				// logFw.write("Imported Features by name: " + count + "\n");
				//logFw.write("Imported locations: " + coorInd.getCount() + "\n");
				// logFw.write("average name length: " + names / count);
				return;
			}
		}
	}
	
	public int getCount() {
		return count;
	}
	/**
	 * Creates the hash table based from the valid records
	 * 
	 * @param database RandomAccessFile
	 * @throws IOException
	 */
	public void createHash(RandomAccessFile database) throws IOException {
		nameInd.insert(database);

//		database.seek(0);
//		String dbStr = database.readLine(); // Read the first line of the database
//		while (true) {
//			long offset = database.getFilePointer();
//			dbStr = database.readLine();
//			// System.out.println(dbStr);
//			if (dbStr != null) {
//				GISRecord dbRec = new GISRecord(dbStr, offset);
//				nameInd.insert(dbRec);
//			} else {
//				return;
//			}
//		}
//		database.seek(0);
//		String dbStr = database.readLine(); // Read the first line of the database
//		while (true) {
//			long offset = database.getFilePointer();
//			dbStr = database.readLine();
//			// System.out.println(dbStr);
//			if (dbStr != null) {
//				GISRecord dbRec = new GISRecord(dbStr, offset);
//				String nameHash = dbRec.getFeatureName() + ":" + dbRec.getState();
//				nameEntry nE = new nameEntry(nameHash, offset);
//				// System.out.println(nE);
//				// System.out.println(nE.state());
//				if (hashT.find(nE) != null) {
//
//					nameEntry additional = (nameEntry) hashT.find(nE);
//					for (int i = 0; i < nE.locations().size(); i++) {
//						additional.addLocation(nE.locations().get(i));
//					}
//
//				}
//				hashT.insert(nE);
//			} else {
//				return;
//			}
//		}
	}

	/**
	 * Creates the tree and inserts points into the tree
	 * 
	 * @param database RandomAccessFile
	 * @throws IOException
	 */
	public void createTree(RandomAccessFile database) throws IOException {
		coorInd.insert(database);

//		database.seek(0);
//		String dbStr = database.readLine(); // Read the first line of the database
//		dbStr = database.readLine();
//		while (dbStr != null) {
//			long offset = database.getFilePointer();
//			dbStr = database.readLine();
//			if (dbStr != null) {
//				GISRecord dbRec = new GISRecord(dbStr, offset);
//				coorInd.insert(dbRec);
//			} else {
//				return;
//			}
//		}
		// coorInd.insert(database);
//		database.seek(0);
//		String dbStr = database.readLine(); // Read the first line of the database
//		while (true) {
//			long offset = database.getFilePointer();
//			dbStr = database.readLine();
//			if (dbStr != null) {
//				GISRecord dbRec = new GISRecord(dbStr, offset);
//				// System.out.println(dbRec.getFeatureName() + " : " + offset);
//				int longitude = convertLong(dbRec.getLongitude());
//				int latitude = convertLat(dbRec.getLatitude());
//				// Point testPoint = new Point(longitude, latitude);
//				// System.out.println(testPoint);
//				Point insertPoint = new Point(longitude, latitude, offset);
//				// System.out.println(insertPoint.getOffset());
//				// System.out.println(insertPoint);
//				tree.insert(insertPoint);
////				if (tree.find(insertPoint) != null) {
////					System.out.println(tree.find(insertPoint).getOffset());
////				}
//			} else {
//				return;
//			}
//		}
	}

	/**
	 * Shows the hashtable
	 * 
	 * @param fw FileWriter
	 * @throws IOException
	 */
	public void hashShow(FileWriter fw) throws IOException {
		nameInd.show(fw);
	}

	/**
	 * Shows the quadTree
	 * 
	 * @param fw FileWriter
	 * @throws IOException
	 */
	public void quadShow(FileWriter fw) throws IOException {
		coorInd.show(fw);
	}

	/**
	 * Shows the bufferPool
	 * 
	 * @param fw FileWriter
	 * @throws IOException
	 */
	public void bufferShow(FileWriter fw) throws IOException {
		buffInd.show(fw);
	}

	/**
	 * For every GIS record in the database file that matches the given <feature
	 * name> and <state abbreviation>, log the offset at which the record was found,
	 * and the county name, the primary latitude, and the primary longitude. Do not
	 * log any other data from the matching records.
	 * 
	 * @param featName String
	 * @param state    String
	 * @param database RandomAccessFile
	 * @param logFw    FileWriter
	 * @throws IOException
	 */
	public void whatIs(String featName, String state, RandomAccessFile database, FileWriter logFw) throws IOException {
		nameEntry ne = new nameEntry(featName + ":" + state);
		// System.out.println(state);
		nameEntry found = nameInd.whatIs(featName, state);
		// System.out.println(ne.key());
		if (found != null) {
			for (int i = 0; i < found.locations().size(); i++) {
				database.seek(found.locations().get(i));
				Long offset = database.getFilePointer();
				String gisStr = database.readLine();
				if (buffInd.find(gisStr, offset) == false) {
					buffInd.insert(gisStr, offset);
				} else {
					buffInd.replace(gisStr, offset);
				}
				GISRecord gisRec = new GISRecord(gisStr, offset);
				logFw.write("\t" + found.locations().get(i) + ": " + gisRec.getCounty() + "\t ("
						+ formatLong(gisRec.getLongitude()) + ", " + formatLat(gisRec.getLatitude()) + ")\n");
			}
		} else {
			logFw.write("No records match " + featName + " " + state + "\n");
		}
	}

	/**
	 * whatIsAt For every GIS record in the database file that matches the given
	 * <geographic coordinate>, log the offset at which the record was found,r
	 * 
	 * @param longitude
	 * @param latitude
	 * @param database
	 * @param logFw
	 * @throws IOException
	 */
	public static void whatIsAt(long longitude, long latitude, RandomAccessFile database, FileWriter logFw)
			throws IOException {

		Point findPoint = new Point(longitude, latitude);
		// System.out.println("find point: " + findPoint.getX());
		Point foundPoint = coorInd.whatIsAt(longitude, latitude);
		// System.out.println("found point: " + foundPoint);
		if (foundPoint != null) {
			// System.out.println("found point offset: " + findPoint.getOffset());
			ArrayList<Long> offset = foundPoint.getOffset();
			for (int i = 0; i < offset.size(); i++) {
				database.seek(offset.get(i));
				String gisStr = database.readLine();
				if (buffInd.find(gisStr, offset.get(i)) == false) {
					buffInd.insert(gisStr, offset.get(i));
				} else {
					buffInd.replace(gisStr, offset.get(i));
				}
				GISRecord gisRec = new GISRecord(gisStr, offset.get(i));
				logFw.write(offset.get(i) + ": " + gisRec.getFeatureName() + "\t" + gisRec.getCounty() + "\t"
						+ gisRec.getState() + "\n\t");
			}
		} else {
			String longi = "" + longitude;
			String latit = "" + latitude;
			logFw.write("Nothing was found at + (" + formatLong(longi) + "," + formatLat(latit) + ")\n");
		}
	}

	/**
	 * For every GIS record in the database file whose coordinates fall within the
	 * closed rectangle with the specified height and width, centered at the
	 * <geographic coordinate>, log the offset at which the record was found, and
	 * the feature name, the state name, and the primary latitude and primary
	 * longitude. Do not log any other data from the matching records. The
	 * half-height and half-width are specified as seconds.
	 * 
	 * @param longitude  long
	 * @param latitude   long
	 * @param halfHeight long
	 * @param halfWidth  long
	 * @param database   RandomAccessFuke
	 * @param logFw      FileWriter
	 * @throws IOException
	 */
	public static void whatIsIn(long longitude, long latitude, long halfHeight, long halfWidth,
			RandomAccessFile database, FileWriter logFw) throws IOException {

		// Getting the coordinates to put into the prQuadTree.find(...)
		long xMin = longitude - halfWidth;
		long xMax = longitude + halfWidth;
		long yMin = latitude - halfHeight;
		long yMax = latitude + halfHeight;
		ArrayList<Point> foundPoints = new ArrayList();
		foundPoints = coorInd.whatIsIn(xMin, xMax, yMin, yMax);
		int printed = 0;

		// iterating through the foundPoints
		for (int i = 0; i < foundPoints.size(); i++) {
			for (int j = 0; j < foundPoints.get(i).getOffset().size(); j++) {
				database.seek(foundPoints.get(i).getOffset().get(j));
				// System.out.println(foundPoints.get(i).getOffset());
				Long offset = foundPoints.get(i).getOffset().get(j);
				String gisStr = database.readLine();
				if (buffInd.find(gisStr, offset) == false) {
					buffInd.insert(gisStr, offset);
				} else {
					buffInd.replace(gisStr, offset);
				}
				GISRecord gisRec = new GISRecord(gisStr, offset);
				// whatIsInCount++;
				logFw.write("\t" + offset + ": " + gisRec.getFeatureName() + "\t" + gisRec.getState() + "\t("
						+ formatLong(gisRec.getLongitude()) + " +/- " + halfWidth + ", "
						+ formatLat(gisRec.getLatitude()) + " +/- " + halfHeight + ")\n");
				printed++;
			}
		}
		String longi = "" + longitude;
		String latit = "" + latitude;
		if (printed == 0) {
			logFw.write("Nothing found in (" + formatLong(longi) + " +/- " + halfWidth + ", " + formatLat(latit)
					+ " +/- " + halfHeight + ")\n");
		}

	}

	/**
	 * Getter for coordinate index
	 * 
	 * @return
	 */
	public CoordinateIndex getCoordinateIndex() {
		return coorInd;
	}

	/**
	 * Getter for nameindex
	 * 
	 * @return
	 */
	public NameIndex getNameIndex() {
		return nameInd;
	}

	/**
	 * converts longitude into seconds
	 * 
	 * @param longitude
	 * @return
	 */
	public static int convertLong(String longitude) {
		String loc = longitude.substring(7);
		int degrees, minutes, seconds;
		if (longitude.equals("Unknown")) {
			return 0;
		}
		degrees = Integer.parseInt(longitude.substring(0, 3));
		minutes = Integer.parseInt(longitude.substring(3, 5));
		seconds = Integer.parseInt(longitude.substring(5, 7));

		int dms = 3600 * degrees + 60 * minutes + seconds;

		if (loc.contains("W")) {
			dms = dms * -1;
		}

		return dms;
	}

	/**
	 * Converts latitude into seconds
	 * 
	 * @param latitude
	 * @return
	 */
	public static int convertLat(String latitude) {
		String loc = latitude.substring(6);
		int degrees, minutes, seconds;
		if (latitude.equals("Unknown")) {
			return 0;
		}
		degrees = Integer.parseInt(latitude.substring(0, 2));
		minutes = Integer.parseInt(latitude.substring(2, 4));
		seconds = Integer.parseInt(latitude.substring(4, 6));

		int dms = 3600 * degrees + 60 * minutes + seconds;

		if (loc.contains("S")) {
			dms = dms * -1;
		}

		return dms;
	}

	/**
	 * formats longitude
	 * 
	 * @param lon longitude
	 * @return
	 */
	public static String formatLong(String lon) {
		String days;
		if (lon.charAt(0) == '0') {
			days = lon.substring(1, 3);
		} else {
			days = lon.substring(0, 3);
		}

		String min;
		if (lon.charAt(3) == '0') {
			min = lon.substring(4, 5);
		} else {
			min = lon.substring(3, 5);
		}

		String sec;
		if (lon.charAt(5) == '0') {
			sec = lon.substring(6, 7);
		} else {
			sec = lon.substring(5, 7);
		}

		String loc = lon.substring(7);
		switch (loc) {
		case "W":
			loc = "West";
			break;
		case "E":
			loc = "East";
			break;
		}

		return (days + "d " + min + "m " + sec + "s " + loc);
	}

	/**
	 * formats latitude
	 * 
	 * @param latitude
	 * @return
	 */
	public static String formatLat(String lat) {
		String days = lat.substring(0, 2);

		String min;
		if (lat.charAt(3) == '0') {
			min = lat.substring(3, 4);
		} else {
			min = lat.substring(2, 4);
		}

		String sec;
		if (lat.charAt(4) == '0') {
			sec = lat.substring(5, 6);
		} else {
			sec = lat.substring(4, 6);
		}

		String loc = lat.substring(6);
		switch (loc) {
		case "N":
			loc = "North";
			break;
		case "S":
			loc = "South";
			break;
		}

		return (days + "d " + min + "m " + sec + "s " + loc);
	}
}
