package marc;

import java.util.ArrayList;
import java.util.List;

public class Library {
	private Catalogue catalogue;
	private List<Record> author;
	private List<Record> subject;
	
	public Library(){
		catalogue = new Catalogue();
		author = new ArrayList<Record>();
		subject = new ArrayList<Record>();
	}
	
	
}
