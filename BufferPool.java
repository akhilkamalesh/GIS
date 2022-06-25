import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.*;
import java.lang.*;

/**
 * Bufffer pool is a data structure that records entries that are last called
 * into the ram
 * 
 * @author akhilkamalesh
 *
 * @param <T>
 */
public class BufferPool<T> {

	LinkedList<String> list;
	final int maxSize = 15;

	/*
	 * Constructor for BufferPool
	 */
	public BufferPool() {
		list = new LinkedList<String>();
	}

	/*
	 * Add method for the bufferpool
	 */
	public void add(String gisStr, Long offset) {
		// Checking to see if list < 15

		if (list.size() < 15) {
			list.addFirst(offset.toString() + ":\t" + gisStr);
			// System.out.println(offset.toString() + ":\t" + gisStr);
		} else {
			list.removeLast();
			list.addFirst(offset.toString() + ":\t" + gisStr);
			// System.out.println(offset.toString() + ":\t" + gisStr);
		}
	}

	/**
	 * Display displays the BufferPool
	 * 
	 * @param fw
	 * @throws IOException
	 */
	public void display(FileWriter fw) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			String gisStr = list.get(i);
			fw.write(gisStr + "\n");
		}
	}

	/**
	 * Find method for BufferPool; checks to see if it is the buffer pool
	 * 
	 * @param gisStr
	 * @param offset
	 * @return
	 */
	public boolean find(String gisStr, Long offset) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(offset.toString() + ":\t" + gisStr)) {
				return true;
			}

		}

		return false;
	}

	/**
	 * Replace method takes an item in the list and puts it into the top
	 * 
	 * @param gisStr
	 * @param offset
	 */
	public void replace(String gisStr, Long offset) {
		gisStr = offset.toString() + ":\t" + gisStr;
		int position = list.indexOf(gisStr);
		list.remove(position);
		list.addFirst(gisStr);
	}

}
