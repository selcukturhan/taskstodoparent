package taskcontextsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Ignore;
import org.junit.Test;

//@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"testContext.xml"})
public class RankingServiceTest {

	// @Autowired
	// IRankingService rankingService;
	//
	// @Autowired
	// SessionFactory sessionFactory;

	@Test
	 @Ignore
	public void testThreadingConnect() throws InterruptedException,
			IOException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		Collection<Callable<Document>> list = new ArrayList<Callable<Document>>();
		list.add(new FetchIndexWorker(
				"http://en.wikipedia.org/wiki/JAR_(file_format)",""));
		list.add(new FetchIndexWorker("http://www.stern.de",""));
		list.add(new FetchIndexWorker("http://www.spiegel.de",""));
		list.add(new FetchIndexWorker("http://www.rp-online.de",""));
		list.add(new FetchIndexWorker("http://www.heise.de",""));
		list.add(new FetchIndexWorker("http://www.wdr.de",""));
		list.add(new FetchIndexWorker("http://www.zdf.de",""));
		list.add(new FetchIndexWorker("http://www.ard.de",""));
		list.add(new FetchIndexWorker("http://www.einslive.de",""));
		list.add(new FetchIndexWorker("http://www.java-magazin.de",""));
		list.add(new FetchIndexWorker("http://www.twitter.de",""));
		list.add(new FetchIndexWorker("http://www.bild.de",""));
		list.add(new FetchIndexWorker("http://www.cnn.de",""));
		list.add(new FetchIndexWorker("http://www.n-tv.de",""));
		list.add(new FetchIndexWorker("http://www.focus.de",""));
		list.add(new FetchIndexWorker("http://www.peta.de",""));
		list.add(new FetchIndexWorker("http://www.java.de",""));
		list.add(new FetchIndexWorker("http://www.eclipse.de",""));
		list.add(new FetchIndexWorker("http://www.neuss.de",""));
		list.add(new FetchIndexWorker("http://www.cunda.de",""));

		List<Future<Document>> result = executor.invokeAll(list);
		GermanAnalyzer standardanalyzer = new GermanAnalyzer(
				Version.LUCENE_4_9);
		IndexWriterConfig indexwriterconfig = new IndexWriterConfig(
				Version.LUCENE_4_9, standardanalyzer);

		Directory ramdirectory = new SimpleFSDirectory(new File("/home/regulator/myindexnotvg"));

		IndexWriter indexwriter = new IndexWriter(ramdirectory,
				indexwriterconfig);

