package org.example.vk.news.search;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public class TagStatisticsBuilder {

    public TagStatisticsBuilder(INewsFetcherClient client) {
        this.newsClient = client;
    }

    public List<Integer> DoSearch(String tag, Integer nHours, Long now) throws IOException {
        ArrayList<Integer> tagsCount = new ArrayList<>(nHours);
        newsClient.UpdateSearchTag(tag);
        var requestTimeRanges = BuildRequestTimeRanges(nHours, now);
        for (int i = 0; i < requestTimeRanges.size(); i++) {
            newsClient.UpdateStartTime(requestTimeRanges.get(i).getKey().toString());
            newsClient.UpdateEndTime(requestTimeRanges.get(i).getValue().toString());
            tagsCount.add(i, newsClient.GetTagStatistics());
        }
        return tagsCount;
    }
    protected List<Map.Entry<Long, Long>> BuildRequestTimeRanges(int nHours, long now){
        var resultList = new ArrayList<Map.Entry<Long, Long>>();
        long timeTo = now;
        long timeFrom = timeTo;
        for (int i = 0; i < nHours; i++){
            timeFrom -= TimeUnit.HOURS.toSeconds(1);
            resultList.add(i, new AbstractMap.SimpleEntry<>(timeFrom, timeTo));
            timeTo = timeFrom;
        }
        return resultList;
    }

    INewsFetcherClient newsClient;
}
