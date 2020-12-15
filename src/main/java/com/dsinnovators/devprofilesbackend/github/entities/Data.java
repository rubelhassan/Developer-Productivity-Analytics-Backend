package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Data {
    private GithubUser user;
    private RateLimit rateLimit;
}
