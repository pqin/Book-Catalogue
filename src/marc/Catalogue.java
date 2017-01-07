package marc;

import java.awt.Component;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import application.CatalogueView;
import application.MarcComponent;
import application.RecordView;

public class Catalogue implements MarcComponent {
	private File file;
	private ArrayList<Record> data;
	private ArrayList<CatalogueView> catalogueView;
	private ArrayList<RecordView> recordView;

	public Catalogue(){
		data = new ArrayList<Record>();
		create();
	}
	public Catalogue(List<Record> list){
		data = new ArrayList<Record>(list);
		create();
	}
	public Catalogue(Record[] list){
		data = new ArrayList<Record>(Arrays.asList(list));
		create();
	}
	
	@Override
	public void create() {
		catalogueView = new ArrayList<CatalogueView>();
		recordView = new ArrayList<RecordView>();
		file = new File("");
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
		Record r = null;
		try {
			r = data.get(index);
		} catch (IndexOutOfBoundsException ignored){
			
		}
		return r;
	}
	public List<Record> toList(){
		return data;
	}
	public Record[] toArray(){
		Record[] array = new Record[data.size()];
		array = data.toArray(array);
		return array;
	}
	
	/**
	 * @return the file
	 */
	public final File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public final void setFile(File file) {
		this.file = file;
	}
	public void add(Record record){
		data.add(record);
	}
	public void add(Record[] records){
		ArrayList<Record> list = new ArrayList<Record>(Arrays.asList(records));
		data.addAll(list);
	}
	public void setData(ArrayList<Record> records) {
		data.clear();
		data.addAll(records);
	}
	public void set(int index, Record record){
		data.set(index, record);
	}
	
	public Record remove(int index){
		Record recordRemoved = data.remove(index);
		return recordRemoved;
	}
	public boolean remove(Record record){
		boolean recordRemoved = data.remove(record);
		return recordRemoved;
	}
	public void clear(){
		data.clear();
	}
	public List<Record> extract(int index, int length){
		List<Record> sublist = data.subList(index, index + length);
		return sublist;
	}
	
	public Record generateRecord(){
		Record record = new Record();
		LocalDate currentDate = LocalDate.now(MARC.TIME_ZONE);
		record.setEntryDate(currentDate);
		return record;
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

	public void updateCatalogueView(){
		Iterator<CatalogueView> iterator = catalogueView.iterator();
		while (iterator.hasNext()){
			iterator.next().updateView(this);
		}
	}
	public void updateRecordView(int index){
		Record record = null;
		Iterator<RecordView> iterator = null;
		if (index >= 0 && index < data.size()){
			record = data.get(index);
			iterator = recordView.iterator();
			while (iterator.hasNext()){
				iterator.next().updateView(record);
			}
		}
	}
	
	@Override
	public String toString(){
		return String.format("%s[size=%d]", getClass().getName(), data.size());
	}
}
