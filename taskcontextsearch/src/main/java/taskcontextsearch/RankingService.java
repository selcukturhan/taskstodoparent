package taskcontextsearch;

import com.google.api.services.customsearch.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.entity.Task;
import org.taskstodo.to.ResultTO;
import org.taskstodo.to.TaskContextSearchResultTO;
import taskcontextsearch.rank.IRankingEngine;
import taskcontextsearch.search.ISearchEngineService;

import java.util.List;
import java.util.Optional;

@Service(value = "RankingService")
public class RankingService implements IRankingService {

    @Autowired
    private ISearchEngineService searchEngineService;
    @Autowired
    private IRankingEngine rankingEngine;

    public static final Logger logger = LoggerFactory.getLogger(RankingService.class);

    @Override
    public TaskContextSearchResultTO getSearchResultForTerm(final String term, final Task currentTask) {

        final TaskContextSearchResultTO contextSearchResult = new TaskContextSearchResultTO();
        final List<Result> results = searchEngineService.doSearch(term);

        results.forEach(result ->
                        contextSearchResult.getPrimaryResult().add(new ResultTO(result.getSnippet(), result.getLink()))
        );

        Optional.of(results).ifPresent(
                resultsToRerank -> rankingEngine.rank(currentTask, resultsToRerank).forEach(document ->
                        contextSearchResult.getRankedResult().add(new ResultTO(document.get("snippet"), document.get("url"))))
        );

        return contextSearchResult;
    }
}
