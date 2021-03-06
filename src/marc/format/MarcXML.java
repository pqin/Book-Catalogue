package marc.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import marc.field.DataField;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;
import marc.record.Record;
import marc.record.RecordBuilder;

public class MarcXML extends AbstractMarc {
	private static final String NAMESPACE = "xmlns";
	private static final String NAMESPACE_URL = "http://www.loc.gov/MARC21/slim";
	private static final String CATALOGUE = "collection";
	private static final String RECORD = "record";
	private static final String LEADER = "leader";
	private static final String CONTROLFIELD = "controlfield";
	private static final String DATAFIELD = "datafield";
	private static final String TAG = "tag";
	private static final Pattern INDICATOR = Pattern.compile("ind([0-9]+)");
	private static final String SUBFIELD = "subfield";
	private static final String CODE = "code";
	
	@Override
	public FileNameExtensionFilter getExtensionFilter() {
    	String description = "MARC XML";
		String[] ext = {"marcxml", "xml"};
		String filterDesc = buildFilterDescription(description, ext);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, ext);
		return filter;
	}

	@Override
	public ArrayList<Record> read(File file) throws FileNotFoundException, IOException {
		ArrayList<Record> list = new ArrayList<Record>();
		RecordBuilder builder = new RecordBuilder();
		char code = '\0';
        
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream in = new FileInputStream(file);
	    XMLStreamReader reader = null;
	    String localName = null;
	    String attributeName = null;
	    String content = null;
	    int g = -1;
		Matcher m = null;
	    
	    try {
			reader = factory.createXMLStreamReader(in);
			while (reader.hasNext()){
				switch (reader.next()){
				case XMLStreamConstants.START_ELEMENT:
					localName = reader.getLocalName();
					switch (localName){
					case CATALOGUE:
						break;
					case RECORD:
						break;
					case LEADER:
						break;
					case CONTROLFIELD:
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							if (TAG.equals(reader.getAttributeLocalName(i))){
								builder.createField(reader.getAttributeValue(i));
							}
						}
						break;
					case DATAFIELD:
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							attributeName = reader.getAttributeLocalName(i);
							m = INDICATOR.matcher(attributeName);
							if (TAG.equals(attributeName)){
								builder.createField(reader.getAttributeValue(i));
							} else if (m.matches()){
								g = -1;
								g = Integer.parseInt(m.group(1), 10) - 1;
								if (g != -1){
									builder.setIndicator(g, reader.getAttributeValue(i).charAt(0));
								}
							}
						}
						break;
					case SUBFIELD:
						for (int i = 0; i < reader.getAttributeCount(); ++i){
							if (CODE.equals(reader.getAttributeLocalName(i))){
								code = reader.getAttributeValue(i).charAt(0);
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
					case RECORD:
						list.add(builder.build());
						builder.reset();
						break;
					case LEADER:
						builder.setLeader(content);
						break;
					case CONTROLFIELD:
						builder.setControlData(content);
						builder.addField();
						break;
					case DATAFIELD:
						builder.addField();
						break;
					case SUBFIELD:
						builder.addSubfield(code, content);
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
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		final String version = "1.0";
		final Charset encoding = StandardCharsets.UTF_8;
		final String charsetName = encoding.name();
		final String newline = System.lineSeparator();
		final String indent = "  ";
		
		Record record = null;
		List<Field> field = null;
		Field f = null;
		String tag = null;
		Subfield subfield = null;
		int subfieldCount = 0;
		String content = null;
		Iterator<Record> it = null;
		
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
	    XMLStreamWriter writer = null;
	    FileOutputStream out = new FileOutputStream(file);
	    try {
			writer = factory.createXMLStreamWriter(out, charsetName);
			writer.writeStartDocument(charsetName, version);
			writer.writeCharacters(newline);
			writer.writeStartElement(CATALOGUE);
			writer.writeNamespace(NAMESPACE, NAMESPACE_URL);
			writer.writeCharacters(newline);
			it = data.iterator();
			while (it.hasNext()){
				record = it.next();
				field = record.getFields();
				
				writer.writeCharacters(indent);
				writer.writeStartElement(RECORD);
				writer.writeCharacters(newline);
				for (int i = 0; i < field.size(); ++i){
					writer.writeCharacters(indent);
					writer.writeCharacters(indent);
					f = field.get(i);
					tag = f.getTag();
					if (Leader.TAG.equals(tag)){
						content = String.valueOf(f.getFieldData());
						writer.writeStartElement(LEADER);
						writer.writeCharacters(content);
						writer.writeEndElement();
						writer.writeCharacters(newline);
					} else if (Field.isControlTag(tag)){
						content = String.valueOf(f.getFieldData());
						writer.writeStartElement(CONTROLFIELD);
						writer.writeAttribute(TAG, tag);
						writer.writeCharacters(content);
						writer.writeEndElement();
						writer.writeCharacters(newline);
					} else {
						writer.writeStartElement(DATAFIELD);
						writer.writeAttribute(TAG, tag);
						for (int j = 0; j < Field.INDICATOR_COUNT; ++j){
							writer.writeAttribute(String.format("ind%d", j+1), String.valueOf(f.getIndicator(j)));
						}
						writer.writeCharacters(newline);
						subfieldCount = f.getDataCount();
						for (int s = 0; s < subfieldCount; ++s){
							subfield = ((DataField) f).getSubfield(s);
							writer.writeCharacters(indent);
							writer.writeCharacters(indent);
							writer.writeCharacters(indent);
							writer.writeStartElement(SUBFIELD);
							writer.writeAttribute(CODE, String.valueOf(subfield.getCode()));
							writer.writeCharacters(subfield.getData());
							writer.writeEndElement();
							writer.writeCharacters(newline);
						}
						writer.writeCharacters(indent);
						writer.writeCharacters(indent);
						writer.writeEndElement();
						writer.writeCharacters(newline);
					}
				}
				writer.writeCharacters(indent);
				writer.writeEndElement();
				writer.writeCharacters(newline);
			}
			writer.writeEndElement();
			writer.writeCharacters(newline);
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

}
