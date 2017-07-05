package com.justynsoft.simpleworkflow.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WorkflowWorkitemLinkerDAO extends CrudRepository<WorkflowWorkitemLinker, Long> {
}
