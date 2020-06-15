package com.cobi.HueWebApp;

import java.awt.Color;

public class Light {
	
	private int id;
	private String name;
	private Color color; //rgb
	private int brightness;
	private boolean status;
	
	public Light(int id, String name, Color color, int brightness, boolean status) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.brightness = brightness;
		this.status = status;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}
	
	public int getBrightness() {
		return this.brightness;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public void setColor(Color value) {
		this.color = value;
	}
	
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	
	public void setStatus() {
		this.status = !status;
	}
}
