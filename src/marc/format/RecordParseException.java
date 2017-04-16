package marc.format;

public final class RecordParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public RecordParseException(String message){
		super(message);
	}
	public RecordParseException(String message, Throwable cause){
		super(message, cause);
	}
}
