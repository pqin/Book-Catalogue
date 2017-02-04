package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import gui.RecordSelector;
import gui.search.SearchManager;
import gui.table.RecordSearchFilter;

public class SearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private SearchManager manager;
	private RecordSearchFilter filter;
	private RecordSelector selector;

	public SearchAction(SearchManager manager, RecordSelector selector){
		super("Find");
		this.manager = manager;
		this.selector = selector;
		this.filter = selector.getSearchFilter();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean status = manager.searchCatalogue();
		if (status){
			filter.showSearch(true);
			filter.setFilteredIndices(manager.getSearchResults());
			selector.updateView();
			selector.fireDataUpdated();
		}
		 
	}

}
