package application;

import marc.record.Record;

public interface RecordView {
	/**
	 * Updates view of record at index.
	 * @param record
	 * @param index
	 */
	abstract void updateView(Record record, int index);
}
