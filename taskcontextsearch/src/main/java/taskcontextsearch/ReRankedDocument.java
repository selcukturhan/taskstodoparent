package taskcontextsearch;

import org.apache.lucene.document.Document;
import org.taskstodo.to.ResultTO;

public final class ReRankedDocument {
	
	private Document document;
	private int rank;
	private int reRankedRank;
	private String url;
	private String snippet;

	public ReRankedDocument newInstance(final Document document,final int rank,final int reRankedRank){
		return new ReRankedDocument(document, rank, reRankedRank);
	}

	private ReRankedDocument(final Document document, final int rank, final int reRankedRank) {
		this.document = document;
		this.rank = rank;
		this.reRankedRank = reRankedRank;
	}

	public ResultTO getResult(){
		return new ResultTO(document.get("snippet"),document.get("url"));
	}

	public Document getDocument() {
		return document;
	}
	public int getRank() {
		return rank;
	}
	public int getReRankedRank() {
		return reRankedRank;
	}

}
