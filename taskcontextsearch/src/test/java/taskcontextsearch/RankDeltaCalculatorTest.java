package taskcontextsearch;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.taskstodo.to.ResultTO;
import taskcontextsearch.rank.GoogleResultOriginDocument;
import taskcontextsearch.rank.IRankDeltaCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"testContext.xml"})
public class RankDeltaCalculatorTest {

    public static final Logger logger = LoggerFactory.getLogger(RankDeltaCalculatorTest.class);

    @Autowired
    private IRankDeltaCalculator IRankDeltaCalculator;

    @Autowired
    private IPageHarvester pageHarvester;

    @Before
    public void setUp() {

    }

    @Test
    public void testHarvasting() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("http://www.stern.de");
        list.add("http://www.spiegel.de");
        list.add("http://www.rp-online.de");
        list.add("http://www.heise.de");
        list.add("http://www.wdr.de");
        list.add("http://www.zdf.de");
        list.add("http://www.ard.de");
        list.add("http://www.einslive.de");
        list.add("http://www.java-magazin.de");
        list.add("http://www.twitter.de");
        list.add("http://www.bild.de");
        list.add("http://www.cnn.de");
        list.add("http://www.n-tv.de");
        list.add("http://www.focus.de");
        list.add("http://www.peta.de");
        list.add("http://www.java.de");
        list.add("http://www.eclipse.de");
        list.add("http://www.neuss.de");
        list.add("http://www.cunda.de");

        List<com.google.api.services.customsearch.model.Result> searchResults = new ArrayList<>(20);
        for (String url : list) {
            com.google.api.services.customsearch.model.Result resultTO = new com.google.api.services.customsearch.model.Result();
            resultTO.setLink(url);
            resultTO.setSnippet("Snippet xxxxx");
            searchResults.add(resultTO);
        }
        List<GoogleResultOriginDocument> futures = pageHarvester.execute(searchResults);
        for (GoogleResultOriginDocument e : futures) {
            if (e != null && e.getOriginDocument() != null)
                logger.info("" + e.getOriginDocument().getField("content"));
        }
    }


    @Test
    public void testRankDelta() {

        final List<ResultTO> initialResult = Arrays.asList(
                new ResultTO("Die Komik des Scheiterns war eines seiner Themen", "spiegel.de"),
                new ResultTO("Er kam, sah - und hatte weniger Publikum als erwartet.", "stern.de"));

        final List<ResultTO> rerankedResult = Arrays.asList(
                new ResultTO("Er kam, sah - und hatte weniger Publikum als erwartet.", "stern.de"),
                new ResultTO("Die Komik des Scheiterns war eines seiner Themen", "spiegel.de")
        );

        IRankDeltaCalculator.compute(initialResult, rerankedResult);
        Assert.assertEquals(1, rerankedResult.get(0).getRerankedPosition());
        Assert.assertEquals(2, rerankedResult.get(1).getRerankedPosition());
    }

}
