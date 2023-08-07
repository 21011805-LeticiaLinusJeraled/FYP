/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 3:17:06 pm 
 * 
 */
package FYP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 21011805
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/items")
	public String viewItem(Model model) {
		List<Item> listItems = itemRepository.findAll();
		model.addAttribute("listItems", listItems);

		// Get the authenticated user (seller)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String sellerUsername = authentication.getName();
		model.addAttribute("sellerUsername", sellerUsername);

		List<Object[]> topItems = orderItemRepository.findTop3TrendingItems();
		List<Object[]> top3Items = topItems.subList(0, Math.min(3, topItems.size()));
		model.addAttribute("top3Items", top3Items);

		return "viewItem";
	}

	@GetMapping("/items/add")
	public String addItem(Model model) {
		model.addAttribute("item", new Item());
		List<Category> catList = categoryRepository.findAll();
		List<Member> getMember = memberRepository.findAll();
		List<Member> memberList = getMember.stream().filter(member -> "ROLE_USER".equals(member.getRole()))
				.collect(Collectors.toList());
		model.addAttribute("catList", catList);
		model.addAttribute("memberList", memberList);
		return "addItem";
	}

	@PostMapping("/items/save")
	public String saveItem(@Valid Item item, BindingResult bindingResult, Model model,
			@RequestParam("itemImage") MultipartFile imgFile) {

		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			model.addAttribute("item", new Item());
			return "addItem.html";
		}

		String imageName = imgFile.getOriginalFilename();

		// Set the image name in item object
		item.setImgName(imageName);

		// Save the item obj to the db
		Item savedItem = itemRepository.save(item);

		try {
			// Preparing the directory path
			String uploadDir = "uploads/items/" + savedItem.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path: " + uploadPath);

			// Checking if the upload path exists, if not it will be created.
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Prepare path for file
			Path fileToCreatePath = uploadPath.resolve(imageName);
			System.out.println("File path: " + fileToCreatePath);

			// Copy file to the upload location
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		}

		catch (IOException io) {
			io.printStackTrace();
		}

		return "redirect:/items";
	}

	// edit
	  @GetMapping("/items/edit/{id}")
	  public String editItem(@PathVariable("id") Integer id, Model model) {
	    Item item = itemRepository.getById(id);
	    model.addAttribute("item", item);
	    List<Category> catList = categoryRepository.findAll();
	    model.addAttribute("catList", catList);
	    List<Member> getMember = memberRepository.findAll();
	    List<Member> memberList = getMember.stream().filter(member -> "ROLE_USER".equals(member.getRole()))
	        .collect(Collectors.toList());
	    model.addAttribute("memberList", memberList);
	    
	    return "editItem";
	  }

	@PostMapping("/items/edit/{id}")
	public String saveUpdatedItem(@PathVariable("id") Integer id, Item item,
			@RequestParam("itemImage") MultipartFile imgFile) {

		System.out.println("save updateItem");
		if (!imgFile.isEmpty()) {
			String imageName = imgFile.getOriginalFilename();
			System.out.println("Image name from imgFile: " + imageName);

			item.setImgName(imageName);
			Item savedItem = itemRepository.save(item);

			try {
				// Preparing the directory path
				String uploadDir = "uploads/items/" + savedItem.getId();
				Path uploadPath = Paths.get(uploadDir);
				System.out.println("Directory path: " + uploadPath);

				// Checking if the upload path exists, if not it will be created.
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				// Prepare path for file
				Path fileToCreatePath = uploadPath.resolve(imageName);
				System.out.println("File path: " + fileToCreatePath);

				// Copy file to the upload location
				Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
			}

			catch (Exception io) {
				io.printStackTrace();
			}
		}

		else { // No edit to image, save the item object as passed.
			System.out.println("Image name from item object: " + item.getImgName());
			itemRepository.save(item);
		}

		return "redirect:/items";
	}

	// delete
	@GetMapping("/items/delete/{id}")
	public String deleteItem(@PathVariable("id") Integer id) {
		itemRepository.deleteById(id);
		return "redirect:/items";
	}

	@GetMapping("/items/{id}")
	public String viewSingleItem(@PathVariable("id") Integer id, Model model) {
		Item item = itemRepository.getById(id);
		List<Review> reviews = reviewRepository.findByItem(item); // Fetch reviews for the item
		model.addAttribute("item", item);
		model.addAttribute("reviews", reviews); // Pass reviews to the view
		return "viewSingleItem";
	}

	@GetMapping(path = "/items/category/{id}")
	public String getItemsbyCategory(@PathVariable Integer id, Model model) {
		List<Item> listItems = itemRepository.findByCategory_Id(id);
		model.addAttribute("listItems", listItems);

		// Get the authenticated user (seller)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String sellerUsername = authentication.getName();
		model.addAttribute("sellerUsername", sellerUsername);

		List<Object[]> topItems = orderItemRepository.findTop3TrendingItems();
		List<Object[]> top3Items = topItems.subList(0, Math.min(3, topItems.size()));
		model.addAttribute("top3Items", top3Items);


		return "viewItem";
	}

	@GetMapping("/profilelisting")
	public String viewProfileListing(Model model, Principal principal) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		if (loggedInMember.getMember().getRole().equals("ROLE_USER")) {
			System.out.println(loggedInMember.getMember().getName());
			List<Item> ItemList = itemRepository.findByMemberId(loggedInMemberId);
			model.addAttribute("itemList", ItemList);
		} else {
			List<Item> ItemList = itemRepository.findAll();
			model.addAttribute("itemList", ItemList);
		}

		return "profilelisting";

	}

	@GetMapping("/itemRR")
	public String getItemRR(Model model) {
		// Retrieve the item object from your data source
		Optional<Item> optionalItem = itemRepository.findById(3); // Assuming you want to fetch item with ID 1
		if (optionalItem.isPresent()) {
			Item item = optionalItem.get();
			model.addAttribute("item", item);
		}
		return "itemRR";
	}

	@GetMapping("/items/report/{id}")
	public String confirmReport(@PathVariable("id") Integer id, @RequestParam("reportReason") String reportReason) {
		Item item = itemRepository.getById(id);
		item.setReportReason(reportReason);
		item.setReportCount(item.getReportCount() + 1);
		itemRepository.save(item);// Increment report count itemRepository.save(item);
		return "redirect:/items"; // Redirect to item list page 
	}
}
