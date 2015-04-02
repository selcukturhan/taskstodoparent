package taskcontextsearch;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public final class FetchIndexWorker implements Callable<Document>{

	public static final Logger logger = LoggerFactory.getLogger(FetchIndexWorker.class);
	private final String url;
	private final String snippet;
	private final String requestUserAgentKey = "User-Agent";
	private final String requestUserAgent = "Mozila/5.0 (Windows) NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	private final int WRITE_LIMIT = 10000000;

	public FetchIndexWorker(final String url, final String snippet) {
		this.url = url;
		this.snippet = snippet;
	}
	@Override
	public Document call() throws Exception {
		InputStream inputStream = null;
		final Document document = new Document();
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.addRequestProperty(requestUserAgentKey, requestUserAgent);
			inputStream = urlConnection.getInputStream();
			final Parser parser = new AutoDetectParser();
			final BodyContentHandler handler = new BodyContentHandler(WRITE_LIMIT);
		    final Metadata metadata = new Metadata();
		    parser.parse(inputStream, handler, metadata, new ParseContext());
		    String plainText = handler.toString();
		    plainText = plainText.replaceAll("\t+"," ").replaceAll("\n+"," ").replaceAll(" +"," ");

		    document.add(new TextField("content", plainText	, org.apache.lucene.document.Field.Store.NO));
			document.add(new StoredField("url" , url));
			document.add(new StoredField("snippet", snippet));
			logger.info(url);
		} catch (Exception e) {
			logger.error("Error during page harvesting: " + e.getLocalizedMessage());
			throw new RuntimeException("Error during page harvesting: ", e);
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error during ressourcereleasing: " + e.getLocalizedMessage());
				}
			}
		}
		return document;
	}
}
