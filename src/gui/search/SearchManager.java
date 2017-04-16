package gui.search;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import application.CatalogueView;
import application.MarcComponent;
import gui.FormDialog;
import gui.MarcDialog;
import marc.Catalogue;

public class SearchManager implements MarcComponent, CatalogueView {
	private Component parent;
	private MarcDialog dialog;
	private SearchForm searchForm;
	private SearchParser parser;
	private Catalogue data;
	private ArrayList<Integer> searchResults;

	public SearchManager(Component owner){
		parent = owner;
		dialog = new FormDialog(parent);
		data = null;
		create();
	}
	
	@Override
	public void create() {
		searchForm = new SearchForm();
		parser = new SearchParser(searchForm);
		searchResults = new ArrayList<Integer>();
		
		dialog.setTitle("Search Catalogue");
		dialog.setContent(searchForm);
		String[] options = { "Search", "Cancel" };
		dialog.setOptions(options);
		dialog.create();
	}

	@Override
	public void destroy() {
		dialog.destroy();
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
	
	public void clearResults(){
		searchResults.clear();
	}
	
	public List<Integer> getSearchResults(){
		return searchResults;
	}
	public boolean searchCatalogue(){
		searchForm.resetForm();
		searchResults.clear();
		int option = dialog.showDialog();
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
	public void updateView(Catalogue catalogue, int index) {
		data = catalogue;
	}
}
