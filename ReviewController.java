/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Aug-02 10:14:04 pm 
 * 
 */
package FYP;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author 21011805
 *
 */
@Controller
public class ReviewController {
	
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    
    @GetMapping("submitReview/items/{transactionId}")
    public String submitReviewForm(@PathVariable("transactionId") String transactionId, Model model) {
        // Retrieve the relevant OrderItem based on the transactionId
        OrderItem orderItem = orderItemRepository.findByTransactionId(transactionId);

        // Pass the purchased item to the model
        model.addAttribute("item", orderItem.getItem());
        model.addAttribute("review", new Review());

        return "submitReview";
    }
    
    
    @PostMapping("submitReview/items/{id}")
    public String submitReview(@PathVariable("id") Integer itemId, @ModelAttribute("review") Review review, Model model) {
        Item item = itemRepository.getById(itemId);
        review.setItem(item);

        // Get the currently logged-in member
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()); // Replace with your repository method to find member by username
        review.setMember(member); // Set the member property of the review

        reviewRepository.save(review);
        
        List<Review> allReviews = reviewRepository.findByItem(item);
        model.addAttribute("reviews", allReviews);
        
        return "redirect:/items/" + itemId;
    }
}
