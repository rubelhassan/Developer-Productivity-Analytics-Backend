package com.dsinnovators.devprofilesbackend.github.querymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DateRangeWithUserName{
    private final String userName;
    private final String fromDate;
    private final String toDate;

    public DateRangeWithUserName(String userName, DateRange dateRange) {
        this.userName = userName;
        this.fromDate = dateRange.getFromDate();
        this.toDate = dateRange.getToDate();
    }
}
