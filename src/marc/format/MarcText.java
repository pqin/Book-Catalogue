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

import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedField;
import marc.field.Leader;
import marc.field.Subfield;
import marc.record.Record;
import marc.record.RecordBuilder;

public class MarcText extends AbstractMarc {
	private static final Charset IO_CHARSET = StandardCharsets.UTF_8;
	private static final Pattern FIELD_REGEX = Pattern.compile("(LDR|\\d{3})([#\\d])([#\\d])");
    private static final Pattern SUBFIELD_REGEX = Pattern.compile("\\$([a-z0-9])([^\\$\\r\\n]*)");
    private static final char INDICATOR_BLANK_REPLACEMENT = 0x23;
    private static final char FIELD_BLANK_REPLACEMENT = 0x23;
	
    @Override
	public FileNameExtensionFilter getExtensionFilter() {
    	String description = "Plain MARC";
		String[] ext = {"txt"};
		String filterDesc = buildFilterDescription(description, ext);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDesc, ext);
		return filter;
	}

	@Override
	public ArrayList<Record> read(File file) throws FileNotFoundException, IOException {
		BufferedReader in = null;
        String line = null;

        Matcher m1 = null;	// match field pattern
        Matcher m2 = null;	// match subfield pattern
        
        ArrayList<Record> list = new ArrayList<Record>();
        int recordCount = list.size();
        RecordBuilder builder = new RecordBuilder();
        Record record = null;
        String tag, data;
        char[] ind = new char[Field.INDICATOR_COUNT];
        
        in = new BufferedReader(new InputStreamReader(new FileInputStream(file), IO_CHARSET));
        while ((line = in.readLine()) != null){
            m1 = FIELD_REGEX.matcher(line);
            if (m1.find()){
            	tag = m1.group(1);
            	for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
            		ind[i] = m1.group(2+i).charAt(0);
            	}
            	if (Leader.TAG.equals(tag)){
            		if (recordCount != list.size()){
            			record = builder.build();
                		list.add(record);
                		builder.reset();
            		}
            		++recordCount;
            	}
            	builder.createField(tag);
            	for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
					builder.setIndicator(i, (ind[i] == INDICATOR_BLANK_REPLACEMENT) ? Field.BLANK_INDICATOR : ind[i]);
				}
            	m2 = SUBFIELD_REGEX.matcher(line);
            	while (m2.find()){
            		data = m2.group(2);
            		if (Field.isFixedFieldTag(tag)){
            			builder.setControlData(data.replace(FIELD_BLANK_REPLACEMENT, FixedField.BLANK));
            			break;
            		} else if (Field.isControlTag(tag)){
            			builder.setControlData(data);
            			break;
            		} else {
            			builder.addSubfield(m2.group(1).charAt(0), data);
            		}
            	}
            	builder.addField();
            }
        }
        in.close();
        // add data from last loop
        if (recordCount != list.size()){
    		record = builder.build();
    		list.add(record);
    	}
        return list;
	}
	
	@Override
	public void write(File file, List<Record> data) throws FileNotFoundException, IOException {
		BufferedWriter out = null;
		
		Record record = null;
		String tag = null;
		char[] ind = new char[Field.INDICATOR_COUNT];
		String fieldData = null;
		Subfield subfield = null;
		Iterator<Record> it = null;
		
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), IO_CHARSET));
		it = data.iterator();
		while (it.hasNext()){
			record = it.next();
			for (Field f : record.getFields()){
				tag = f.getTag();
				for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
					ind[i] = f.getIndicator(i);
					ind[i] = (ind[i] == Field.BLANK_INDICATOR) ? INDICATOR_BLANK_REPLACEMENT : ind[i];
				}
				out.write(tag);
				out.write(ind);
				if (Field.isFixedFieldTag(tag)){
					fieldData = f.getFieldString();
					fieldData = fieldData.replace(FixedField.BLANK, FIELD_BLANK_REPLACEMENT);
					out.write("$a");
					out.write(fieldData);
				} else if (Field.isControlTag(tag)){
					fieldData = f.getFieldString();
					out.write("$a");
					out.write(fieldData);
				} else {
					for (int s = 0; s < f.getDataCount(); ++s){
						subfield = ((DataField) f).getSubfield(s);
						out.write('$');
						out.write(subfield.getCode());
						out.write(subfield.getData());
					}
				}
				out.newLine();
			}
			out.newLine();
		}
		out.close();
	}
}
