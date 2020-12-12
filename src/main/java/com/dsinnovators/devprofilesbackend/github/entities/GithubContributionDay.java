package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GithubContributionDay {
    private Integer weekday;
    private Integer contributionCount;
    private Date date;
}
