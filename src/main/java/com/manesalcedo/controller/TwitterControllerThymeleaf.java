package com.manesalcedo.controller;

import com.manesalcedo.configuration.TwitterConfiguration;
import com.manesalcedo.model.GitHubSearchResponse;
import com.manesalcedo.model.Item;
import com.manesalcedo.model.TwitterSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class TwitterControllerThymeleaf {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloTwitter(Model model) {
        Twitter twitter = twitterConfiguration.getTwitterTemplate();

        model.addAttribute(twitter.userOperations().getUserProfile());

        RestTemplate restTemplate = new RestTemplate();
        GitHubSearchResponse gitHubSearchResponse = restTemplate.getForObject("https://api.github.com/search/repositories?q=reactive", GitHubSearchResponse.class);

        List<TwitterSearchResult> twitterSearchResponses = new LinkedList<>();

        for (Item i : gitHubSearchResponse.getItems()) {
            String query = i.getHtmlURL();
            SearchResults searchResults = twitter.searchOperations().search(
                    new SearchParameters(query)
                            .resultType(SearchParameters.ResultType.RECENT)
                            .count(10)
                            .includeEntities(false));
            if (searchResults.getTweets().isEmpty()) {
                continue;
            }
            TwitterSearchResult twitterSearchResponse = TwitterSearchResult.builder()
                    .query(query)
                    .tweets(searchResults.getTweets().stream()
                            .map(Tweet::getText)
                            .collect(Collectors.toList()))
                    .build();
            twitterSearchResponses.add(twitterSearchResponse);
        }
        model.addAttribute("twitterSearchResponses", twitterSearchResponses);

        return "hello";
    }
}
