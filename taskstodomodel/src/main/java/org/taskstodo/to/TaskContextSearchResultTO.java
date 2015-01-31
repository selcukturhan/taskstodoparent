package org.taskstodo.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskContextSearchResultTO implements Serializable{
    
    private List<ResultTO> primaryResult = new ArrayList<ResultTO>();
    private List<ResultTO> rankedResult = new ArrayList<ResultTO>();
	
    public List<ResultTO> getPrimaryResult() {
		return primaryResult;
	}
	public void setPrimaryResult(List<ResultTO> primaryResult) {
		this.primaryResult = primaryResult;
	}
	public List<ResultTO> getRankedResult() {
		return rankedResult;
	}
	public void setRankedResult(List<ResultTO> rankedResult) {
		this.rankedResult = rankedResult;
	}
	   
}
