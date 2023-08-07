/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jan-20 12:40:27 pm 
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
public interface MemberRepository extends JpaRepository<Member, Integer> {
	public Member findByUsername(String username);
	public List<Member> findById(MemberDetails loggedInMember);
	
    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.member = :member")
    Integer getTotalOrdersForMember(@Param("member") Member member);
}