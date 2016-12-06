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

import marc.MARC;
import marc.Record;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;
import marc.marc8.Marc8;
import marc.resource.Resource;

public class MarcDefault extends AbstractMarc {
	private static final Charset ASCII = StandardCharsets.US_ASCII;
	private static final Charset LATIN1 = StandardCharsets.ISO_8859_1;
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	
	private static final int DIRECTORY_ENTRY_LENGTH = 12;
	
	@Override
	public FileNameExtensionFilter getExtensionFilter() {
    	String description = "MARC";
		String[] ext = {"mrc", "marc", "marc8"};
		String filterDesc = buildFilterDescription(description, ext);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, ext);
		return filter;
	}

	private Leader parseLeader(final byte[] data){
		byte[] bytes = Arrays.copyOfRange(data, 0, MARC.LEADER_FIELD_LENGTH);
		Leader leader = new Leader();
		leader.setAllSubfields(bytes, ASCII);
		return leader;
	}
	
    private Record parseRecord(final byte[] data, final Charset encoding){
    	Marc8 marc8 = new Marc8();
    	Record record = new Record();
    	// parse Leader
    	Leader leader = parseLeader(data);
		record.setLeader(leader);
		// parse Directory
		int baseAddress = leader.getBaseAddress();
		byte[] directory = Arrays.copyOfRange(data, MARC.LEADER_FIELD_LENGTH, baseAddress);
		int entryCount = 0;
		if ((directory.length - 1) % DIRECTORY_ENTRY_LENGTH == 0){
			entryCount = Math.floorDiv(directory.length - 1, DIRECTORY_ENTRY_LENGTH);
		}
		int[][] index = new int[entryCount][4];
		for (int r = 0; r < entryCount; ++r){
			index[r][0] = (r * DIRECTORY_ENTRY_LENGTH) + 0;
			index[r][1] = (r * DIRECTORY_ENTRY_LENGTH) + 3;
			index[r][2] = (r * DIRECTORY_ENTRY_LENGTH) + 7;
			index[r][3] = (r * DIRECTORY_ENTRY_LENGTH) + DIRECTORY_ENTRY_LENGTH;
		}
		byte[] bytes = null;
		String[] tag = new String[entryCount];
		for (int r = 0; r < entryCount; ++r){
			bytes = Arrays.copyOfRange(directory, index[r][0], index[r][1]);
			tag[r] = new String(bytes, ASCII);
		}
		int[][] map = new int[entryCount][2];
		int mapValue = 0;
		for (int r = 0; r < entryCount; ++r){
			bytes = Arrays.copyOfRange(directory, index[r][1], index[r][2]);
			mapValue = MARC.parseValue(bytes, ASCII, 10);
			map[r][1] = mapValue;	// field length
			
			bytes = Arrays.copyOfRange(directory, index[r][2], index[r][3]);
			mapValue = MARC.parseValue(bytes, ASCII, 10);
			map[r][0] = mapValue;	// field index
		}
		// build Record
		ControlField cField = null;
		Resource rField = null;
		DataField dField = null;
		int fieldOffset = 0;
		int fieldLength = 0;
		String fieldData = null;
		char ind1, ind2;
		String[] subData = null;
		int index0, index1;
		for (int r = 0; r < entryCount; ++r){
			fieldOffset = baseAddress + map[r][0];
			fieldLength = map[r][1];
			bytes = Arrays.copyOfRange(data, fieldOffset, fieldOffset + fieldLength);
			if (encoding.equals(UTF8)){
				fieldData = new String(bytes, encoding);
			} else {
				fieldData = marc8.decode(bytes);
			}
			if (tag[r].startsWith("00")){
				ind1 = MARC.BLANK_CHAR;
				ind2 = MARC.BLANK_CHAR;
				index0 = 0;
				index1 = fieldData.indexOf(Marc8.FIELD_TERMINATOR);
				fieldData = fieldData.substring(index0, index1);
				subData = new String[1];
				if (tag[r].equals(MARC.RESOURCE_TAG)){
					subData[0] = fieldData.replace(' ', MARC.BLANK_CHAR);
				} else {
					subData[0] = fieldData;
				}
			} else {
				ind1 = fieldData.charAt(0);
				ind2 = fieldData.charAt(1);
				index0 = fieldData.indexOf(Marc8.SUBFIELD_DELIMITER)+1;
				index1 = fieldData.indexOf(Marc8.FIELD_TERMINATOR);
				fieldData = fieldData.substring(index0, index1);
				subData = fieldData.split(String.format("%c", Marc8.SUBFIELD_DELIMITER));
			}
			ind1 = (ind1 == ' ')? MARC.BLANK_CHAR: ind1;
			ind2 = (ind2 == ' ')? MARC.BLANK_CHAR: ind2;
			if (tag[r].startsWith("00")){
				if (tag[r].equals(MARC.RESOURCE_TAG)){
					rField = new Resource();
					rField.setAllSubfields(subData[0]);
					record.setResource(rField);
				} else {
					cField = new ControlField(tag[r], subData[0].length());
					cField.setAllSubfields(subData[0]);
					record.addField(cField);
				}
			} else {
				dField = new DataField(tag[r], ind1, ind2);
				for (int i = 0; i < subData.length; ++i){
					dField.addSubfield(subData[i].charAt(0), subData[i].substring(1));
				}
				record.addField(dField);
			}
		}
    	return record;
    }
    
	@Override
	public ArrayList<Record> read(File file) throws FileNotFoundException, IOException {
		Charset encoding = null;
		
		byte[] leader = new byte[MARC.LEADER_FIELD_LENGTH];
		byte[] directory = null;
		byte[] fieldData = null;
		byte[] recordData = null;
		
		ArrayList<Record> catalogue = new ArrayList<Record>();
		Record record = null;
		Leader ldr = null;
		int recordLength = 0;
		int baseAddress = 0;
		int destPos = 0;
		
		FileInputStream in = new FileInputStream(file);
		while (in.read(leader) != -1){
			ldr = parseLeader(leader);
			recordLength = ldr.getLength();
			baseAddress = ldr.getBaseAddress();
			directory = new byte[baseAddress - MARC.LEADER_FIELD_LENGTH];
			fieldData = new byte[recordLength - baseAddress];
			recordData = new byte[recordLength];
			
			if (ldr.getCharacterCodingScheme() == 'a'){
				encoding = UTF8;
			} else {
				encoding = LATIN1;
				// TODO MARC-8 custom Charset?
			}

			in.read(directory);
			in.read(fieldData);
			destPos = 0;
			System.arraycopy(leader, 0, recordData, destPos, leader.length);
			destPos += leader.length;
			System.arraycopy(directory, 0, recordData, destPos, directory.length);
			destPos += directory.length;
			System.arraycopy(fieldData, 0, recordData, destPos, fieldData.length);
			
			record = parseRecord(recordData, encoding);
			catalogue.add(record);
		}
		in.close();
		
		return catalogue;
	}

	@Override
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		final Charset encoding = UTF8;
		final char charCodingScheme = 'a';
		FileOutputStream out = new FileOutputStream(file);
		Record record = null;
		ArrayList<Field> field = null;
		Field f = null;
		String tag = null;
		byte[] leader = null;
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
			leader = getBytes(record.getLeader().getData(0, MARC.LEADER_FIELD_LENGTH), ASCII);
			field = record.getFields();
			directory = new byte[field.size() - 1][DIRECTORY_ENTRY_LENGTH];
			fieldData = new byte[field.size() - 1][];
			fi = 0;
			fl = 0;
			for (int i = 1; i < field.size(); ++i){
				f = field.get(i);
				tag = f.getTag();
				fieldData[i-1] = getBytes(f, encoding);
				fl = fieldData[i-1].length;
				dirEntry[0] = getBytes(tag, ASCII);
				dirEntry[1] = getBytes(fl, 4, ASCII);
				dirEntry[2] = getBytes(fi, 5, ASCII);
				directory[i-1] = concatenateBytes(dirEntry);
				fi += fl;
			}
			// calculate addresses
			recordLength = leader.length;
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
			Leader l = record.getLeader();
			l.setLength(recordLength);
			l.setBaseAddress(baseAddress);
			l.setCharacterCodingScheme(charCodingScheme);
			leader = getBytes(l.getData(0, MARC.LEADER_FIELD_LENGTH), ASCII);
			out.write(leader);
			// write directory
			for (int i = 0; i < directory.length; ++i){
				out.write(directory[i]);
			}
			out.write(Marc8.FIELD_TERMINATOR);
			// write Field data
			for (int i = 0; i < fieldData.length; ++i){
				out.write(fieldData[i]);
			}
			out.write(Marc8.RECORD_TERMINATOR);
		}
		out.close();
	}
	
	private static byte[] getBytes(Field f, Charset charset){
		final boolean controlField = f.isControlField();
		final int subCount = f.getSubfieldCount();
		Subfield s = null;
		byte[][] tmp = null;
		int k = 0;
		if (controlField){
			tmp = new byte[subCount][];
			for (int i = 0; i < subCount; ++i){
				s = f.getSubfield(i);
				tmp[i] = getBytes(s.getData(), charset);
			}
		} else {
			tmp = new byte[(2*subCount) + 1][];
			// indicators
			tmp[0] = new byte[2];
			tmp[0][0] = getBytes(f.getIndicator1(), charset)[0];
			tmp[0][1] = getBytes(f.getIndicator2(), charset)[0];
			// subfield code and data
			for (int i = 0; i < subCount; ++i){
				s = f.getSubfield(i);
				k = (i * 2) + 1;
				tmp[k+0] = new byte[2];
				tmp[k+0][0] = Marc8.SUBFIELD_DELIMITER;
				tmp[k+0][1] = getBytes(s.getCode(), charset)[0];
				tmp[k+1] = getBytes(s.getData(), charset);
			}
		}
		
		byte[] t = concatenateBytes(tmp);
		byte[] b = Arrays.copyOf(t, t.length + 1);
		b[t.length] = Marc8.FIELD_TERMINATOR;
		return b;
	}
	
	private static byte[] getBytes(Character c, Charset charset){
		String s = String.valueOf(c);
		byte[] b = s.getBytes(charset);
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
	private static byte[] concatenateBytes(byte[][] src){
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
