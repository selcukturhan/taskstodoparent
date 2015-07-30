package taskcontextsearch.rank;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.taskstodo.entity.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service(value = "rankingEngine")
public class RankingEngine implements IRankingEngine {

    public static final Logger logger = LoggerFactory.getLogger(RankingEngine.class);

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

    @Override
    public List<Document> rank(final Task currentTask, final List<Document> originPageContent) {
        Assert.notNull(originPageContent);
        Assert.notEmpty(originPageContent);
        final Directory ramDirectory = new RAMDirectory();
        try {

            //#1: index the retrieved set of pagecontent
            final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, standardAnalyzer);
            final IndexWriter indexwriter = new IndexWriter(ramDirectory, indexWriterConfig);
            originPageContent.forEach(document -> {
                try {
                    indexwriter.addDocument(document);
                } catch (Exception e) {
                    logger.error("Error during indexing: " + e.getLocalizedMessage());
                    throw new RuntimeException();
                }
            });
            indexwriter.close();
            persistIndex(originPageContent);

            //#2: start reranking based on taskcontextinformation
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

    private void persistIndex(final List<Document> result) {
        new Thread(() -> {
            try {
                final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, standardAnalyzer);
                indexWriterConfig.setSimilarity(tfidfSimilarity);
                final Directory fileDirectory = new SimpleFSDirectory(new File(sink + new SimpleDateFormat("hhmm_ddMMyyyy").format(new Date())));
                final IndexWriter indexwriter = new IndexWriter(fileDirectory, indexWriterConfig);
                result.forEach(document -> {
                    try {
                        indexwriter.addDocument(document);
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
