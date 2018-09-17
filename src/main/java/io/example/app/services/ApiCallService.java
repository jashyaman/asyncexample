package io.example.app.services;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiCallService {
	
	@Autowired
	RestTemplate restTemplate;
	
	public String callApi(String url) {
		HttpEntity<String> httpEntity = new HttpEntity<>("");
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, List.class);
		//TODO BAD PRACTICE. MAP TO CLASS. NOT LINKEDHASHMAP. NOT TYPESAFE
		return (String) ((List<LinkedHashMap<String,Object>>)response.getBody()).get(0).get("title");
		
	}

}
