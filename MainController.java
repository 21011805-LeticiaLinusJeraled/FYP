/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-04 10:00:35 pm 
 * 
 */
package FYP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 21011805
 *
 */
@Controller
public class MainController {

	@GetMapping("/techcentral")
	public String viewOS() {
		return "index";
	}

	@GetMapping("/about")
	public String viewAbout() {
		return "about";
	}
	
	@GetMapping("/faq")
	public String viewFAQ() {
		return "faq";
	}

	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/spin")
	public String spin() {
		return "spin";
	}
	
}
