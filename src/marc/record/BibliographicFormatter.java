package marc.record;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.form.RomanNumeral;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Subfield;

public class BibliographicFormatter extends RecordFormatter {
	private static final Pattern FICTION_REGEX = Pattern.compile("^\\[([a-zA-Z]+)\\]$");
	private static final Pattern SEGMENTATION_REGEX = Pattern.compile("[/\u2032;]");
	
	private String ddc, lccn, isbn;
	private String description, edition, series;
	private String summary, summaryLabel;
	private String notes;
	private String[] imprint, topic, tracing;
	
	public BibliographicFormatter(){
		super();
	}
	
	@Override
	public void parse(Record record){
		// get heading
		String[] headingTag = {"100", "110", "111", "130"};
		int t = 0;
		String m = null;
		while (m == null && t < headingTag.length){
			m = record.getData(headingTag[t], 'a');
			++t;
		}
		heading = (m == null) ? "" : m;
		
		// get title
		char[] titlecode = {'a', 'h', 'b', 'c'};
		DataField titleField = (DataField) record.getFirstMatchingField("245");
		int nonfiling = 0;
		final int radix = 10;
		if (titleField == null){
			title = "";
		} else {
			title = format(titleField, titlecode, " ");
			nonfiling = Character.digit(titleField.getIndicator2(), radix);
			if (nonfiling < 0){
				nonfiling = 0;
			}
		}
		filingTitle = title.substring(nonfiling);
		
		ddc = record.getData("082", 'a');	// get Dewey Decimal classification number
		callNumber = parseCallNumber(heading, ddc);
		lccn = record.getData("010", 'a');	// get Library of Congress Catalog number
		char[] isbnCode = {'a', 'q'};
		isbn = formatFirst(record, "020", isbnCode, " ");
		
		char[] descriptionCode = {'a', 'b', 'c', 'e'};
		description = formatFirst(record, "300", descriptionCode, " ");		
		
		edition = record.getData("250", 'a');
		
		char[] seriesCode = {'a', 'v'};
		series = formatFirst(record, "190", seriesCode, " ");
		if (series != null && series.length() > 0){
			series = "(" + series + ")";
		}
		
		String[] s = parseSummary(record);
		summaryLabel = s[0];
		summary = s[1];
		
		notes = record.getData("500", 'a');
		
		char[] imprintCode = {'a', 'b', 'c'};
		imprint = formatAll(record, "260", imprintCode, " ");
		
		char[] topicCode = {'a', 'x', 'z', 'y', 'v'};
		String[] tmpTopic = formatAll(record, "650", topicCode, " -- ");
		topic = new String[tmpTopic.length];
		for (int i = 0; i < tmpTopic.length; ++i){
			topic[i] = String.format("%d. %s", i+1, tmpTopic[i]);
		}
		
		char[] tracingCode = {'a', 'b'};
		String[] tmpTrace = formatAll(record, "700", tracingCode, " ");
		tracing = new String[tmpTrace.length + 1];
		tracing[0] = "Title";
		for (int i = 0; i < tmpTrace.length; ++i){
			tracing[i+1] = tmpTrace[i];
		}
		
		for (int i = 0; i < tracing.length; ++i){
			tracing[i] = String.format("%s. %s", RomanNumeral.parse(i+1), tracing[i]);
		}
	}
	private String parseCallNumber(String mainEntry, String dewey){
		final int width = 8;
		String d = getDeweyNumber(dewey, width);
		String m = getAuthorIndicator(3, mainEntry);
		
		String[] tmp = new String[2];
		String format = String.format("%%%ds", -1*width);
		tmp[0] = String.format(format, d);
		tmp[1] = String.format(format, m);
		
		return String.format("%s%n%s", tmp[0], tmp[1]);
	}
	private String getDeweyNumber(String dewey, final int length){
		String d = "???";
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
	private String[] parseSummary(Record record){
		String[] s = new String[2];
		DataField summaryField = (DataField) record.getFirstMatchingField("520");
		char indicator = Field.BLANK_INDICATOR;
		if (summaryField == null){
			s[0] = "Summary";
			s[1] = null;
		} else {
			indicator = summaryField.getIndicator1();
			switch (indicator){
			case Field.BLANK_INDICATOR:
				s[0] = "Summary";
				break;
			case '0':
				s[0] = "Subject";
				break;
			case '1':
				s[0] = "Review";
				break;
			case '2':
				s[0] = "Scope and content";
				break;
			case '3':
				s[0] = "Abstract";
				break;
			case '4':
				s[0] = "Content advice";
				break;
			case '8':
				s[0] = "";	// No display constant generated.
				break;
			default:
				s[0] = "Summary";
				break;
			}
			char summaryCode = 'a';
			Subfield sub = null;
			for (int i = 0; i < summaryField.getDataCount(); ++i){
				sub = summaryField.getSubfield(i);
				if (sub.getCode() == summaryCode){
					s[1] = sub.getData();
					break;
				}
			}
		}
		return s;
	}
	
	@Override
	public String getContent(){
		StringBuilder b = new StringBuilder();
		appendLn(b, heading);
		append(b, title);
		if (edition != null && !edition.isEmpty()){
			append(b, ". -- ");
			append(b, edition);
		}
		b.append('\n');
		appendLn(b, imprint);
		appendLn(b, description);
		appendLn(b, series);
		b.append('\n');
		
		appendLn(b, summaryLabel, summary);
		appendLn(b, notes);
		appendLn(b, "ISBN", isbn);
		b.append('\n');
		
		appendLn(b, topic);
		appendLn(b, tracing);
		
		if (lccn != null && ddc != null){
			append(b, lccn);
			append(b, "    ");
			append(b, ddc);
		} else if (lccn != null){
			appendLn(b, lccn);
		} else if (ddc != null){
			appendLn(b, ddc);
		}
		
		return b.toString();
	}
}
