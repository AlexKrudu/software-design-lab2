package org.example.vk.news.search;

import java.io.IOException;
import java.util.Collections;
import java.util.TreeMap;

public class MockNewsFeedClient implements INewsFetcherClient {

    public MockNewsFeedClient(TreeMap<Long, String> tagsEntries) {
        this.tagsEntries = tagsEntries;
    }

    @Override
    public void UpdateSearchTag(String searchTag) {
        this.targetTag = searchTag;
    }

    @Override
    public void UpdateStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public void UpdateEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public Integer GetTagStatistics() throws IOException {
        return Collections.frequency
                (tagsEntries.subMap
                        (Long.parseLong(startTime),
                                Long.parseLong(endTime)).values(), targetTag);
    }

    private final TreeMap<Long, String> tagsEntries;
    private String targetTag;
    private String startTime;
    private String endTime;
}
