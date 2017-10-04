package com.manesalcedo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

@Component
public class TwitterClient {

    @Autowired
    TwitterTemplate twitterTemplate;

    public SearchResults searchTweets(String query) {
        return twitterTemplate.searchOperations().search(
                new SearchParameters(query)
                        .resultType(SearchParameters.ResultType.RECENT)
                        .count(10)
                        .includeEntities(false));
    }
}
