/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Aug-02 10:15:50 pm 
 * 
 */
package FYP;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 21011805
 *
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findByItem(Item item); }
