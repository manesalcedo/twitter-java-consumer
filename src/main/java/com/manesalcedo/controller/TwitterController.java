package com.manesalcedo.controller;

import com.manesalcedo.configuration.TwitterConfiguration;
import com.manesalcedo.pojo.GitHubSearchResponse;
import com.manesalcedo.pojo.Item;
import com.manesalcedo.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/")
public class TwitterController {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @RequestMapping(method = RequestMethod.GET)
    public String helloTwitter(Model model) {
        Twitter twitter = twitterConfiguration.getTwitterTemplate();

        model.addAttribute(twitter.userOperations().getUserProfile());

        RestTemplate restTemplate = new RestTemplate();
        GitHubSearchResponse gitHubSearchResponse = restTemplate.getForObject("https://api.github.com/search/repositories?q=reactive", GitHubSearchResponse.class);

        List<Result> results = new LinkedList<>();

        for (Item i : gitHubSearchResponse.getItems()) {
            String query = i.getHtmlURL();
            SearchResults searchResults = twitter.searchOperations().search(
                    new SearchParameters(query)
                            .resultType(SearchParameters.ResultType.RECENT)
                            .count(10)
                            .includeEntities(false));
            if(searchResults.getTweets().isEmpty()){
                continue;
            }
            Result result = Result.builder()
                    .query(query)
                    .tweets(searchResults.getTweets())
                    .build();
            results.add(result);
        }
        model.addAttribute("results", results);

        return "hello";
    }

}