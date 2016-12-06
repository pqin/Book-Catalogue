package gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;

public class RecordSearchFilter extends RowFilter<NavigationTableModel, Integer> {
	private boolean showSearch;
	private ArrayList<Integer> indices;
	
	public RecordSearchFilter(){
		super();
		showSearch = false;
		indices = new ArrayList<Integer>();
	}
	
	public boolean isShowingSearch(){
		return showSearch;
	}
	public void showSearch(boolean arg0){
		showSearch = arg0;
	}
	public void clearSearch(){
		showSearch = false;
		indices.clear();
	}
	public void setFilteredIndices(List<Integer> arg0){
		indices.clear();
		indices.addAll(arg0);
	}
	
	@Override
	public boolean include(Entry<? extends NavigationTableModel, ? extends Integer> e) {
		boolean match = false;
		final Integer index = e.getIdentifier();
		if (showSearch){
			if (index == null || index < 0){
				match = false;
			} else {
				match = indices.contains(index);
			}
		}
		return match;
	}

}
