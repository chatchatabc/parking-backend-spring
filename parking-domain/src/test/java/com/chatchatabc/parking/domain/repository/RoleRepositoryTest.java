package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {RoleRepository.class} )
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        // Create a test role and save it to the database
        Role role = new Role();
        role.setName("ROLE_TEST");
        roleRepository.save(role);

        // Invoke the method being tested
        Optional<Role> result = roleRepository.findByName("ROLE_TEST");

        // Assert that the role is found
        assertTrue(result.isPresent());
        assertEquals("ROLE_TEST", result.get().getName());
    }

    @Test
    public void testFindRolesIn() {
        // Create test roles and save them to the database
        Role role1 = new Role();
        role1.setName("ROLE_TEST1");
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setName("ROLE_TEST2");
        roleRepository.save(role2);

        Role role3 = new Role();
        role3.setName("ROLE_TEST3");
        roleRepository.save(role3);

        // Create a list of role names to search for
        List<String> roleNames = Arrays.asList("ROLE_TEST1", "ROLE_TEST2");

        // Invoke the method being tested
        List<Role> result = roleRepository.findRolesIn(roleNames);

        // Assert the result
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(role -> role.getName().equals("ROLE_TEST1")));
        assertTrue(result.stream().anyMatch(role -> role.getName().equals("ROLE_TEST2")));
    }
}