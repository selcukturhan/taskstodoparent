package taskcontextsearch.rank;

import org.taskstodo.to.ResultTO;

import java.util.List;

/**
 * Created by ninja on 4/13/15.
 */
public interface IRankDeltaCalculator {
    void compute(List<ResultTO> initialSearchResult, List<ResultTO> rerankedSearchResult);
}
