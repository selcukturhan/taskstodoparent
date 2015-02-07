package taskcontextsearch.rank;

import java.util.List;

import org.apache.lucene.document.Document;
import org.taskstodo.entity.Task;

import com.google.api.services.customsearch.model.Result;

public interface IRankingEngine {
	public List<Document> rank(final Task currentTask, final List<Result> searchResults);
}
