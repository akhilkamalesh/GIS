import java.util.ArrayList;

/**
* 
*/

/**
 * @author akhilkamalesh
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

public class Point implements Compare2D<Point> {

	public long xcoord;
	public long ycoord;
	public ArrayList<Long> offset;

	public Point() {
		xcoord = 0;
		ycoord = 0;
	}

	public Point(long x, long y) {
		xcoord = x;
		ycoord = y;
	}

	public Point(long x, long y, long off) {
		xcoord = x;
		ycoord = y;
		offset = new ArrayList<Long>();
		offset.add(off);
	}

	public long getX() {
		return xcoord;
	}

	public long getY() {
		return ycoord;
	}

	public ArrayList<Long> getOffset() {
		return offset;
	}

	// Direction from coordinates (x, y)
	public Direction directionFrom(long X, long Y) {
		// Checking to see if northeast
		if (xcoord > X && ycoord >= Y) {
			return Direction.NE;
		}
		// Checking to see if south-east
		else if (xcoord >= X && ycoord < Y) {
			return Direction.SE;
		}
		// Checking to see if Northwest
		else if (xcoord <= X && ycoord > Y) {
			return Direction.NW;
		}
		// Checking to see if Southwest
		else if (xcoord < X && ycoord <= Y) {
			return Direction.SW;
		}
		// Checking to see if equals
		else if (xcoord == X && ycoord == Y) {
			return Direction.NE;
		}

		return Direction.NOQUADRANT;

	}

	// Checks to see if coords are in a quadrant
	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
		// Check to see if northeast

		if (xcoord < xLo || xcoord > xHi || ycoord < yLo || ycoord > yHi) {
			return Direction.NOQUADRANT;
		} else if (xcoord > (xHi + xLo) / 2 && ycoord >= (yHi + yLo) / 2) {
			return Direction.NE;
		}
		// Check to see if south east
		else if (xcoord >= (xHi + xLo) / 2 && ycoord < (yHi + yLo) / 2) {
			return Direction.SE;
		}
		// Check to see if north west
		else if (xcoord <= (xHi + xLo) / 2 && ycoord > (yHi + yLo) / 2) {
			return Direction.NW;
		}
		// Check to see if south west
		else if (xcoord < (xHi + xLo) / 2 && ycoord <= (yHi + yLo) / 2) {
			return Direction.SW;
		}
		// check to see if equal
		else if (xcoord == (xHi + xLo) / 2 && ycoord == (yHi + yLo) / 2) {
			return Direction.NE;
		}

		// just in case
		return Direction.NOQUADRANT;

	}

	// Returns true iff the user data object lies within or on the boundaries
	// of the rectangle specified by the parameters.
	public boolean inBox(double xLo, double xHi, double yLo, double yHi) {
		if (xLo <= xcoord && xcoord <= xHi && yLo <= ycoord && ycoord <= yHi) {
			return true;
		}

		return false;
	}

	public String toString() {

		return new String("(" + xcoord + ", " + ycoord + ")");
	}

	// Checks to see to pointers are equals
	public boolean equals(Object o) {
		// Checking to see if this and o point to same memory location
		if (this == o) {
			return true;
		}
		// checking to see if o is null
		if (o == null) {
			return false;
		}
		// checking to see if o is a point
		if (this.getClass().equals(o.getClass())) {
			// casting to a point
			Point oPoint = (Point) o;
			// checking to see if coords were the same
			if (this.getX() == oPoint.getX() && this.getY() == oPoint.getY()) {
				return true;
			} else {
				return false;
			}
		}

		return false;

	}
}
