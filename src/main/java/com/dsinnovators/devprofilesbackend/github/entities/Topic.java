package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Topic {
    private String name;
    private String stargazers;
}
