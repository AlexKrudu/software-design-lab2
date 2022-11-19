package org.example;

import org.example.vk.news.search.TagStatisticsBuilder;
import org.example.vk.news.search.VkNewsFeedClient;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("Incorrect arguments format.");
        }
        String tag = args[0];
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("Provided searching tag is empty");
        }
        int nHours;
        try {
            nHours = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Unable to convert hours range to search by to integer value");
        }
        if (nHours <= 0 || nHours > 24) {
            throw new IllegalArgumentException("Hours range value should be between 0 and 24");
        }

        var statsBuilder = new TagStatisticsBuilder(new VkNewsFeedClient("src/main/resources/app.config"));
        var frequencies = statsBuilder.DoSearch(tag, nHours, Instant.now().getEpochSecond());

        System.out.println(frequencies.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}