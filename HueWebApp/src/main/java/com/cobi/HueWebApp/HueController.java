package com.cobi.HueWebApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
public class HueController {
	/* @RestController automatically includes @ResponseBody, which will convert the return String to a JSON object.
	 * Using @Controller ensures that idLogin returns a web-page.
	 */
	@GetMapping("/")
	public String defaultLogin(Model model) {
		model.addAttribute("hue", new Hue()); //this creates a hue object that we can bind username to when the form is submitted
		return "index";
	}
	
	@GetMapping("/index")
	public String LoginFromIndex(Model model) {
		model.addAttribute("hue", new Hue());
		return "index";
	}
	
	@PostMapping("/index")
	public String loginSubmit(@ModelAttribute("hue") Hue hue) { //model attribute allows us to grab the hue object created from before
		System.out.println("Hue username: " + hue.getUsername());
		hue.getLightsFromBridge();
		return "home"; //render home.html
	}
	
	@PostMapping(value="/home", params="action=toggle")
	public String toggleLight(@ModelAttribute("hue") Hue hue, @RequestParam("id") int id) {
		System.out.println("Toggling light " + id);
		hue.toggleLight(id);
		return "home";
	}
	
	@PostMapping(value="/home", params="action=color")
	public String changeColor(@ModelAttribute("hue") Hue hue, @RequestParam("id") int id, @RequestParam("color") int color){
		System.out.println("Changing light " + id + " to color " + color);
		System.out.println("Current color: " + color);
		hue.changeColor(id, color);
		return "home";
	}
	
	@PostMapping(value="/home", params="action=brightness")
	public String changeBrightness(@ModelAttribute("hue") Hue hue, @RequestParam("id") int id, @RequestParam("brightness") int brightness) {
		System.out.println("Changing light " + id + " brightness to " + brightness);
		hue.changeBrightness(id, brightness);
		return "home";
	}
	
	@PostMapping(value="/home", params="action=reset")
	public String resetLight(@ModelAttribute("hue") Hue hue, @RequestParam("id") int id) {
		System.out.println("Resetting light " + id);
		hue.resetLight(id);
		return "home";
	}
}
