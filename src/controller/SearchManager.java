package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import application.CatalogueView;
import application.MarcComponent;
import gui.MatchType;
import gui.form.SearchForm;
import marc.Catalogue;
import marc.Record;

public class SearchManager implements MarcComponent, CatalogueView {
	private Component parent;
	private SearchForm searchForm;
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
		searchResults = new ArrayList<Integer>();
	}

	@Override
	public void destroy() {
		searchForm.resetForm();
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
		String[] keyword = searchForm.getKeywords();
		boolean isCaseSensitive = searchForm.isCaseSensitive();
		MatchType type = searchForm.getMatchType();
		String tag = searchForm.getTag();
		String lang = searchForm.getLanguage();
		
		boolean controlSearch = false;
		boolean controlMatch = false;
		boolean wildcardMatch = false;
		boolean dataMatch = false;
		String phrase = null;
		Record result = null;
		
		controlSearch = !lang.isEmpty();
		if (keyword.length > 0){
			// wildcard matching
			for (int k = 0; k < keyword.length; ++k){
				if (keyword[k].equals("*")){
					wildcardMatch = true;
				}
			}
			// match phrase
			if (type == MatchType.MATCH_PHRASE){
				phrase = String.join(" ", keyword);
				keyword = new String[1];
				keyword[0] = phrase;
			}
			
			for (int i = 0; i < data.size(); ++i){
				// get potential result
				result = data.get(i);
				// if control fields specified, filter out non-matching Records
				if (controlSearch){
					controlMatch = true;
					if (!lang.isEmpty()){
						controlMatch &= result.containsLanguage(lang);
					}
				} else {
					controlMatch = true;
				}
				// find keywords
				if (wildcardMatch){
					dataMatch = result.contains("", tag, false);
				} else {
					dataMatch = result.contains(keyword[0], tag, isCaseSensitive);
					for (int k = 1; k < keyword.length; ++k){
						if (type == MatchType.MATCH_ALL){
							dataMatch &= result.contains(keyword[k], tag, isCaseSensitive);
						} else if (type == MatchType.MATCH_ANY){
							dataMatch |= result.contains(keyword[k], tag, isCaseSensitive);
						}
					}
				}
				if (controlMatch && dataMatch){
					searchResults.add(i);
				}
			}
		}
	}
	@Override
	public void updateView(Catalogue catalogue) {
		data = catalogue;
	}
}
