package com.dsinnovators.devprofilesbackend.github.querymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GraphqlQuery {
    private final String query;
    private final String variables;
}
