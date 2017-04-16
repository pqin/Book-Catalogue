/**
 * 
 */
package gui.form;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import marc.field.DataField;
import marc.field.Field;
import marc.field.Subfield;

/**
 * @author Peter
 *
 */
public class CatalogCardPanel extends RecordPanel {
	private static final Pattern FICTION_REGEX = Pattern.compile("^\\[([a-zA-Z]+)\\]$");
	private static final Pattern SEGMENTATION_REGEX = Pattern.compile("[/\u2032;]");
	private JTextArea callNumberField, contentField;

	public CatalogCardPanel(){
		super();
	}
	protected void layoutComponents(){
		Font monospace = new Font("monospaced", Font.PLAIN, 12);
		callNumberField = new JTextArea();
		callNumberField.setEditable(false);
		callNumberField.getCaret().setVisible(true);
		callNumberField.setLineWrap(false);
		callNumberField.setFont(monospace);
		
		contentField = new JTextArea();
		contentField.setEditable(false);
		contentField.getCaret().setVisible(true);
		contentField.setLineWrap(true);
		contentField.setWrapStyleWord(true);
		contentField.setFont(monospace);
		
		panel.setLayout(new BorderLayout());
		panel.add(callNumberField, BorderLayout.WEST);
		panel.add(new JScrollPane(contentField), BorderLayout.CENTER);
	}
	
	@Override
	public void clearForm(){
		callNumberField.setText(null);
		contentField.setText(null);
	}
	
	@Override
	protected void updateView(){
		char newline = '\n';
		
		String mainEntry = record.getMainEntry();
		if (mainEntry == null){
			mainEntry = "";
		}
		String[] callNumber = getCallNumber(mainEntry);
		callNumberField.setText(String.format("%s%n%s", callNumber[0], callNumber[1]));
		
		StringBuilder b = new StringBuilder();
		appendLn(b, mainEntry);
		append(b, record.getTitle());
		String edition = getEdition();
		if (edition != null && !edition.isEmpty()){
			append(b, ". -- ");
			append(b, edition);
		}
		b.append(newline);
		appendLn(b, getImprint());
		appendLn(b, getPhysicalDescription());
		appendLn(b, getSeries());
		b.append(newline);
		
		String[] summary = getSummary();
		appendLn(b, summary[0], summary[1]);
		appendLn(b, getNotes());
		appendLn(b, "ISBN", getISBN());
		b.append(newline);
		
		appendLn(b, getTopics());
		appendLn(b, getTracings());
		
		String lccn = getLCCN();
		String dcc = getDCC();
		if (lccn != null && dcc != null){
			append(b, lccn);
			append(b, "    ");
			append(b, dcc);
		} else if (lccn != null){
			appendLn(b, lccn);
		} else if (dcc != null){
			appendLn(b, dcc);
		}
		
		contentField.setText(b.toString());
	}
	
	private void append(StringBuilder builder, String value){
		if (value != null && !value.isEmpty()){
			builder.append(value);
		}
	}
	private void appendLn(StringBuilder builder, String value){
		if (value != null && !value.isEmpty()){
			builder.append(value);
			builder.append('\n');
		}
	}
	private void appendLn(StringBuilder builder, String key, String value){
		if (value != null && !value.isEmpty()){
			builder.append(key);
			builder.append(": ");
			builder.append(value);
			builder.append('\n');
		}
	}
	private void appendLn(StringBuilder builder, String[] value){
		if (value != null){
			for (int i = 0; i < value.length; ++i){
				builder.append(value[i]);
				builder.append('\n');
			}
		}
	}
	
