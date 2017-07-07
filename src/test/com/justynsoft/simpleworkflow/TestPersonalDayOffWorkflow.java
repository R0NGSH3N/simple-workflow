package com.justynsoft.simpleworkflow;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.DeleteDbFiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPersonalDayOffWorkflow {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/workflow";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private static final String PERSONAL_DAYOFF_WORKFLOW_INSERT = "INSERT INTO workflow_setup(create_datetime, name, description) " +
            "VALUES(CURRENT_DATE(), 'Peronal Day Off', 'This is workflow for employee to apply a personal day off'";


    @Before
    public void setUp() throws Exception {
        //delete test.mv
        DeleteDbFiles.execute("~", "test", true);
        //create new workflow and insert into database

    }

    private void insertWorkflowTemplate(){
        //String newWorkflowTemplate

    }

    private void cleanUpDatabase() {
        //delete existing template and workflow
        Connection connection = getDBConnection();

    }


    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
}
