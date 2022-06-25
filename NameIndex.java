import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * NameIndex class talks to CommandProcessor class and hold a hashTable object
 * 
 * @author akhilkamalesh
 *
 */
public class NameIndex {

	hashTable<nameEntry> hashT;

	/**
	 * Constructor for the NameIndex
	 */
	public NameIndex() {
		hashT = new hashTable<nameEntry>(null, null);
	}

	/**
	 * insert method for NameIndex; inserts if the records are valid
	 * @param database
	 * @throws IOException
	 */
	public void insert(RandomAccessFile database) throws IOException {
		database.seek(0);
		String dbStr = database.readLine(); // Read the first line of the database
		while (true) {
			long offset = database.getFilePointer();
			dbStr = database.readLine();
			// System.out.println(dbStr);
			if (dbStr != null) {
				GISRecord dbRec = new GISRecord(dbStr, offset);
				// System.out.println(dbRec);
				String nameHash = dbRec.getFeatureName() + ":" + dbRec.getState();
				nameEntry nE = new nameEntry(nameHash, offset);
				// System.out.println(nE);
				// System.out.println(nE.state());
				// checking to see if the entry is found
				if (hashT.find(nE) != null) {

					nameEntry additional = (nameEntry) hashT.find(nE);
					// iterating through the found entry to see if the offsets match
					for (int i = 0; i < additional.locations().size(); i++) {
						if (offset == hashT.find(nE).locations().get(i)) {
							return;
						}
					}

					additional.locations.add(offset);

				} else {
					hashT.insert(nE);
				}
			} else {
				return;
			}
		}

	}

	/*
	 * Insert method for NameIndex Take in a gisRecord object and inserts it into
	 * the hash table
	 */
//	public void insert(GISRecord gisRec) {
//		String nameHash = gisRec.getFeatureName() + ":" + gisRec.getState();
//		nameEntry nE = new nameEntry(nameHash, gisRec.getOffset());
//		if (hashT.find(nE) != null) {
//			nameEntry additional = (nameEntry) hashT.find(nE);
//			for (int i = 0; i < nE.locations().size(); i++) {
//				if(hashT.find(nE).locations().get(i) != gisRec.getOffset()) {
//					additional.addLocation(nE.locations().get(i));
//				}
//			}
//
//		}
//		hashT.insert(nE);
//	}

	/**
	 * what is method calls the HashTable find method
	 * @param featName
	 * @param state
	 * @return
	 */
	public nameEntry whatIs(String featName, String state) {
		nameEntry ne = new nameEntry(featName + ":" + state);
		// System.out.println(state);
		return (nameEntry) hashT.find(ne);
		// System.out.println(ne.key());
	}

	/**
	 * Display method that calls HashTable display method
	 * @param fw
	 * @throws IOException
	 */
	public void show(FileWriter fw) throws IOException {
		hashT.display(fw);
	}
}
