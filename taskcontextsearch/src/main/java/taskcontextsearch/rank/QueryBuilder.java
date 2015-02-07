package taskcontextsearch.rank;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueryBuilder {
    public static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

    public Query buildQuery(QueryContext queryContext) throws Exception{

        if(queryContext == null) {
            logger.error("Precondition error, please provide queryContext");
            throw new IllegalArgumentException("Precondition error, please provide queryContext");
        }

        final BooleanQuery rootQuery = new BooleanQuery();
        final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        QueryContext currentQueryContext = queryContext;
        while(currentQueryContext != null) {
        	Query query = new QueryParser(Version.LUCENE_4_9,
                    currentQueryContext.getField(),
                    analyzer).parse(currentQueryContext.getText());
            query.setBoost(currentQueryContext.getBoost());
            rootQuery.add(query, BooleanClause.Occur.SHOULD);
            currentQueryContext = currentQueryContext.getParent();
        }
        return rootQuery;
    }
}
