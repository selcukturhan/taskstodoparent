package taskcontextsearch.search;

import java.util.List;

import com.google.api.services.customsearch.model.Result;

public interface ISearchEngineService {
	public abstract List<Result> doSearch(final String searchKeyWord);
}