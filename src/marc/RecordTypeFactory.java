package marc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import marc.field.Leader;
import marc.type.ConfigType;
import marc.type.FixedDataParser;
import marc.type.Format;

public final class RecordTypeFactory {
	private static Map<Character, Format> map = loadMap(new File("resource/fixedfielddata.xml"));
	
	public static final Map<Character, Format> loadMap(File file){
		FixedDataParser parser = new FixedDataParser();
		Map<Character, Format> output = null;
		try {
			output = parser.read(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (output == null){
				output = new HashMap<Character, Format>();
			}
		}
		return output;
	}
	
	public static final Format getFormat(Leader leader){
		final char type = leader.getData(Leader.TYPE, 1)[0];
		Format format = map.get(type);
		if (format == null){
			format = new Format("");
		}
		return format;
	}
	
	public static final ConfigType getConfigType(Format format, Leader leader, String tag){
		ConfigType config = null;
		if (format == null){
			config = new ConfigType("", 0);
		} else {
			char[] type = leader.getData(Leader.TYPE, 2);
			int i = (type[0] == 'a') ? 1 : 0;
			String key = String.valueOf(type[i]);
			config = format.getConfiguration(tag, Leader.TYPE, key);
			if (config == null){
				config = new ConfigType("", 0);
			}
		}
		return config;
	}
}
