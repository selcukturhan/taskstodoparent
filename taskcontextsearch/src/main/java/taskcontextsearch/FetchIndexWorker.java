package taskcontextsearch;

import com.google.api.services.customsearch.model.Result;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import taskcontextsearch.rank.GoogleResultOriginDocument;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public final class FetchIndexWorker implements Callable<GoogleResultOriginDocument>{

	public static final Logger logger = LoggerFactory.getLogger(FetchIndexWorker.class);
	private final Result result;
    private static final String REQUEST_USER_AGENT_KEY = "User-Agent";
	private static final String REQUEST_USER_AGENT = "Mozila/5.0 (Windows) NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	private static final int WRITE_LIMIT = 10000000;


    public FetchIndexWorker(Result result) {
        this.result = result;
    }

    @Override
	public GoogleResultOriginDocument call() throws Exception {
		InputStream inputStream = null;
        GoogleResultOriginDocument googleResultOriginDocument = new GoogleResultOriginDocument();
        final Document document = new Document();
		try {
            HttpURLConnection urlConnection =  (HttpURLConnection) new URL(result.getLink()).openConnection();

            urlConnection.addRequestProperty(REQUEST_USER_AGENT_KEY, REQUEST_USER_AGENT);
            inputStream = urlConnection.getInputStream();
			if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("####CODE####" + urlConnection.getResponseCode() );
                return null;
            }
			final Parser parser = new AutoDetectParser();
			final BodyContentHandler handler = new BodyContentHandler(WRITE_LIMIT);
		    final Metadata metadata = new Metadata();
		    parser.parse(inputStream, handler, metadata, new ParseContext());
		    String plainText = handler.toString();
		    plainText = plainText.replaceAll("\t+"," ").replaceAll("\n+"," ").replaceAll(" +"," ");

		    document.add(new TextField("content", plainText	, org.apache.lucene.document.Field.Store.NO));
			document.add(new StoredField("url" , result.getLink()));
			document.add(new StoredField("snippet", result.getSnippet()));
            googleResultOriginDocument.setOriginDocument(document);
			googleResultOriginDocument.setGoogleResult(result);

			logger.info(result.getLink());
		} catch (Exception e) {
            //Mark broken job inorder to remove it from comparation list
			logger.error("Error during page harvesting: " + e.getLocalizedMessage());
            return null;
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error during ressourcereleasing: " + e.getLocalizedMessage());
				}
			}
		}
		return googleResultOriginDocument;
	}
}
