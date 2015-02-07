package taskcontextsearch.rank;

import com.google.api.services.customsearch.model.Result;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.taskstodo.entity.Task;
import taskcontextsearch.FetchIndexWorker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
//TODO: refactor class in several others
@Service(value = "RankingEngine")
public class RankingEngine implements IRankingEngine {

    public static final Logger logger = LoggerFactory.getLogger(RankingEngine.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IWeightingStrategy weightingStrategy;

    // TODO: simil. options
    @Override
    public List<Document> rank(final Task currentTask, final List<Result> searchResults) {

        //transform searchResult => tf-idf/BM25

//        BM25Similarity bm25similarity = new BM25Similarity();
        final TFIDFSimilarity tfidfSimilarity = new DefaultSimilarity();
        final Directory ramDirectory = new RAMDirectory();

        try {

            //LOAD
            final Collection<Callable<Document>> preparedDocuments = new ArrayList<Callable<Document>>();

            for (Result result : searchResults) {
                final String url = result.getLink();
                final String snippet = result.getSnippet();
                preparedDocuments.add(new FetchIndexWorker(url, snippet));
            }

            final List<Future<Document>> result = new ArrayList<>(preparedDocuments.size());
            preparedDocuments.
                    forEach(
                            document -> result.add(taskExecutor.submit(document))
                    );

            final StandardAnalyzer standardanalyzer = new StandardAnalyzer(Version.LUCENE_4_9);
            final IndexWriterConfig indexwriterconfig = new IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer);
            indexwriterconfig.setSimilarity(tfidfSimilarity);

            final IndexWriter indexwriter = new IndexWriter(ramDirectory, indexwriterconfig);

            for (Future<Document> document : result) {
                indexwriter.addDocument(document.get());
            }
            indexwriter.close();

            //TODO: optimize(fire & forget) + dest
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final IndexWriterConfig indexFilewriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer);
                        indexFilewriterConfig.setSimilarity(tfidfSimilarity);
                        final Directory fileDirectory = new SimpleFSDirectory(new File("/home/ninja/index/" + new SimpleDateFormat("hhmm_ddMMyyyy").format(new Date())));
                        final IndexWriter indexwriter = new IndexWriter(fileDirectory, indexFilewriterConfig);
                        for (Future<Document> document : result) {
                            indexwriter.addDocument(document.get());
                        }
                        indexwriter.close();
                    } catch (Exception e) {
                        logger.error("Error during indexwriting: " + e.getLocalizedMessage());
                    }

                }
            }).run();


            final IndexReader indexReader = DirectoryReader.open(ramDirectory);
            final int num = indexReader.numDocs();
            for (int i = 0; i < num; i++) {
                Document d = indexReader.document(i);
                logger.info("d=" + d);
            }

            indexReader.close();

            final QueryContext queryContext = weightingStrategy.computeWeighting(currentTask);
            final Query reRankQuery = new QueryBuilder().buildQuery(queryContext);

            final IndexSearcher indexsearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
            indexsearcher.setSimilarity(tfidfSimilarity);
            final TopDocs topdocs = getResults(indexsearcher, reRankQuery, 20);
            List<Document> documents = Collections.emptyList();
            if (topdocs != null) {
                documents = new ArrayList<>();
                for (int j = 0; j < topdocs.scoreDocs.length; j++) {
                    Document document = indexsearcher.doc(topdocs.scoreDocs[j].doc);
                    documents.add(document);
                }
            }
            return documents;
        } catch (Exception e) {
            logger.error("Error during reranking: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    private TopDocs getResults(final IndexSearcher indexsearcher, final Query query, final int i)
            throws IOException {
        return indexsearcher.search(query, i);
    }
}
