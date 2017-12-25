package marc.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedField;
import marc.field.Leader;
import marc.field.Subfield;
import marc.marc8.Marc8;
import marc.record.Record;
import marc.record.RecordBuilder;

public class MarcBinary extends AbstractMarc {
	// encodings
	private static final Charset ASCII = StandardCharsets.US_ASCII;
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	private static final Charset MARC8 = new Marc8();
	
	public static final byte RECORD_TERMINATOR = 0x1D;
	public static final byte FIELD_TERMINATOR = 0x1E;
	public static final byte SUBFIELD_DELIMITER = 0x1F;
	private static final String SUBFIELD_SPLITTER_REGEX = String.valueOf((char) SUBFIELD_DELIMITER);
	
	private class DirectoryEntry {
		private static final int LENGTH = 12;
		private static final int RADIX = 10;
		
		private int[] map;
		protected String tag;
		private int fieldPos, fieldLength;
		
		protected DirectoryEntry(int index, byte[] data){
			int[] mapOffset = {3, 4, 5, 0};
			int offset = 0;
			map = new int[mapOffset.length];
			for (int i = 0; i < map.length; ++i){
				map[i] = (index * LENGTH) + offset;
				offset += mapOffset[i];
			}
			
			tag = Field.UNKNOWN_TAG;
			fieldPos = 0;
			fieldLength = 0;
		}
		protected void parseDirectory(final byte[] data){
			byte[] bytes = null;
			bytes = Arrays.copyOfRange(data, map[0], map[1]);
			tag = new String(bytes, ASCII);
			
			bytes = Arrays.copyOfRange(data, map[1], map[2]);
			fieldLength = parseInt(bytes, ASCII, RADIX);
			
			bytes = Arrays.copyOfRange(data, map[2], map[3]);
			fieldPos = parseInt(bytes, ASCII, RADIX);
		}
		protected String parseField(final Charset charset, final byte[] data){
			byte[] bytes = Arrays.copyOfRange(data, fieldPos, fieldPos + fieldLength);
			String fieldData = new String(bytes, charset);
			return fieldData;
		}
	};
	
