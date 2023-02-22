package com.example.checkchallenge.controller.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ChallengeRequestTest {

	@Test
    void testChallengeRequestBuilder() {
		ChallengeRequest challengeRequest = ChallengeRequest.builder()
                .name("blabla")
                .description("")
                .inProcessing(false)
                .unevaluated(true)
                .comleted(false)
                .build();
    	
    	assertNotNull(challengeRequest);
        assertEquals("blabla", challengeRequest.getName());
        assertEquals("", challengeRequest.getDescription());
        assertEquals(false, challengeRequest.isInProcessing());
        assertEquals(true, challengeRequest.isUnevaluated());
        assertEquals(false, challengeRequest.isComleted());
    }
    
    @Test
    void testChallengeRequestNullConstuctor() {
    	ChallengeRequest challengeRequest = new ChallengeRequest();
    	challengeRequest.setName("blabla");
    	challengeRequest.setDescription("");
    	challengeRequest.setInProcessing(false);
    	challengeRequest.setUnevaluated(true);
    	challengeRequest.setComleted(false);
    	
    	assertNotNull(challengeRequest);
        assertEquals("blabla", challengeRequest.getName());
        assertEquals("", challengeRequest.getDescription());
        assertEquals(false, challengeRequest.isInProcessing());
        assertEquals(true, challengeRequest.isUnevaluated());
        assertEquals(false, challengeRequest.isComleted());
       
    }
}
