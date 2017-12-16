package marc.formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gui.FormatterListener;

public class FormatterModel {
	private Map<String, RecordFormatter> map;
	private List<FormatterListener> listener;
	
	public FormatterModel(){
		map = new HashMap<String, RecordFormatter>();
		listener = new ArrayList<FormatterListener>();
	}
	
	public void addEntry(String key, RecordFormatter value){
		map.put(key, value);
	}
	
	public void addFormatterListener(FormatterListener l){
		listener.add(l);
	}
	public void updateListeners(String key){
		RecordFormatter formatter = map.get(key);
		if (formatter != null){
			Iterator<FormatterListener> it = listener.iterator();
			while (it.hasNext()){
				it.next().setFormatter(formatter);
			}
		}
	}
}
