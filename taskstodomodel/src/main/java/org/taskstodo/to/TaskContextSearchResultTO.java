package org.taskstodo.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskContextSearchResultTO implements Serializable{
    
    private List<ResultTO> primaryResult = new ArrayList<ResultTO>();
    private List<ResultTO> rankedResult = new ArrayList<ResultTO>();
	
    public List<ResultTO> getPrimaryResult() {
		return Collections.unmodifiableList(primaryResult);
	}

	public List<ResultTO> getRankedResult() {
		return Collections.unmodifiableList(rankedResult);
	}

	public void addPrimaryResult(final ResultTO result){
		primaryResult.add(result);
	}

	public void addRankedResult(final ResultTO result){
		rankedResult.add(result);
	}

}
