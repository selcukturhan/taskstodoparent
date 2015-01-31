package taskcontextsearch.rank;

import java.util.List;

import org.apache.lucene.document.Document;
import org.taskstodo.entity.Task;

import com.google.api.services.customsearch.model.Result;

/**
 * Created by regulator on 9/6/14.
 */
public interface IRankingEngine {
	public List<Document> rank(Task currentTask, List<Result> searchResults);
}
