package com.example.checkchallenge.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EvaluationRequest {
	
	private String implementationCriteria;
	
	private String algorithmicCriteria;
	
	private String structureCriteria;
	
	private String errorHandlingCriteria;
	
	private String formattingCriteria;
	
	private String commitHistoryCriteria;

	private String readmeCriteria;
	
	private String testQualityCriteria;
	
	private String designPatternsCriteria;
	
	private String stylingCriteria;
	
	private String ciCdCriteria;
	
	private String dockerCriteria;

	private String challengeId;

	private String userEmail;

}
