package com.example.checkchallenge.config.serializer;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class GrantedAuthoritySerializer extends JsonSerializer<GrantedAuthority> {
	@Override
    public void serialize(GrantedAuthority value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getAuthority());
    }
}
