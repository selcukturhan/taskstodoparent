package taskcontextsearch.rank;

import com.google.api.services.customsearch.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import taskcontextsearch.FetchIndexWorker;
import taskcontextsearch.IPageHarvester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class PageHarvesterImpl implements IPageHarvester {

    public static final Logger logger = LoggerFactory.getLogger(PageHarvesterImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public List<SEResultWithOriginDocument> execute(final List<Result> searchResults) throws InterruptedException, ExecutionException {
        //#1: We want to fetch the whole pagecontent and not just the snippet.
        //Therefore get pagecontent for retrieved results.
        //Prepare initialsearchresults for reranking
        logger.info("Preparing FetchIndexWorkers...");
        final Collection<Callable<SEResultWithOriginDocument>> fetchIndexWorkers = new ArrayList<>();
        searchResults.forEach(result ->
                fetchIndexWorkers.add(new FetchIndexWorker(result)));

        //Now execute FetchIndexWorker for each initial searchresult, to get the whole pagecontent
        final List<Future<SEResultWithOriginDocument>> result = new ArrayList<>(fetchIndexWorkers.size());
        fetchIndexWorkers.
                forEach(
                        fetchIndexWorker -> result.add(taskExecutor.submit(fetchIndexWorker))
                );
        logger.info("FetchIndexWorkers submitted...");

        for (int i = 0; i < result.size(); i++) {
            logger.info("Check if documents must be deleted...");
            if (isOriginDocumentSuccesfullyObtained(result.get(i))) {
                logger.info("Removing document with index: " + i);
                result.remove(i);
            }

        }

        final List<SEResultWithOriginDocument> originResult = new ArrayList<>(result.size());
        result.forEach(googleResultOriginDocument -> {
            try {
                originResult.add(googleResultOriginDocument.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error: " + e.getLocalizedMessage());
            }
        });
        return originResult;
    }

    private boolean isOriginDocumentSuccesfullyObtained(final Future<SEResultWithOriginDocument> originDocument) {
        try {
            return originDocument == null
                    || originDocument.get() == null
                    || originDocument.get().getOriginDocument() == null
                    || originDocument.get().getOriginDocument().get("content") == null
                    || originDocument.get().getOriginDocument().get("content").trim().isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error: " + e.getLocalizedMessage());
            return false;
        }
    }
}
