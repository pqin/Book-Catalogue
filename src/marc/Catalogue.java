package marc;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import application.CatalogueView;
import application.MarcComponent;
import application.RecordView;

public class Catalogue implements MarcComponent {
	private File file;
	private GregorianCalendar calendar;
	private ArrayList<Record> data;
	private ArrayList<CatalogueView> catalogueView;
	private ArrayList<RecordView> recordView;
	
	// TODO
	// import java.time.*; JAVA 8
	// adding Record sets Record entry date, keep one Calendar within this class

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
		calendar = (GregorianCalendar) GregorianCalendar.getInstance();
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
	public List<Record> split(int index, int length){
		// TODO
		return data;
	}
	
	public Record generateRecord(){
		Record record = new Record();
		calendar.setTime(new Date());
		int year = calendar.get(GregorianCalendar.YEAR);
		int month = calendar.get(GregorianCalendar.MONTH) + 1;
		int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		record.setEntryDate(year, month, day);
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
