package taskcontextsearch.rank;

import org.taskstodo.entity.Task;

/**
 * Created by regulator on 9/4/14.
 */
public interface IWeightingStrategy {
    public QueryContext computeWeighting(final Task currentTask);
}
