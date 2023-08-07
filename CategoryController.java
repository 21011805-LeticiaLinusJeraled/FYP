/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 3:12:55 pm 
 * 
 */
package FYP;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 21011805
 *
 */
@Controller
public class CategoryController {

	@Autowired // helps to wire the categoryRepository to the controller
	private CategoryRepository categoryRepository;

	@GetMapping("/categories")
	public String viewCategories(Model model) { // model is used to send data to the view
		List<Category> listCategories = categoryRepository.findAll(); // findAll() is a API from JpaRepository that is similar to SELECT * FROM Category
		model.addAttribute("listCategories", listCategories);
		return "viewCategories";
	}

	// add
	@GetMapping("/categories/add")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "addCategory";
	}

	@PostMapping("/categories/save")
	public String saveCategory(@Valid Category category, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			return "addCategory";
		}
		categoryRepository.save(category); // save inserts the data into the category table
		return "redirect:/categories";
	}
}
