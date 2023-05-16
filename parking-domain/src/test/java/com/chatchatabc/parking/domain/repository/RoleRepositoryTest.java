package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Role;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataSet("db/datasets/role.xml")
public class RoleRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName_NameIsFound() {
        assertThat(roleRepository.findByName("ROLE_ADMIN")).isPresent();
    }

    @Test
    public void testFindRolesIn() {
        // Invoke the method being tested
        List<Role> result = roleRepository.findRolesIn(Arrays.asList("ROLE_ADMIN", "ROLE_MEMBER"));
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Role::getName).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_MEMBER");
    }
}