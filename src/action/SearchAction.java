package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import gui.RecordSelector;
import gui.search.SearchManager;

public class SearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private SearchManager manager;
	private RecordSelector selector;

	public SearchAction(SearchManager manager, RecordSelector selector){
		super("Search");
		this.manager = manager;
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		boolean status = manager.searchCatalogue();
		if (status){
			selector.setSearchIndices(manager.getSearchResults());
		}
		 
	}

}
