package com.test.logfile.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.logfile.io.db.entity.LogEventEntity;
import com.test.logfile.io.db.repository.LogEventRepository;
import com.test.logfile.io.exception.LogfileIoException;
import com.test.logfile.io.service.LogProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LogfileIoApplication.class, initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class LogProcessorE2ETest {

    public static final String DATA_CORRECT_LOGFILE_TXT = "data/correct-logfile.txt";
    public static final String BROKEN_JSON_LOGFILE_TXT = "data/bad-json-logfile.txt";
    public static final String ORPHAN_EVENT_LOGFILE_TXT = "data/orphan-event-logfile.txt";

    @Autowired
    LogProcessor logProcessor;

    @Autowired
    private LogEventRepository logEventRepository;

    @BeforeEach
    public void setup() {
        logEventRepository.deleteAll();
    }

    @Test
    public void whenProcessingLogFile_thenDbDataCorrect() throws LogfileIoException{
        //Given
        File correctLogFile = getFile(DATA_CORRECT_LOGFILE_TXT);

        //When
        logProcessor.process(correctLogFile);

        //Then
        assertEquals(3, logEventRepository.count());
        List<LogEventEntity> logEvents = logEventRepository.findByEventIdentifier("scsmbstgra");
        assertEquals(1, logEvents.size());
        LogEventEntity entity = logEvents.get(0);
        assertEquals(new Long(5L), entity.getDuration());
    }

    @Test
    public void whenProcessingBadJson_thenExpectException() {
        //Given
        File brokenJsonLogFile = getFile(BROKEN_JSON_LOGFILE_TXT);

        //When
        LogfileIoException lie = assertThrows(LogfileIoException.class, () -> {
            logProcessor.process(brokenJsonLogFile);
        });

        //Then
        String expectedMessage = "Error deserializing a log event line";
        String actualMessage = lie.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenProcessingOrphanEvent_thenExpectException() {
        //Given
        File orphanEventLogFile = getFile(ORPHAN_EVENT_LOGFILE_TXT);

        //When
        LogfileIoException lie = assertThrows(LogfileIoException.class, () -> {
            logProcessor.process(orphanEventLogFile);
        });

        //Then
        String expectedMessage = "Working storage map has unmatched (orphaned) log events";
        String actualMessage = lie.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private File getFile(String fileName) {
        File logFile = null;
        try {
            logFile = new ClassPathResource(fileName).getFile();
        } catch (IOException ioe) {
            log.error("Error accessing the test file: " + fileName, ioe);
        }
        return logFile;
    }
}
