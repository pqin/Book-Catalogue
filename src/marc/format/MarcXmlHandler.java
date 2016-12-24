package marc.format;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import marc.MARC;
import marc.Record;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Leader;
import marc.field.Subfield;
import marc.resource.Resource;

public class MarcXmlHandler extends DefaultHandler {
	private static String CATALOGUE = "collection";
	private static String RECORD = "record";
	private static String LEADER = "leader";
	private static String CONTROLFIELD = "controlfield";
	private static String DATAFIELD = "datafield";
	private static String SUBFIELD = "subfield";
	private static String TAG = "tag";
	private static String INDICATOR1 = "ind1";
	private static String INDICATOR2 = "ind2";
	private static String CODE = "code";
	
	private ArrayList<Record> list;
	private Record record;
	private String tag;
	private char ind1, ind2;
	private ControlField cField;
	private Leader leader;
	private Resource resource;
	private DataField dField;
	private Subfield subField;
	private char code;
	private String data;
	
	public MarcXmlHandler(){
		super();
		list = null;
		record = null;
		tag = MARC.UNKNOWN_TAG;
		ind1 = MARC.BLANK_CHAR;
		ind2 = MARC.BLANK_CHAR;
		cField = null;
		leader = null;
		resource = null;
		dField = null;
		subField = null;
		code = 'a';
		data = null;
	}
	
	public ArrayList<Record> getRecords(){
		return list;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		int length = attributes.getLength();
		String attributeKey = null;
		String attributeValue = null;
		if (qName.equals(CATALOGUE)){
			if (list == null){
				list = new ArrayList<Record>();
			}
		} else if (qName.equals(RECORD)){
			record = new Record();
		} else if (qName.equals(LEADER)){
			leader = new Leader();
			tag = leader.getTag();
			ind1 = leader.getIndicator1();
			ind2 = leader.getIndicator2();
			code = 'a';
			data = null;
		} else if (qName.equals(CONTROLFIELD)){
			cField = null;
			tag = MARC.UNKNOWN_TAG;
			ind1 = MARC.BLANK_CHAR;
			ind2 = MARC.BLANK_CHAR;
			code = 'a';
			data = null;
			for (int i = 0; i < length; ++i){
				attributeKey = attributes.getQName(i);
				attributeValue = attributes.getValue(i);
				if (attributeKey.equals(TAG)){
					tag = attributeValue;
				}
			}
			if (tag.equals(MARC.RESOURCE_TAG)){
				resource = new Resource();
			}
		} else if (qName.equals(DATAFIELD)){
			dField = new DataField();
			tag = dField.getTag();
			ind1 = dField.getIndicator1();
			ind2 = dField.getIndicator2();
			code = 'a';
			data = null;
			for (int i = 0; i < length; ++i){
				attributeKey = attributes.getQName(i);
				attributeValue = attributes.getValue(i);
				if (attributeKey.equals(TAG)){
					tag = attributeValue;
					dField.setTag(tag);
				} else if (attributeKey.equals(INDICATOR1)){
					if (attributeValue.length() > 0){
						ind1 = attributeValue.replace(' ', MARC.BLANK_CHAR).charAt(0);
						dField.setIndicator1(ind1);
					}
				} else if (attributeKey.equals(INDICATOR2)){
					if (attributeValue.length() > 0){
						ind2 = attributeValue.replace(' ', MARC.BLANK_CHAR).charAt(0);
						dField.setIndicator2(ind2);
					}
				}
			}
		} else if (qName.equals(SUBFIELD)){
			subField = new Subfield();
			code = subField.getCode();
			data = null;
			for (int i = 0; i < length; ++i){
				attributeKey = attributes.getQName(i);
				attributeValue = attributes.getValue(i);
				if (attributeKey.equals(CODE)){
					code = attributeValue.charAt(0);
					subField.setCode(code);
				}
			}
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(CATALOGUE)){
			// do nothing
		} else if (qName.equals(RECORD)){
        	record.sortFields();
        	if (list == null){
        		list = new ArrayList<Record>();
        	}
			list.add(record);
			record = null;
		} else if (qName.equals(LEADER)){
			data = data.replace(' ', MARC.BLANK_CHAR);
			leader.setAllSubfields(data);
			record.setLeader(leader);
		} else if (qName.equals(CONTROLFIELD)){
			if (tag.equals(MARC.RESOURCE_TAG)){
				data = data.replace(' ', MARC.BLANK_CHAR);
				resource.setAllSubfields(data);
				record.setResource(resource);
			} else {
				cField = new ControlField(data.length());
				cField.setTag(tag);
				cField.setAllSubfields(data);
				record.addField(cField);
			}
		} else if (qName.equals(DATAFIELD)){
			record.addField(dField);
		} else if (qName.equals(SUBFIELD)){
			dField.addSubfield(code, data);
		}
	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data = String.copyValueOf(ch, start, length);
	}
}
