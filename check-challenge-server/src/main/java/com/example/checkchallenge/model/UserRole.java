package com.example.checkchallenge.model;

import com.example.checkchallenge.config.deserializer.UserRoleDeserializer;
import com.example.checkchallenge.config.serializer.UserRoleSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonSerialize(using = UserRoleSerializer.class)
@JsonDeserialize(using = UserRoleDeserializer.class)
public enum UserRole  {    
    USER,
    ADMIN;
}
