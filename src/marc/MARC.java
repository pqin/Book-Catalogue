package marc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;


public class MARC {
	public static final char BLANK_CHAR = 0x20;
	public static final char FILL_CHAR = 0x7C;
	
	public static final int TAG_LENGTH = 3;
	public static final String UNKNOWN_TAG = "???";
	
	public static final ZoneId TIME_ZONE = ZoneId.systemDefault();
	public static final LocalDate EPOCH_START = LocalDate.of(1968, 1, 1);
	public static final Locale COUNTRY_LOCALE = Locale.US;
	public static final Locale LANGUAGE_LOCALE = Locale.ENGLISH;
}
