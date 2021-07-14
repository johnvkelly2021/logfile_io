package com.test.logfile.io.service;

import java.io.File;

import com.test.logfile.io.exception.LogfileIoException;

public interface LogProcessor {

    void process(File file) throws LogfileIoException;
}
