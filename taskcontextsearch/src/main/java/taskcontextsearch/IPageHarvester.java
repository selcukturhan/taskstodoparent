package taskcontextsearch;

import com.google.api.services.customsearch.model.Result;
import taskcontextsearch.rank.SEResultWithOriginDocument;

import java.util.List;

public interface IPageHarvester {
    public List<SEResultWithOriginDocument> execute(final List<Result> searchResults) throws Exception;
}
