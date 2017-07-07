package com.justynsoft.simpleworkflow.workflow;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SimpleWorkflowDAO extends CrudRepository<SimpleWorkflow, Long>{
}
