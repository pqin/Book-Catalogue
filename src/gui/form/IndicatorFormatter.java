package gui.form;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import marc.field.Field;

public class IndicatorFormatter extends MaskFormatter {
	private static final long serialVersionUID = 1L;
	private static final char PLACEHOLDER = '_';
	
	public IndicatorFormatter(){
		super();
		
		try {
			setMask("*");
		} catch (ParseException e){
			e.printStackTrace();
		}
		char[] letter = new char[26];
		char[] uppercase = new char[letter.length];
		for (int i = 0; i < letter.length; ++i){
			letter[i] = (char)('a' + i);
			uppercase[i] = Character.toUpperCase(letter[i]);
		}
		char[] digit = new char[10];
		for (int i = 0; i < digit.length; ++i){
			digit[i] = (char)('0' + i);
		}
		StringBuilder buf = new StringBuilder();
		buf.append(Field.BLANK_INDICATOR);
		buf.append(letter);
		buf.append(uppercase);
		buf.append(digit);
		String validChars = buf.toString();
		setValidCharacters(validChars);
		
		setPlaceholder(null);
		setPlaceholderCharacter(PLACEHOLDER);
	}
	
	@Override
	public Class<?> getValueClass(){
		return Character.class;
	}
	
	@Override
	public String valueToString(Object object) throws ParseException {
		if (object == null){
			return super.valueToString(object);
		} else {
			char value = (char) object;
			return super.valueToString(value);
		}
	}
	@Override
	public Object stringToValue(String string) throws ParseException {
		if (string == null){
			throw new ParseException("String is null", 0);
		} else if (string.length() == 0){
			throw new ParseException("Empty String", 0);
		} else {
			char value = string.charAt(0);
			if (value == PLACEHOLDER){
				throw new ParseException("Empty String", 0);
			}
			if (Character.isUpperCase(value)){
				value = Character.toLowerCase(value);
			}
			return value;
		}
	}
}
