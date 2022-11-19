package org.example.vk.news.search;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class TagsStatisticsTest {

    @Test
    public void testBuildRequestParamsSimple() {
        var actualRequestParams = new TagStatisticsBuilder(new MockNewsFeedClient(new TreeMap<>())).BuildRequestTimeRanges(1, 3600);
        assertEquals(1, actualRequestParams.size());
        assertEquals(Map.of(0L, 3600L).entrySet().stream().toList(), actualRequestParams);
    }

    @Test
    public void testBuildRequestParamsMultiple() {
        var newsSearcher = new TagStatisticsBuilder(new MockNewsFeedClient(new TreeMap<>()));
        Long now = 1000000L;
        for (int nHours = 2; nHours <= 24; nHours++) {
            var actualRequestParams = newsSearcher.BuildRequestTimeRanges(nHours, now);
            assertEquals(now, actualRequestParams.get(0).getValue());
            assertEquals(now - 3600 * nHours, actualRequestParams.get(actualRequestParams.size() - 1).getKey());
            for (int i = 0; i < actualRequestParams.size() - 1; i++) {
                var curReqParams = actualRequestParams.get(i);
                var nextReqParams = actualRequestParams.get(i + 1);
                assertEquals(3600, curReqParams.getValue() - curReqParams.getKey());
                assertEquals(curReqParams.getKey(), nextReqParams.getValue());
            }
        }

    }

    @Test
    public void testTagsStatsNoTags() throws IOException {
        var newsSearcher = new TagStatisticsBuilder(new MockNewsFeedClient(new TreeMap<>()));
        var expected = new ArrayList<>(Collections.nCopies(24, 0));
        assertEquals(expected, newsSearcher.DoSearch("any", 24, 100000L));
    }

    @Test
    public void testTagsStatsSomeTags() throws IOException {
        TreeMap<Long, String> tagsEntries = new TreeMap<>();
        long curHourStart = 0L;
        for (int i = 1; i < 25; i++){
            Long innerHourStart = curHourStart;
            for (int j = 0; j < i; j++){
                tagsEntries.put(innerHourStart, "a");
                innerHourStart++;
            }
            curHourStart += 3600;
        }
        var newsSearcher = new TagStatisticsBuilder(new MockNewsFeedClient(tagsEntries));
        var actual = newsSearcher.DoSearch("a", 24, 86400L);
        assertEquals(24, actual.size());
        for (int i = 0; i < actual.size(); i++){
            assertEquals(24 - i, actual.get(i));
        }
    }

}
