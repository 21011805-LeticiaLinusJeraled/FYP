/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jan-20 12:39:42 pm 
 * 
 */
package FYP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;
import java.io.File;

/**
 * @author 21011805
 *
 */
@Controller
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@GetMapping("/members")
	public String viewMembers(Model model) {
		List<Member> allMembers = memberRepository.findAll();
		List<Member> adminMembers = allMembers.stream().filter(member -> "ROLE_USER".equals(member.getRole()))
				.collect(Collectors.toList());
		model.addAttribute("listMembers", adminMembers);
		return "viewMember";
	}

	@PostMapping("/members/save")
	public String saveMember(@ModelAttribute Member member, @RequestParam("memberImage") MultipartFile imgFile,
			RedirectAttributes redirectAttribute) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);

		String imageName = imgFile.getOriginalFilename();

		// Set the image name in item object
		member.setImgName(imageName);

		// Save the item obj to the db
		Member savedItem = memberRepository.save(member);

		try {
			// Preparing the directory path
			String uploadDir = "uploads/members/" + savedItem.getId();
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

		memberRepository.save(member);
		redirectAttribute.addFlashAttribute("success", "Member registered");
		return "redirect:/techcentral";
	}

	@GetMapping("/members/add")
	public String addMembers(Model model) {
		model.addAttribute("member", new Member());
		return "addMember";
	}

	@GetMapping("/members/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id, Model model) {
		memberRepository.deleteById(id);
		return "redirect:/members";
	}

	// User report a seller
	@GetMapping("/members/report/{id}")
	public String banMembers(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getById(id);
		model.addAttribute("member", member);
		Integer ban_count = member.getBanCount(); // get current count
		System.out.println(ban_count);
		member.setBanCount(ban_count + 1);
		memberRepository.save(member);
		return "redirect:/members";
	}

	@GetMapping("/profile")
	public String viewProfile(Model model, @RequestParam(name = "prize", required = false) String prize) {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUsername = authentication.getName();
		Member member = memberRepository.findByUsername(currentUsername);

		// Calculate and set the total spending for the member
		Double totalSpending = orderItemRepository.getTotalSpendingByMember(member.getId());
		if (totalSpending == null) {
			totalSpending = 0.0;
		}
		member.setTotalSpending(totalSpending);
		memberRepository.save(member);

		Integer totalOrders = memberRepository.getTotalOrdersForMember(member);

		model.addAttribute("member", member);
		model.addAttribute("prize", prize);
		model.addAttribute("totalOrders", totalOrders);

		return "profile";
	}

	// Edit Profile
	@GetMapping("/members/editProfile/{id}")
	public String editProfile(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepository.getById(id);
		model.addAttribute("member", member);
		return "editProfile"; // assuming the HTML file is named editprofile.html
	}

	@PostMapping("/members/editProfile/{id}")
	public String saveUpdatedProfile(@PathVariable("id") Integer id, Member member,
			@RequestParam("memberImage") MultipartFile imgFile) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);

		System.out.println("save updateItem");
		if (!imgFile.isEmpty()) {
			String imageName = imgFile.getOriginalFilename();
			System.out.println("Image name from imgFile: " + imageName);

			member.setImgName(imageName);
			Member savedItem = memberRepository.save(member);

			try {
				// Preparing the directory path
				String uploadDir = "uploads/members/" + savedItem.getId();
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
			memberRepository.save(member);
		}

		return "redirect:/profile";
	}

}
