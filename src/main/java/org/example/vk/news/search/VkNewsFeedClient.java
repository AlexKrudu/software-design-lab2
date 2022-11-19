package org.example.vk.news.search;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class VkNewsFeedClient implements INewsFetcherClient {

    public VkNewsFeedClient(String configFilePath) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Config file not found: " + ex.getMessage());
        } catch (IOException ex) {
            throw new RuntimeException("An exception occurred while reading config file: " + ex.getMessage());
        }
        this.apiToken = prop.getProperty("api_token");
        if (prop.containsKey("api_path")) {
            this.apiPath = prop.getProperty("api_path");
        }
    }

    @Override
    public void UpdateSearchTag(String searchTag) {
        this.queryTag = searchTag;
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
        String charset = StandardCharsets.UTF_8.name();
        String query = String.format("v=%s&access_token=%s&q=%s&start_time=%s&end_time=%s",
                URLEncoder.encode(apiVersion, charset),
                URLEncoder.encode(this.apiToken, charset),
                URLEncoder.encode(this.queryTag, charset),
                URLEncoder.encode(this.startTime, charset),
                URLEncoder.encode(this.endTime, charset));
        var response = GetJsonResponse(new URL(apiPath + "?" + query), charset);
        return response.getJSONObject("response").getInt("total_count");
    }

    private JSONObject GetJsonResponse(URL requestUrl, String charset) throws IOException {
        URLConnection connection = requestUrl.openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response = connection.getInputStream();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(response, charset));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return new JSONObject(responseStrBuilder.toString());
    }
    private String apiPath = "https://api.vk.com/method/newsfeed.search";
    private static final String apiVersion = "5.131";

    private final String apiToken;
    private String queryTag;
    private String startTime;
    private String endTime;
}
