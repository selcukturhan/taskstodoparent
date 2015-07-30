package taskcontextsearch;

import com.google.api.services.customsearch.model.Result;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import taskcontextsearch.search.GoogleSearchService;
import taskcontextsearch.search.ISearchEngineService;

import java.util.List;
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@Import(CustomSearchTest.CustomSearchTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomSearchTest {

	@Configuration
	public static class CustomSearchTestConfig{
		@Bean
		public GoogleSearchService searchEngineService(){
			return new GoogleSearchService();
		}
	}

 	@Autowired
	ISearchEngineService searchEngineService;

	public CustomSearchTest(){}

	@Test
	public void testCustomSearchConnect() {
		final List<Result> result = searchEngineService.doSearch("jsf");
		Assert.assertNotNull(result);
		Assert.assertFalse(result.isEmpty());
	}
	
}
