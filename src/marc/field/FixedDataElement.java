package marc.field;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import marc.MARC;

public final class FixedDataElement extends FixedField {
	public static final String TAG = "008";
	private static final int ENTRYDATE_INDEX = 0;
	private static final int ENTRYDATE_LENGTH = 6;
	private static final DateTimeFormatter formatter;
	static {
		// build DateTimeFormatter with pattern: yyMMdd, where years are in the range 1968 - 2067
		final int width = 2;
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder = builder.appendValueReduced(ChronoField.YEAR, width, width, MARC.EPOCH_START);
		builder = builder.appendValue(ChronoField.MONTH_OF_YEAR, width);
		builder = builder.appendValue(ChronoField.DAY_OF_MONTH, width);
		formatter = builder.toFormatter(MARC.LANGUAGE_LOCALE);
	}
	
	public FixedDataElement(){
		super(TAG, ENTRYDATE_LENGTH);
		setEntryDate(MARC.EPOCH_START);
	}
	public FixedDataElement(final int length){
		super(TAG, length);
		setEntryDate(MARC.EPOCH_START);
	}
	
	final public void setEntryDate(LocalDate entryDate){
		char[] value = entryDate.format(formatter).toCharArray();
		setData(value, ENTRYDATE_INDEX, ENTRYDATE_LENGTH);
	}
	final public LocalDate getEntryDate(){
		String text = String.copyValueOf(data, ENTRYDATE_INDEX, ENTRYDATE_LENGTH);
		LocalDate entryDate = LocalDate.parse(text, formatter);
		return entryDate;
	}
	
	@Override
	public final void clear(){
		LocalDate tmp = getEntryDate();
		super.clear();
		setEntryDate(tmp);
	}
	
	public FixedDataElement copy(){
		FixedDataElement copy = new FixedDataElement(this.data.length);
		copy.setFieldData(this.data);
		return copy;
	}
}