	private String getFormattedData(String tag, char[] code, String delimiter){
		String[] list = record.getFormattedData(tag, code, delimiter);
		if (list.length == 0){
			return "";
		} else {
			return list[0];
		}
	}
	private String[] getCallNumber(String mainEntry){
		final int width = 8;
		String d = getDeweyNumber(width);
		String m = getAuthorIndicator(3, mainEntry);
		
		String[] callNumber = new String[2];
		String format = String.format("%%%ds", -1*width);
		callNumber[0] = String.format(format, d);
		callNumber[1] = String.format(format, m);
		return callNumber;
	}
	private String getDeweyNumber(final int length){
		String d = "???";
		String dewey = getDCC();
		if (dewey == null){
			return d;
		} else if (dewey.isEmpty()){
			return d;
		}
		Matcher m = null;
		m = FICTION_REGEX.matcher(dewey);
		if (m.matches() && m.groupCount() > 0){
			return m.group(1);
		}
		String[] token = SEGMENTATION_REGEX.split(dewey);
		StringBuilder b = new StringBuilder(token[0]);
		int numberLength = token[0].length();
		for (int i = 1; i < token.length; ++i){
			if (numberLength + token[i].length() <= length){
				b.append(token[i]);
				numberLength += token[i].length();
			} else {
				break;
			}
		}
		d = b.toString();
		return d;
	}
	/**
	 * <p>Get the first <i>length</i> letters from <i>main entry</i>, converted to uppercase.</p>
	 * @param length the number of characters to get
	 * @param mainEntry the author's name
	 * @return
	 */
	private String getAuthorIndicator(int length, String mainEntry){
		int endIndex = mainEntry.indexOf(',');
		if (endIndex == -1){
			endIndex = mainEntry.length();
		}
		char[] buffer = new char[length];
		Arrays.fill(buffer, ' ');
		int i = 0;
		int k = 0;
		char c;
		while (i < endIndex && k < buffer.length){
			c = mainEntry.charAt(i);
			++i;
			if (Character.isLetter(c)){
				buffer[k] = c;
				++k;
			}
		}
		String m = String.copyValueOf(buffer);
		m = m.toUpperCase(Locale.ENGLISH);
		return m;
	}
	private String getDCC(){
		String dewey = record.getData("082", 'a');
		return (dewey == null ? null : dewey.trim());
	}
	private String getLCCN(){
		return record.getData("010", 'a');
	}
	private String getISBN(){
		char[] code = {'a', 'q'};
		String isbn = getFormattedData("020", code, " ");
		return isbn;
	}
	private String getPhysicalDescription(){
		char[] code = {'a', 'b', 'c', 'e'};
		String description = getFormattedData("300", code, " ");
		return description;
	}
	private String getEdition(){
		return record.getData("250", 'a');
	}
	private String getSeries(){
		char[] code = {'a', 'v'};
		String series = getFormattedData("190", code, " ");
		if (series != null && series.length() > 0){
			series = "(" + series + ")";
		}
		return series;
	}
	private String[] getSummary(){
		String[] summary = new String[2];
		String tag = "520";
		char code = 'a';
		DataField f = (DataField) record.getFirstMatchingField(tag);
		Subfield sub = null;
		char indicator = Field.BLANK_INDICATOR;
		if (f == null){
			summary[0] = "Summary";
		} else {
			indicator = f.getIndicator1();
			switch (indicator){
			case Field.BLANK_INDICATOR:
				summary[0] = "Summary";
				break;
			case '0':
				summary[0] = "Subject";
				break;
			case '1':
				summary[0] = "Review";
				break;
			case '2':
				summary[0] = "Scope and content";
				break;
			case '3':
				summary[0] = "Abstract";
				break;
			case '4':
				summary[0] = "Content advice";
				break;
			case '8':
				summary[0] = "";	// No display constant generated.
				break;
			default:
				summary[0] = "Summary";
				break;
			}
			for (int i = 0; i < f.getDataCount(); ++i){
				sub = f.getSubfield(i);
				if (sub.getCode() == code){
					summary[1] = sub.getData();
					break;
				}
			}
		}
		return summary;
	}
	private String getNotes(){
		return record.getData("500", 'a');
	}
	private String[] getImprint(){
		char[] code = {'a', 'b', 'c'};
		String[] imprint = record.getFormattedData("260", code, " ");
		return imprint;
	}
	
	private String[] getTopics(){
		char[] code = {'a', 'x', 'z', 'y', 'v'};
		String[] f = record.getFormattedData("650", code, " -- ");
		String[] topic = new String[f.length];
		String format = "%d. %s";
		for (int i = 0; i < f.length; ++i){
			topic[i] = String.format(format, i+1, f[i]);
		}
		
		return topic;
	}
	private String[] getTracings(){
		char[] code = {'a', 'b'};
		String[] f = record.getFormattedData("700", code, " ");
		String[] tracing = new String[f.length + 1];
		tracing[0] = "Title";
		for (int i = 0; i < f.length; ++i){
			tracing[i+1] = f[i];
		}
		
		String format = "%s. %s";
		for (int i = 0; i < tracing.length; ++i){
			tracing[i] = String.format(format, RomanNumeral.parse(i+1), tracing[i]);
		}
		return tracing;
	}
}
