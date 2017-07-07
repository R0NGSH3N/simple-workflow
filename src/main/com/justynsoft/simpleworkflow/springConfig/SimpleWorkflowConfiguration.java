package com.justynsoft.simpleworkflow.springConfig;

import com.justynsoft.simpleworkflow.workflow.SimpleWorkflowManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleWorkflowConfiguration {

    @Bean
    public SimpleWorkflowManager SimpleWorkflowManager(){
        return new SimpleWorkflowManager();
    }
}
