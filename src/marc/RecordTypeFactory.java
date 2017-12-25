package marc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import marc.field.Field;
import marc.field.FixedField;
import marc.field.Leader;
import marc.record.Record;
import marc.type.ConfigIdentifier;
import marc.type.ConfigType;
import marc.type.FixedDataParser;
import marc.type.Format;
import marc.type.RecordType;

public final class RecordTypeFactory {
	private static final int MAX_TYPE_LENGTH = 2;
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
	
	public static final RecordType getRecordType(Leader leader){
		RecordType recordType = null;
		final char type = leader.getData(Leader.TYPE, 1)[0];
		Format format = map.get(type);
		if (format != null){
			String formatName = format.getName();
			for (RecordType t : RecordType.values()){
				if (formatName.equals(t.getName())){
					recordType = t;
					break;
				}
			}
		}
		return recordType;
	}
	
	public static final Format getFormat(Leader leader){
		final char type = leader.getData(Leader.TYPE, 1)[0];
		Format format = map.get(type);
		if (format == null){
			format = new Format("");
		}
		return format;
	}
	
	public static final ConfigType getConfigType(Format format, FixedField reference, int refIndex, String tag){
		ConfigType config = null;
		if (format == null){
			config = new ConfigType("", 0);
		} else {
			char[] type = reference.getData(refIndex, MAX_TYPE_LENGTH);
			String key = null;
			int i = 0;
			// TODO implement as Format method
			while (config == null && i < type.length){
				key = String.copyValueOf(type, 0, i+1);
				config = format.getConfiguration(tag, key);
				++i;
			}
			if (config == null){
				config = new ConfigType("", 0);
			}
		}
		return config;
	}
	public static final ConfigType getConfigType(Format format, Record record, Field field){
		ConfigType config = null;
		if (format == null){
			config = new ConfigType("", 0);
		} else {
			String tag = field.getTag();
			ConfigIdentifier[] identifier = format.getIdentifier(field.getTag());
			FixedField refField = null;
			ConfigIdentifier formatID = null;
			for (ConfigIdentifier id : identifier){
				if (tag.equals(id.getTag())){
					refField = (FixedField) field;
				} else {
					refField = (FixedField) record.getFirstMatchingField(id.getTag());
				}
				if (refField != null){
					formatID = id;
					break;
				}
			}
			char[] type = null;
			if (formatID == null){
				System.err.println("no matching reference Field");
			} else {
				type = refField.getData(formatID.getIndex(), MAX_TYPE_LENGTH);
				String key = null;
				int i = 0;
				while (config == null && i < type.length){
					key = String.copyValueOf(type, 0, i+1);
					config = format.getConfiguration(formatID.getTag(), formatID.getIndex(), tag, key);
					++i;
				}
			}
			if (config == null){
				config = new ConfigType("", 0);
			}
		}
		return config;
	}
}
