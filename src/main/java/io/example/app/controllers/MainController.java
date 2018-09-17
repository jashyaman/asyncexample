package io.example.app.controllers;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.example.app.services.ProcessorService;

@RestController
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	
	
	@Autowired
	ProcessorService processorService;
	
	@GetMapping("/title/{offset}")
	public String callApi(@PathVariable("offset") Integer offset) throws SQLException {
		//long startTime = System.currentTimeMillis(); //TODO  BAD PRACTICE. USE INTERCEPTOR
		String response =  processorService.processRequest(offset);
		//long endTime = System.currentTimeMillis();
		//log.info("total time : " + (endTime - startTime)/1+ " ms");
		return response;
	}
	@GetMapping("/v2/title/{offset}")
	public String callApiv2(@PathVariable("offset") Integer offset) throws Exception {
		//long startTime = System.currentTimeMillis(); // TODO BAD PRACTICE. USE INTERCEPTOR
		String response =  processorService.processRequestThreadBasedV1(offset);
		//long endTime = System.currentTimeMillis();

		//log.info("total time : " + (endTime - startTime)/1+ " ms");

		return response;
	}
	@GetMapping("/v3/title/{offset}")
	public String callApiv3(@PathVariable("offset") Integer offset) throws SQLException, InterruptedException, ExecutionException {
		//long startTime = System.currentTimeMillis(); // TODO BAD PRACTICE. USE INTERCEPTOR
		String response =  processorService.processRequestThreadBasedV2(offset);
		//long endTime = System.currentTimeMillis();

		//log.info("total time : " + (endTime - startTime)/1+ " ms");

		return response;
	}
}
