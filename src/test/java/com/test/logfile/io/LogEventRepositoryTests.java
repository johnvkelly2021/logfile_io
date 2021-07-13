package com.test.logfile.io;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.logfile.io.db.entity.LogEventEntity;
import com.test.logfile.io.db.repository.LogEventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(args = "--file=src/test/resources/data/logfile.txt")
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
public class LogEventRepositoryTests {

   public static final String UNIQUE_HOST = "UNIQUE-HOST-AAAfgh124Rr";

    @Autowired
    private LogEventRepository logEventRepository;

    @Test
    public void whenFindingLogEventByHost_thenCorrect() {
        //Delete the row if previously added
        logEventRepository.deleteByHost(UNIQUE_HOST);

        logEventRepository.save(getSampleLogEvent());
        Optional<LogEventEntity> logEventWrapper = logEventRepository.findByHost(UNIQUE_HOST);
        assertNotNull(logEventWrapper);
        assertNotNull(logEventWrapper.get());
    }

    private LogEventEntity getSampleLogEvent() {
        return LogEventEntity.builder().duration(5L).alert(true).host(UNIQUE_HOST).type("type").build();
    }

}
