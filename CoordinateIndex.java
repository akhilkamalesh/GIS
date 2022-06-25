import java.io.*;
import java.util.ArrayList;

/**
 * CoordinateIndex talks to Command Processor, and uses a tree data structure,
 * and creates a tree and handles the methods that relate to prQuadTree
 * 
 * @author akhilkamalesh
 *
 */
public class CoordinateIndex {

	// Creata a tree
	static prQuadTree<Point> tree;
	static int count;

	// Constructor for the CoordinateIndex object
	public CoordinateIndex() {

	}

	/**
	 * insert method inserts a record into the tree, based on if it is valid or not,
	 * and if it is already in the tree
	 * 
	 * @param database RandomAccessFile
	 * @throws IOException
	 */
	public void insert(RandomAccessFile database) throws IOException {
		database.seek(0);
		String dbStr = database.readLine();
		dbStr = database.readLine();
		while (true) {
			long offset = database.getFilePointer();
			dbStr = database.readLine();
			if (dbStr != null) { // Checking to see if dbStr != null
				GISRecord dbRec = new GISRecord(dbStr, offset);
				int longitude = convertLong(dbRec.getLongitude());
				int latitude = convertLat(dbRec.getLatitude());
				Point insertPoint = new Point(longitude, latitude, offset);
				// checking to see if the point is already in the tree
				if (tree.find(insertPoint) != null) {
					// iterating throught the offsets
					for (int i = 0; i < tree.find(insertPoint).getOffset().size(); i++) {
						if (offset == tree.find(insertPoint).getOffset().get(i)) {
							return;
						}
					}
					tree.find(insertPoint).getOffset().add(offset);
					//count++;
				} else {
					tree.insert(insertPoint);
					count++;
				}
			} else {
				return;
			}
		}
	}
	
//	public int getCount() {
//		return count;
//	}

	/*
	 * Insert method for Coordinate index takes a dbRec object and inserts it into
	 * the tree
	 */
//	public void insert(GISRecord dbRec) {
//		int longitude = convertLong(dbRec.getLongitude());
//		int latitude = convertLat(dbRec.getLatitude());
//		Point insertPoint = new Point(longitude, latitude, dbRec.getOffset());
//		if(tree.find(insertPoint) != null) {
//			for(int i = 0; i < tree.find(insertPoint).getOffset().size(); i++) {
//				if(dbRec.getOffset() != tree.find(insertPoint).getOffset().get(i)) {
//					tree.find(insertPoint).getOffset().add(dbRec.getOffset());
//				}
//			}
//		}
//		tree.insert(insertPoint);	
//	}

	/**
	 * whatIsAt Method calls the tree.find(element) to find the points at the
	 * certain coordinate
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 * @throws IOException
	 */
	public Point whatIsAt(long longitude, long latitude) throws IOException {
		return tree.find(new Point(longitude, latitude));
	}

	/**
	 * whatIsIn method calls tree.find(long, long, long, long) to find the
	 * coordinates in the enclosed location
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Point> whatIsIn(long xMin, long xMax, long yMin, long yMax) throws IOException {

		// Getting the coordinates to put into the prQuadTree.find(...)
		return tree.find(xMin, xMax, yMin, yMax);
	}

	/**
	 * Calls the display method from tree
	 * @param fw
	 * @throws IOException
	 */
	public void show(FileWriter fw) throws IOException {
		tree.printTreeHelper(tree.root, "", fw);
	}

	/**
	 * convertsLongitude into seconds
	 * @param longitude
	 * @return
	 */
	public static int convertLong(String longitude) {
		String loc = longitude.substring(7);
		int degrees, minutes, seconds;
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
	 * converts latitude into seconds
	 * @param latitude
	 * @return
	 */
	public static int convertLat(String latitude) {
		String loc = latitude.substring(6);
		int degrees, minutes, seconds;
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
	 * Sets the world for the valid records to enter
	 * @param westLong
	 * @param eastLong
	 * @param southLat
	 * @param northLat
	 */
	public void setWorld(long westLong, long eastLong, long southLat, long northLat) {
		tree = new prQuadTree(westLong, eastLong, southLat, northLat);
	}

	/*
	 * return the xMin
	 */
	public long getXMin() {
		return tree.xMin;
	}

	/*
	 * returns the xMax
	 */
	public long getXMax() {
		return tree.xMax;
	}

	/*
	 * return the yMin
	 */
	public long getYMin() {
		return tree.yMin;
	}

	/*
	 * return the yMax
	 */
	public long getYMax() {
		return tree.yMax;
	}

}
