package com.manesalcedo.controller;

import com.manesalcedo.model.GitHubSearchResponse;
import com.manesalcedo.model.TwitterSearchResponses;
import com.manesalcedo.model.TwitterSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@RestController
public class TwitterController {

    @Autowired
    private TwitterTemplate twitterTemplate;

    private static final String GITHUB_SEARCH_REPO_URI = "https://api.github.com/search/repositories?q=reactive";

    @RequestMapping(value = "/githubOnTwitter/", method = RequestMethod.GET)
    public TwitterSearchResponses getGitHubRepoMentionedOnTwitter() {
        RestTemplate restTemplate = new RestTemplate();
        GitHubSearchResponse gitHubSearchResponse = restTemplate.getForObject(GITHUB_SEARCH_REPO_URI, GitHubSearchResponse.class);

        return TwitterSearchResponses.builder()
                .twitterSearchResultList(gitHubSearchResponse.getItems().parallelStream()
                        .map(i -> TwitterSearchResult.builder()
                                .query(i.getHtmlURL())
                                .tweets(twitterTemplate.searchOperations().search(
                                        new SearchParameters(i.getHtmlURL())
                                                .resultType(SearchParameters.ResultType.RECENT)
                                                .count(10)
                                                .includeEntities(false))
                                        .getTweets().parallelStream()
                                        .map(Tweet::getText)
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList())
                )
                .build();
    }
}