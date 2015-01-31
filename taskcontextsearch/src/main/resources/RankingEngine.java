package taskcontextsearch.rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.entity.Task;

import taskcontextsearch.FetchIndexWorker;

import com.google.api.services.customsearch.model.Result;

/**
 * Created by regulator on 9/3/14.
 */

@Service(value = "RankingEngine")
@Transactional(propagation=Propagation.MANDATORY)
public class RankingEngine implements IRankingEngine {

	@Autowired
	private IWeightingStrategy weightingStrategy;

    // TODO: simil. options
	@Override
    public List<Document> rank(final Task currentTask, final List<Result> searchResults){

        //transform searchResult => tf-idf/BM25

//        BM25Similarity bm25similarity = new BM25Similarity();
		TFIDFSimilarity tfidfSimilarity = new DefaultSimilarity();
        RAMDirectory ramdirectory = new RAMDirectory();

        try {
        	
        	//LOAD
            ExecutorService executor = Executors.newFixedThreadPool(20);
            Collection<Callable<Document>> list = new ArrayList<Callable<Document>>();

            for (Result result : searchResults) {
                final String url = result.getLink();
                final String snippet = result.getSnippet(); 
                list.add(new FetchIndexWorker(url, snippet));
            }


            List<Future<Document>> result = executor.invokeAll(list);
            StandardAnalyzer standardanalyzer = new StandardAnalyzer(Version.LUCENE_4_9);
            IndexWriterConfig indexwriterconfig = new IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer);
        	indexwriterconfig.setSimilarity(tfidfSimilarity);

            IndexWriter indexwriter = new IndexWriter(ramdirectory, indexwriterconfig);

            for (Future<Document> document : result) {
                indexwriter.addDocument(document.get());
            }
            indexwriter.close();

            IndexReader indexReader =  DirectoryReader.open(ramdirectory);
            int num = indexReader.numDocs();
            for ( int i = 0; i < num; i++){
                Document d = indexReader.document( i);
                System.out.println( "d=" +d);
            }
            
            indexReader.close();

            QueryContext queryContext = weightingStrategy.computeWeighting(currentTask);
            Query reRankQuery = new QueryBuilder().buildQuery(queryContext);

            IndexSearcher indexsearcher = new IndexSearcher(DirectoryReader.open(ramdirectory));
            indexsearcher.setSimilarity(tfidfSimilarity);
            TopDocs topdocs = getResults(indexsearcher, reRankQuery, 20);
            List<Document> documents = Collections.emptyList();
            if(topdocs != null){	
            	documents = new ArrayList<Document>();
                for(int j = 0; j < topdocs.scoreDocs.length; j++){
                    Document document = indexsearcher.doc(topdocs.scoreDocs[j].doc);
                    documents.add(document);
                }
            }
            return documents;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


	private TopDocs getResults(IndexSearcher indexsearcher, Query query, int i)
			throws IOException {
		return indexsearcher.search(query, i);
	}
}
