package io.example.app.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MultiThreadedApiCallExecutor implements Callable<String> {
	
	private RestTemplate restTemplate;
	
	private String apiUrl;

	public MultiThreadedApiCallExecutor(String apiUrl,RestTemplate restTemplate) {
		this.apiUrl = apiUrl;
		this.restTemplate = restTemplate;
	}

	@Override
	public String call() throws Exception {
		HttpEntity<String> httpEntity = new HttpEntity<>(""); // << add security if required
		ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, List.class);
		
		//TODO USE OBJECT, LINKEDHASHMAP IS NOT TYPE SAFE
		return (String) ((List<LinkedHashMap<String,Object>>)response.getBody()).get(0).get("title");
		
	}
	
	

}
