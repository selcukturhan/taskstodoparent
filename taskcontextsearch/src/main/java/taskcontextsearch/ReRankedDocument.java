package taskcontextsearch;

import org.apache.lucene.document.Document;
import org.taskstodo.to.ResultTO;

public class ReRankedDocument {
	
	protected Document document;
	protected int rank;
	protected int reRankedRank;
	private String url;
	private String snippet;
	
	public ReRankedDocument(Document document, int rank, int reRankedRank) {
		super();
		this.document = document;
		this.rank = rank;
		this.reRankedRank = reRankedRank;
	}
	
	public ReRankedDocument() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultTO getResult(){
		return new ResultTO(document.get("snippet"),document.get("url"));
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getReRankedRank() {
		return reRankedRank;
	}

	public void setReRankedRank(int reRankedRank) {
		this.reRankedRank = reRankedRank;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
}
