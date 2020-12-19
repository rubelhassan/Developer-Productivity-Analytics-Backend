package com.dsinnovators.devprofilesbackend.modules.stats;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value="summaries", produces = "application/hal+json")
@AllArgsConstructor
public class SummaryController {
    private SummaryService summaryService;

    @GetMapping
    ResponseEntity<SummaryResource> summary(){
        return ok(new SummaryResource(summaryService.generateStats()));
    }
}
