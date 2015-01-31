package taskcontextsearch.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.customsearch.model.Result;
//@Primary
@Service(value = "MockSearchService")
public class MockSearchService implements ISearchEngineService {
 
	private static final List<Result> mockItems = new ArrayList<Result>();	
	private static final List<String> payload = Arrays.asList(
			"Standard Eclipse package suited for Java and plug-in development plus adding new plugins; already includes Git, Marketplace Client, source code and.",
	    	"The essential tools for any Java developer, including a Java IDE, a CVS client, Git client, XML Editor, Mylyn, Maven integration and WindowBuilder ...",
	    	"Written mostly in Java, Eclipse can be used to develop applications. By means of various plug-ins, Eclipse may also be used to develop applications in other ...",
	    	"The essential tools for any Java developer, including a Java IDE, a CVS client, Git client, XML Editor, Mylyn, Maven integration and WindowBuilder ...",
	    	"Package Description. Tools for Java developers creating Java EE and Web applications, including a Java IDE, tools for Java EE, JPA, JSF, Mylyn and others.",
	    	"Package Description. The essential tools for any Java developer, including a Java IDE, a CVS client, XML Editor and Mylyn.",
	    	"This series of 16 tutorials is designed to help get you started writing Java programs using the Eclipse integrated development environment. The tutorials and all ...",
	    	"The essential tools for any Java developer, including a Java IDE, a CVS client, Git client, XML Editor, Mylyn, Maven integration and WindowBuilder ...",
	    	"Mar 18, 2014 ... Starting with I20140318-0830 all Luna (4.4) builds on our downloads page contain the Eclipse support for Java™ 8. For Kepler SR2 (4.3.2) a ...",
	    	"The JDT project provides the tool plug-ins that implement a Java IDE supporting the development of any Java application, including Eclipse plug-ins. It adds a ...",
	    	"Jun 25, 2014 ... Installing Eclipse is relatively easy, but does involve a few steps and software from at least two different sources. Eclipse is a Java-based ...",
	    	"May 2, 2009 ... Visit my website at https://buckysroom.org/ for all of my videos and tutorials! Facebook ...",
	    	"Mar 27, 2014 ... Java 8 has not yet landed in our standard download packages. But you can add it to your existing Eclipse Kepler SR2 package.",
	    	"IMPORTANT: Install Java first, BEFORE you install Eclipse. Java. The following instructions were composed for installing Sun Java 1.5.0_08 (aka JDK 5.0 ...",
	    	"Eclipse Create Java Project - Learn Eclipse IDE in simple and easy steps starting from its installation, views, menus, windows, perspectives, project creation, ...",
	    	"Mobile Tools for the Java Platform project creates tools and frameworks to extend eclipse platform to support mobile device Java application development.",
	    	"Watch/Edit · Accessibility features in Eclipse ... Maximizing and minimizing in the eclipse presentation ..... Browsing Java elements using the package explorer",
	    	"Apr 4, 2014 ... The Azure Toolkit for Eclipse with Java (by Microsoft Open Technologies) provides templates and functionality that allow you to easily create, ...",
	    	"Jun 25, 2014 ... At the same time, Eclipse released support for Java 8 through an add-on. Now with Eclipse Luna, Java 8 support is a first class citizen in ...",
	    	"Eclipse support for Java™ 8 is built–in to Luna (4.4) so it, and all subsequent builds, contain full support for Java™ 8. For Kepler SR2 (4.3.2), a feature patch ...");
	
	    	
	 static{
		 for (String element : payload) {
			Result tmpResult = new Result();
			tmpResult.setSnippet(element);
			mockItems.add(tmpResult);
		}
		 
	 }   	
    //TODO: config-params for resultsize
    /* (non-Javadoc)
	 * @see org.taskstodo.dao.SearchService#doSearch(java.lang.String)
	 */
    @Override
	public List<Result> doSearch(final String searchTerm) {
    	return mockItems;
    }
 }