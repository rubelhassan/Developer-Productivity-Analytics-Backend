package com.dsinnovators.devprofilesbackend.github.querymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DateRange {
    private final String fromDate;
    private final String toDate;
}
