package com.justynsoft.simpleworkflow.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SimpleWorkitemRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_WORKITEM =
            "INSERT INTO workitem(workflow_id, workitem_template_id, status, create_datetime, lastupdate_datetime" +
                    " VALUES(?,?,?,?)";
    public void insert(SimpleWorkitem simpleWorkitem){
        jdbcTemplate.update( INSERT_WORKITEM, new Object[]{
                simpleWorkitem.getWorkflowId(),
                simpleWorkitem.getWorkitemTemplate(),
                simpleWorkitem.getStatus(),
                simpleWorkitem.getCreateDate() == null ? new Date() : simpleWorkitem.getCreateDate(),
                simpleWorkitem.getLastUpdateDateTime() == null ? new Date(): simpleWorkitem.getLastUpdateDateTime()
                }
        );
    }
}
