package com.example.checkchallenge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "challenges")
public class Challenge {
	
	@Id
	private String id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String html_url;

	private String description;
	
	@NotBlank
	private String created_at;
	
	private String updated_at;
	
	private boolean comleted = false;
	
	private boolean inProcessing = false;
	
	private boolean unevaluated = true;
	
	private Map<String, Integer> evaluationsInfo = new HashMap<>();
	
	private double averageScore;
	
	public double countAverageScore(int maxScore) {
		double average = !evaluationsInfo.isEmpty() ? evaluationsInfo.values().stream().mapToInt(Integer::intValue).average().orElse(0.0) : 0.0;
		return average > maxScore ? 30.0 : average;
	}
}
