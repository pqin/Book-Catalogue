package marc;

public class RecordBuilder {
	private Record record;
	
	public RecordBuilder(){
		record = new Record();
	}
	
	public Record build(){
		return record;
	}
	
	
}
