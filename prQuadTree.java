/**
* 
*/

/**
 * @author akhilkamalesh
 * prQuadTree 
 *
 */

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
// akhilk24

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

// To support testing, we will make certain elements of the generic.
//
// You may safely add data members and function members as needed, but
// you must not modify any of the public members that are shown.
//
public class prQuadTree<T extends Compare2D<? super T>> {

	// Inner classes for nodes (public so test harness has access)
	public abstract class prQuadNode {

	}

	public class prQuadLeaf extends prQuadNode {
		// Use an ArrayList to support a bucketed implementation later.
		ArrayList<T> Elements = new ArrayList<T>();

		/*
		 * Constructor for prQuadLeaf
		 * 
		 * @param ArrayList<T>
		 */
		public prQuadLeaf() {
			super();
		}
	}

	public class prQuadInternal extends prQuadNode {
		// Use base-type pointers since children can be either leaf nodes
		// or internal nodes.
		prQuadNode NW, NE, SE, SW;

		/*
		 * Constructor for prQuadInternal
		 * 
		 */
		public prQuadInternal() {
			super();
		}
	}

	// prQuadTree elements (public so test harness has access)
	public prQuadNode root;
	public long xMin, xMax, yMin, yMax;
	static int bucket = 4;

	// Add private data members as needed...

	// Initialize quadtree to empty state, representing the specified region.
	// Pre: xMin < xMax and yMin < yMax
	public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	// Pre: elem != null
	// Post: If elem lies within the tree's region, and elem is not already
	// present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree.
	public boolean insert(T elem) {

		prQuadNode temp = insert(elem, root, xMin, xMax, yMin, yMax);
		if (temp == null) {
			return false;
		} else {
			root = temp;
			return true;
		}
	}

	/**
	 * 
	 * @param Elem Object takes an object and uses a private find method to search
	 *             for element in tree
	 * @return Object
	 */
	public T find(T Elem) {
		prQuadLeaf temp = (prQuadLeaf) find(Elem, root, xMin, xMax, yMin, yMax);
		if (temp == null) {
			return null;
		} else {
			for (int i = 0; i < temp.Elements.size(); i++) {
				if (temp.Elements.get(i).getX() == Elem.getX() && temp.Elements.get(i).getY() == Elem.getY()) {
					return temp.Elements.get(i);
				}
			}
		}

		return null;
	}

	/**
	 * Returns a collection of (references to) all elements x such that x is in the
	 * tree and x lies at coordinates within the defined rectangular region,
	 * including the boundary of the region.
	 * 
	 * @param xLo long
	 * @param xHi long
	 * @param yLo long
	 * @param yHi long
	 * @return array list
	 */
	// Pre: xLo < xHi and yLo < yH
	public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {

		ArrayList<T> first = new ArrayList();
		ArrayList<T> sol = new ArrayList();
		find(root, xLo, xHi, yLo, yHi, first);
		for (T thing : first) {
			if (thing.inBox(xLo, xHi, yLo, yHi)) {
				sol.add(thing);
			}
		}

		return sol;
	}

