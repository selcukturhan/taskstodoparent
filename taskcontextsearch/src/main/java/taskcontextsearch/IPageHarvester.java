package taskcontextsearch;

import com.google.api.services.customsearch.model.Result;
import org.apache.lucene.document.Document;
import taskcontextsearch.rank.GoogleResultOriginDocument;

import java.util.List;
import java.util.concurrent.Future;

public interface IPageHarvester {
    public List<GoogleResultOriginDocument> execute(final List<Result> searchResults) throws Exception;
}
