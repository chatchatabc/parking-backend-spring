package com.chatchatabc.parking.db;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class JdbcTemplateTest extends TestContainersBaseTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DataSet("db/datasets/role.xml")
    public void testOperation() {
        jdbcTemplate.query("select * from role", rs -> {
            System.out.println(rs.getString("name"));
        });
    }
}
