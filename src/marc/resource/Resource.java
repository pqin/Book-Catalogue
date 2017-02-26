package marc.resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Arrays;

import marc.MARC;
import marc.field.FixedField;

public final class Resource extends FixedField {
	private static final int DATE_LENGTH = 4;
	private static final DateTimeFormatter formatter = buildFormatter();
	
	public static final int ENTRY_DATE = 0;
	public static final int DATE_TYPE = 6;
	public static final int DATE_1 = 7;
	public static final int DATE_2 = 11;
	public static final int PLACE = 15;
	public static final int LANGUAGE = 35;
	public static final int MODIFIED_RECORD = 38;
	public static final int CATALOGUING_SOURCE = 39;
	
	public Resource(){
		super(MARC.RESOURCE_TAG, MARC.RESOURCE_FIELD_LENGTH);
		
		char[] defaultPlace = {'x', 'x', MARC.BLANK_CHAR};
		char[] defaultLanguage = {MARC.BLANK_CHAR, MARC.BLANK_CHAR, MARC.BLANK_CHAR};
		setData(defaultPlace, Resource.PLACE, defaultPlace.length);
		setData(defaultLanguage, Resource.LANGUAGE, defaultLanguage.length);
		setEntryDate(MARC.EPOCH_START);
	}
	
	private static final DateTimeFormatter buildFormatter(){
		// build DateTimeFormatter with pattern: yyMMdd, where years are in the range 1968 - 2067
		final int width = 2;
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder = builder.appendValueReduced(ChronoField.YEAR, width, width, MARC.EPOCH_START);
		builder = builder.appendValue(ChronoField.MONTH_OF_YEAR, width);
		builder = builder.appendValue(ChronoField.DAY_OF_MONTH, width);
		DateTimeFormatter f = builder.toFormatter(MARC.LANGUAGE_LOCALE);
		return f;
	}
	
	@Override
	public void setData(char[] value, int index, int length){
		switch (index){
		case DATE_1:
		case DATE_2:
			setDate(index, value);
			break;
		case PLACE:
		case LANGUAGE:
			setDataToValue(value, MARC.BLANK_CHAR, index, length);
			break;
		default:
			setDataToValue(value, MARC.FILL_CHAR, index, length);
			break;
		}
	}
	
	final public void setEntryDate(int year, int month, int day){		
		setDataToValue(year % 100, 0, 2);
		setDataToValue(month, 2, 2);
		setDataToValue(day, 4, 2);
	}
	final public void setEntryDate(LocalDate entryDate){
		char[] value = entryDate.format(formatter).toCharArray();
		setData(value, ENTRY_DATE, value.length);
	}
	final public LocalDate getEntryDate(){
		String text = String.copyValueOf(data, ENTRY_DATE, DATE_TYPE - ENTRY_DATE);
		LocalDate entryDate = MARC.EPOCH_START;
		try {
			entryDate = LocalDate.parse(text, formatter);
		} catch (DateTimeParseException e){
			//TODO entry date error exception handling
		}
		return entryDate;
	}

	final public void setDate1(String date){
		setDate(DATE_1, date.toCharArray());
	}
	final public String getDate1(){
		return getDate(DATE_1);
	}
	final public void setDate2(String date){
		setDate(DATE_2, date.toCharArray());
	}
	final public String getDate2(){
		return getDate(DATE_2);
	}
	
	final private void setDate(int index, char[] date){
		for (int i = 0; i < date.length; ++i){
			if (date[i] == '?'){
				date[i] = 'u';
			}
		}
		setDataToValue(date, MARC.BLANK_CHAR, index, DATE_LENGTH);
	}
	final private String getDate(int index){		
		char[] value = Arrays.copyOfRange(data, index, index + DATE_LENGTH);
		String date = String.copyValueOf(value);
		String d = date.replace('u', '?');
		return d;
	}
	
	@Override
	public final void clear(){
		LocalDate tmp = getEntryDate();
		super.clear();
		setEntryDate(tmp);
	}
	
	public Resource copy(){
		Resource copy = new Resource();
		copy.setFieldData(this.data);
		return copy;
	}
}
