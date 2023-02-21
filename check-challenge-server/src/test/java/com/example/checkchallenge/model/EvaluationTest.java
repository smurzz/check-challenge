package com.example.checkchallenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class EvaluationTest {
	
	@Test
	public void testConstructor(){
		User user = new User("John", "Doe", "Engineer", "johndoe@example.com", "password");
		Challenge challenge = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, null, 0.0);
		Evaluation evaluation = new Evaluation(1, 2, 2, 1, 2, 1, 3, 1, 2, 2, 1, 1, challenge, user);
		assertNotNull(evaluation);
		assertEquals(1, evaluation.getImplementationCriteria());
		assertEquals(2, evaluation.getAlgorithmicCriteria());
		assertEquals(2, evaluation.getStructureCriteria());
		assertEquals(1, evaluation.getErrorHandlingCriteria());
		assertEquals(2, evaluation.getFormattingCriteria());
		assertEquals(1, evaluation.getCommitHistoryCriteria());
		assertEquals(3, evaluation.getReadmeCriteria());
		assertEquals(1, evaluation.getTestQualityCriteria());
		assertEquals(2, evaluation.getDesignPatternsCriteria());
		assertEquals(2, evaluation.getStylingCriteria());
		assertEquals(1, evaluation.getCiCdCriteria());
		assertEquals(1, evaluation.getDockerCriteria());
		assertEquals(challenge, evaluation.getChallenge());
		assertEquals(user, evaluation.getUser());
	}
	
	@Test
	public void testSettersAndGetters(){
		User user = new User("John", "Doe", "Engineer", "johndoe@example.com", "password");
		Challenge challenge = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, null, 0.0);
		Evaluation evaluation = new Evaluation();
		evaluation.setId("1");
		evaluation.setImplementationCriteria(1);
		evaluation.setAlgorithmicCriteria(2);
		evaluation.setStructureCriteria(2);
		evaluation.setErrorHandlingCriteria(1);
		evaluation.setFormattingCriteria(2);
		evaluation.setCommitHistoryCriteria(1);
		evaluation.setReadmeCriteria(3);
		evaluation.setTestQualityCriteria(1);
		evaluation.setDesignPatternsCriteria(2);
		evaluation.setStylingCriteria(2);
		evaluation.setCiCdCriteria(1);
		evaluation.setDockerCriteria(1);
		evaluation.setChallenge(challenge);
		evaluation.setUser(user);
		
		assertNotNull(evaluation);
		assertEquals("1", evaluation.getId());
		assertEquals(1, evaluation.getImplementationCriteria());
		assertEquals(2, evaluation.getAlgorithmicCriteria());
		assertEquals(2, evaluation.getStructureCriteria());
		assertEquals(1, evaluation.getErrorHandlingCriteria());
		assertEquals(2, evaluation.getFormattingCriteria());
		assertEquals(1, evaluation.getCommitHistoryCriteria());
		assertEquals(3, evaluation.getReadmeCriteria());
		assertEquals(1, evaluation.getTestQualityCriteria());
		assertEquals(2, evaluation.getDesignPatternsCriteria());
		assertEquals(2, evaluation.getStylingCriteria());
		assertEquals(1, evaluation.getCiCdCriteria());
		assertEquals(1, evaluation.getDockerCriteria());
		assertEquals(challenge, evaluation.getChallenge());
		assertEquals(user, evaluation.getUser());
	}
	
	@Test
	public void testToString() {		
		String result = "Evaluation(id=null, implementationCriteria=1, algorithmicCriteria=2, structureCriteria=2, errorHandlingCriteria=1, "
				+ "formattingCriteria=2, commitHistoryCriteria=1, readmeCriteria=3, testQualityCriteria=1, designPatternsCriteria=2, "
				+ "stylingCriteria=2, ciCdCriteria=1, dockerCriteria=1, challenge=null, user=null, score=19, maxScore=30)";
		
		Evaluation evaluation = new Evaluation(1, 2, 2, 1, 2, 1, 3, 1, 2, 2, 1, 1, null, null);
        assertNotNull(evaluation);
        assertEquals(result, evaluation.toString());
	}

}
