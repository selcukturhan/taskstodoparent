package taskcontextsearch.rank;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;



/**
 * Created by regulator on 9/3/14.
 */
public class QueryBuilder {

    public Query buildQuery(QueryContext queryContext) throws Exception{

        if(queryContext == null)
            throw new IllegalArgumentException("Precondition error");

        BooleanQuery rootQuery = new BooleanQuery();
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        QueryContext currentQueryContext = queryContext;
        while(currentQueryContext != null) {
        	Query query = new QueryParser(Version.LUCENE_4_9,
                    currentQueryContext.getField(),
                    analyzer).parse(currentQueryContext.getText());
//        	Query query = new QueryParser(Version.LUCENE_4_9, currentQueryContext.getField(), analyzer).parse(currentQueryContext.getText());
//            Query query = new TermQuery(new Term(currentQueryContext.getField(), currentQueryContext.getText()));
            query.setBoost(currentQueryContext.getBoost());
            rootQuery.add(query, BooleanClause.Occur.SHOULD);
            currentQueryContext = currentQueryContext.getParent();
        }
        return rootQuery;
    }
    
    
    

}
