package com.justynsoft.simpleworkflow.template;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WorkflowWorkitemLinkerDAO extends CrudRepository<WorkflowWorkitemLinker, Long> {
}
