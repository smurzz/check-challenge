package com.example.checkchallenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserTest {
	
	@Test
    public void testConstructor1() {
        User user = new User("John", "Doe", "Engineer", "johndoe@example.com", "password");
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Engineer", user.getPosition());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(true, user.isActive());
        assertEquals(null, user.getId());
        assertEquals(null, user.getRoles());
    }
	
	@Test
    public void testConstructor2() {
        User user = new User("Jeffrey", "Smith", "Developer", "jeffreysmith@example.com", "123", false, List.of(UserRole.ADMIN, UserRole.USER));
        assertNotNull(user);
        assertEquals("Jeffrey", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("Developer", user.getPosition());
        assertEquals("jeffreysmith@example.com", user.getEmail());
        assertEquals("123", user.getPassword());
        assertEquals(false, user.isActive());
        assertEquals(null, user.getId());
        assertEquals(List.of(UserRole.ADMIN, UserRole.USER), user.getRoles());
    }
	
	@Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setId("1");
        user.setFirstName("Kate");
        user.setLastName("Joe");
        user.setPosition("software developer");
        user.setEmail("katejoe@example.com");
        user.setPassword("1234");
        user.setActive(true);
        user.setRoles(List.of(UserRole.ADMIN, UserRole.USER));
        
        assertEquals("1", user.getId());
        assertEquals("Kate", user.getFirstName());
        assertEquals("Joe", user.getLastName());
        assertEquals("software developer", user.getPosition());
        assertEquals("katejoe@example.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals(true, user.isActive());
        assertEquals(List.of(UserRole.ADMIN, UserRole.USER), user.getRoles());
    }
	
	@Test
    public void testGetAuthorities() {
        User user = new User();
        List<UserRole> roles = List.of(UserRole.ADMIN, UserRole.USER);
        user.setRoles(roles);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        assertEquals(new SimpleGrantedAuthority("ADMIN"), authorities.toArray()[0]);
        assertEquals(new SimpleGrantedAuthority("USER"), authorities.toArray()[1]);
    }

}
