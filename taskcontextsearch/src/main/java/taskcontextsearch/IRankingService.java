package taskcontextsearch;

import org.taskstodo.entity.Task;
import org.taskstodo.to.TaskContextSearchResultTO;

public interface IRankingService {

	public abstract TaskContextSearchResultTO getSearchResultForTerm(String term,
			Task currentTask);

}