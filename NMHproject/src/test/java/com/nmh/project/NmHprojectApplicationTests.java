package com.nmh.project;

import com.nmh.project.util.DatabaseConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class NmHprojectApplicationTests {
    //NOTE: AUTHORS OF THIS CLASS: JACOB

    @Test
    void setup() throws SQLException { //fails if throws Exception, so no need to handle it futher.

        Connection connection = DatabaseConnectionManager.getDatabaseConnection();
        Assertions.assertTrue(connection.isValid(10)); //fails if failed to connect within 10 sec.
    }
}
