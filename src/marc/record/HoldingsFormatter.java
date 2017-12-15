package marc.record;

import java.util.List;

import marc.field.DataField;
import marc.field.Field;

public class HoldingsFormatter extends RecordFormatter {
	private String[] location;
	private String[] copies;
	private String[] itemsHeld;
	private String publicationPattern;
	private String notes;

	public HoldingsFormatter(){
		super();
		
		location = new String[0];
		itemsHeld = new String[0];
		publicationPattern = "";
		notes = "";
	}
	
	@Override
	public void parse(Record record) {
		// TODO parse item holdings
		List<Field> locField = record.getField("852");
		DataField locDF;
		char[] locCode = {'a', 'b', 'c'};
		char[] copyCode = {'t', '3'};
		location = new String[locField.size()];
		copies = new String[location.length];
		for (int i = 0; i < location.length; ++i){
			locDF = (DataField) locField.get(i);
			location[i] = format(locDF, locCode, ", ");
			copies[i] = format(locDF, copyCode, " ");
		}
		
		//final DataField itemField = (DataField) record.getFirstMatchingField("853");
		
		
		//final DataField noteField = (DataField) record.getFirstMatchingField("506");
	}

	@Override
	public String getContent() {
		StringBuilder buf = new StringBuilder();
		
		buf.append("Call Number: ");
		buf.append(callNumber);
		buf.append('\n');
		
		buf.append("Location:\n");
		for (int i = 0; i < location.length; ++i){
			buf.append(location[i]);
			buf.append(" Copy ");
			buf.append(copies[i]);
			buf.append('\n');
		}
		
		buf.append("Items Held:\n");
		for (int i = 0; i < itemsHeld.length; ++i){
			buf.append(itemsHeld[i]);
			buf.append('\n');
		}
		
		buf.append("Publication Pattern:\n");
		buf.append(publicationPattern);
		buf.append('\n');
		
		buf.append("Notes:\n");
		buf.append(notes);
		buf.append('\n');
		
		return buf.toString();
	}

}
