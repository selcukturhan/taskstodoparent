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
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.taskstodo.entity.Task;
import taskcontextsearch.FetchIndexWorker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


@Service(value = "RankingEngine")
public class RankingEngine implements IRankingEngine {

    public static final Logger logger = LoggerFactory.getLogger(RankingEngine.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IWeightingStrategy weightingStrategy;

    @Autowired
    private TFIDFSimilarity tfidfSimilarity;

    @Autowired
    private StandardAnalyzer standardAnalyzer;

    @Value("${sink}")
    private String sink;

    @Value("${chunkSize}")
    private int chunkSize;

    // TODO: simil. options
    @Override
    public List<Document> rank(final Task currentTask, final List<Result> searchResults) {
        Assert.notNull(searchResults);
        Assert.notEmpty(searchResults);
        final Directory ramDirectory = new RAMDirectory();

        try {
            //LOAD
            final Collection<Callable<Document>> preparedDocuments = new ArrayList<>();
            searchResults.forEach(result ->
                    preparedDocuments.add(new FetchIndexWorker(result.getLink(), result.getSnippet())
                    ));


            final List<Future<Document>> result = new ArrayList<>(preparedDocuments.size());
            preparedDocuments.
                    forEach(
                            document -> result.add(taskExecutor.submit(document))
                    );

            final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, standardAnalyzer);
            final IndexWriter indexwriter = new IndexWriter(ramDirectory, indexWriterConfig);
            result.forEach(document -> {
                try {
                    indexwriter.addDocument(document.get());
                } catch (Exception e) {
                    logger.error("Error during indexing: " + e.getLocalizedMessage());
                    throw new RuntimeException();
                }
            });
            indexwriter.close();
            persistIndex(result);

            final IndexReader indexReader = DirectoryReader.open(ramDirectory);
            for (int i = 0; i < indexReader.numDocs(); i++) {
                Document d = indexReader.document(i);
                logger.info("d=" + d);
            }
            indexReader.close();

            final QueryContext queryContext = weightingStrategy.computeWeighting(currentTask);
            final Query reRankQuery = new QueryBuilder().buildQuery(queryContext);

            final IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
            indexSearcher.setSimilarity(tfidfSimilarity);
            final TopDocs topdocs = getResults(indexSearcher, reRankQuery, chunkSize);
            List<Document> documents = Collections.emptyList();
            if (topdocs != null) {
                documents = new ArrayList<>();
                for (int j = 0; j < topdocs.scoreDocs.length; j++) {
                    Document document = indexSearcher.doc(topdocs.scoreDocs[j].doc);
                    documents.add(document);
                }
            }
            return documents;
        } catch (Exception e) {
            logger.error("Error during reranking: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private void persistIndex(final List<Future<Document>> result) {
        new Thread(() -> {
            try {
                final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, standardAnalyzer);
                indexWriterConfig.setSimilarity(tfidfSimilarity);
                final Directory fileDirectory = new SimpleFSDirectory(new File(sink + new SimpleDateFormat("hhmm_ddMMyyyy").format(new Date())));
                final IndexWriter indexwriter = new IndexWriter(fileDirectory, indexWriterConfig);
                result.forEach(document -> {
                    try {
                        indexwriter.addDocument(document.get());
                    } catch (Exception e) {
                        logger.error("Error during indexwriting: " + e.getLocalizedMessage());
                    }
                });
                indexwriter.close();
            } catch (Exception e) {
                logger.error("Error during indexwriting: " + e.getLocalizedMessage());
            }

        }).run();
    }

    private TopDocs getResults(final IndexSearcher indexsearcher, final Query query, final int chunkSize)
            throws IOException {
        return indexsearcher.search(query, chunkSize);
    }
}
