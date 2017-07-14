package com.justynsoft.simpleworkflow.springConfig;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowEventListener;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowExceptionHandler;
import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SimpleWorkflowConfiguration {

    @Bean
    public SimpleWorkflowManager SimpleWorkflowManager(){
        return new SimpleWorkflowManager();
    }

    @Bean
    public SimpleWorkflowEventListener getSimpleWorkflowEventListener(){
        return new SimpleWorkflowEventListener();
    }

    @Bean
    public SimpleWorkflowExceptionHandler SimpleWorkflowExceptionHandler(){
        return new SimpleWorkflowExceptionHandler();
    }

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }
}