	@Override
	public FileNameExtensionFilter getExtensionFilter() {
    	String description = "MARC";
		String[] ext = {"mrc", "marc", "marc8"};
		String filterDesc = buildFilterDescription(description, ext);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, ext);
		return filter;
	}

	private char[] getAsciiChars(final byte[] bytes){
		char[] data = new char[bytes.length];
		for (int i = 0; i < bytes.length; ++i){
			data[i] = (char) bytes[i];
		}
		return data;
	}
	
    private static int parseInt(final byte[] bytes, final Charset charset, final int radix){
		int value = 0;
		try {
			value = Integer.parseInt(new String(bytes, charset), radix);
		} catch (NumberFormatException e){
			value = 0;
		}
		return value;
	}

	private Record parseRecord(final RecordBuilder builder, final byte[] directory, final byte[] data, final Charset charset){
		// parse Directory
		int entryCount = 0;
		if ((directory.length - 1) % DirectoryEntry.LENGTH == 0){
			entryCount = Math.floorDiv(directory.length - 1, DirectoryEntry.LENGTH);
		}
		DirectoryEntry[] entry = new DirectoryEntry[entryCount];
		for (int i = 0; i < entryCount; ++i){
			entry[i] = new DirectoryEntry(i, directory);
			entry[i].parseDirectory(directory);
		}
		
		// build Record
		String fieldData = null;
		String[] subData = null;
		int index0, index1;

		for (int r = 0; r < entryCount; ++r){
			fieldData = entry[r].parseField(charset, data);
			builder.createField(entry[r].tag);
			if (Field.isControlTag(entry[r].tag)){
				index0 = 0;
				index1 = fieldData.indexOf(FIELD_TERMINATOR);
				if (index1 == -1){
					index1 = fieldData.length();
				}
				builder.setControlData(fieldData.substring(index0, index1));
			} else {
				for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
					builder.setIndicator(i, fieldData.charAt(i));
				}
				index0 = fieldData.indexOf(SUBFIELD_DELIMITER)+1;
				index1 = fieldData.indexOf(FIELD_TERMINATOR, index0);
				if (index1 == -1){
					index1 = fieldData.length();
				}
				fieldData = fieldData.substring(index0, index1);
				subData = fieldData.split(SUBFIELD_SPLITTER_REGEX);
				for (int i = 0; i < subData.length; ++i){
					if (subData[i].length() > 0){
						builder.addSubfield(subData[i].charAt(0), subData[i].substring(1));
					}
				}
			}
			builder.addField();
		}
    	return builder.build();
    }
    
	@Override
	public ArrayList<Record> read(File file) throws FileNotFoundException, IOException {		
		byte[] leader = new byte[Leader.FIELD_LENGTH];
		byte[] directory = null;
		byte[] fieldData = null;
		
		ArrayList<Record> list = new ArrayList<Record>();
		RecordBuilder builder = new RecordBuilder();
		Record record = null;
		Leader ldr = new Leader();
		Charset charset = null;
		char[] leaderData = null;
		int recordLength = 0;
		int baseAddress = 0;
		
		FileInputStream in = new FileInputStream(file);
		while (in.read(leader) == Leader.FIELD_LENGTH){
			leaderData = getAsciiChars(leader);
			ldr.setFieldData(leaderData);
			recordLength = ldr.getLength();
			baseAddress = ldr.getBaseAddress();
			directory = new byte[baseAddress - Leader.FIELD_LENGTH];
			fieldData = new byte[recordLength - baseAddress];

			in.read(directory);
			in.read(fieldData);
			
			switch (ldr.getCharacterCodingScheme()){
			case FixedField.BLANK:
				charset = MARC8;
				break;
			case 'a':
				charset = UTF8;
				break;
			default:
				charset = ASCII;
				break;
			}
			
			builder.setLeader(leaderData);
			record = parseRecord(builder, directory, fieldData, charset);
			list.add(record);
			builder.reset();
		}
		in.close();
		
		return list;
	}

	@Override
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		final Charset encoding = UTF8;
		final char charCodingScheme = 'a';
		FileOutputStream out = new FileOutputStream(file);
		Record record = null;
		Leader leader = null;
		List<Field> field = null;
		Field f = null;
		String tag = null;
		byte[] ldr = null;
		byte[][] directory = null;
		int fi = 0;
		int fl = 0;
		byte[][] dirEntry = new byte[3][];
		byte[][] fieldData = null;
		int recordLength = 0;
		int baseAddress = 0;
		
		Iterator<Record> it = data.iterator();
		while (it.hasNext()){
			record = it.next();
			// get all data
			field = record.getFields();
			directory = new byte[field.size() - 1][DirectoryEntry.LENGTH];
			fieldData = new byte[field.size() - 1][];
			fi = 0;
			fl = 0;
			for (int i = 1; i < field.size(); ++i){
				f = field.get(i);
				tag = f.getTag();
				if (Field.isControlTag(tag)){
					fieldData[i-1] = getBytes((ControlField)f, encoding);
				} else {
					fieldData[i-1] = getBytes((DataField)f, encoding);
				}
				fl = fieldData[i-1].length;
				dirEntry[0] = getBytes(tag, ASCII);
				dirEntry[1] = getBytes(fl, 4, ASCII);
				dirEntry[2] = getBytes(fi, 5, ASCII);
				directory[i-1] = flatten(dirEntry);
				fi += fl;
			}
			// calculate addresses
			recordLength = Leader.FIELD_LENGTH;
			for (int i = 0; i < directory.length; ++i){
				recordLength += directory[i].length;
			}
			++recordLength;
			baseAddress = recordLength;
			for (int i = 0; i < fieldData.length; ++i){
				recordLength += fieldData[i].length;
			}
			++recordLength;
			// write Leader
			leader = record.getLeader();
			leader.setLength(recordLength);
			leader.setBaseAddress(baseAddress);
			leader.setCharacterCodingScheme(charCodingScheme);
			ldr = getBytes(leader.getFieldData(), ASCII);
			out.write(ldr);
			// write directory
			for (int i = 0; i < directory.length; ++i){
				out.write(directory[i]);
			}
			out.write(FIELD_TERMINATOR);
			// write Field data
			for (int i = 0; i < fieldData.length; ++i){
				out.write(fieldData[i]);
			}
			out.write(RECORD_TERMINATOR);
		}
		out.flush();
		out.close();
	}
	
	private static byte[] getBytes(ControlField f, Charset charset){
		byte[] t = getBytes(f.getFieldData(), charset);
		byte[] b = Arrays.copyOf(t, t.length + 1);
		b[t.length] = FIELD_TERMINATOR;
		return b;
	}
	private static byte[] getBytes(DataField f, Charset charset){
		final int subCount = f.getDataCount();
		Subfield s = null;
		byte[][] tmp = null;
		int k = 0;
		if (f.isControlField()){
			tmp = new byte[subCount][];
			for (int i = 0; i < subCount; ++i){
				s = f.getSubfield(i);
				tmp[i] = getBytes(s.getData(), charset);
			}
		} else {
			tmp = new byte[(2*subCount) + 1][];
			// indicators
			tmp[0] = new byte[Field.INDICATOR_COUNT];
			for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
				tmp[0][i] = getBytes(f.getIndicator(i), charset)[0];
			}
			// subfield code and data
			for (int i = 0; i < subCount; ++i){
				s = f.getSubfield(i);
				k = (i * 2) + 1;
				tmp[k+0] = new byte[2];
				tmp[k+0][0] = SUBFIELD_DELIMITER;
				tmp[k+0][1] = getBytes(s.getCode(), charset)[0];
				tmp[k+1] = getBytes(s.getData(), charset);
			}
		}
		
		byte[] t = flatten(tmp);
		byte[] b = Arrays.copyOf(t, t.length + 1);
		b[t.length] = FIELD_TERMINATOR;
		return b;
	}
	
	private static byte[] getBytes(char c, Charset charset){
		byte[] b = String.valueOf(c).getBytes(charset);
		return b;
	}
	private static byte[] getBytes(char[] c, Charset charset){
		String s = new String(c);
		byte[] b = s.getBytes(charset);
		return b;
	}
	private static byte[] getBytes(String s, Charset charset){
		byte[] b = s.getBytes(charset);
		return b;
	}
	private static byte[] getBytes(int i, int length, Charset charset){
		String format = String.format("%%%02dd", length);
		String s = String.format(format, i);
		byte[] b = s.getBytes(charset);
		return b;
	}
	private static byte[] flatten(byte[][] src){
		int length = 0;
		for (int i = 0; i < src.length; ++i){
			length += src[i].length;
		}
		byte[] dest = new byte[length];
		int k = 0;
		for (int r = 0; r < src.length; ++r){
			for (int c = 0; c < src[r].length; ++c){
				dest[k] = src[r][c];
				++k;
			}
		}
		return dest;
	}
}
