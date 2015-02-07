package taskcontextsearch.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
@Primary
@Service(value = "GoogleSearchService")
public class GoogleSearchService implements ISearchEngineService {

    public static final Logger logger = LoggerFactory.getLogger(GoogleSearchService.class);
    //api key
    final private String API_KEY = "AIzaSyDIE9K-9OVpQUPkLRAB1YjJ0neHfNSG9tM";
    //custom search engine ID
    final private String SEARCH_ENGINE_ID = "003281050901061570646:jfufua7gj3a";

    /* (non-Javadoc)
	 * @see org.taskstodo.dao.SearchService#doSearch(java.lang.String)
	 */
    @Override
	public List<Result> doSearch(final String searchTerm) {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Customsearch customsearch = new Customsearch(httpTransport, jsonFactory, null);
        List<Result> resultList = new ArrayList<Result>();
        Customsearch.Cse.List list;
        try {
            list = customsearch.cse().list(searchTerm);
            list.setKey(API_KEY);
            list.setCx(SEARCH_ENGINE_ID);

            for (long i = 0; i < 2; i++) {
                list.setNum(10L);
                list.setStart(1L + 10L * i );
                Search results = list.execute();
                resultList.addAll(results.getItems());
            }
            
            for (Result result : resultList) {
                logger.info(result.getSnippet());
			}
        }catch (Exception e){
           logger.error("Error during googlesearch: " + e.getLocalizedMessage());
        }
        return resultList;
    }
 }