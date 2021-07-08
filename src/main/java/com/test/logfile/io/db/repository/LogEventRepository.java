package com.test.logfile.io.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.logfile.io.db.entity.LogEventEntity;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {
    //Used only for repo test
    Optional<LogEventEntity> findByHost(String host);
    Long deleteByHost(String host);
}
