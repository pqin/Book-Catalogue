package gui.search;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import application.CatalogueView;
import application.MarcComponent;
import marc.Catalogue;

public class SearchManager implements MarcComponent, CatalogueView {
	private Component parent;
	private SearchForm searchForm;
	private SearchParser parser;
	private Catalogue data;
	private ArrayList<Integer> searchResults;

	public SearchManager(){
		parent = null;
		data = null;
		create();
	}
	public SearchManager(Component owner){
		parent = owner;
		data = null;
		create();
	}
	
	@Override
	public void create() {
		searchForm = new SearchForm();
		parser = new SearchParser(searchForm);
		searchResults = new ArrayList<Integer>();
	}

	@Override
	public void destroy() {
		searchForm.resetForm();
		parser.reset();
		searchResults.clear();
		data = null;
		parent = null;
	}
	@Override
	public Component getComponent(){
		return null;
	}
	
	public void setParent(Component owner){
		parent = owner;
	}
	
	public void clearResults(){
		searchResults.clear();
	}
	
	public List<Integer> getSearchResults(){
		return searchResults;
	}
	public boolean searchCatalogue(){
		searchForm.resetForm();
		searchResults.clear();
		int option = JOptionPane.showConfirmDialog(parent,
				searchForm, "Record Search",
				JOptionPane.OK_CANCEL_OPTION);
		boolean status = (option == JOptionPane.OK_OPTION);
		if (status){
			processSearchFormInput();
		}
		return status;
	}
	private void processSearchFormInput(){
		parser.reset();
		parser.parseQuery(searchForm);
		for (int i = 0; i < data.size(); ++i){
			if (parser.match(data.get(i))){
				searchResults.add(i);
			}
		}
	}
	
	@Override
	public void updateView(Catalogue catalogue) {
		data = catalogue;
	}
}
