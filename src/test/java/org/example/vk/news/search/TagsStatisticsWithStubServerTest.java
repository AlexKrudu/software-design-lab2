package org.example.vk.news.search;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8081)
public class TagsStatisticsWithStubServerTest {

    @Test
    public void testTagsStatsNoTags(WireMockRuntimeInfo info) throws IOException {
        stubFor(get(urlPathEqualTo("/method/newsfeed.search")).
                willReturn(aResponse().withBody("{\"response\" : {\"total_count\" : 0}}")));
        var newsSearcher = new TagStatisticsBuilder(new VkNewsFeedClient("src/main/resources/test.app.config"));
        var expected = new ArrayList<>(Collections.nCopies(24, 0));
        assertEquals(expected, newsSearcher.DoSearch("any", 24, 100000L));
    }

    @Test
    public void testTagsStatsSomeTags() throws IOException {
        stubFor(get(urlPathEqualTo("/method/newsfeed.search")).withQueryParams(Map.of("q", equalTo("a"))).
                willReturn(aResponse().withBody("{\"response\" : {\"total_count\" : 1}}")));
        stubFor(get(urlPathEqualTo("/method/newsfeed.search")).withQueryParams(Map.of("q", equalTo("b"))).
                willReturn(aResponse().withBody("{\"response\" : {\"total_count\" : 3}}")));
        var newsSearcher = new TagStatisticsBuilder(new VkNewsFeedClient("src/main/resources/test.app.config"));
        var expected_a = new ArrayList<>(Collections.nCopies(24, 1));
        var expected_b = new ArrayList<>(Collections.nCopies(24, 3));
        assertEquals(expected_a, newsSearcher.DoSearch("a", 24, 100000L));
        assertEquals(expected_b, newsSearcher.DoSearch("b", 24, 100000L));
    }

}
