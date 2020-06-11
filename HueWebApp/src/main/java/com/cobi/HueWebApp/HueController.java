package com.cobi.HueWebApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller 
public class HueController {
	/* @RestController automatically includes @ResponseBody, which will convert the return String to a JSON object.
	 * Using @Controller ensures that idLogin returns a web-page.
	 */
	@GetMapping("/")
	public String idLogin(Model model) {
		model.addAttribute("hue", new Hue()); //this creates a hue object that we can bind id to when the form is submitted
		return "index";
	}
	
	@GetMapping("/index")
	public String idLoginFromIndex(Model model) {
		model.addAttribute("hue", new Hue());
		return "index";
	}
	
	@PostMapping("/index")
	public String idSubmit(@ModelAttribute("hue") Hue hue) { //model attribute allows us to grab the hue object created from before
		System.out.println("Hue id: " + hue.getUsername());
		//TODO add lights from id
		hue.getLights();
//		hue.addLights(null, 0, false);
//		for(Light l : hue.lights) {
//			System.out.println(l.getName());
//		}
		return "home";
	}
	
}
