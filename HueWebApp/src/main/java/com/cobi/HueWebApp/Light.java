package com.cobi.HueWebApp;

public class Light {
	
	private String name;
	private int color; //hex value
	private boolean status;
	
	public Light(String name, int color, boolean status) {
		this.name = name;
		this.color = color;
		this.status = status;
	}
	
	public String getName() {
		return this.name;
	}

	public int getColor() {
		return this.color;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public void setColor(int value) {
		this.color = value;
	}
	
	public void setStatus() {
		this.status = !status;
	}
}
