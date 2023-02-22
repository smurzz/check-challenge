package com.example.checkchallenge.controller.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class EvaluationRequestTest {

    @Test
    void testEvaluationRequestBuilder() {
    	EvaluationRequest evaluationRequest = EvaluationRequest.builder()
                .implementationCriteria("1")
                .algorithmicCriteria("1")
                .structureCriteria("1")
                .errorHandlingCriteria("1")
                .formattingCriteria("1")
                .ciCdCriteria("1")
                .commitHistoryCriteria("1")
                .designPatternsCriteria("1")
                .dockerCriteria("1")
                .readmeCriteria("1")
                .stylingCriteria("1")
                .testQualityCriteria("1")
                .userEmail("8798734923")
                .challengeId("345464576")
                .build();
    	
    	assertNotNull(evaluationRequest);
        assertEquals("1", evaluationRequest.getImplementationCriteria());
        assertEquals("1", evaluationRequest.getAlgorithmicCriteria());
        assertEquals("1", evaluationRequest.getStructureCriteria());
        assertEquals("1", evaluationRequest.getErrorHandlingCriteria());
        assertEquals("1", evaluationRequest.getFormattingCriteria());
        assertEquals("1", evaluationRequest.getCiCdCriteria());
        assertEquals("1", evaluationRequest.getCommitHistoryCriteria());
        assertEquals("1", evaluationRequest.getDesignPatternsCriteria());
        assertEquals("1", evaluationRequest.getDockerCriteria());
        assertEquals("1", evaluationRequest.getReadmeCriteria());
        assertEquals("1", evaluationRequest.getStylingCriteria());
        assertEquals("1", evaluationRequest.getTestQualityCriteria());
        assertEquals("8798734923", evaluationRequest.getUserEmail());
        assertEquals("345464576", evaluationRequest.getChallengeId());
    }
    
    @Test
    void testEvaluationRequestNullConstuctor() {
    	EvaluationRequest evaluationRequest = new EvaluationRequest();
    	evaluationRequest.setImplementationCriteria("1");
    	evaluationRequest.setAlgorithmicCriteria("1");
    	evaluationRequest.setStructureCriteria("1");
    	evaluationRequest.setErrorHandlingCriteria("1");
    	evaluationRequest.setFormattingCriteria("1");
    	evaluationRequest.setCiCdCriteria("1");
    	evaluationRequest.setCommitHistoryCriteria("1");
    	evaluationRequest.setDesignPatternsCriteria("1");
    	evaluationRequest.setDockerCriteria("1");
    	evaluationRequest.setReadmeCriteria("1");
    	evaluationRequest.setStylingCriteria("1");
    	evaluationRequest.setTestQualityCriteria("1");
    	evaluationRequest.setUserEmail("8798734923");
    	evaluationRequest.setChallengeId("345464576");
    	
    	assertNotNull(evaluationRequest);
        assertEquals("1", evaluationRequest.getImplementationCriteria());
        assertEquals("1", evaluationRequest.getAlgorithmicCriteria());
        assertEquals("1", evaluationRequest.getStructureCriteria());
        assertEquals("1", evaluationRequest.getErrorHandlingCriteria());
        assertEquals("1", evaluationRequest.getFormattingCriteria());
        assertEquals("1", evaluationRequest.getCiCdCriteria());
        assertEquals("1", evaluationRequest.getCommitHistoryCriteria());
        assertEquals("1", evaluationRequest.getDesignPatternsCriteria());
        assertEquals("1", evaluationRequest.getDockerCriteria());
        assertEquals("1", evaluationRequest.getReadmeCriteria());
        assertEquals("1", evaluationRequest.getStylingCriteria());
        assertEquals("1", evaluationRequest.getTestQualityCriteria());
        assertEquals("8798734923", evaluationRequest.getUserEmail());
        assertEquals("345464576", evaluationRequest.getChallengeId());
       
    }
}
