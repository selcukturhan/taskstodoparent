package taskcontextsearch;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import taskcontextsearch.search.GoogleSearchService;
import taskcontextsearch.search.ISearchEngineService;

import com.google.api.services.customsearch.model.Result;

public class IndexingExamples{
	

	//get searchresult from google...
	//create temp index...
	//create weigthed term ()
	
	
    public static void main(String args[])throws Exception{
    	
    	ISearchEngineService googleSearchClient = new GoogleSearchService();
    	List<Result> result = googleSearchClient.doSearch("raspberry pi");
    	
    	
    	StandardAnalyzer standardanalyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        IndexWriterConfig indexwriterconfig = new IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer);
        
        BM25Similarity bm25similarity = new BM25Similarity();
        indexwriterconfig.setSimilarity(bm25similarity);
        RAMDirectory ramdirectory = new RAMDirectory();
        
        IndexWriter indexwriter = new IndexWriter(ramdirectory, indexwriterconfig);
        index(indexwriter, result);
        indexwriter.close();
        
        TermQuery termquery1 = new TermQuery(new Term("title", "java"));
        termquery1.setBoost(5f);
        TermQuery termquery2 = new TermQuery(new Term("title", "c"));
        termquery2.setBoost(0.8f);
        
        BooleanQuery query = new BooleanQuery();
        query.add(termquery1, Occur.SHOULD);
        query.add(termquery2, Occur.SHOULD);
        
        IndexSearcher indexsearcher = new IndexSearcher(DirectoryReader.open(ramdirectory));
        indexsearcher.setSimilarity(bm25similarity);
        printResults(indexsearcher, query, 100);
    }

    private static void index(IndexWriter indexwriter, List<Result> results)
        throws IOException{
        for (Result result : results) {
		    Document document = new Document();
            document.add(new TextField("title", result.getTitle(), org.apache.lucene.document.Field.Store.YES));
            indexwriter.addDocument(document);
        }

    }

    private static void printResults(IndexSearcher indexsearcher, Query query, int i)
        throws IOException
    {
        TopDocs topdocs = indexsearcher.search(query, i);
        System.out.println("RESULTS:");
        if(topdocs != null)
        {	
            for(int j = 0; j < topdocs.scoreDocs.length; j++)
            {
                Document document = indexsearcher.doc(topdocs.scoreDocs[j].doc);
                System.out.println((new StringBuilder()).append("Doc: ").append(document.get("title")));
            }

        }
    }
   
}
