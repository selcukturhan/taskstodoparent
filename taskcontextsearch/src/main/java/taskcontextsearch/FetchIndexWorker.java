package taskcontextsearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

public class FetchIndexWorker implements Callable<Document>{

	private String url;
	private String snippet;
	
	public FetchIndexWorker(final String url, final String snippet) {
		this.url = url;
		this.snippet = snippet;
	}
	@Override
	public Document call() throws Exception {
		InputStream inputStream = null;
		Document document = new Document();
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.
				addRequestProperty("User-Agent", "Mozila/5.0 (Windows) NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			inputStream = urlConnection.getInputStream();
			Parser parser = new AutoDetectParser();
			BodyContentHandler handler = new BodyContentHandler(10000000);
		    Metadata metadata = new Metadata();
		    parser.parse(inputStream, handler, metadata, new ParseContext());
		    String plainText = handler.toString();
		    plainText = plainText.replaceAll("\t+"," ").replaceAll("\n+"," ").replaceAll(" +"," ");
//		    System.out.println(new LanguageIdentifier(plainText).getLanguage() + " " + url);
		    document.add(new TextField("content", plainText	, org.apache.lucene.document.Field.Store.NO));
			document.add(new StoredField("url" , url));
			document.add(new StoredField("snippet", snippet));
			System.out.println(url);
			 
		} catch (MalformedURLException mue) {
			System.out.println(mue);
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		return document;
	}
}
