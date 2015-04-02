package taskcontextsearch;

import org.taskstodo.entity.Task;
import org.taskstodo.to.TaskContextSearchResultTO;

public interface IRankingService {

	TaskContextSearchResultTO getSearchResultForTerm(final String term, final Task currentTask);

}