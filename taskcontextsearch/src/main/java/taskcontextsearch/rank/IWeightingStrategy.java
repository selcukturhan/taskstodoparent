package taskcontextsearch.rank;

import org.taskstodo.entity.Task;

public interface IWeightingStrategy {
    QueryContext computeWeighting(final Task currentTask);
}
