package marc.format;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import marc.MARC;
import marc.Record;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;

public class MarcXML extends AbstractMarc {
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
		ArrayList<Record> list = null;
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        MarcXmlHandler userhandler = new MarcXmlHandler();
        SAXParser parser = null;
        try {
        	parser = factory.newSAXParser();
        	parser.parse(file, userhandler);
        } catch (SAXException e){
        	e.printStackTrace();
        } catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			list = userhandler.getRecords();
		}
		return list;
	}

	private void writeLeader(BufferedWriter out, Leader leader) throws IOException{
		Subfield subfield = leader.getSubfield(0);
		String data = subfield.getData().replace(MARC.BLANK_CHAR, ' ');
		String element = String.format("    <leader>%s</leader>", data);
		out.write(element);
		out.newLine();
	}
	private void writeControlField(BufferedWriter out, Field field) throws IOException{
		String tag = field.getTag();
		Subfield subfield = field.getSubfield(0);
		String data = subfield.getData();
		if (tag.equals(MARC.RESOURCE_TAG)){
			data = data.replace(MARC.BLANK_CHAR, ' ');
		}
		String element = String.format("    <controlfield tag=\"%s\">%s</controlfield>", tag, data);
		out.write(element);
		out.newLine();
	}
	private void writeDataField(BufferedWriter out, Field field) throws IOException {
		String tag = field.getTag();
		char ind1 = field.getIndicator1();
		char ind2 = field.getIndicator2();
		Subfield subfield = null;
		
		ind1 = (ind1 == MARC.BLANK_CHAR) ? ' ': ind1;
		ind2 = (ind2 == MARC.BLANK_CHAR) ? ' ': ind2;
		
		String element = String.format("    <datafield tag=\"%s\" ind1=\"%c\" ind2=\"%c\">", tag, ind1, ind2);
		out.write(element);
		out.newLine();
		for (int s = 0; s < field.getDataCount(); ++s){
			subfield = field.getSubfield(s);
			element = String.format("      <subfield code=\"%c\">%s</subfield>", subfield.getCode(), subfield.getData());
			out.write(element);
			out.newLine();
		}
		out.write("    </datafield>");
		out.newLine();
	}
	
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		final int version = 1;
		final Charset encoding = StandardCharsets.UTF_8;
		final String charsetName = encoding.displayName(Locale.US);
		BufferedWriter out = null;
		
		Record record = null;
		ArrayList<Field> field;
		String tag = null;
		Iterator<Record> it = null;
		
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
		out.write(String.format("<?xml version=\"%d.0\" encoding=\"%s\"?>", version, charsetName));
		out.newLine();
		out.write(String.format("<collection xmlns=\"%s\">", "http://www.loc.gov/MARC21/slim"));
		out.newLine();
		
		it = data.iterator();
		while (it.hasNext()){
			record = it.next();
			out.write("  <record>");
			out.newLine();
			writeLeader(out, record.getLeader());
			field = record.getFields();
			Collections.sort(field);
			for (Field f : field){
				tag = f.getTag();
				if (tag.equals(MARC.LEADER_TAG)){
					// do nothing
				} else if (tag.startsWith("00")){
					writeControlField(out, f);
				} else {
					writeDataField(out, f);
				}
			}
			out.write("  </record>");
			out.newLine();
		}
		
		out.write("</collection>");
		out.newLine();
		out.close();
	}

}
