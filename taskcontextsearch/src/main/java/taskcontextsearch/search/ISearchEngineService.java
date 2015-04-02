package taskcontextsearch.search;

import java.util.List;

import com.google.api.services.customsearch.model.Result;


public interface ISearchEngineService {
	/**
	 * Returns results for keywords or list of size 0
	 * */
	List<Result> doSearch(final String searchKeyWord);
}