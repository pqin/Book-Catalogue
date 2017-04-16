package marc.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import marc.record.Record;

public abstract class AbstractMarc {
	/**
	 * Returns a new instance of FileNameExtensionFilter for this format.
	 * @return the extension filter
	 */
	public abstract FileNameExtensionFilter getExtensionFilter();
	
	/**
	 * Reads the specified file and parses the data, storing it into the list.
	 * @param file the File to read
	 * @return the set of Records in the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract ArrayList<Record> read(FileInputStream in) throws FileNotFoundException, IOException, RecordParseException;
	/**
	 * Writes the list of Records to the specified file.
	 * @param file the File to write to
	 * @param data the set of Records to write
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract void write(File file, List<Record> data) throws FileNotFoundException, IOException;

	protected String buildFilterDescription(String description, String[] ext) {
		String[] e = new String[ext.length];
		for (int i = 0; i < e.length; ++i){
			e[i] = "*." + ext[i];
		}
		String filterDesc = String.format("%s (%s)", description, String.join(";", e));
		return filterDesc;
	}
}
