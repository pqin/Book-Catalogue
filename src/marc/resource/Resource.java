package marc.resource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import marc.MARC;
import marc.field.ControlField;

public class Resource extends ControlField {
	private static final int DATE_LENGTH = 4;
	
	public static final int ENTRY_DATE = 0;
	public static final int DATE_TYPE = 6;
	public static final int DATE_1 = 7;
	public static final int DATE_2 = 11;
	public static final int PLACE = 15;
	public static final int LANGUAGE = 35;
	public static final int MODIFIED_RECORD = 38;
	public static final int CATALOGUING_SOURCE = 39;
	
	private TimeZone timeZone;
	private Locale locale;
	private Calendar calendar;
	private Date entryDate;
	private SimpleDateFormat entryFormat, outputFormat;
	private char[] place, language;
	
	public Resource(){
		super(MARC.RESOURCE_TAG, MARC.RESOURCE_FIELD_LENGTH);
		
		timeZone = TimeZone.getTimeZone("GMT");
		locale = Locale.US;
		calendar = Calendar.getInstance(timeZone, locale);
		calendar.set(MARC.EPOCH_START, 0, 1, 0, 0, 0);
		entryDate = calendar.getTime();
		entryFormat = new SimpleDateFormat("yyMMdd", locale);
		entryFormat.setTimeZone(timeZone);
		outputFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
		outputFormat.setTimeZone(timeZone);
		
		place = new char[3];
		language = new char[3];
		char[] defaultPlace = {'x', 'x', MARC.BLANK_CHAR};
		char[] defaultLanguage = new char[3];
		Arrays.fill(defaultLanguage, MARC.BLANK_CHAR);
		setPlace(new String(defaultPlace));
		setLanguage(new String(defaultLanguage));
	}
	
	@Override
	public int[] getIndexArray(){
		int[] array = {
				ENTRY_DATE,
				DATE_TYPE,
				DATE_1,
				DATE_2,
				PLACE,
				LANGUAGE,
				MODIFIED_RECORD,
				CATALOGUING_SOURCE
		};
		return array;
	}
	@Override
	public int getIndexLength(int index){
		int indexLength = 1;
		switch (index){
		case ENTRY_DATE:
			indexLength = 6;
			break;
		case DATE_1:
		case DATE_2:
			indexLength = DATE_LENGTH;
			break;
		case PLACE:
		case LANGUAGE:
			indexLength = 3;
			break;
		default:
			indexLength = 1;
			break;
		}
		return indexLength;
	}
	
	@Override
	public void setData(char[] value, int index, int length){
		switch (index){
		case DATE_1:
		case DATE_2:
			setDate(index, value);
			break;
		case PLACE:
			place = Arrays.copyOf(value, place.length);
			setDataToValue(value, MARC.BLANK_CHAR, index, length);
			break;
		case LANGUAGE:
			language = Arrays.copyOf(value, language.length);
			setDataToValue(value, MARC.BLANK_CHAR, index, length);
			break;
		default:
			setDataToValue(value, MARC.FILL_CHAR, index, length);
			break;
		}
	}
	
	@Override
	final public void setAllSubfields(String value){
		super.setAllSubfields(value);
		place = Arrays.copyOfRange(data, PLACE, PLACE + place.length);
		language = Arrays.copyOfRange(data, LANGUAGE, LANGUAGE + language.length);
		
		int year, month, day;
		year = getDateUnitFromData(0, 2);
		month = getDateUnitFromData(2, 2);
		day = getDateUnitFromData(4, 2);
		if (year >= 0 && year < 100){
			if (year < (MARC.EPOCH_START - 1900)){
				year += 2000;
			} else {
				year += 1900;
			}
		} else {
			year = MARC.EPOCH_START;
		}
		if (month < 0){
			month = 1;
		}
		if (day < 0){
			day = 1;
		}
		
		calendar.set(year, month-1, day, 0, 0, 0);
		entryDate = calendar.getTime();
	}
	
	// Date Record was entered into system. Format: yymmdd
	// Epoch start: 1968
	final public void setEntryDate(int year, int month, int day){
		calendar.set(year, month-1, day, 0, 0, 0);
		entryDate = calendar.getTime();
		
		year = calendar.get(Calendar.YEAR) % 100;
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		setDataToValue(year, 0, 2);
		setDataToValue(month, 2, 2);
		setDataToValue(day, 4, 2);
	}
	final private int getDateUnitFromData(int index, int length){
		char[] raw = Arrays.copyOfRange(data, index, index + length);
		String s = String.copyValueOf(raw);
		int unit = -1;
		try {
			unit = Integer.parseInt(s, 10);
		} catch (NumberFormatException ignore){}
		return unit;
	}
	final public String getEntryDate(){
		String d = outputFormat.format(entryDate);
		return d;
	}
	final public int getEntryYear(){
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	final public int getEntryMonth(){
		int month = calendar.get(Calendar.MONTH) + 1;
		return month;
	}
	final public int getEntryDay(){
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
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
	
	final public void setPlace(final String value){
		final char[] v = value.toCharArray();
		place = Arrays.copyOf(v, place.length);
		setDataToValue(v, MARC.BLANK_CHAR, PLACE, place.length);
	}
	final public String getPlace(){
		return String.valueOf(place);
	}
	final public void setLanguage(final String value){
		final char[] v = value.toCharArray();
		language = Arrays.copyOf(v, language.length);
		setDataToValue(v, MARC.BLANK_CHAR, LANGUAGE, language.length);
	}
	final public String getLanguage(){
		return String.valueOf(language);
	}
}
