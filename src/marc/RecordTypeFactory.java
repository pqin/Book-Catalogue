package marc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import marc.field.FixedField;
import marc.field.Leader;
import marc.record.Record;
import marc.type.ConfigIdentifier;
import marc.type.ConfigType;
import marc.type.FixedDataParser;
import marc.type.Format;
import marc.type.RecordType;

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
	
	public static final ConfigType getConfigType(Record record, FixedField field){
		Format format = getFormat(record.getLeader());
		return getConfigType(format, record, field);
	}
	public static final ConfigType getConfigType(Format format, FixedField reference, int refIndex, String tag){
		ConfigType config = null;
		if (format == null){
			config = new ConfigType("", 0);
		} else {
			config = format.getConfiguration(reference, refIndex, tag);
			if (config == null){
				config = new ConfigType("", 0);
			}
		}
		return config;
	}
	private static final ConfigType getConfigType(Format format, Record record, FixedField field){
		ConfigType config = null;
		if (format == null){
			System.err.println("Format is null.");
			config = new ConfigType("", 0);
		} else {
			String tag = field.getTag();
			ConfigIdentifier[] identifier = format.getIdentifier(field.getTag());
			FixedField refField = null;
			ConfigIdentifier formatID = null;
			for (ConfigIdentifier id : identifier){
				if (tag.equals(id.getTag())){
					refField = field;
				} else {
					refField = (FixedField) record.getFirstMatchingField(id.getTag());
				}
				if (refField != null){
					formatID = id;
					break;
				}
			}
			if (formatID != null){
				config = format.getConfiguration(refField, formatID.getIndex(), tag);
			}
			if (config == null){
				config = new ConfigType("", 0);
			}
		}
		return config;
	}
	public static Map<Character, String> getConfigCodes(Record record, String tag){
		Map<Character, String> codes = new HashMap<Character, String>();
		Format format = getFormat(record.getLeader());
		ConfigIdentifier[] identifier = format.getIdentifier(tag);
		char code = '\0';
		ConfigType type = null;
		String value = null;
		for (ConfigIdentifier id : identifier){
			code = id.getCode();
			type = format.getConfiguration(id.getTag(), id.getIndex(), tag, String.valueOf(code));
			value = (type == null) ? "???" : type.getName();
			codes.put(id.getCode(), value);
		}
		return codes;
	}
}
