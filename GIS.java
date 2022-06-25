import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//On my honor:
//
//- I have not discussed the Java language code in my program with
//anyone other than my instructor or the teaching assistants
//assigned to this course.
//
//- I have not used Java language code obtained from another student,
//or any other unauthorized source, including the Internet, either
//modified or unmodified.
//
//- If any Java language code or documentation used in my program
//was obtained from another source, such as a text book or course
//notes, that has been clearly noted with a proper citation in
//the comments of my program.
//
//- I have not designed this program in such a way as to defeat or
//interfere with the normal operation of the grading code.
//
//Akhil Kamalesh
//akhilk24

/**
 * GIS is the main class that hold the command processor object. This class will
 * be responsible for printing ot the log file
 * 
 * @param args
 * @throws IOException
 */

public class GIS {

	/**
	 * main method for the GIS
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Initializing the args
		RandomAccessFile dB = new RandomAccessFile(args[0], "rw"); // setting dB to args[0]
		dB.setLength(0);
		RandomAccessFile commandScripts = new RandomAccessFile(args[1], "r"); // args[1] would be scripts file
		File output = new File(args[2]); // setting output to args[2]
		FileWriter fw = new FileWriter(output); // writing to the output file
		// FileWriter dbFw = new FileWriter(args[0]);
		NameIndex nameIndex = new NameIndex();
		CoordinateIndex coorIndex = new CoordinateIndex();
		BufferIndex bufferIndex = new BufferIndex();
		// coorIndex.setWorld(xMin, xMax, yMin, yMax);

		// create commandProcessor object
		CommandProcessor commandProcessor = new CommandProcessor(fw, coorIndex, nameIndex, bufferIndex);
		int commandCount = 0;

		commandScripts.seek(0); // setting the pointer to the start of the file

		String line = commandScripts.readLine();
		fw.write(line + "\n");

		line = commandScripts.readLine();
		fw.write(line + "\n");

		line = commandScripts.readLine();
		fw.write(line + "\n");

		line = commandScripts.readLine();
		fw.write(line + "\n");

		line = commandScripts.readLine();

		// Checking to see if the line is not null
		while (line != null) {
			if (line.contains(";")) {
				fw.write(line + "\n");
			} else {
				// case for quit
				if (line.contains("quit")) {
					commandCount++;
					fw.write("Command " + commandCount + ": quit\n\n");
					fw.write("Terminating execution of commands.\n");
					java.util.Date date = new java.util.Date();
					fw.write("End time: " + date + "\n");
					fw.write("--------------------------------------------------------\n");
					fw.close();
					// case for world
				} else if (line.contains("world")) {
					fw.write(line + "\n");

					fw.write("\n");
					fw.write("GIS Program\n");
					fw.write("\n");

					fw.write("dbFile:\t " + args[0] + "\n" + "scripts:\t " + args[1] + "\n" + "log:\t " + args[2]
							+ "\n");
					java.util.Date date = new java.util.Date();
					fw.write("Start time: " + date + "\n");
					fw.write("Quadtree children are printed in the order SW  SE  NE  NW\n");
					fw.write("--------------------------------------------------------\n");
					fw.write(
							"Latitude/longitude values in index entries are shown as signed integers, in total seconds\n");
					fw.write("World boundaries are set to:\n");

					// Splitting the lines to get the long/lat values
					String[] split = line.split("\t");
					int westLong = coorIndex.convertLong(split[1]);
					int eastLong = coorIndex.convertLong(split[2]);
					int southLat = coorIndex.convertLat(split[3]);
					int northLat = coorIndex.convertLat(split[4]);

					fw.write("\t\t " + northLat + "\n");
					fw.write("\t" + westLong + "\t\t" + eastLong + "\n");
					fw.write("\t\t" + southLat + "\n");
					fw.write("--------------------------------------------------------\n");

					// Initialize the tree with the coordinates of the world
					coorIndex.setWorld(westLong, eastLong, southLat, northLat);
					CommandProcessor.world(westLong, eastLong, southLat, northLat);
					// System.out.println(tree.xMin + " " + tree.xMax + " " + tree.yMin + " " +
					// tree.yMax);

					// case for import
				} else if (line.contains("import")) {
					dB.seek(dB.length());
					commandCount++;
					String[] split = line.split("\t");
					RandomAccessFile records = new RandomAccessFile(split[1], "r");
					fw.write("Command " + commandCount + ": import\t" + split[1] + "\n");
					fw.write("\n");
					commandProcessor.importFile(records, dB, fw, coorIndex); // creation of the database file
					commandProcessor.createHash(dB);
					commandProcessor.createTree(dB);
					fw.write("Imported features by name: " + commandProcessor.getCount() + "\n");
					fw.write("Imported locations: " + commandProcessor.getCount());
					fw.write("\n");
					fw.write("--------------------------------------------------------\n");

					// case for quad
				} else if (line.contains("quad")) {
					commandCount++;
					fw.write("Command " + commandCount + ": show quad\n");
					commandProcessor.quadShow(fw);
					fw.write("\n");
					fw.write("--------------------------------------------------------\n");

					// case for hash
				} else if (line.contains("hash")) {
					commandCount++;
					fw.write("Command " + commandCount + ": show hash\n");
					commandProcessor.hashShow(fw);
					fw.write("--------------------------------------------------------\n");

					// case for pool
				} else if (line.contains("pool")) {
					commandCount++;
					fw.write("Command " + commandCount + ": show pool\n");
					fw.write("MRU\n");
					commandProcessor.bufferShow(fw);
					fw.write("LRU\n");
					fw.write("--------------------------------------------------------\n");

					// case for what is at
				} else if (line.contains("what_is_at")) {
					commandCount++;
					String[] split = line.split("\t");
					// System.out.println(split[1]);
					// System.out.println(split[2]);

					String longStr = commandProcessor.formatLong(split[2]);
					String latStr = commandProcessor.formatLat(split[1]);

					fw.write("Command " + commandCount + ": what_is_at\t" + split[1] + "\t\t" + split[2] + "\n\n");

					int longitude = commandProcessor.convertLong(split[2]);
					// System.out.println(longitude).;
					int latitude = commandProcessor.convertLat(split[1]);
					// System.out.println(latitude);
					// System.out.println("(" + longStr + ", " + latStr + ")");
					fw.write("\n\t The following features were found at (" + longStr + ", " + latStr + ")\n\t");
					commandProcessor.whatIsAt(longitude, latitude, dB, fw);

					fw.write("\n--------------------------------------------------------\n");

					// case for what is in
				} else if (line.contains("what_is_in")) {
					commandCount++;
					String[] split = line.split("\t");
					String longStr = split[2];
					String latStr = split[1];
					long halfHeight = Long.parseLong(split[3]);
					long halfWidth = Long.parseLong(split[4]);
					long longitude = commandProcessor.convertLong(longStr);
					long latitude = commandProcessor.convertLat(latStr);

					System.out.println(longitude + " " + latitude + " " + halfHeight + " " + halfWidth);

					fw.write("Command " + commandCount + ": what_is_in " + latStr + "\t" + longStr + "\t" + halfHeight
							+ "\t" + halfWidth + "\n");
					fw.write("\n\t The following features were found in (" + commandProcessor.formatLong(longStr) + ", "
							+ commandProcessor.formatLat(latStr) + ")\n");
					commandProcessor.whatIsIn(longitude, latitude, halfHeight, halfWidth, dB, fw);
					fw.write("-------------------------------------------------------\n");

					// case for what is
				} else if (line.contains("what_is")) {
					commandCount++;
					String[] split = line.split("\t");
					String name = split[1];
					// System.out.println(name);
					// String name = nameState.substring()
					String state = split[2];
					// System.out.println(state);
					fw.write("Command " + commandCount + ": what_is\t" + name + "\t" + state + "\n\n");
					fw.write("\t");
					commandProcessor.whatIs(name, state, dB, fw);
					fw.write("-------------------------------------------------------\n");

				}

			}

			line = commandScripts.readLine();
		}

		fw.close();

	}
}