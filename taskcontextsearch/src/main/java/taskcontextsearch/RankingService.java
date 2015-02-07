package taskcontextsearch;

import java.util.List;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.entity.Task;
import org.taskstodo.to.ResultTO;
import org.taskstodo.to.TaskContextSearchResultTO;

import taskcontextsearch.rank.IRankingEngine;
import taskcontextsearch.search.ISearchEngineService;

import com.google.api.services.customsearch.model.Result;

@Service(value = "RankingService")
public class RankingService implements IRankingService {

	@Autowired
	private ISearchEngineService searchEngineService;
	@Autowired
	private IRankingEngine rankingEngine;

	
	
	public static final Logger logger = LoggerFactory
			.getLogger(RankingService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * taskcontextsearch.IRankingService#getSearchResultForTerm(java.lang.String
	 * , org.taskstodo.entity.Task)
	 */
	@Override
	public TaskContextSearchResultTO getSearchResultForTerm(final String term, final Task currentTask) {

		final TaskContextSearchResultTO contextSearchResult = new TaskContextSearchResultTO();
		final List<Result> results = searchEngineService.doSearch(term);

		if(results != null && !results.isEmpty()){

			for (Result result : results) {
				contextSearchResult.getPrimaryResult().add(new ResultTO(result.getSnippet(), result.getLink()));
			}
			final List<Document> documents = rankingEngine.rank(currentTask, results);
			for (Document document : documents) {
				contextSearchResult.getRankedResult().add(new ResultTO(document.get("snippet"),document.get("url")));
			}
		}
		return contextSearchResult;
	}
}
