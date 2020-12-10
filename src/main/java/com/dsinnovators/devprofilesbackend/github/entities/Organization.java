package com.dsinnovators.devprofilesbackend.github.entities;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Organization {
    private String name;
    private String email;
    private String description;
    private String url;
    private String websiteUrl;
}
