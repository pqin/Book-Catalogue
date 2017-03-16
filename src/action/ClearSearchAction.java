package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import gui.RecordSelector;
import gui.search.SearchManager;

public class ClearSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private SearchManager manager;
	private RecordSelector selector;

	public ClearSearchAction(SearchManager manager, RecordSelector selector){
		super("Clear");
		this.manager = manager;
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		manager.clearResults();
		selector.clearSearch();
	}

}
