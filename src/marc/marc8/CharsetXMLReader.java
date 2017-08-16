package marc.marc8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

final public class CharsetXMLReader {
	private static final String CHARACTER_SET = "characterSet";
	private static final String NAME = "name";
	private static final String FINAL = "ISOcode";
	private static final String CODE = "code";
	private static final String MARC = "marc";
	private static final String UCS = "ucs";
	
	private static final int START_INDEX = 0x21;
	private static final int END_INDEX = 0x7E;
	private static final int TABLE_LENGTH = (END_INDEX - START_INDEX) + 1;
	private static final int LOW_BYTE_MASK = 0x7F;
	private static final int RADIX = 16;
	
	public List<GraphicSet> read(String filename){
		List<GraphicSet> list = new ArrayList<GraphicSet>();
		File file = new File(filename);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in == null){
				return list;
			}
		}
		
		GraphicSet set = null;
		char[] table = new char[0];
		Map<String, Character> map = new HashMap<String, Character>(128, 0.75f);
		int[] base = new int[0];
		int[] tmp = new int[0];
		int tableIndex = 0;
		
		String localName = null;
		String content = null;
		
		String name = null;
		String finalByte = null;
		byte f = 0x00;
		int byteLength = 0;
		String marcValue = null;
		int ucsValue = 0;
		char[] c = null;
		boolean extended = false;
		
		XMLStreamReader reader = null;
		try {
			reader = factory.createXMLStreamReader(in);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		try {
			while (reader.hasNext()){
				switch (reader.next()){
				case XMLStreamConstants.START_ELEMENT:
					localName = reader.getLocalName();
					switch (localName){
					case CHARACTER_SET:
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case NAME:
								name = reader.getAttributeValue(i);
								break;
							case FINAL:
								finalByte = reader.getAttributeValue(i);
								f = 0;
								try {
									f = (byte) Integer.parseUnsignedInt(finalByte, RADIX);
								} catch (NumberFormatException e){
								} finally {
									extended = (f == 0x45);	// ANSEL is only set with extended intermediate bytes
								}
							default:
								break;
							}
						}
						byteLength = 0;
						map.clear();
						break;
					case CODE:
						break;
					case MARC:
						marcValue = null;
						break;
					case UCS:
						ucsValue = 0;
						break;
					default:
						break;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					content = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					localName = reader.getLocalName();
					switch (localName){
					case CHARACTER_SET:
						set = new GraphicSet(name, f, byteLength, extended);
						
						if (base.length != byteLength){
							base = new int[byteLength];
							tmp = new int[byteLength];
							int length = 1;
							for (int i = 0; i < byteLength; ++i){
								base[i] = length;
								length *= TABLE_LENGTH;
								tmp[i] = 0;
							}
						}
						table = new char[TABLE_LENGTH*base[byteLength - 1]];
						Arrays.fill(table, GraphicSet.UNKNOWN_CHAR);
						
						Iterator<Entry<String, Character>> it = map.entrySet().iterator();
						Entry<String, Character> entry = null;
						int b = 0;
						int g = 0;
						while (it.hasNext()){
							entry = it.next();
							marcValue = entry.getKey();
							tableIndex = 0;
							if (marcValue.length() == byteLength*2){
								for (int i = 0; i < byteLength; ++i){
									b = Integer.parseUnsignedInt(marcValue.substring(2*i, 2*(i+1)), RADIX);
									if (b >= START_INDEX && b <= END_INDEX){
										tableIndex += ((b & LOW_BYTE_MASK) - START_INDEX)*base[i];
										g = 0;
									} else if (b >= START_INDEX+0x80 && b <= END_INDEX+0x80){
										tableIndex += ((b & LOW_BYTE_MASK) - START_INDEX)*base[i];
										g = 1;
									} else {
										tableIndex = b;
										break;
									}
								}
								if (tableIndex >= 0 && tableIndex < table.length){
									table[tableIndex] = entry.getValue();
								}
							}
						}
						set.setTable(table);
						set.setGraphicSet(g);
						
						list.add(set);
						break;
					case CODE:
						if (marcValue != null){
							map.put(marcValue, c[0]);
						}
						break;
					case MARC:
						if (content.length() > 0 && content.length() % 2 == 0){
							int newByteLength = content.length() / 2;
							if (newByteLength > byteLength){
								byteLength = newByteLength;
							}
							marcValue = content;
						}
						break;
					case UCS:
						try {
							ucsValue = Integer.parseUnsignedInt(content, RADIX);
							c = Character.toChars(ucsValue);
						} catch (NumberFormatException e){
						} catch (IllegalArgumentException e){
						}
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		} catch (XMLStreamException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (XMLStreamException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
}
