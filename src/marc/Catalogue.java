package marc;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import application.CatalogueView;
import application.MarcComponent;
import application.RecordView;
import marc.record.Record;

public final class Catalogue implements MarcComponent {
	private ArrayList<Record> data;
	private ArrayList<CatalogueView> catalogueView;
	private ArrayList<RecordView> recordView;

	public Catalogue(){
		data = new ArrayList<Record>();
		create();
	}
	
	@Override
	public void create() {
		catalogueView = new ArrayList<CatalogueView>();
		recordView = new ArrayList<RecordView>();
	}
	@Override
	public void destroy() {
		data.clear();
		catalogueView.clear();
		recordView.clear();
	}
	@Override
	public Component getComponent(){
		return null;
	}

	public boolean contains(Record record){
		boolean hasRecord = data.contains(record);
		return hasRecord;
	}
	
	public int size(){
		return data.size();
	}
	
	public Record get(int index) {
		Record record = null;
		try {
			record = data.get(index);
		} catch (IndexOutOfBoundsException ignored){
			
		}
		return record;
	}
	public List<Record> toList(){
		return data;
	}
	public Record[] toArray(){
		Record[] array = new Record[data.size()];
		array = data.toArray(array);
		return array;
	}
	
	public void add(Record record){
		int index = data.size();
		data.add(record);
		updateCatalogueView(index);
	}
	public void add(Record[] records){
		addData(Arrays.asList(records));
	}
	public void loadData(List<Record> list) {
		data.clear();
		addData(list);
	}
	private void addData(List<Record> list){
		int size = data.size();
		int index = list.size() > 0 ? size : size - 1;
		data.addAll(list);
		updateCatalogueView(index);
	}
	public void set(int index, Record record){
		data.set(index, record);
	}
	
	public void remove(int index){
		data.remove(index);
		if (index >= data.size()){
			index = data.size() - 1;
		}
		updateCatalogueView(index);
	}
	
	public void clear(){
		data.clear();
	}
	public List<Record> extract(int index, int length){
		List<Record> sublist = data.subList(index, index + length);
		return sublist;
	}
	
	public void addCatalogueView(CatalogueView view){
		catalogueView.add(view);
	}
	public void removeCatalogueView(CatalogueView view){
		catalogueView.remove(view);
	}
	public void addRecordView(RecordView view){
		recordView.add(view);
	}
	public void removeRecordView(RecordView view){
		recordView.remove(view);
	}

	public void updateCatalogueView(int index){
		Iterator<CatalogueView> iterator = catalogueView.iterator();
		while (iterator.hasNext()){
			iterator.next().updateView(this, index);
		}
	}
	public void updateRecordView(int index){
		Record record = null;
		if (index >= 0 && index < data.size()){
			record = data.get(index);
		} else {
			record = null;
			index = -1;
		}
		Iterator<RecordView> iterator = recordView.iterator();
		while (iterator.hasNext()){
			iterator.next().updateView(record, index);
		}
	}
	
	@Override
	public String toString(){
		return String.format("%s[size=%d]", getClass().getName(), data.size());
	}
}
