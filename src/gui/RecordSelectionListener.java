package gui;

public interface RecordSelectionListener {
	abstract void dataUpdated(RecordSelector source);
	abstract void selectionUpdated(RecordSelector source, int index, int row);
}
