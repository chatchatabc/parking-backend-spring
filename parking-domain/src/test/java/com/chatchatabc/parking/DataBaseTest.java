package com.chatchatabc.parking;

import com.github.database.rider.core.api.dataset.DataSet;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.FileOutputStream;

public class DataBaseTest extends TestContainersBaseTest {
    @Autowired
    private DataSource dataSource;

    @Test
    @DataSet({"db/datasets/role.xml",
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
    public void testLoadDataset() {
        Assertions.assertTrue(true);
    }

    /**
     * Database Rider and JUnit5
     * generate database.dtd to detect database changes
     */
    @Test
    public void testDTDGeneration() throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection(dataSource.getConnection());
        final IDataSet dataSet = databaseConnection.createDataSet();
        FlatDtdDataSet.write(dataSet, new FileOutputStream("database.dtd"));
        FlatDtdDataSet.write(dataSet, new FileOutputStream("src/test/resources/db/datasets/database.dtd"));
        FlatDtdDataSet.write(dataSet, new FileOutputStream("src/test/resources/db/datasets/log/database.dtd"));
        FlatDtdDataSet.write(dataSet, new FileOutputStream("src/test/resources/db/datasets/file/database.dtd"));
    }
}