		List<Document> myRefs = new ArrayList<Document>();
		for (Future<Document> document : result) {
			myRefs.add(document.get());
			indexwriter.addDocument(document.get());
		}
		indexwriter.close();
		ramdirectory.close();
		
		
		System.out.println(myRefs.get(0).get("url"));
//		IndexReader indexReader = DirectoryReader.open(ramdirectory);
		// int num = indexReader.numDocs();
		// for ( int i = 0; i < num; i++){
		// Document d = indexReader.document( i);
		// System.out.println( "\n#######################" +d);
		// }

//		Terms terms = SlowCompositeReaderWrapper.wrap(indexReader).terms(
//				"content");
//		System.out.println(terms.getMax().utf8ToString());
//		System.out.println(terms.getSumDocFreq());
//		TermsEnum termsEnum = terms.iterator(null);
//
//		BytesRef text;
//		while ((text = termsEnum.next()) != null) {
//			System.out.println("field=" + text.utf8ToString());
//		}
//		int numDocs = indexReader.numDocs();
//		for (int j = 0; j < numDocs; j++) {
//			Document tmpDoc = indexReader.document(j);
//			Terms field = indexReader.getTermVector(j, "content");
//			System.out.println(field);
//			
//		}
//		//computeTermIdMap(indexReader);
//		indexReader.close();
	}
	
	
	
	private Map<String, Integer> computeTermIdMap(IndexReader reader) throws IOException {
	    Map<String,Integer> termIdMap = new HashMap<String,Integer>();
	    int id = 0;
	    Fields fields = MultiFields.getFields(reader);
	    Terms terms = fields.terms("content");
	    TermsEnum itr = terms.iterator(null);
	    BytesRef term = null;
	    while ((term = itr.next()) != null) {               
	        String termText = term.utf8ToString();              
	        if (termIdMap.containsKey(termText))
	            continue;
	        System.out.println(termText); 
	        termIdMap.put(termText, id++);
	    }

	    return termIdMap;
	}

	@Test
	@Ignore
	public void testHtmlParserConnect() {
		InputStream inputStream = null;
		try {
			inputStream = new URL("http://spiegel.de").openStream();

			/*
			 * LinkContentHandler linkHandler = new LinkContentHandler();
			 * BodyContentHandler textHandler = new BodyContentHandler();
			 * ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
			 * TeeContentHandler teeHandler = new TeeContentHandler(linkHandler,
			 * textHandler, toHTMLHandler); Metadata metadata = new Metadata();
			 * ParseContext parseContext = new ParseContext();
			 */
			Parser parser = new AutoDetectParser();

			BodyContentHandler handler = new BodyContentHandler(10000000);
			Metadata metadata = new Metadata();

			parser.parse(inputStream, handler, metadata, new ParseContext());
			String[] metadataNames = metadata.names();

			// Display all metadata
			for (String name : metadataNames) {
				System.out.println(name + ": " + metadata.get(name));
			}

			String plainText = handler.toString();
			System.out.println(plainText.replaceAll("\t+", " ")
					.replaceAll("\n+", " ").replaceAll(" +", " "));
			// Parser parser = new AutoDetectParser();
			// ContentHandler handler = new BodyContentHandler();
			// Metadata metadata = new Metadata();
			// parser.parse(inputStream, handler, new Metadata(), new
			// ParseContext());
			// String plainText = textHandler.toString();
			// System.out.println(plainText);
			// System.out.println(plainText.replaceAll("\t+"," ").replaceAll("\n+"," ").replaceAll(" +"," "));
			// // plainText =
			// plainText.replaceAll("\t+"," ").replaceAll("\n+"," ").replaceAll(" +"," ");
			// StandardAnalyzer standardanalyzer = new
			// StandardAnalyzer(Version.LUCENE_4_9);
			// IndexWriterConfig indexwriterconfig = new
			// IndexWriterConfig(Version.LUCENE_4_9, standardanalyzer);
			//
			// RAMDirectory ramdirectory = new RAMDirectory();
			//
			// IndexWriter indexwriter = new IndexWriter(ramdirectory,
			// indexwriterconfig);
			// Document document = new Document();
			// document.add(new TextField("snippet", plainText,
			// org.apache.lucene.document.Field.Store.YES));
			// indexwriter.addDocument(document);
			// indexwriter.close();
		} catch (MalformedURLException mue) {
			System.out.println(mue);
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	// @Ignore
	// @Test
	// public void testDataConnect() {
	// InitialResult initialResult = new InitialResult();
	// initialResult.setRank(2);
	// initialResult.setSnippet("Halloi");
	// Serializable result = sessionFactory.openSession().save(initialResult);
	// System.out.println(result);
	//
	// Result res = (Result) sessionFactory.openSession().load(Result.class,
	// result);
	// System.out.println(res);
	// }

	// @Ignore
	// @Test
	// public void testSearchResultForTerm() {
	// Task rootTask = new Task();
	// rootTask.setTitle("java");
	// Task childTask = new Task();
	// childTask.setTitle("programming");
	// childTask.setParentTask(rootTask);
	// Task childChildTask = new Task();
	// childChildTask.setTitle("c++");
	// childChildTask.setParentTask(childTask);
	// // QueryContext queryContext = re.prepareWeighting(childChildTask);
	// // System.out.println(queryContext);
	// TaskContextSearchResultTO result =
	// rankingService.getSearchResultForTerm("java", childChildTask);
	//
	// System.out.println("-------------------------------RERANKED RESULT--------------------------------------");
	// System.out.println(result);
	// }

}
