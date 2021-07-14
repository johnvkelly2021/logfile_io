package com.test.logfile.io;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.logfile.io.db.entity.LogEventEntity;
import com.test.logfile.io.db.repository.LogEventRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LogfileIoApplication.class, initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class LogEventRepositoryTest {

    @Autowired
    private LogEventRepository logEventRepository;

    @Test
    public void whenSavingLogEvent_thenCorrect() {
        logEventRepository.save(getSampleLogEvent());
        List<LogEventEntity> logEvents = logEventRepository.findByEventIdentifier("TEST");
        assertEquals(1, logEvents.size());
    }

    private LogEventEntity getSampleLogEvent() {
        return LogEventEntity.builder()
                             .eventIdentifier("TEST")
                             .duration(9L)
                             .alert(true)
                             .host("host")
                             .type("type")
                             .build();
    }
}
