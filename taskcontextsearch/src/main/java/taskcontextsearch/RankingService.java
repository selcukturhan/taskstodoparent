package taskcontextsearch;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.entity.Task;
import org.taskstodo.to.ResultTO;
import org.taskstodo.to.TaskContextSearchResultTO;
import taskcontextsearch.rank.SEResultWithOriginDocument;
import taskcontextsearch.rank.IRankingEngine;
import taskcontextsearch.search.ISearchEngineService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "rankingService")
public class RankingService implements IRankingService {

    @Autowired
    private ISearchEngineService searchEngineService;
    @Autowired
    private IRankingEngine rankingEngine;
    @Autowired
    private IPageHarvester pageHarvester;


    public static final Logger logger = LoggerFactory.getLogger(RankingService.class);

    @Override
    public TaskContextSearchResultTO getSearchResultForTerm(final String term, final Task currentTask) {

        final TaskContextSearchResultTO contextSearchResult = new TaskContextSearchResultTO();
        List<SEResultWithOriginDocument> results = Collections.emptyList();
        try {
            results = pageHarvester.execute(searchEngineService.doSearch(term));
        } catch (Exception e) {
            logger.error("Error during pageharvest", e.getLocalizedMessage());
        }

        results.forEach(result ->
                        contextSearchResult.addPrimaryResult(new ResultTO(result.getSeResult().getSnippet(),
                                result.getSeResult().getLink()))
        );

        final List<Document> originPages = results.stream()
                .map(SEResultWithOriginDocument::getOriginDocument)
                .collect(Collectors.toList());

        rankingEngine.rank(currentTask, originPages).
                forEach(document ->
                        contextSearchResult.addRankedResult(new ResultTO(document.get("snippet"), document.get("url"))));

        return contextSearchResult;
    }
}
