package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.SearchManager;
import gui.RecordSelector;
import gui.table.RecordSearchFilter;

public class ClearSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private SearchManager manager;
	private RecordSearchFilter filter;
	private RecordSelector selector;

	public ClearSearchAction(SearchManager manager, RecordSelector selector){
		super("Clear");
		this.manager = manager;
		this.selector = selector;
		this.filter = selector.getSearchFilter();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.clearResults();
		filter.clearSearch();
		selector.updateView();
		selector.fireDataUpdated();
	}

}
