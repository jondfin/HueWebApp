package com.cobi.HueWebApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.util.Pair;

public class Hue {
	private static String username; //id used to connect to bridge
	private static String ip; //ip of the bridge
	public static ArrayList<Light> lights; //list of lights connected to bridge

	
	//Getters and setters are automatically identified
	public String getUsername() {
		return username;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setUsername(String newUsername) {
		username = newUsername;
	}
	
	public void setIp(String newIp) {
		ip = newIp;
	}

	//Contact bridge to get lights
	//Note: this isnt name getLights because of how the framework operates
	//Getters are automatically called(? need to verify this)
	public void getLightsFromBridge() {
		final String uri = "http://" + ip + "/api/" + username + "/lights"; //URI that talks to the bridge
		System.out.println(uri);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		
		ObjectMapper objMapper = new ObjectMapper();
    	JsonNode rootNode;
		try {
			rootNode = objMapper.readTree(result);
			lights = new ArrayList<>();
			for(int i = 1; i <= rootNode.size(); i++) {
	    		JsonNode numNode = rootNode.path(Integer.toString(i));
	    		JsonNode state = numNode.path("state");
	    		String name = numNode.get("name").asText();
	    		boolean status = state.get("on").asBoolean();
	    		//TODO color
	    		System.out.println(name);
	    		System.out.println(status);
	    		lights.add(new Light(i, name, 0, status));
	    	}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	//Sends a PUT request to the bridge
	public void toggleLight(int number) {
		final String uri = "http://" + ip + "/api/" + username + "/lights/" + number + "/state"; //URI that talks to the bridge
		System.out.println(uri);
		System.out.println("Light " + number + " is now " + (lights.get(number-1).getStatus() ? "On" : "Off"));
		RestTemplate restTemplate = new RestTemplate();
		boolean state = lights.get(number-1).getStatus() ? true : false;
		HashMap<String, Boolean> params = new HashMap<>();
		params.put("on", state);
		HttpEntity entity = new HttpEntity(params);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		System.out.println(result.getBody());
		if(result.getBody().contains("success")) lights.get(number-1).setStatus(); //change current status of light
	}
	
}
