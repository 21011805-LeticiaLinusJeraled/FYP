/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jul-17 6:21:05 pm 
 * 
 */
package FYP;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 21011805
 *
 */
@Controller
public class AdminController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	// Retrieve the user
	@GetMapping("/banUser")
	public String viewUser(Model model) {
		List<Member> allMembers = memberRepository.findAll();
		List<Member> memberList = allMembers.stream().filter(member -> "ROLE_USER".equals(member.getRole()))
				.collect(Collectors.toList());
		model.addAttribute("memberList", memberList);
		return "viewSeller";
	}

	// View all admin
	@GetMapping("/admin")
	public String viewAdmin(Model model) {
		List<Member> allMembers = memberRepository.findAll();
		List<Member> adminMembers = allMembers.stream().filter(member -> "ROLE_ADMIN".equals(member.getRole()))
				.collect(Collectors.toList());
		model.addAttribute("listMembers", adminMembers);
		return "viewAdmin";
	}

	// Add admin
	@GetMapping("/admin/add")
	public String addAdmin(Model model) {
		model.addAttribute("member", new Member());
		return "addAdmin";
	}

	@PostMapping("/admin/save")
	public String addAdmin(Member member, RedirectAttributes redirectAttribute) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		memberRepository.save(member);
		redirectAttribute.addFlashAttribute("success", "Member registered");
		return "redirect:/admin";
	}

	// Delete admin
	@GetMapping("/admin/delete/{id}")
	public String deleteMember(@PathVariable("id") Integer id, Model model) {
		memberRepository.deleteById(id);
		return "redirect:/admin";
	}

	// Ban user
	@PostMapping("/seller/ban/{id}")
	public String banSeller(@PathVariable("id") Integer id) {
		Member member = memberRepository.getById(id);
		member.setBanned(true);
		memberRepository.save(member);

		// Send email
		String subject = "Your Account Has Been Banned!";
		String body = "Your account has been temporary banned due to many reports made against you! Kindly wait till your account has been unbanned. Thank You.";
		String to = member.getEmail();
		sendBanNoti(to, subject, body);
		return "redirect:/banUser";
	}

	// Unban user
	@PostMapping("/seller/unban/{id}")
	public String unbanSeller(@PathVariable("id") Integer id) {
		Member member = memberRepository.getById(id);
		member.setBanned(false);
		memberRepository.save(member);

		// Send email
		String subject = "Your Account Has Been Unbanned!";
		String body = "Your account has been unbanned. Welcome to shopping with us again. Thank You.";
		String to = member.getEmail();
		sendUnBanNoti(to, subject, body);
		return "redirect:/banUser";
	}

	public void sendBanNoti(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");
	}

	public void sendUnBanNoti(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");
	}

	@GetMapping("/adminDashboard")
	public String adminDashboard(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("itemList", items);
		
		 List<Item> reportedItems = itemRepository.findByReportCountGreaterThan(0);
	        model.addAttribute("reportedItemList", reportedItems);
	        return "adminDashboard";
	    }

	@GetMapping("/report")
	public String viewItem(Model model) {
		List<Item> listItems = itemRepository.findAll();
		model.addAttribute("listItems", listItems);
		return "viewItem";
	}

	@GetMapping("/items/report/{id}/{reportReason}")
	public String confirmReport(@PathVariable("id") Integer id, @PathVariable("reportReason") String reportReason) {
		Item item = itemRepository.getById(id);
		item.setReportReason(reportReason);
		item.setReportCount(item.getReportCount() + 1); // Increment report count
		itemRepository.save(item);

		// Send email notification to admin
		String subject = "Reported Item: " + item.getName();
		String body = "Hello Admin,\n\nAn item has been reported with the following details:\n\n" + "Item ID: "
				+ item.getId() + "\n" + "Item Name: " + item.getName() + "\n" + "Report Reason: " + reportReason
				+ "\n\n" + "Please review the report and take appropriate action.\n\n"
				+ "Thank you,\nThe Item Reporting System";

		// Replace 'adminEmail' with the actual email of the admin
		String adminEmail = "admin@example.com";
		sendEmail(adminEmail, subject, body);
		return "redirect:/items"; // Redirect to item list page
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
		javaMailSender.send(msg);
	}
}