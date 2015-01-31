package taskcontextsearch.rank;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Task;

/**
 * Created by regulator on 9/4/14.
 */
@Service(value="LevelWeightingStrategy")
public class LevelWeightingStrategy implements IWeightingStrategy {
	@Override
	public QueryContext computeWeighting(Task currentTask) {
		if (currentTask.getParentTask() == null) {
			return new QueryContext("content", getKeywordsAsString(currentTask.getKeywords()), 1f, null);
		}
		QueryContext tmpQueryContext = computeWeighting(currentTask.getParentTask());
		return new QueryContext("content", getKeywordsAsString(currentTask.getKeywords()), tmpQueryContext.getBoost() + 1, tmpQueryContext);
	}
	
	protected String getKeywordsAsString(Set<Keyword> keywords){
		StringBuilder keywordsAsString = new StringBuilder(""); 
		if(keywords != null){
			for (Keyword keyword : keywords) {
				keywordsAsString.append(keyword.getValue() + " ");
			}
		}
		return keywordsAsString.toString();
	}
}
