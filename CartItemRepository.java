/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Feb-10 9:38:50 am 
 * 
 */
package FYP;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 21011805
 *
 */
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	public List<CartItem> findByMemberId(int id);

	public CartItem findByMemberIdAndItemId(int memberId, int itemId);
}
