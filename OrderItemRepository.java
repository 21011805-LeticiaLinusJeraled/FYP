/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Feb-10 10:20:36 am 
 * 
 */
package FYP;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 21011805
 *
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	public List<OrderItem> findByMemberId(int id);
	OrderItem findByTransactionId(String transactionId);

    @Query("SELECT SUM(oi.quantity * oi.item.price) FROM OrderItem oi WHERE oi.member.id = :memberId")
    Double getTotalSpendingByMember(@Param("memberId") int memberId);
	
    @Query(value = "SELECT oi.item, SUM(oi.quantity) AS totalQuantity FROM OrderItem oi GROUP BY oi.item.id ORDER BY totalQuantity DESC", nativeQuery = false)
    List<Object[]> findTop3TrendingItems();
    
}
