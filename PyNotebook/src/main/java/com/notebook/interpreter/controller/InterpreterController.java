package com.notebook.interpreter.controller;

import com.notebook.interpreter.model.*;
import com.notebook.interpreter.model.exception.InterpreterException;
import com.notebook.interpreter.service.InterpreterRequestParsingService;
import com.notebook.interpreter.service.InterpreterService;
import com.notebook.interpreter.service.InterpreterServiceFactory;
import com.notebook.interpreter.validation.CorrectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

@RestController
@Validated
public class InterpreterController {

    @Autowired
    private InterpreterRequestParsingService interpreterRequestParsingService;

    @Autowired
    private InterpreterServiceFactory interpreterServiceFactory;
    
	private static final Logger logger = LoggerFactory.getLogger(InterpreterController.class);


    @PostMapping("/execute")
    public ResponseEntity<InterpreterResponse> execute(@CorrectRequest @RequestBody InterpreterRequest interpreterRequest, HttpSession httpSession) throws InterpreterException {
        ExecutionRequest request = interpreterRequestParsingService.parseInterpreterRequest(interpreterRequest);
        InterpreterService interpreterService = interpreterServiceFactory.getInterpreterService(request.getLanguage());
        String sessionId = interpreterRequest.getSessionId() != null ? interpreterRequest.getSessionId() : httpSession.getId();
        request.setSessionId(sessionId);
        ExecutionResponse executionResponse = interpreterService.execute(request);
        InterpreterResponse interpreterResponse = new InterpreterResponse();
        interpreterResponse.setResponse(executionResponse.getOutput());
        interpreterResponse.setErrors(executionResponse.getErrors());
        interpreterResponse.setSessionId(sessionId);
        return ResponseEntity.ok(interpreterResponse);
    }
    
    @RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale fact is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "hello";
	}
}
