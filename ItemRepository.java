/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 3:17:25 pm 
 * 
 */
package FYP;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 21011805
 *
 */
//interface item repository extends to the JpaRespository to communicate to the database to the Category table, Integer is the PK
public interface ItemRepository extends JpaRepository<Item, Integer> {
	public List<Item> findByCategory_Id(int id);
	public List<Item> findByMember_Id(int id);
	public List<Item> findByMemberId(int loggedInMemberId);
	public List<Item> findByReportCountGreaterThan(int count);
}
