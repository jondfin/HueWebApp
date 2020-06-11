package com.cobi.HueWebApp;

import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;

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

	//Contact the bridge to get the list of lights
	public void getLights() {
		final String uri = "http://" + this.ip + "/api/" + this.username + "/lights"; //URI that talks to the bridge
		System.out.println(uri);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		
		System.out.println(result); //TODO parse JSON return
	}
	
	//temp
	public void addLights(String name, int color, boolean status) {
		//lights.add(new Light(name, color, status));
		lights = new ArrayList<>();
		lights.add(new Light("L1", 0x66ccff, true));
		lights.add(new Light("L2", 0x4dc3ff, false));
		lights.add(new Light("L3", 0x00334d, false));
		lights.add(new Light("L4", 0xffffff, true));
	}
}
