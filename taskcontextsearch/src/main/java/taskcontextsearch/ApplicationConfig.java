package taskcontextsearch;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ApplicationConfig {
    @Bean
    public DefaultSimilarity tfidfSimilarity(){
        return new DefaultSimilarity();
    }

    @Bean
    public BM25Similarity bm25similarity(){
        return new BM25Similarity();
    }

    @Bean
    public StandardAnalyzer standardanalyzer(){
        return new StandardAnalyzer(Version.LUCENE_4_9);
    }

    @Bean
    public IndexWriterConfig indexwriterconfig(){
        return new IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer());
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setMaxPoolSize(40);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }
}
