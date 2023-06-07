package com.chatchatabc.parking;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@Import(TestContainersConfiguration.class)
@Testcontainers
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, columnSensing = true, schema = "public")
@DataSet(
        {
                "db/datasets/role.xml",
                "db/datasets/user.xml",
                "db/datasets/vehicle.xml",
                "db/datasets/parking_lot.xml",
                "db/datasets/rate.xml",
                "db/datasets/invoice.xml",
                "db/datasets/log/user_login_log.xml",
                "db/datasets/log/user_logout_log.xml",
                "db/datasets/file/parking_lot_image.xml",
                "db/datasets/log/user_ban_history_log.xml",
                "db/datasets/report.xml",
        })
public abstract class TestContainersBaseTest extends SpringBootBaseTest {
}
