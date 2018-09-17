package io.example.app.services;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessorService {
	
	@Autowired
	ApiCallService apiCallService;

	@Autowired
	DBInsertService dbInsertService;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ExecutorService executorService;
	
	private static final Logger log = LoggerFactory.getLogger(ProcessorService.class);

	public String processRequest(Integer offset) throws SQLException {
		
		
	//	log.info("request offset "+offset);
		//TODO CHANGE TO STRING BUILDER
		String apiUrl = "http://jservice.io/api/categories";
		if(offset != null) {
			apiUrl+="?offset="+offset;
		} else {
			apiUrl+="?offset=24";
		}
	//	log.info(apiUrl);
		String responseTitle = apiCallService.callApi(apiUrl);
	//	log.info("api response : title -  " + responseTitle);
		
		
		String loadstatus = dbInsertService.insertRecord(responseTitle);
		 return responseTitle+" and the insert into db status : " + loadstatus;

		 
		 
	}
	
	// Approach 2: apicall+dbinsert runs in a single thread.
	public String processRequestThreadBasedV1(Integer offset) throws Exception {
		
		// TODO THIS IS MAYBE INEFFICIENT. NEEDS TO BE AUTOWIRED
		MultiThreadedExecutor multiThreadedExecutor = new MultiThreadedExecutor(apiCallService, dbInsertService, offset);
		
		Future<String> response = executorService.submit(multiThreadedExecutor);

		return response.get();
	}

	
	public String processRequestThreadBasedV2(Integer offset) throws InterruptedException, ExecutionException {
		
	//	log.info("request offset "+offset);
		//TODO CHANGE TO STRING BUILDER
		String apiUrl = "http://jservice.io/api/categories";
		if(offset != null) {
			apiUrl+="?offset="+offset;
		} else {
			apiUrl+="?offset=24";
		}
	//	log.info(apiUrl);
		
		// TODO THIS IS MAYBE INEFFICIENT. NEEDS TO BE AUTOWIRED
		MultiThreadedApiCallExecutor mtapiExec = new MultiThreadedApiCallExecutor(apiUrl, restTemplate);
		Future<String> response = executorService.submit(mtapiExec);
		
		// TODO THIS IS  MAYBE INEFFICIENT. NEEDS TO BE AUTOWIRED
		MultiThreadedDBInsertExecutor multiThreadedDBInsertExecutor
				= new MultiThreadedDBInsertExecutor(response.get(), dataSource);
		Future<String> insertStatus = executorService.submit(multiThreadedDBInsertExecutor);
		
		//TODO CHANGE TO STRING BUILDER
		return "\nresponse title : " + response.get() + " insert status : " + insertStatus.get() + "\n\n";
		
	}
}