	/**
	 * Private method for insert
	 * 
	 * @param element
	 * @param root
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return prQuadNode
	 */
	private prQuadNode insert(T element, prQuadNode root, long xMin, long xMax, long yMin, long yMax) {
		// Check to see if element is not null
		if (element != null) {
			// Check to see if element is in box
			if (element.inBox(xMin, xMax, yMin, yMax) == true) {
				// Check to see if there the tree is null (base case)
				if (root == null) {
					prQuadLeaf newNode = new prQuadLeaf(); // Create new leaf node
					newNode.Elements.add(element); // add the element inside the arraylist
					return newNode;
				}
				// Going to the case where the root is an leaf node
				else if (root.getClass().getName().equals(prQuadLeaf.class.getName())) {
					prQuadLeaf newLeaf = (prQuadLeaf) root; // Create new leaf from the root (since it is a leaf)
					// System.out.println(newLeaf.Elements);
					prQuadInternal internal = new prQuadInternal(); // Create new internal node to put the leaves

					// Checking to see if the newLeaf point is equal to element
					for (int i = 0; i < newLeaf.Elements.size(); i++) {
						if (newLeaf.Elements.get(i).getX() == element.getX()
								&& newLeaf.Elements.get(i).getY() == element.getY()) {
							return null;
						}
					}

					/**
					 * Checking to see if the size is greater than the bucket, then split the
					 * element
					 */
					if (newLeaf.Elements.size() >= bucket) {
						for (int i = 0; i < newLeaf.Elements.size(); i++) {
							internal = (prQuadInternal) insert(newLeaf.Elements.get(i), internal, xMin, xMax, yMin,
									yMax);
						}
						internal = (prQuadInternal) insert(element, internal, xMin, xMax, yMin, yMax);
						return internal;
					}

					newLeaf.Elements.add(element);
					return root;

				}
				// Going to the case where the root is an internal node
				else if (root.getClass().getName().equals(prQuadInternal.class.getName())) {
					prQuadInternal internal = (prQuadInternal) root; // making the root as internal node
					Direction dir = element.inQuadrant(xMin, xMax, yMin, yMax); // obtaining direction from the element
					if (dir == Direction.NE) {
						internal.NE = insert(element, internal.NE, (xMin + xMax) / 2, xMax, (yMin + yMax) / 2, yMax);
					} else if (dir == Direction.NW) {
						internal.NW = insert(element, internal.NW, xMin, (xMin + xMax) / 2, (yMin + yMax) / 2, yMax);
					} else if (dir == Direction.SE) {
						internal.SE = insert(element, internal.SE, (xMin + xMax) / 2, xMax, yMin, (yMin + yMax) / 2);
					} else if (dir == Direction.SW) {
						internal.SW = insert(element, internal.SW, xMin, (xMax + xMin) / 2, yMin, (yMin + yMax) / 2);
					}

					return internal;

				}

			}
		}

		return null;
	}

//	private prQuadNode split(prQuadLeaf leaf, long xMin, long xMax, long yMin, long yMax) {
//		prQuadLeaf ne = new prQuadLeaf();
//		prQuadLeaf nw = new prQuadLeaf();
//		prQuadLeaf se = new prQuadLeaf();
//		prQuadLeaf sw = new prQuadLeaf();
//
//		for(int i = 0; i < leaf.Elements.size(); i++) {
//			Direction dir = leaf.Elements.get(i).inQuadrant(xMin, xMax, yMin, yMax);
//			if(dir == Direction.NE) {
//				ne.Elements.leaf.Elements.get(i);
//			}
//		}
//	}

