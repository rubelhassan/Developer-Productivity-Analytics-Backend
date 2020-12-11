package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RateLimit {
    private Integer limit;
    private Integer cost;
    private Integer remaining;
    private Date resetAt;
}
