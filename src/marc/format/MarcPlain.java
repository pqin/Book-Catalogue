package marc.format;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileNameExtensionFilter;

import marc.MARC;
import marc.Record;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;
import marc.resource.Resource;

public class MarcPlain extends AbstractMarc {
	public static final Pattern FIELD_REGEX = Pattern.compile("(LDR|\\d{3})([#\\d])([#\\d])");
    public static final Pattern SUBFIELD_REGEX = Pattern.compile("\\$([a-z0-9])([^\\$\\r\\n]*)");
	
    @Override
	public FileNameExtensionFilter getExtensionFilter() {
    	String description = "Plain MARC";
		String[] ext = {"txt"};
		String filterDesc = buildFilterDescription(description, ext);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, ext);
		return filter;
	}
    
    private void build(Record record, Field field){
    	String tag = field.getTag();
    	char ind1, ind2;
    	String data = null;
    	Subfield subfield = null;
    	Leader leader = null;
    	Resource resource = null;
    	ControlField cField = null;
    	if (tag.startsWith("00")){
    		subfield = field.getSubfield(0);
    		cField = new ControlField(tag, subfield.getData().length());
    		cField.setSubfield(0, subfield);
    	}
    	
		if (tag.equals(MARC.LEADER_TAG)){
    		leader = new Leader();
    		subfield = field.getSubfield(0);
        	if (subfield == null){
        		data = "";
        	} else {
        		data = subfield.getData();
        	}
    		leader.setAllSubfields(data.replace('#', MARC.BLANK_CHAR));
    		record.setLeader(leader);
    	} else if (tag.equals(MARC.RESOURCE_TAG)){
    		resource = new Resource();
    		subfield = field.getSubfield(0);
        	if (subfield == null){
        		data = "";
        	} else {
        		data = subfield.getData();
        	}
    		resource.setAllSubfields(data.replace('#', MARC.BLANK_CHAR));
    		record.setResource(resource);
    	} else {
    		ind1 = field.getIndicator1();
    		ind2 = field.getIndicator2();
    		ind1 = (ind1 == '#')? MARC.BLANK_CHAR: ind1;
    		ind2 = (ind2 == '#')? MARC.BLANK_CHAR: ind2;
    		field.setIndicators(ind1, ind2);
    		if (tag.startsWith("00")){
    			record.addField(cField);
    		} else {
    			record.addField((DataField) field);
    		}
    	}
    }

	@Override
	public ArrayList<Record> read(File file) throws FileNotFoundException, IOException {
		BufferedReader in = null;
        String line = null;

        Matcher m1 = null;	// match field pattern
        Matcher m2 = null;	// match subfield pattern
        
        ArrayList<Record> list = new ArrayList<Record>();
        Record record = null;
        DataField field = null;
        String tag = null;
        char ind1 = MARC.BLANK_CHAR;
        char ind2 = MARC.BLANK_CHAR;
        
        in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        while ((line = in.readLine()) != null){
            m1 = FIELD_REGEX.matcher(line);
            m2 = SUBFIELD_REGEX.matcher(line);
            if (m1.find()){
                tag = m1.group(1);
                ind1 = m1.group(2).charAt(0);
                ind2 = m1.group(3).charAt(0);
                // add data from before previous loop
                if (record != null){
                	build(record, field);
                    if (tag.equals(MARC.LEADER_TAG)){
                    	record.sortFields();
                        list.add(record);
                    }
                }
                // start new loop
                if (tag.equals(MARC.LEADER_TAG)){
                    record = new Record();
                }
                field = new DataField(tag, ind1, ind2);
            }
            if (field != null){
            	while (m2.find()){
                    field.addSubfield(m2.group(1).charAt(0), m2.group(2));
                }
            }
        }
        in.close();
        // add data from last loop
        if (record != null){
        	build(record, field);
        	record.sortFields();
            list.add(record);
        }
        return list;
	}
	
	@Override
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		final Charset encoding = StandardCharsets.UTF_8;
		final String charsetName = encoding.displayName(MARC.COUNTRY_LOCALE);
		BufferedWriter out = null;
		
		Record record = null;
		String tag = null;
		char ind1, ind2;
		String subdata = null;
		Subfield subfield = null;
		Iterator<Record> it = null;
		
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
		it = data.iterator();
		while (it.hasNext()){
			record = it.next();
			for (Field f : record.getFields()){
				tag = f.getTag();
				ind1 = f.getIndicator1();
				ind2 = f.getIndicator2();
				ind1 = (ind1 == MARC.BLANK_CHAR)? '#': ind1;
	    		ind2 = (ind2 == MARC.BLANK_CHAR)? '#': ind2;
				out.write(tag);
				out.write(ind1);
				out.write(ind2);
				for (int s = 0; s < f.getDataCount(); ++s){
					subfield = f.getSubfield(s);
					out.write('$');
					out.write(subfield.getCode());
					subdata = subfield.getData();
					if (tag.equals(MARC.LEADER_TAG)){
						subdata = subdata.replace(MARC.BLANK_CHAR, '#');
					} else if (tag.equals(MARC.RESOURCE_TAG)){
						subdata = subdata.replace(MARC.BLANK_CHAR, '#');
					}
					out.write(subdata);
				}
				out.newLine();
			}
			out.newLine();
		}
		out.close();
	}
}
