package taskcontextsearch.rank;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Task;


@Service(value="LevelWeightingStrategy")
public class LevelWeightingStrategy implements IWeightingStrategy {
	@Override
	public QueryContext computeWeighting(final Task currentTask) {
		if (currentTask.getParentTask() == null) {
			return QueryContext.newInstance("content", getKeywordsAsString(currentTask.getKeywords()), 1f, null);
		}
		final QueryContext tmpQueryContext = computeWeighting(currentTask.getParentTask());
		return QueryContext.newInstance("content", getKeywordsAsString(currentTask.getKeywords()), tmpQueryContext.getBoost() + 1, tmpQueryContext);
	}
	
	protected String getKeywordsAsString(final Set<Keyword> keywords){
		final StringBuilder keywordsAsString = new StringBuilder("");
		if(keywords != null){
			for (Keyword keyword : keywords) {
				keywordsAsString.append(keyword.getValue() + " ");
			}
		}
		return keywordsAsString.toString();
	}
}
