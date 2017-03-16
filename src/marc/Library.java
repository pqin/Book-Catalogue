package marc;

import java.util.ArrayList;
import java.util.List;

import marc.record.Record;

public class Library {
	private Catalogue catalogue;
	private List<Record> author;
	private List<Record> subject;
	private List<Record> holdings;
	
	public Library(){
		catalogue = new Catalogue();
		author = new ArrayList<Record>();
		subject = new ArrayList<Record>();
		holdings = new ArrayList<Record>();
	}
	
	public Catalogue getCatalogue(){
		return catalogue;
	}
	public List<Record> getAuthors(){
		return author;
	}
	public List<Record> getSubjects(){
		return subject;
	}
	public List<Record> getHoldings(){
		return holdings;
	}
	
	
}
