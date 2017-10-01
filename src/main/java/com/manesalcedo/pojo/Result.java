package com.manesalcedo.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;

@Getter
@Setter
@Builder
public class Result {
    String query;
    List<Tweet> tweets;
}
