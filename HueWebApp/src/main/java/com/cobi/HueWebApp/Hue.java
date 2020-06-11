package com.cobi.HueWebApp;

import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Hue {
	private String username; //id used to connect to bridge
	private String ip; //ip of the bridge
	public ArrayList<Light> lights; //list of lights connected to bridge

	
	//Getters and setters are automatically identified
	public String getUsername() {
		return this.username;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	//Contact bridge to get lights
	//Note: this isnt name getLights because of how the framework operates
	//Getters are automatically called(? need to verify this)
	public void getLightsFromBridge() {
		final String uri = "http://" + this.ip + "/api/" + this.username + "/lights"; //URI that talks to the bridge
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
	    		lights.add(new Light(name, 0, status));
	    	}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void addLights() {
		System.out.println("Adding lights");
		lights = new ArrayList<>();
		lights.add(new Light("L1", 0, false));
		lights.add(new Light("L2", 0, false));
		lights.add(new Light("L3", 0, false));
	}
}
