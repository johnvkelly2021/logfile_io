package com.test.logfile.io;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.logfile.io.exception.LogfileIoException;

@RunWith(SpringRunner.class)
@SpringBootTest(args = "--file=src/test/resources/data/doesnotexist.txt")
@TestPropertySource(locations="classpath:application-test.properties")
public class LogfileIoTests {

    public static final String FILE_ARGUMENT_DOES_NOT_EXIST_OR_IS_NOT_A_FILE = "File argument does not exist or is not a file";

    @Test
    public void whenNotFileOrNotExists_thenException() {
        Exception exception = assertThrows(LogfileIoException.class, () -> {
            //No action required here
        });

        assertTrue(exception.getMessage().contains(FILE_ARGUMENT_DOES_NOT_EXIST_OR_IS_NOT_A_FILE));
    }
}
