package com.dsinnovators.devprofilesbackend.modules.developers.entities;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GithubCode {
    @NotEmpty
    private String code;
}
