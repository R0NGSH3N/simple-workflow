package com.justynsoft.simpleworkflow.workflow;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SimpleWorkitemDAO extends CrudRepository<SimpleWorkitem, Long>{

    List<SimpleWorkitem> findByWorkflowId(Long workflowId);
}
