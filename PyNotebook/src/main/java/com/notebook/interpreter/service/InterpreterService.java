package com.notebook.interpreter.service;

import com.notebook.interpreter.model.ExecutionRequest;
import com.notebook.interpreter.model.ExecutionResponse;
import com.notebook.interpreter.model.Interpreter;
import com.notebook.interpreter.model.exception.InterpreterException;

/**
 * Interpreters Service Interface
 *
 */
public interface InterpreterService {

    /**
     * Get Interpreter from Service
     * @return
     */
    Interpreter getInterpreterLanguage();

    /**
     * Interpret users requests
     * @param request user request with code to be interpreted
     * @return
     * @throws InterpreterException
     */
    ExecutionResponse execute(ExecutionRequest request) throws InterpreterException;
}
