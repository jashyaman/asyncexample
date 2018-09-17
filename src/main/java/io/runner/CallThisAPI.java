package io.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class CallThisAPI {

	private static final Logger log = LoggerFactory.getLogger(CallThisAPI.class);

	public static void main(String[] args) {
		
		
		
		RestTemplate restTemplate = new RestTemplate();

		long totalStartTime = System.currentTimeMillis();
		long periodicTime = 0;
		for (int offset = 10; offset < 20000; offset++) {
			long startTime = System.currentTimeMillis();
			String apiUrl = "http://localhost:8081/v3/title/{offset}";
			
			restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<String>(""), 
					String.class, offset);
			long endTime = System.currentTimeMillis();
			periodicTime+= (endTime - startTime);
			if(offset%100 == 0)
			log.info("avg time per execution for "+offset/100+"00 offsets in milliseconds " +  periodicTime/ offset+ " ms" );

		}
		long totalEndTime = System.currentTimeMillis();
		
		log.info("total time in minutes " + ((totalEndTime - totalStartTime) / 60000) + 
				" mins " + (((totalEndTime - totalStartTime) / 100) - ((totalEndTime - totalStartTime) / 60000) )  + "s" );
		
		//200 records---------------------
		//Approach 1
		//dbp 10 11s
		//dbp 20 9~10s
		//dbp 25 9~10s
		
		
		//Approach 2
		//cps mps dbp 10 50 10 : 9~10s
		//cps mps dbp 10 50 20 : 9~10s
		//cps mps dbp 50 100 20 : 9~10s
		//cps mps dbp 50 100 25 : 9s
		
		//Approach 3
		//cps mps dbp 10 50 10 : 10s

		
		//20000 records
		
		//Approach 3
		//cps mps dbp 10 50 10 : too long.
		//cps mps dbp 500 1000 10 : avg time reduced.
		//cps mps dbp 500 1000 100 : avg time increased
		// //cps mps dbp 500 1000 100 : (removed logging statements) no difference
		
	}
}
