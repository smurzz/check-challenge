package com.example.checkchallenge.config.serializer;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.example.checkchallenge.model.UserRole;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


@JsonComponent
public class UserRoleSerializer extends JsonSerializer<UserRole> {
	@Override
    public void serialize(UserRole value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", value.name());
        gen.writeEndObject();
    }
}
