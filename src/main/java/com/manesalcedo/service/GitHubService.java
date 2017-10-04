package com.manesalcedo.service;

import com.manesalcedo.model.GitHubSearchResponse;
import com.manesalcedo.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GitHubService {

    private static final String GITHUB_SEARCH_REPO_URI = "https://api.github.com/search/repositories?q=reactive";

    public List<Item> searchReactiveRepositories(){
        RestTemplate restTemplate = new RestTemplate();
        GitHubSearchResponse gitHubSearchResponse = restTemplate.getForObject(GITHUB_SEARCH_REPO_URI, GitHubSearchResponse.class);

        return gitHubSearchResponse.getItems();
    }
}
