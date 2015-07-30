package taskcontextsearch;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import taskcontextsearch.rank.PageHarvesterImpl;
import taskcontextsearch.rank.SEResultWithOriginDocument;

import java.util.ArrayList;
import java.util.List;


@ContextConfiguration
@Import(HarvestTest.HarvestTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class HarvestTest {

    @Configuration
    public static class HarvestTestConfig{

        @Bean
        public IPageHarvester pageHarvester(){
            return new PageHarvesterImpl();
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

    public static final Logger logger = LoggerFactory.getLogger(HarvestTest.class);



    @Autowired
    private IPageHarvester pageHarvester;


    @Test
    public void testHarvasting() throws Exception {
        List<String> urlsToHarvest = new ArrayList<>();
        urlsToHarvest.add("http://www.stern.de");
        urlsToHarvest.add("http://www.spiegel.de");
        urlsToHarvest.add("http://www.rp-online.de");
        urlsToHarvest.add("http://www.heise.de");
        urlsToHarvest.add("http://www.wdr.de");
        urlsToHarvest.add("http://www.zdf.de");
        urlsToHarvest.add("http://www.ard.de");
        urlsToHarvest.add("http://www.einslive.de");
        urlsToHarvest.add("http://www.java-magazin.de");
        urlsToHarvest.add("http://www.twitter.de");
        urlsToHarvest.add("http://www.bild.de");
        urlsToHarvest.add("http://www.cnn.de");
        urlsToHarvest.add("http://www.n-tv.de");
        urlsToHarvest.add("http://www.focus.de");
        urlsToHarvest.add("http://www.peta.de");
        urlsToHarvest.add("http://www.java.de");
        urlsToHarvest.add("http://www.eclipse.de");
        urlsToHarvest.add("http://www.neuss.de");
        urlsToHarvest.add("http://www.cunda.de");

        final List<com.google.api.services.customsearch.model.Result> searchResults = new ArrayList<>(20);
        for (String urlToHarvest : urlsToHarvest) {
            com.google.api.services.customsearch.model.Result resultTO = new com.google.api.services.customsearch.model.Result();
            resultTO.setLink(urlToHarvest);
            resultTO.setSnippet("Snippet xxxxx");
            searchResults.add(resultTO);
        }
        final List<SEResultWithOriginDocument> harvestResults = pageHarvester.execute(searchResults);
        for (SEResultWithOriginDocument seResultWithOriginDocument : harvestResults) {
            if (seResultWithOriginDocument != null && seResultWithOriginDocument.getOriginDocument() != null)
                System.out.println("" + seResultWithOriginDocument.getOriginDocument().getField("content"));
        }
    }

    @Test
    public void testHarvastingOrder() throws Exception{}

    @Test
    public void testFailedHarvasting() throws Exception{
        List<String> urlsToHarvest = new ArrayList<>();
        urlsToHarvest.add("http://www.stern.de");
        urlsToHarvest.add("http://www.spiegel.de");
        urlsToHarvest.add("http://www.heise.de");
        urlsToHarvest.add("http://www.zdf.de");
        urlsToHarvest.add("http://www.ard.de");
        urlsToHarvest.add("null");
        urlsToHarvest.add("http://www.focus.de");
        urlsToHarvest.add("http://www.xx");
        urlsToHarvest.add("http://www.java.de");
        urlsToHarvest.add("http://www.cunda.de");

        final List<com.google.api.services.customsearch.model.Result> searchResults = new ArrayList<>(20);
        for (String urlToHarvest : urlsToHarvest) {
            com.google.api.services.customsearch.model.Result resultTO = new com.google.api.services.customsearch.model.Result();
            resultTO.setLink(urlToHarvest);
            resultTO.setSnippet("Snippet xxxxx");
            searchResults.add(resultTO);
        }
        final List<SEResultWithOriginDocument> harvestResults = pageHarvester.execute(searchResults);
        Assert.assertTrue(harvestResults.get(0).getSeResult().getLink().equals("http://www.stern.de"));
        Assert.assertTrue(harvestResults.get(1).getSeResult().getLink().equals("http://www.spiegel.de"));
        Assert.assertTrue(harvestResults.get(2).getSeResult().getLink().equals("http://www.heise.de"));
        Assert.assertTrue(harvestResults.get(3).getSeResult().getLink().equals("http://www.zdf.de"));
        Assert.assertTrue(harvestResults.get(4).getSeResult().getLink().equals("http://www.ard.de"));
        Assert.assertTrue(harvestResults.get(5).getSeResult().getLink().equals("http://www.focus.de"));
        Assert.assertTrue(harvestResults.get(6).getSeResult().getLink().equals("http://www.java.de"));
        Assert.assertTrue(harvestResults.get(7).getSeResult().getLink().equals("http://www.cunda.de"));

    }
}
