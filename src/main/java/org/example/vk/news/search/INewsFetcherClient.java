package org.example.vk.news.search;

import java.io.IOException;
import java.util.List;

public interface INewsFetcherClient {

    void UpdateSearchTag(String searchTag);

    void UpdateStartTime(String startTime);

    void UpdateEndTime(String endTime);

    Integer GetTagStatistics() throws IOException;
}
