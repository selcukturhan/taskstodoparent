package taskcontextsearch.rank;

import com.google.api.services.customsearch.model.Result;
import org.apache.lucene.document.Document;
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
public class PageHarvesterImpl implements IPageHarvester{

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public List<GoogleResultOriginDocument> execute(final List<Result> searchResults) throws InterruptedException, ExecutionException {
        //#1: We want to fetch the whole pagecontent and not just the snippet.
        //Therefore get pagecontent for retrieved results.
        //Prepare in<<itialsearchresults for reranking
        final Collection<Callable<GoogleResultOriginDocument>> fetchIndexWorkers = new ArrayList<>();
        searchResults.forEach(result ->
                fetchIndexWorkers.add(new FetchIndexWorker(result)));

        //Now execute FetchIndexWorker for each initial searchresult, to get the whole pagecontent
        //TODO: CHECK if order is the same
        final List<Future<GoogleResultOriginDocument>> result = new ArrayList<>(fetchIndexWorkers.size());
        fetchIndexWorkers.
            forEach(
                    document -> result.add(taskExecutor.submit(document))
            );

        for (int i = 0; i < result.size(); i++) {
            if(result.get(i) == null
                    || result.get(i).get() == null
                    ||result.get(i).get().getOriginDocument() == null
                    || result.get(i).get().getOriginDocument().get("content") == null
                    ||result.get(i).get().getOriginDocument().get("content").trim().isEmpty())
                result.remove(i);
        }

        final List<GoogleResultOriginDocument> originResult = new ArrayList<>(result.size());
        result.forEach(googleResultOriginDocument -> {
            try {
                originResult.add(googleResultOriginDocument.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        return originResult;
    }
}
