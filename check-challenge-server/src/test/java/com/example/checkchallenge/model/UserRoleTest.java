package com.example.checkchallenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserRoleTest {

    @Test
    public void testUserRoleValues() {
        assertEquals("USER", UserRole.USER.name());
        assertEquals("ADMIN", UserRole.ADMIN.name());
    }

    @Test
    public void testUserRoleToString() {
        assertEquals("USER", UserRole.USER.toString());
        assertEquals("ADMIN", UserRole.ADMIN.toString());
    }

}
