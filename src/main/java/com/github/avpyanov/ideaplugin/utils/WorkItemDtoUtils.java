package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.github.avpyanov.testit.client.dto.IdDto;
import com.github.avpyanov.testit.client.dto.WorkItem;
import com.github.avpyanov.testit.client.dto.WorkItemPutDto;
import com.github.avpyanov.testit.client.dto.WorkItemStep;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkItemDtoUtils {

    public static WorkItemPutDto getPutRequestDto(WorkItem workItem, Map.Entry<PsiMethod, TestCase> entry) {
        WorkItemPutDto workItemPutDto = new WorkItemPutDto();
        workItemPutDto.setName(entry.getValue().getName());
        workItemPutDto.setId(workItem.getId());
        workItemPutDto.setSectionId(workItem.getSectionId());

        List<IdDto> attachments = workItem.getAttachments().stream()
                .map(attachment -> new IdDto(attachment.getId()))
                .collect(Collectors.toList());
        workItemPutDto.setAttachments(attachments);

        List<IdDto> autotests = workItem.getAutoTests().stream()
                .map(autotestDto -> new IdDto(autotestDto.getId()))
                .collect(Collectors.toList());
        workItemPutDto.setAutoTests(autotests);
        workItemPutDto.setDescription(workItem.getDescription());
        workItemPutDto.setState(workItem.getState());
        workItemPutDto.setPriority(workItem.getPriority());

        workItemPutDto.setPreconditionSteps(workItem.getPreconditionSteps());
        workItemPutDto.setPostconditionSteps(workItem.getPostconditionSteps());

        List<WorkItemStep> testSteps = getSteps(entry.getValue().getSteps());
        workItemPutDto.setSteps(testSteps);

        workItemPutDto.setAttributes(workItem.getAttributes());
        workItemPutDto.setTags(workItem.getTags());
        workItemPutDto.setLinks(workItem.getLinks());
        workItemPutDto.setDuration(workItem.getDuration());

        return workItemPutDto;
    }

    private static List<WorkItemStep> getSteps(List<TestStep> steps) {
        List<WorkItemStep> workItemSteps = new ArrayList<>();
        for (TestStep step : steps) {
            WorkItemStep workItemStep = new WorkItemStep();
            workItemStep.action(step.getName());
            workItemSteps.add(workItemStep);
        }
        return workItemSteps;
    }

    private WorkItemDtoUtils() {
    }
}