	/**
	 * Private find method for the find(element) method
	 * 
	 * @param element
	 * @param root
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return prQuadNode
	 */
	private prQuadNode find(T element, prQuadNode root, long xMin, long xMax, long yMin, long yMax) {
		// Checking to see if element is not null
		if (element != null) {
			// Checking to see if element is in the box
			if (element.inBox(xMin, xMax, yMin, yMax) == true) {
				// check to see if the root is null
				if (root != null) {
					// Base case where you are at the node you want to return
					if (root.getClass().equals(prQuadLeaf.class)) {
						prQuadLeaf found = (prQuadLeaf) root;
						for (int i = 0; i < found.Elements.size(); i++) {
							if (found.Elements.get(i).getX() == element.getX()
									&& found.Elements.get(i).getY() == element.getY()) {
								return found;
							}
						}
						return null;
					} else if (root.getClass().equals(prQuadInternal.class)) {
						prQuadInternal internal = (prQuadInternal) root;
						Direction dir = element.inQuadrant(xMin, xMax, yMin, yMax);
						if (dir == Direction.NE) {
							return find(element, internal.NE, (xMin + xMax) / 2, xMax, (yMin + yMax) / 2, yMax);
						} else if (dir == Direction.NW) {
							return find(element, internal.NW, xMin, (xMin + xMax) / 2, (yMin + yMax) / 2, yMax);
						} else if (dir == Direction.SE) {
							return find(element, internal.SE, (xMin + xMax) / 2, xMax, yMin, (yMin + yMax) / 2);
						} else if (dir == Direction.SW) {
							return find(element, internal.SW, xMin, (xMax + xMin) / 2, yMin, (yMin + yMax) / 2);
						}

						// return internal;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Private find method for find(long, long, long, long)
	 * 
	 * @param root
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param sol
	 */
	private void find(prQuadNode root, long xMin, long xMax, long yMin, long yMax, ArrayList<T> sol) {

		// Checking if the arraylist is not null
		if (sol != null) {
			// Checking if the root is not null
			if (root != null) {
				// Base Case: when the root is a leaf
				if (root.getClass().equals(prQuadLeaf.class)) {
					prQuadLeaf leaf = (prQuadLeaf) root;
					for (int i = 0; i < leaf.Elements.size(); i++) {
						sol.add(leaf.Elements.get(i));
					}
				}
				// When the root is an internal node
				else if (root.getClass().equals(prQuadInternal.class)) {
					prQuadInternal internal = (prQuadInternal) root;
					// Check to see if the mid points are less that max for NE
					if (((xMin + xMax) / 2) <= xMax && ((yMin + yMax) / 2) <= yMax) {
						find(internal.NE, (xMin + xMax) / 2, xMax, (yMin + yMax) / 2, yMax, sol);
					}
					// Check to see if mid point is greater than xMin and less than yMax for NW
					if (xMin <= ((xMin + xMax) / 2) && ((yMin + yMax) / 2) <= yMax) {
						find(internal.NW, xMin, (xMin + xMax) / 2, (yMin + yMax) / 2, yMax, sol);
					}
					// Check to see if mid point is less that xMax and greater than yMin SE
					if (((xMin + xMax) / 2) <= xMax && yMin <= ((yMin + yMax) / 2)) {
						find(internal.SE, (xMin + xMax) / 2, xMax, yMin, (yMin + yMax) / 2, sol);
					}
					// Check to see if mid point is greater than min for SW
					if ((xMin <= ((xMin + xMax) / 2)) && yMin <= ((yMin + yMax) / 2)) {
						find(internal.SW, xMin, (xMax + xMin) / 2, yMin, (yMin + yMax) / 2, sol);
					}
				}

			}
		}

		return;
	}

	/**
	 * Print helper method that displays the prQuadTree
	 * 
	 * @param sRoot
	 * @param Padding
	 * @param fw
	 * @throws IOException
	 */
	public void printTreeHelper(prQuadNode sRoot, String Padding, FileWriter fw) throws IOException {
		// Check for empty leaf
		if (sRoot == null) {
			fw.write(Padding + "*\n");
			return;
		}
		// Check for and process SW and SE subtrees
		if (sRoot.getClass().equals(prQuadInternal.class)) {
			prQuadInternal p = (prQuadInternal) sRoot;
			printTreeHelper(p.SW, Padding + " ", fw);
			printTreeHelper(p.SE, Padding + " ", fw);
		}
		// Display indentation padding for current node
		fw.write(Padding);
		// Determine if at leaf or internal and display accordingly
		if (sRoot.getClass().equals(prQuadLeaf.class)) {
			prQuadLeaf p = (prQuadLeaf) sRoot;
			for (int i = 0; i < p.Elements.size(); i++) {
				fw.write(Padding + "[" + p.Elements.get(i));
				Point point = (Point) p.Elements.get(i);
				String str = point.getOffset().get(0).toString();
				if (point.getOffset().size() > 1) {
					for (int j = 1; j < point.getOffset().size(); j++) {
						str = str + ", " + point.getOffset().get(j);
					}
				}

				fw.write(", " + str + "]");
			}

			fw.write("\n");
		} else
			fw.write(Padding + "@\n");
		// Check for and process NE and NW subtrees
		if (sRoot.getClass().equals(prQuadInternal.class)) {
			prQuadInternal p = (prQuadInternal) sRoot;
			printTreeHelper(p.NE, Padding + " ", fw);
			printTreeHelper(p.NW, Padding + " ", fw);
		}

		fw.write("\n");
	}

}
