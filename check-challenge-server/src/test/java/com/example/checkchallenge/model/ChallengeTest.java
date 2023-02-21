package com.example.checkchallenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class ChallengeTest {
	
	@Test
	public void testConstructor(){
		Challenge challenge = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, null, 0.0);
		assertNotNull(challenge);
		assertEquals("1", challenge.getId());
		assertEquals("Challenge1", challenge.getName());
		assertEquals("https://github.com/users/user/repos/challenge1", challenge.getHtml_url());
		assertEquals("description", challenge.getDescription());
		assertEquals("2011-11-12T04:16:42Z", challenge.getCreated_at());
		assertEquals("2011-11-12T04:16:42Z", challenge.getUpdated_at());
		assertEquals(false, challenge.isComleted());
		assertEquals(false, challenge.isInProcessing());
		assertEquals(true, challenge.isUnevaluated());
		assertEquals(null, challenge.getEvaluationsInfo());
		assertEquals(0.0, challenge.getAverageScore());
	}
	
	@Test
	public void testSettersAndGetters() {
		Challenge challenge = new Challenge();
		challenge.setId("1");
		challenge.setName("Challenge1");
		challenge.setHtml_url("https://github.com/users/user/repos/challenge1");
		challenge.setDescription("description");
		challenge.setCreated_at("2011-11-12T04:16:42Z");
		challenge.setUpdated_at("2011-11-12T04:16:42Z");
		challenge.setComleted(false);
		challenge.setInProcessing(true);
		challenge.setUnevaluated(false);
		Map<String, Integer> evaluationsInfo = Map.of("evalId", 24);
		challenge.setEvaluationsInfo(evaluationsInfo);
		challenge.setAverageScore(24);
		
		assertNotNull(challenge);
		assertEquals("1", challenge.getId());
		assertEquals("Challenge1", challenge.getName());
		assertEquals("https://github.com/users/user/repos/challenge1", challenge.getHtml_url());
		assertEquals("description", challenge.getDescription());
		assertEquals("2011-11-12T04:16:42Z", challenge.getCreated_at());
		assertEquals("2011-11-12T04:16:42Z", challenge.getUpdated_at());
		assertEquals(false, challenge.isComleted());
		assertEquals(true, challenge.isInProcessing());
		assertEquals(false, challenge.isUnevaluated());
		assertEquals(evaluationsInfo, challenge.getEvaluationsInfo());
		assertEquals(24, challenge.getAverageScore());
	}
	
	@Test
	public void countAverageScoreTest() {
		int maxValue = 50;
	
		Challenge challenge = new Challenge();
		assertEquals(0.0, challenge.countAverageScore(maxValue));
		
		Challenge challenge2 = new Challenge();
		challenge2.setId("1");
		challenge2.setName("Challenge1");
		challenge2.setHtml_url("https://github.com/users/user/repos/challenge1");
		challenge2.setDescription("description");
		challenge2.setCreated_at("2011-11-12T04:16:42Z");
		challenge2.setUpdated_at("2011-11-12T04:16:42Z");
		challenge2.setComleted(false);
		challenge2.setInProcessing(true);
		challenge2.setUnevaluated(false);
		Map<String, Integer> evaluationsInfo = Map.of("evalId1", 24, "evalId2", 10);
		challenge2.setEvaluationsInfo(evaluationsInfo);
		challenge2.setAverageScore(17);
		assertEquals(challenge2.getAverageScore(), challenge2.countAverageScore(maxValue));
		
		Map<String, Integer> evaluationsInfo2 = Map.of("evalId1", 84, "evalId2", 15);
		challenge2.setEvaluationsInfo(evaluationsInfo2);
		challenge2.setAverageScore(challenge2.countAverageScore(maxValue));
		assertEquals(maxValue, challenge2.countAverageScore(maxValue));
		
	}

}
