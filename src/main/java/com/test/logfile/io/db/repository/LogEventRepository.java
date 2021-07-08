package com.test.logfile.io.db.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.logfile.io.db.entity.LogEvent;

public interface LogEventRepository extends CrudRepository<LogEvent, Long> {

}
