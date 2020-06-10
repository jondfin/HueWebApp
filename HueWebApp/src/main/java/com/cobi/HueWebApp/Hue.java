package com.cobi.HueWebApp;

import java.util.ArrayList;

public class Hue {
	private String id; //id used to connect to hub
	private ArrayList<Light> lights; //list of lights connected to hub

	public String getId() {
		return this.id;
	}
	
	public void addLights(String name, int color, boolean status) {
		//lights.add(new Light(name, color, status));
		//temp
		System.out.println("adding lights");
		lights = new ArrayList<>();
		lights.add(new Light("L1", 0x66ccff, true));
		lights.add(new Light("L2", 0x4dc3ff, false));
		lights.add(new Light("L3", 0x00334d, false));
		lights.add(new Light("L4", 0xffffff, true));
	}
}
