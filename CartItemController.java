/**
 * 
a  * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Feb-10 9:37:39 am 
 * 
 */
package FYP;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 21011805
 *
 */
@Controller
public class CartItemController {
	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@GetMapping("/cart")
	public String showCart(Model model, Principal principal) {
		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();
		model.addAttribute("memberId", loggedInMemberId);

		// Get shopping cart items added by this user
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(loggedInMemberId);

		// Add the shopping cart items to the model
		model.addAttribute("cartItemList", cartItemList);

		// Calculate the total cost of all items in the shopping cart
		// Intialise the cart total to zero
		double cartTotal = 0.0;

		// loop through each item to find the total cost of items in the cart
		for (int i = 0; i < cartItemList.size(); i++) {

			// get one item from the cart
			CartItem currentCartItem = cartItemList.get(i);

			// get the quantity of that item
			int itemQuantityInCart = currentCartItem.getQuantity();
			if (itemQuantityInCart >= 1) {

				// Get the item info from the item table
				Item item = currentCartItem.getItem();
				double itemPrice = item.getPrice();

				// Calculate the subtotal and save it to the variable
				double subTotal = itemPrice * itemQuantityInCart;
				currentCartItem.setSubTotal(subTotal);
				cartTotal += subTotal;
			}
		}

		// Add the shopping cart total to the model
		model.addAttribute("cartTotal", cartTotal); // model is used to pass the controller to the html file
		return "cart";
	}

	@PostMapping("/cart/add/{itemId}")
	public String addToCart(@PathVariable("itemId") int itemId, @RequestParam("quantity") int quantity,
			Principal principal) {

		// Get currently logged in user
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		// Check in the cartItemRepo if item was previously added by user.
		// *Hint: we will need to write a new method in the CartItemRepository
		CartItem cartItem = cartItemRepo.findByMemberIdAndItemId(loggedInMemberId, itemId);

		// cartItem is not null if the item is found in the cart
		if (cartItem != null) {

			// if the item was previously added, then we get the quantity that was
			// previously added and increase that
			int currentQuantity = cartItem.getQuantity();
			cartItem.setQuantity(currentQuantity + quantity);
			cartItemRepo.save(cartItem);
		}

		// cartItem is null if the item is not found in the cart
		// if the item was NOT previously added,
		// then prepare the item and member objects
		else {
			// Create a new CartItem object
			CartItem newCartItem = new CartItem();

			// Set the item and member as well as the new quantity in the new CartItem
			// object
			Item item = itemRepo.getById(itemId);
			newCartItem.setItem(item);
			Member member = loggedInMember.getMember();
			newCartItem.setMember(member);
			newCartItem.setQuantity(quantity);

			// Save the CartItem object back to the repository
			cartItemRepo.save(newCartItem);
		}

		return "redirect:/cart";
	}

	@PostMapping("/cart/update/{id}")
	public String updateCart(@PathVariable("id") int cartItemId, @RequestParam("qty") int qty) {

		// Get cartItem object by cartItem's id
		CartItem cartItemToUpdate = cartItemRepo.getById(cartItemId);

		// Set the quantity of the carItem object retrieved
		cartItemToUpdate.setQuantity(qty);

		// Save the cartItem back to the cartItemRepo
		cartItemRepo.save(cartItemToUpdate);

		return "redirect:/cart";
	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable("id") int cartItemId) {

		CartItem cartItemToRemove = cartItemRepo.getById(cartItemId);
		// Remove item from cartItemRepo
		cartItemRepo.delete(cartItemToRemove);

		return "redirect:/cart";
	}

