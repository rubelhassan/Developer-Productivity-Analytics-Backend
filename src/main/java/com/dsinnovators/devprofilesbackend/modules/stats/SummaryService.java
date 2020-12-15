package com.dsinnovators.devprofilesbackend.modules.stats;

import com.dsinnovators.devprofilesbackend.modules.developers.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SummaryService {
    private ProfileRepository profileRepository;

    public DevelopersSummary generateStats() {
        return profileRepository.getAccumulatedValuesQuery();
    }
}
