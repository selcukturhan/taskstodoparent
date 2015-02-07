package taskcontextsearch.rank;

import org.taskstodo.entity.Task;

public interface IWeightingStrategy {
    public QueryContext computeWeighting(final Task currentTask);
}
