package com.manesalcedo.controller;

import com.manesalcedo.model.GitHubSearchResponse;
import com.manesalcedo.model.Item;
import com.manesalcedo.model.TwitterSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
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
    private TwitterTemplate twitterTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloTwitter(Model model) {
        //with `write` permission we can show the Profile Username
        //, <span th:text="${twitterProfile.name}">Some User</span>!
        //model.addAttribute(twitter.userOperations().getUserProfile());

        RestTemplate restTemplate = new RestTemplate();
        GitHubSearchResponse gitHubSearchResponse = restTemplate.getForObject("https://api.github.com/search/repositories?q=reactive", GitHubSearchResponse.class);

        List<TwitterSearchResult> twitterSearchResponses = new LinkedList<>();

        for (Item i : gitHubSearchResponse.getItems()) {
            String query = i.getHtmlURL();
            SearchResults searchResults = twitterTemplate.searchOperations().search(
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
