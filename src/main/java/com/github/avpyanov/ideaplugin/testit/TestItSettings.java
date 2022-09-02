package com.github.avpyanov.ideaplugin.testit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TestItSettings {

    private String endpoint;
    private String token;
    private String projectId;
    private String rootSectionId;
    private String featureAttributeId;
    private String storyAttributeId;
}