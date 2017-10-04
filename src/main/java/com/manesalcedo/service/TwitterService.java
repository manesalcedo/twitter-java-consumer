package com.manesalcedo.service;

import com.manesalcedo.configuration.TwitterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterService {

    private final TwitterClient twitterClient;

    @Autowired
    public TwitterService(TwitterClient twitterClient){
        this.twitterClient = twitterClient;
    }

    public List<String> getTweets(String keyword){
        return twitterClient.searchTweets(keyword)
                .getTweets().parallelStream()
                .map(Tweet::getText)
                .collect(Collectors.toList());
    }
}
