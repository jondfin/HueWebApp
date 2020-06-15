package com.cobi.HueWebApp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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
//		System.out.println(uri);
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
	    		int brightness = state.get("bri").asInt();
	    		boolean status = state.get("on").asBoolean();
	    		float x = Float.parseFloat(state.withArray("xy").get(0).asText());
	    		float y = Float.parseFloat(state.withArray("xy").get(1).asText());
	    		Color c = convertFromCIE(x, y);
	    		System.out.println(x + "," + y + " = " + c.toString() + " = " + convertToCIE(c).toString());
//	    		System.out.println(name);
//	    		System.out.println(status);
	    		lights.add(new Light(i, name, c, brightness, status));
	    	}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	//Sends a PUT request to the bridge
	public void toggleLight(int id) {
		final String uri = "http://" + ip + "/api/" + username + "/lights/" + id + "/state"; //URI that talks to the bridge
//		System.out.println(uri);
		RestTemplate restTemplate = new RestTemplate();
		boolean state = lights.get(id-1).getStatus() ? false : true;
		HashMap<String, Boolean> params = new HashMap<>();
		params.put("on", state);
		HttpEntity<HashMap<String, Boolean>> entity = new HttpEntity<>(params);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class); //exchange is able to use any HTTP method. See https://stackoverflow.com/questions/48741238/resttemplate-getforobject-exchange-entity-is-there-any-pros-and-cons-for/48743294 for more detail
		System.out.println(result.getBody());
		if(result.getBody().contains("success")) lights.get(id-1).setStatus(); //change current status of light
		System.out.println("Light " + id + " is now " + (lights.get(id-1).getStatus() ? "On" : "Off"));
	}
	
	public void changeColor(int id, int color) {
		//Convert Integer to RGB
		Color c = Color.decode(Integer.toString(color));
		//Convert to compatbile CIE for bridge
		Pair<Float, Float> p = convertToCIE(c);
		float[] vals = {p.getKey(), p.getValue()}; //x and y
		//Send to bridge
		final String uri = "http://" + ip + "/api/" + username + "/lights/" + id + "/state"; //URI that talks to the bridge
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, float[]> params = new HashMap<>();
		params.put("xy", vals);
		HttpEntity<HashMap<String, float[]>> entity = new HttpEntity<>(params);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		if(result.getBody().contains("success")) lights.get(id-1).setColor(convertFromCIE(vals[0], vals[1]));
		System.out.println("Light " + id + " is now " + (lights.get(id-1).getColor().toString()));
	}
	
	//Hue uses CIE color space, need to convert to int value
	//x and y are between 0 and 1
	//Convert to sRGB
	//Reference: https://en.wikipedia.org/wiki/SRGB
	//Note: Hue uses Wide RGB D65 which has a wider spectrum than sRGB
	private Color convertFromCIE(float x, float y) {
		float z = 1.0f - x - y;
		float Y = y; //brightness? need to find right value for this
		float X = (Y/y) * x;
		float Z = (Y/y) * z;
		//Converting to RGB
		float r = X * 3.24096994f - Y * 1.5378318f - Z * 0.49861076f;
		float g = -X * 0.96924364f + Y * 1.8759675f + Z * 0.04155506f;
		float b =  X * 0.05563008f - Y * 0.20397696f + Z * 1.05697151f;
		//Applying gamma correction 
		r = gammaCorrection(r);
		System.out.println(r);
		g = gammaCorrection(g);
		System.out.println(g);
		b = gammaCorrection(b);
		System.out.println(b);
		//temp fix until formula is corrected
		if(r > 1) r = 1.0f;
		if(g > 1) g = 1.0f;
		if(b > 1) b = 1.0f;
		Color color = new Color(r, g, b);
		return color;
	}
	private float gammaCorrection(float u) {
		if(u <= 0.0031308f) return 12.92f*u;
		else return (float) (1.055f*Math.pow(u, 1.0f/2.4f) - 0.055f);
	}
	
	private float reverseGammaCorrection(float u) {
		if(u <= 0.04045) return u/12.92f;
		else return (float) Math.pow(((u + 0.055f)/1.055f), 2.4f);
	}
	
	//Convert to CIE
	//x and y are between 0 and 1
	private Pair<Float, Float> convertToCIE(Color color){
		//Apply reverse gamma correction
		//Java colors are Integers so divide by 255 to get (close) rgb values
		float r = reverseGammaCorrection(color.getRed()/255.0f);
		float g = reverseGammaCorrection(color.getGreen()/255.0f);
		float b = reverseGammaCorrection(color.getBlue()/255.0f);
		//RGB to XYZ using sRGB values
		float X = r * 0.41239080f + g * 0.35758434f + b * 0.18048079f;
		float Y = r * 0.21263901f + g * 0.71516868f + b * 0.07219232f;
		float Z = r * 0.01933082f + g * 0.11919478f + b * 0.95053215f;
		//XYZ to xy
		float x = X / (X + Y + Z);
		float y = Y / (X + Y + Z);
		return new Pair<>(x, y);
	}
	
	//Change brightness of a light
	//Range is 1-254
	public void changeBrightness(int id, int brightness) {
		final String uri = "http://" + ip + "/api/" + username + "/lights/" + id + "/state"; //URI that talks to the bridge
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, Integer> params = new HashMap<>();
		params.put("bri", brightness);
		HttpEntity<HashMap<String, Integer>> entity = new HttpEntity<>(params);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		if(result.getBody().contains("success")) lights.get(id-1).setBrightness(brightness);
		System.out.println("Light " + id + " brightness is now " + (lights.get(id-1).getBrightness()));
	}
	
	//Resets light to the 'read' scene
	public void resetLight(int id) {
		final String uri = "http://" + ip + "/api/" + username + "/lights/" + id + "/state"; //URI that talks to the bridge
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, Object> params = new HashMap<>();
		params.put("bri", 254);
		float[] arr = {0.4452f, 0.4068f};
		params.put("xy", arr);
		HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(params);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		if(result.getBody().contains("success")) {
			lights.get(id-1).setBrightness(254);
			lights.get(id-1).setColor(convertFromCIE(0.4452f, 0.4068f));
		}
		System.out.println("Light " + id + " brightness is now " + (lights.get(id-1).getBrightness()));
	}
	
}
