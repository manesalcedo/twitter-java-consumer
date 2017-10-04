package com.manesalcedo.controller;

import com.manesalcedo.model.TwitterSearchResponses;
import com.manesalcedo.model.TwitterSearchResult;
import com.manesalcedo.service.GitHubService;
import com.manesalcedo.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class TwitterController {

    private static final Logger LOGGER = Logger.getLogger(TwitterController.class.getName());
    private TwitterService twitterService;
    private GitHubService gitHubService;

    @Autowired
    public TwitterController(TwitterService twitterService, GitHubService gitHubService){
        this.twitterService = twitterService;
        this.gitHubService = gitHubService;
    }

    @RequestMapping(value = "/githubOnTwitter/", method = RequestMethod.GET)
    public TwitterSearchResponses getGitHubRepoMentionedOnTwitter() {

        LOGGER.info("start");
        TwitterSearchResponses t = TwitterSearchResponses.builder()
                .twitterSearchResultList(gitHubService.searchReactiveRepositories().parallelStream()
                        .map(i -> TwitterSearchResult.builder()
                                .query(i.getHtmlURL())
                                .tweets(twitterService.getTweets(i.getHtmlURL()))
                                .build())
                        .filter(f -> !f.getTweets().isEmpty())
                        .collect(Collectors.toList())
                )
                .build();
        LOGGER.info("end");

        return t;
    }
}