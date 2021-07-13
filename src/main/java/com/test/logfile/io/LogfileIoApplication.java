package com.test.logfile.io;

import java.io.File;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.test.logfile.io.exception.LogfileIoException;
import com.test.logfile.io.service.LogProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories("com.test.logfile.io.db.repository")
public class LogfileIoApplication implements ApplicationRunner {

    public static final String LOGFILE_TXT = "logfile.txt";

    @Value("${file}")
    private String file;

    private final LogProcessor logProcessor;

    public LogfileIoApplication(LogProcessor logProcessor) {
        this.logProcessor = logProcessor;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LogfileIoApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws LogfileIoException {
        log.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));

        if ( ! args.containsOption("file")) {
            log.error("\n\n\n\t\t***** Required command line argument 'file' is missing. Please run again with:"
                     + " 'mvn spring-boot:run -Dspring-boot.run.arguments=--file=[PATH-TO-LOG-FILE]'... *****\n\n\n");
            throw new LogfileIoException("Argument missing (file)");
        }

        File theLogfile = new File(file);
        if( ! theLogfile.exists() || ! theLogfile.isFile()) {
            log.error("\n\n\n\t\t***** The file parameter passed does not exist or is not a file... *****\n\n\n");
            throw new LogfileIoException("File argument does not exist or is not a file");
        }

        if (! theLogfile.getName().equalsIgnoreCase(LOGFILE_TXT)) {
            log.error("\n\n\n\t\t***** The file is not called 'logfile.txt'... *****\n\n\n");
            throw new LogfileIoException("Filename is incorrect");
        }

        logProcessor.process(theLogfile);
    }
}
