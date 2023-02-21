package com.example.checkchallenge.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRequest {
	
	private String name;

	private String description;
	
	private boolean comleted;
	
	private boolean inProcessing;
	
	private boolean unevaluated;

}
