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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class hashTable<T extends Hashable<T>> {

	private ArrayList<LinkedList<T>> table; // physical basis for the hash table
	private Integer numElements = 0; // number of elements in all the chains
	private Double loadLimit = 0.7; // table resize trigger
	private final Integer defaultTableSize = 256; // default number of table slots
	private int max = 0;

	/**
	 * Constructs an empty hash table with the following properties: Pre: - size is
	 * the user's desired number of lots; null for default - ldLimit is user's
	 * desired load factor limit for resizing the table; null for the default Post:
	 * - table is an ArrayList of size LinkedList objects, 256 slots if size == null
	 * - loadLimit is set to default (0.7) if ldLimit == null
	 */
	public hashTable(Integer size, Double ldLimit) {

		if (size != null) {
			table = new ArrayList<LinkedList<T>>(size);
			for (int i = 0; i < size; i++) {
				LinkedList<T> list = new LinkedList<T>();
				table.add(i, list);
			}
		} else {
			table = new ArrayList<LinkedList<T>>(defaultTableSize);
			for (int i = 0; i < defaultTableSize; i++) {
				LinkedList<T> list = new LinkedList<T>();
				table.add(i, list);
			}
		}

		if (ldLimit != null) {
			loadLimit = ldLimit;
		} else {
			this.loadLimit = loadLimit;
		}

	}

	/**
	 * Inserts elem at the front of the elem's home slot, unless that slot already
	 * contains a matching element (according to the equals() method for the user's
	 * data type. Pre: - elem is a valid user data object Post: - elem is inserted
	 * unless it is a duplicate - if the resulting load factor exceeds the load
	 * limit, the table is rehashed with the size doubled Returns: true iff elem has
	 * been inserted
	 */
	public boolean insert(T elem) {

		LinkedList<T> list = new LinkedList<T>();

		if (elem != null) {

			if (this.find(elem) != null) {
				T element = this.find(elem);
				// System.out.println("found" + elem);
				return false;
			}

			int ind = elem.Hash() % table.size();

			list = table.get(ind);

			if (list.size() <= this.findMaxLength()) {
				list.addLast(elem);
				numElements++;
			}else {
				return false;
			}
			
			// System.out.println(list);
			table.set(ind, list);

			//System.out.println(table.size());
			// System.out.println((double)numElements/table.size());
			// System.out.println(table.size());
			if ((double) numElements / (double) table.size() >= loadLimit) {
				// System.out.println("is doubling");
				// System.out.println(table.size());
				int tSize = table.size();
				//System.out.println(tSize);
				ArrayList<LinkedList<T>> newTable = new ArrayList<LinkedList<T>>(tSize * 2);
				//System.out.println(newTable.size());
				for (int i = 0; i < tSize*2; i++) {
					newTable.add(new LinkedList<T>());
				}
				
				// System.out.println(newTable.size());

				for (LinkedList<T> oldList : table) {
					for (T element : oldList) {

						// System.out.println(newTable.size());
						int index = element.Hash() % newTable.size();
						LinkedList<T> insertList = newTable.get(index);
						insertList.addLast(element);

					}
				}
				table = newTable;
				// System.out.println(tSize);
			}

			return true;
		}

		return false;

	}

	/**
	 * Searches the table for an element that matches elem (according to the
	 * equals() method for the user's data type). Pre: - elem is a valid user data
	 * object Returns: reference to the matching element; null if no match is found
	 */
	public T find(T elem) {
		if (elem != null) {
			int ind = elem.Hash() % table.size();
			// System.out.println(ind);
			LinkedList<T> list = table.get(ind);
			// System.out.println(list);
			for (int i = 0; i < list.size(); i++) {
				if (elem.equals(list.get(i))) {
					return list.get(i);
				}
			}

		}

		return null;

	}

	/**
	 * Removes a matching element from the table (according to the equals() method
	 * for the user's data type). Pre: - elem is a valid user data object Returns:
	 * reference to the matching element; null if no match is found
	 */

	public T remove(T elem) {

		return null;

	} // Not necessary for this assignment
	
	
	private int findMaxLength() {
		for(int i = 0; i < table.size(); i++) {
			if(max < table.get(i).size()) {
				max = table.get(i).size();
			}
		}
		
		return max;
		
	}

	/**
	 * Writes a formatted display of the hash table contents. Pre: - fw is open on
	 * an output file
	 */
	public void display(FileWriter fw) throws IOException {
		fw.write("Number of elements: " + numElements + "\n");
		fw.write("Number of slots: " + table.size() + "\n");
		fw.write("Maximum elements in a slot: " + this.findMaxLength() + "\n");
		fw.write("Load limit: " + loadLimit + "\n");
		fw.write("\n");

		fw.write("Slot Contents\n");
		for (int idx = 0; idx < table.size(); idx++) {

			LinkedList<T> curr = table.get(idx);

			if (curr != null && !curr.isEmpty()) {

				fw.write(String.format("%5d: %s\n", idx, curr.toString()));
			}
		}
	}

}
