package com.manesalcedo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TwitterSearchResult {
    String query;
    List<String> tweets;
}
