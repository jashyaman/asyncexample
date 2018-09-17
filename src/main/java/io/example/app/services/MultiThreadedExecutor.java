package io.example.app.services;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadedExecutor implements Callable<String>{
	
	private static final Logger log = LoggerFactory.getLogger(MultiThreadedExecutor.class);

	private ApiCallService apiCallService;

	private DBInsertService dbInsertService;
	
	private Integer offset;
	
	
	
	public MultiThreadedExecutor(ApiCallService apiCallService, DBInsertService dbInsertService, Integer offset) {
		super();
		this.apiCallService = apiCallService;
		this.dbInsertService = dbInsertService;
		this.offset = offset;
	}




	

	@Override
	public String call() throws Exception {
		log.info("request offset "+offset);
		String apiUrl = "http://jservice.io/api/categories";
		if(offset != null) {
			apiUrl+="?offset="+offset;
		} else {
			apiUrl+="?offset=24";
		}
		log.info(apiUrl);
		String responseTitle = apiCallService.callApi(apiUrl);
		log.info("api response : title -  " + responseTitle);
		
		
		String loadstatus = dbInsertService.insertRecord(responseTitle);
		 return responseTitle+" and the insert into db status : " + loadstatus;

	}

}
