package com.test.logfile.io.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.logfile.io.constants.LogEventState;
import com.test.logfile.io.db.entity.LogEventEntity;
import com.test.logfile.io.db.repository.LogEventRepository;
import com.test.logfile.io.exception.LogfileIoException;
import com.test.logfile.io.mapping.LogEventMapper;
import com.test.logfile.io.model.LogEventModel;
import com.test.logfile.io.service.LogProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogProcessorImpl implements LogProcessor {

    private final LogEventRepository logEventRepository;

    private final LogEventMapper logEventMapper;

    public LogProcessorImpl(LogEventRepository logEventRepository, LogEventMapper logEventMapper) {
        this.logEventRepository = logEventRepository;
        this.logEventMapper = logEventMapper;
    }

    public void process(File file) throws LogfileIoException {
        ConcurrentMap<String, LogEventModel> workingStorage = new ConcurrentHashMap<>();
        LineIterator lineIterator = null;

        try {
            lineIterator = FileUtils.lineIterator(file, "UTF-8");
        } catch (IOException ioe) {
            log.error("Error obtaining/initializing the (Commons I/O) line iterator", ioe);
        }

        if (lineIterator != null) {
            while (lineIterator.hasNext()) {
                ObjectMapper jsonMapper = new ObjectMapper();
                try {
                    LogEventModel currentEvent = jsonMapper.readValue(lineIterator.nextLine(), LogEventModel.class);
                    LogEventEntity entity = logEventMapper.toEntity(currentEvent);
                    FindMatchingEventOrUpdateStorage(workingStorage, currentEvent, entity);
                } catch (JsonProcessingException jpe) {
                    throw new LogfileIoException("Error deserializing a log event line", jpe);
                }
            }
        }

        if (workingStorage.size() > 0) {
            if (log.isDebugEnabled()) {
                workingStorage.forEach((key, value) -> log.debug(key + ":" + value));
            }
            throw new LogfileIoException("Working storage map has unmatched (orphaned) log events");
        }
    }

    /**
     * Checks the map for matching id. If found, then calculate duration/alert based on start/finish order and then remove from map
     * If not found, add to map.
     */
    private void FindMatchingEventOrUpdateStorage(ConcurrentMap<String, LogEventModel> workingStorage, LogEventModel currentEvent,
                                                  LogEventEntity entity) {
        LogEventModel foundEvent = workingStorage.get(currentEvent.getId());
        if (foundEvent != null) {
            if ((LogEventState.STARTED.compareTo(LogEventState.valueOf(foundEvent.getState())) == 0) && (
                    LogEventState.FINISHED.compareTo(LogEventState.valueOf(currentEvent.getState())) == 0)) {
                calculateDuration(entity, foundEvent, currentEvent);
            } else {
                calculateDuration(entity, currentEvent, foundEvent);
            }
            workingStorage.remove(currentEvent.getId());
        } else {
            workingStorage.put(currentEvent.getId(), currentEvent);
        }
    }

    /**
     * Calculate duration/alert and update the database.
     */
    private void calculateDuration(LogEventEntity entity, LogEventModel startEvent, LogEventModel finishEvent) {
        Long finishTime = Long.valueOf(finishEvent.getTimestamp());
        Long startTime = Long.valueOf(startEvent.getTimestamp());
        long duration = finishTime - startTime;
        entity.setDuration(duration);
        if (duration > 4) {
            log.info("Alert for event id: " + startEvent.getId() + " - duration = " + duration);
            entity.setAlert(true);
        } else {
            entity.setAlert(false);
        }
        logEventRepository.save(entity);
    }

}
