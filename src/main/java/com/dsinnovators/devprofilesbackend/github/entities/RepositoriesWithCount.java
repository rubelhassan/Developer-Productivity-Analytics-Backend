package com.dsinnovators.devprofilesbackend.github.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoriesWithCount {
    private List<Repository> repositories = new ArrayList<>();
    private Integer totalCount;
}
