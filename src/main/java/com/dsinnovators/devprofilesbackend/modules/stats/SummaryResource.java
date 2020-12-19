package com.dsinnovators.devprofilesbackend.modules.stats;

import com.dsinnovators.devprofilesbackend.modules.stats.entities.DevelopersSummary;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
@Relation(value = "summary", collectionRelation = "summaries")
public class SummaryResource extends RepresentationModel<SummaryResource> {
    private final DevelopersSummary summary;

    public SummaryResource(DevelopersSummary summary) {
        this.summary = summary;
        add(linkTo(SummaryController.class).withRel("summaries"));
    }
}
