package gui;

import marc.record.RecordFormatter;

public interface FormatterListener {
	/**
	 * Sets the formatter to use.
	 * @param formatter the formatter
	 */
	abstract void setFormatter(RecordFormatter formatter);
}
