package taskcontextsearch.rank;

import org.springframework.stereotype.Component;
import org.taskstodo.to.ResultTO;

import java.util.List;

@Component
public class RankDeltaCalculatorImpl implements IRankDeltaCalculator {
    @Override
    public void compute(final List<ResultTO> initialSearchResult, final List<ResultTO> rerankedSearchResult){
        for (int i = 0; i < initialSearchResult.size(); i++){
            for (int j = 0; j < rerankedSearchResult.size(); j++) {
                if(initialSearchResult.get(i).equals(rerankedSearchResult.get(j))){
                    rerankedSearchResult.get(j).setInitialPosition(i + 1);
                    rerankedSearchResult.get(j).setRerankedPosition(j + 1);
                    rerankedSearchResult.get(j).setReranked(i != j);
                    break;
                }
            }
        }
    }
}