	@PostMapping("/cart/process_order")
	public String processOrder(Model model, @RequestParam("cartTotal") double cartTotal,
			@RequestParam("memberId") int memberId, @RequestParam("orderId") String orderId,
			@RequestParam("transactionId") String transactionId) {
		// Retrieve cart items purchased
		List<CartItem> cartItemList = cartItemRepo.findByMemberId(memberId);

		// Get member object
		Member member = memberRepo.getById(memberId);

		// Loop to iterate through all cart items
		for (int i = 0; i < cartItemList.size(); i++) {
			// Retrieve details about current cart item
			CartItem currentCartItem = cartItemList.get(i);
			int quantitySales = currentCartItem.getQuantity();

			Item itemToUpdate = currentCartItem.getItem();
			int quantityInventory = itemToUpdate.getQuantity();
			int quantityUpdated = quantityInventory - quantitySales;

			// Update item table in the inventory
			itemToUpdate.setQuantity(quantityUpdated);
			itemRepo.save(itemToUpdate);

			// Create a new order item
			OrderItem newOrderItem = new OrderItem();

			// Add item to order table
			newOrderItem.setItem(itemToUpdate);
			newOrderItem.setMember(member);
			newOrderItem.setOrderId(orderId);
			newOrderItem.setTransactionId(transactionId);
			newOrderItem.setQuantity(currentCartItem.getQuantity());
			newOrderItem.setSubtotal(cartTotal);
			orderItemRepo.save(newOrderItem);

			// clear cart items belonging to member
			cartItemRepo.deleteById(currentCartItem.getId());
		}

		// Pass info to view to display success page
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("member", member);
		model.addAttribute("orderId", orderId);
		model.addAttribute("cartTotal", cartTotal);
		model.addAttribute("transactionId", transactionId);

		// Send email
		String subject = "Techcentral order is confirmed!";
		String body = "Thank you for your order!\n" + "Order ID: " + orderId + "\n" + "Transaction ID: " + transactionId
				+ "\n" + "\n" + "Order will be delievered between 1-2 weeks";
		String to = member.getEmail();
		sendEmail(to, subject, body);

		return "success";
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);
		javaMailSender.send(msg);
	}

	@GetMapping("/purchaseHistory")
	public String viewHistory(Model model, Principal principal) {
		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getMember().getId();

		if (loggedInMember.getMember().getRole().equals("ROLE_USER")) {
			List<OrderItem> orderItemList = orderItemRepo.findByMemberId(loggedInMemberId);
			model.addAttribute("orderItemList", orderItemList);
		} else {
			List<OrderItem> orderItemList = orderItemRepo.findAll();
			model.addAttribute("orderItemList", orderItemList);
		}

		return "purchaseHistory";
	}

	@PostMapping("/cart/change_status/{id}")
	public String changeStatus(@PathVariable("id") int orderItemId, @RequestParam("received") boolean received) {
		// Retrieve the order item to update
		OrderItem orderItemToUpdate = orderItemRepo.getById(orderItemId);

		// Check if the order item exists
		if (orderItemToUpdate == null) {
			// Handle the case where the order item doesn't exist
			// You can redirect to an error page or return an error message
			return "redirect:/error";
		}

		// Update the received status
		orderItemToUpdate.setReceived(received);
		orderItemRepo.save(orderItemToUpdate);

		if (received) {
			// Send email notification
			sendEmailNotification(orderItemToUpdate.getTransactionId());
		}

		// Redirect to the purchase history page or any other appropriate page.
		return "redirect:/purchaseHistory";
	}

	@PostMapping("/cart/send_email_notification/{transactionId}")
	public void sendEmailNotification(@PathVariable String transactionId) {
		// Retrieve the relevant OrderItem based on the transactionId
		OrderItem orderItem = orderItemRepo.findByTransactionId(transactionId);

		// Retrieve the relevant member's email
		String email = orderItem.getMember().getEmail();

		// Send the email notification using the JavaMailSender (already available in
		// your code)
		String subject = "Techcentral Order Confirmation";
		String body = "Your order " + transactionId + " has been acknowledged as received." + "\n" + "\n"
				+ "Thank you for your purchase!" + "\n" + "We hope you enjoyed shopping with us. Come Again!";
		sendEmail(email, subject, body);
	}

	@PostMapping("/cart/toggle_status/{transactionId}")
	@ResponseBody
	public String toggleStatus(@PathVariable String transactionId) {
		OrderItem orderItem = orderItemRepo.findByTransactionId(transactionId);
		boolean received = orderItem.isReceived();
		orderItem.setReceived(!received);
		orderItemRepo.save(orderItem);
		return orderItem.isReceived() ? "received" : "not-received";
	}
	
	@GetMapping("/member/report/{id}")
	public String banMembers(@PathVariable("id") Integer id, Model model) {
		Member member = memberRepo.getById(id);
		model.addAttribute("member", member);
		Integer ban_count = member.getBanCount(); // get current count
		System.out.println(ban_count);
		member.setBanCount(ban_count + 1);
		memberRepo.save(member);
		return "redirect:/purchaseHistory";
	}

}
