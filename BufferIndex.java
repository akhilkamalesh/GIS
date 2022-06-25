import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author akhilkamalesh BufferIndex talks to CommandProcessor and has a
 *         BufferPool data structure
 *         Handles methods for BufferPool
 */
public class BufferIndex {

	BufferPool<String> bufferPool;

	/**
	 * 
	 */
	public BufferIndex() {
		bufferPool = new BufferPool();
	}

	/**
	 * find method for BufferIndex
	 * 
	 * @param gisStr String
	 * @param offset long
	 * @return boolean true if found, false if not found
	 */
	public boolean find(String gisStr, long offset) {
		return bufferPool.find(gisStr, offset);
	}

	/**
	 * replace method for BufferIndex; replaces element if found to top
	 * 
	 * @param gisStr String
	 * @param offset Long
	 */
	public void replace(String gisStr, long offset) {
		bufferPool.replace(gisStr, offset);
	}

	/**
	 * insert method for BufferIndex
	 * 
	 * @param gisStr String
	 * @param offset Long
	 */
	public void insert(String gisStr, Long offset) {
		bufferPool.add(gisStr, offset);
	}

	/**
	 * show method for BufferIndex
	 * 
	 * @param fw FileWriter
	 * @throws IOException
	 */
	public void show(FileWriter fw) throws IOException {
		bufferPool.display(fw);

	}

}
