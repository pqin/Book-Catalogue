package marc.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import marc.field.FixedDatum;

public class FixedDataParser {
	private static final int RADIX = 10;
	
	private class Identifier {
		private String tag, value;
		int index;
		
		public Identifier(String tag, int index, String value){
			this.tag = tag;
			this.index = index;
			this.value = value;
		}
	};
	
	public FixedDataParser(){
		
	}
	
	private String sanitize(String value, String nullValue){
		if (value == null){
			value = nullValue;
		} else {
			value = value.trim();
			if (value.isEmpty()){
				value = nullValue;
			}
		}
		return value;
	}
	
	public Map<Character, Format> read(File file) throws FileNotFoundException, IOException, XMLStreamException {
	    String formatName = null;
	    String tag = null;
	    String fieldName = null;
	    String parentName = null;
	    String label = null;
	    String description = null;
	    String codeValue = null;
	    String codeMeaning = null;
	    int fieldLength = 0;
	    int index = 0;
	    int length = 0;
	    
	    XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream in = null;
	    XMLStreamReader reader = null;
	    String localName = null;
	    String content = null;
	    
	    Map<Character, Format> formatList = new HashMap<Character, Format>();
	    Format format = null;
	    List<Character> formatCode = new ArrayList<Character>();
	    List<String> materialParent = new ArrayList<String>();
	    
	    List<ConfigType> parentConfigList = new ArrayList<ConfigType>();
	    ConfigType parentConfig = null;
	    ConfigType config = null;
	    String refTag = null;
	    int refIndex = -1;
	    List<Identifier> fieldID = new ArrayList<Identifier>();
	    
	    List<FixedDatum> fMap = new ArrayList<FixedDatum>();
	    FixedDatum fixedDatum = null;
	    Map<Character, String> codeMap = new HashMap<Character, String>();  
	    
	    try {
			in = new FileInputStream(file);
			reader = factory.createXMLStreamReader(in);
			while (reader.hasNext()){
				switch (reader.next()){
				case XMLStreamConstants.START_ELEMENT:
					localName = reader.getLocalName();
					switch (localName){
					case "format":
					case "material":
						formatName = null;
						formatCode.clear();
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "name":
								formatName = sanitize(reader.getAttributeValue(i), null);
								break;
							default:
								break;
							}
						}
						if (formatName != null){
							format = new Format(formatName);
						}
						break;
					case "common_field":
					case "field":
						tag = null;
						fieldName = null;
						fieldLength = 0;
						parentName = null;
						fieldID.clear();
						fMap.clear();
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "tag":
								tag = reader.getAttributeValue(i);
								break;
							case "length":
								fieldLength = Integer.parseInt(reader.getAttributeValue(i), RADIX);
								break;
							case "name":
								fieldName = sanitize(reader.getAttributeValue(i), null);
								break;
							case "short_form":
								break;
							case "parent":
								parentName = sanitize(reader.getAttributeValue(i), null);
								break;
							default:
								break;
							}
						}
						if (tag == null || fieldName == null){
							config = null;
						} else {
							if (fieldLength > 0){
								// use length attribute if defined
								config = new ConfigType(fieldName, fieldLength);
							} else if (parentName != null){
								// try to use parent length
								Iterator<ConfigType> it = parentConfigList.iterator();
								parentConfig = null;
								while (it.hasNext()){
									parentConfig = it.next();
									if (parentConfig.getName().equals(parentName)){
										fieldLength = parentConfig.getLength();
										break;
									}
								}
								if (parentConfig != null && fieldLength > 0){
									config = new ConfigType(fieldName, fieldLength);
									FixedDatum[] parentMap = parentConfig.getMap();
									for (int i = 0; i < parentMap.length; ++i){
										fMap.add(parentMap[i]);
										
									}
								} else {
									System.out.printf("Parent '%s' not defined.%n", parentName);
									config = null;
								}
							} else {
								System.out.println("Field length not defined.");
								config = null;
							}
						}
						break;
					case "identifier":
						refTag = null;
						refIndex = -1;
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "tag":
								refTag = reader.getAttributeValue(i);
								break;
							case "index":
								refIndex = Integer.parseInt(reader.getAttributeValue(i), RADIX);
								break;
							default:
								break;
							}
						}
						break;
					case "fixed_datum":
						codeMap.clear();
						index = -1;
						length = 0;
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "index":
								index = Integer.parseInt(reader.getAttributeValue(i), RADIX);
								break;
							case "length":
								length = Integer.parseInt(reader.getAttributeValue(i), RADIX);
								break;
							default:
								break;
							}
						}
						break;
					case "label":
						label = null;
						break;
					case "description":
						description = null;
						break;
					case "code":
						codeValue = null;
						codeMeaning = null;
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "value":
								codeValue = reader.getAttributeValue(i);
								break;
							default:
								break;
							}
						}
						break;
					case "type_format":
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							switch (reader.getAttributeLocalName(i)){
							case "name":
								materialParent.add(reader.getAttributeValue(i));
								break;
							default:
								break;
							}
						}
						break;
					default:
						break;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					content = reader.getText();
					break;
				case XMLStreamConstants.END_ELEMENT:
					localName = reader.getLocalName();
					switch (localName){
					case "format":
						if (format != null){
							Iterator<Character> it = formatCode.iterator();
							while (it.hasNext()){
								formatList.put(it.next(), format);
							}
						}
						break;
					case "type_code":
						if (content != null && content.length() == 1){
							formatCode.add(content.charAt(0));
						}
						break;
					case "common_field":
						if (config != null){
							config.setMap(fMap);
							parentConfigList.add(config);
						}
						break;
					case "field":
						if (config != null){
							config.setMap(fMap);
							if (fieldID.isEmpty()){
								// use 'type_code' of parent Format if no 'field_identifier' defined
								for (char code : formatCode){
									format.addConfiguration(tag, code, config);
								}
							} else {
								// use 'field_identifier' if defined
								for (Identifier id : fieldID){
									format.addConfiguration(id.tag, id.index, tag, id.value, config);
								}
							}
						}
						break;
					case "identifier":
						if (refTag != null && refIndex >= 0){
							fieldID.add(new Identifier(refTag, refIndex, content));
						}
						break;
					case "fixed_datum":
						if (length > 0 && index >= 0 && index < config.getLength()){
							fixedDatum = new FixedDatum(index, length, label, description);
							Iterator<Character> it = codeMap.keySet().iterator();
							Character c;
							while (it.hasNext()){
								c = it.next();
								fixedDatum.addEntry(c, codeMap.get(c));
							}
							fMap.add(fixedDatum);
						}
						break;
					case "label":
						label = content;
						break;
					case "description":
						description = content;
						break;
					case "code":
						codeMeaning = content;
						if (codeValue != null && codeMeaning != null){
							codeMap.put(codeValue.charAt(0), codeMeaning);
						}
						break;
					case "material":
						if (format != null){
							Iterator<Character> it = formatList.keySet().iterator();
							char c;
							Format fmt = null;
							while (it.hasNext()){
								c = it.next();
								fmt = formatList.get(c);
								for (String m : materialParent){
									if (fmt.getName().equals(m)){
										fmt.addConfiguration(format);
									}
								}
							}
							format = null;
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
		} finally {
			try {
				if (reader != null){
					reader.close();
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	    return formatList;
	}
}
