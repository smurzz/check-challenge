package com.example.checkchallenge.config.deserializer;

import org.springframework.boot.jackson.JsonComponent;

import com.example.checkchallenge.model.UserRole;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

@JsonComponent
public class UserRoleDeserializer extends JsonDeserializer<UserRole> {

	@Override
	public UserRole deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt)
			throws java.io.IOException, JacksonException {
		 JsonNode node = p.getCodec().readTree(p);
	     return UserRole.valueOf(node.get("name").asText());
	}
}
