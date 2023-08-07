/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jan-20 3:16:13 pm 
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
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}

