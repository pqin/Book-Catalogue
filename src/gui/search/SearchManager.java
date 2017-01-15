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
	/*
	private void processSearchFormInput(){
		String tag = searchForm.getTag(0);
		String[] keyword = searchForm.getKeywords(0);
		MatchType matchType = searchForm.getMatchType(0);
		boolean isCaseSensitive = searchForm.isCaseSensitive();
		int fixedIndex = searchForm.getFixedIndex();
		String fixedValue = searchForm.getFixedValue();
		char[] fixedData = null;
		String language = searchForm.getLanguage();
		String place = searchForm.getPlace();
		
		boolean controlSearch = false;
		boolean controlMatch = false;
		boolean wildcardMatch = false;
		boolean dataMatch = false;
		Record result = null;
		
		controlSearch = !( language.isEmpty() && place.isEmpty() && fixedValue.isEmpty());
		if (keyword.length > 0){
			// wildcard matching
			for (int k = 0; k < keyword.length; ++k){
				if (keyword[k].equals(WILDCARD)){
					wildcardMatch = true;
				}
			}
			
			for (int i = 0; i < data.size(); ++i){
				// get potential result
				result = data.get(i);
				// if control fields specified, filter out non-matching Records
				controlMatch = true;
				if (controlSearch){
					if (!fixedValue.isEmpty()){
						fixedData = result.getResource().getData(fixedIndex, fixedValue.length());
						controlMatch &= String.valueOf(fixedData).equals(fixedValue);
					}
					if (!language.isEmpty()){
						controlMatch &= result.containsLanguage(language);
					}
					if (!place.isEmpty()){
						controlMatch &= result.containsPlace(place);
					}
				}
				// find keywords
				if (wildcardMatch){
					dataMatch = result.contains("", tag, false);
				} else {
					dataMatch = result.contains(keyword[0], tag, isCaseSensitive);
					for (int k = 1; k < keyword.length; ++k){
						if (matchType == MatchType.AND){
							dataMatch &= result.contains(keyword[k], tag, isCaseSensitive);
						} else if (matchType == MatchType.OR){
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
	*/
	
	@Override
	public void updateView(Catalogue catalogue) {
		data = catalogue;
	}
}
