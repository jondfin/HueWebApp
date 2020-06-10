package com.cobi.HueWebApp;

import java.util.ArrayList;

public class Hue {
	private String username; //id used to connect to hub
	public ArrayList<Light> lights; //list of lights connected to hub

	
	//Getters and setters are automatically identified
	public String getUsername() {
		return this.username;
	}
	
	public void setId(String username) {
		this.username = username;
	}
	
	public void addLights(String name, int color, boolean status) {
		//lights.add(new Light(name, color, status));
		//temp
		lights = new ArrayList<>();
		lights.add(new Light("L1", 0x66ccff, true));
		lights.add(new Light("L2", 0x4dc3ff, false));
		lights.add(new Light("L3", 0x00334d, false));
		lights.add(new Light("L4", 0xffffff, true));
	}
}
