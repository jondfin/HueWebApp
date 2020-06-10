package com.cobi.HueWebApp;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller 
public class HueController {
	private ArrayList<Light> lights;
	
	/* @RestController automatically includes @ResponseBody, which will convert the return String to a JSON object.
	 * Using @Controller ensures that idLogin returns a web-page.
	 */
	@GetMapping("/index")
	public String idLogin(Model model) {
		//TODO get light data from API
		model.addAttribute("hue", new Hue()); //this creates a hue object that we can bind id to when the form is submitted
		new Hue().addLights(null, 0, false); //temp
		return "index";
	}
	
	@PostMapping("/index")
	public String asd(@ModelAttribute("hue") Hue id) { //model attribute allows us to grab the hue object created from before
		System.out.println("Reached homepage");
		return "home";
	}
	
}
