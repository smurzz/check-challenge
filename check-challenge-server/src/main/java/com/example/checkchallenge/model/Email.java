package com.example.checkchallenge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
@ToString
public class Email implements Serializable {

    private Email email;

}