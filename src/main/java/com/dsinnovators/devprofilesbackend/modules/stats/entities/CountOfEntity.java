package com.dsinnovators.devprofilesbackend.modules.stats.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CountOfEntity {
    private String name;
    private int count;
}
