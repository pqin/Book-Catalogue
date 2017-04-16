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
	private static final String INDICATOR1 = "ind1";
	private static final String INDICATOR2 = "ind2";
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
	public ArrayList<Record> read(FileInputStream in) throws FileNotFoundException, IOException, RecordParseException {
		ArrayList<Record> list = new ArrayList<Record>();
		RecordBuilder builder = new RecordBuilder();
		char code = '\0';
        
		XMLInputFactory factory = XMLInputFactory.newInstance();
	    XMLStreamReader reader = null;
	    String localName = null;
	    String content = null;
	    
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
							switch (reader.getAttributeLocalName(i)){
							case TAG:
								builder.createField(reader.getAttributeValue(i));
								break;
							case INDICATOR1:
								builder.setIndicator1(reader.getAttributeValue(i).charAt(0));
								break;
							case INDICATOR2:
								builder.setIndicator2(reader.getAttributeValue(i).charAt(0));
								break;
							default:
								break;
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
				throw new RecordParseException("Failed to parse record.", e);
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
		ArrayList<Field> field = null;
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
						writer.writeAttribute(INDICATOR1, String.valueOf(f.getIndicator1()));
						writer.writeAttribute(INDICATOR2, String.valueOf(f.getIndicator2()));
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
