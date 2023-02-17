package com.example.checkchallenge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "evaluations")
public class Evaluation {

	@Id
	private String id;
	
	// Mandatory criteria (20)
	
	@Max(5)
	private int implementationCriteria;
	
	@Max(10)
	private int algorithmicCriteria;
	
	@Max(2)
	private int structureCriteria;
	
	@Max(2)
	private int errorHandlingCriteria;
	
	@Max(1)
	private int formattingCriteria;
	
	// Advanced criteria (10)
	
	@Max(2)
	private int commitHistoryCriteria;
	
	@Max(3)
	private int readmeCriteria;
	
	@Max(3)
	private int testQualityCriteria;
	
	@Max(2)
	private int designPatternsCriteria;
	
	// Bonus (5)
	
	@Max(1)
	private int stylingCriteria;
	
	@Max(2)
	private int ciCdCriteria;
	
	@Max(2)
	private int dockerCriteria;
	
	private Challenge challenge;
	
	private User user;
	
	private int score;
	
	private int maxScore = 30;

	public Evaluation (int implementationCriteria, int algorithmicCriteria, int structureCriteria, int errorHandlingCriteria, int formattingCriteria, 
			int commitHistoryCriteria, int readmeCriteria, int testQualityCriteria, int designPatternsCriteria, int stylingCriteria, int ciCdCriteria,
			int dockerCriteria, Challenge challenge, User user) {
		this.implementationCriteria = implementationCriteria;
		this.algorithmicCriteria = algorithmicCriteria;
		this.structureCriteria = structureCriteria;
		this.errorHandlingCriteria = errorHandlingCriteria;
		this.formattingCriteria = formattingCriteria;
		this.commitHistoryCriteria = commitHistoryCriteria;
		this.readmeCriteria = readmeCriteria;
		this.testQualityCriteria = testQualityCriteria;
		this.designPatternsCriteria = designPatternsCriteria;
		this.stylingCriteria = stylingCriteria;
		this.ciCdCriteria = ciCdCriteria;
		this.dockerCriteria = dockerCriteria;
		this.challenge = challenge;
		this.user = user;
		this.score = implementationCriteria + algorithmicCriteria + structureCriteria + errorHandlingCriteria + formattingCriteria + commitHistoryCriteria + 
				readmeCriteria + testQualityCriteria + designPatternsCriteria + stylingCriteria + ciCdCriteria + dockerCriteria;
	}
}
